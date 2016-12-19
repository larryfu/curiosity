package cn.larry.nlp.datawash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by larry on 7/17/2016.
 */
public class Province {

    private int number;

    private final String code;

    private final String name;

    private String url;

    private final List<City> cities;

    public Province(String code, String name) {
        this.code = code;
        this.name = name;
        this.cities = new ArrayList<>();
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public List<City> getCities() {
        return cities;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String toString() {
        return "code:" + code + ",name:" + name + ",url:" + url;
    }

    public Map<String, Object> getInfos() {
        Map<String, Object> infos = new HashMap<>();
        cities.forEach(c -> {
            infos.put(c.getName(), c.getInfos());
        });
        return infos;
    }

    public Map<Integer,Object> getNumbers(){
        Map<Integer,Object> numbers = new HashMap<>();
        cities.forEach(city -> {
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

    public Address analysis(String addr) {
        Address address = new Address();
        String match = this.matches(addr);
        if (match != null) {
            addr = addr.substring(match.length());
            address.setProvince(this);
            if (addr.length() <= 1)
                return address;
        }
        for (City city : cities) {
            boolean hit = city.analysis(addr, address);
            if (hit)
                return address;
        }
        return null;

    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
