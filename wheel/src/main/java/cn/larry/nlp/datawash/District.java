package cn.larry.nlp.datawash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by larry on 7/17/2016.
 */
public class District {
    private int number;
    private String code;
    private String name;
    private String url;
    private List<Street> streets;

    public District(String code, String name) {
        this.code = code;
        this.name = name;
        this.streets = new ArrayList<>();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public List<Street> getStreets() {
        return streets;
    }

    public String toString() {
        return "code:" + code + ",name:" + name + ",url:" + url;
    }

    public Map<String, List<String>> getInfos() {
        Map<String, List<String>> infos = new HashMap<>();
        for (Street street : streets) {
            infos.put(street.getName(), street.getInfos());
        }
        return infos;
    }

    public Map<Integer,Object> getNumbers(){
        Map<Integer,Object> numbers = new HashMap<>();
        streets.forEach(city -> {
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
            address.setDistrict(this);
            addr = addr.substring(match.length());
            if (addr.length() <= 1)
                return true;
        } else if (address.getCity() == null) {
            return false;
        }
        for (Street street : streets) {
            if (street.analysis(addr, address)) {
                return true;
            }
        }
        if (address.getProvince() != null && address.getCity() != null) {
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
