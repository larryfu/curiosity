package cn.larry.datawash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by larry on 7/17/2016.
 */
public class City {
    private int number;
    private final String code;
    private final String name;
    private String url;
    private final List<District> districts;

    public City(String code, String name) {
        this.code = code;
        this.name = name;
        this.districts = new ArrayList<>();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public List<District> getDistricts() {
        return districts;
    }

    public String toString() {
        return "code:" + code + ",name:" + name + ",url:" + url;
    }

    public Map<String, Object> getInfos() {
        Map<String, Object> infos = new HashMap<>();
        districts.forEach(d -> {
            infos.put(d.getName(), d.getInfos());
        });
        return infos;
    }

    public Map<Integer,Object> getNumbers(){
        Map<Integer,Object> numbers = new HashMap<>();
        districts.forEach(city -> {
            numbers.put(city.getNumber(),city.getNumbers());
        });
        return numbers;
    }


    private String matches(String addr) {
        String name = this.name;
        String mainBody = name.substring(0, name.length() - 1);
        if (addr.startsWith(name)) {
            return name;
        } else if (addr.startsWith(mainBody)) {
            return mainBody;
        } else {
            return null;
        }
    }

    public boolean analysis(String addr, Address address) {
        String match = this.matches(addr);
        if (match != null) {
            address.setCity(this);
            addr = addr.substring(match.length());
            if (addr.length() <= 1)
                return true;
        } else if (address.getProvince() == null) {
            return false;
        }

        for (District district : districts) {
            if (district.analysis(addr, address)) {
                address.setCity(this);
                return true;
            }
        }
        if (address.getProvince() != null) {
            address.setDetail(addr);
            return true;
        }
        return false;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
