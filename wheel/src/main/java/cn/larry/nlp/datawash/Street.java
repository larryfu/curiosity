package cn.larry.nlp.datawash;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by fugz on 2016/7/18.
 */
public class Street {

    //街道或镇或乡
    private int number;
    private String code;
    private String name;
    private String url;
    private List<Village> villages;

    public Street(String code, String name) {
        this.code = code;
        this.name = name;
        this.villages = new ArrayList<>();
    }

    public List<Village> getVillages() {
        return villages;
    }


    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String toString() {
        return "code:" + code + ",name:" + name + ",url:" + url;
    }


    public List<String> getInfos() {
        return villages.stream().map(Village::getName).collect(Collectors.toList());
    }

    public List<Integer> getNumbers() {
        return villages.stream().map(Village::getNumber).collect(Collectors.toList());
    }


    public static void main(String[] args) {
        String str = "景山街道办事处";
        System.out.println(str.substring(0, str.length() - 3));
    }

    private String matches(String addr) {
        String name = this.name;
        String mainBody = name.substring(0, name.length() - 1);

        if (addr.startsWith(name)) {
            return name;
        } else if (addr.startsWith(mainBody)) {
            return mainBody;
        } else if (name.endsWith("街道办事处")) {
            String body = name.substring(0, name.length() - 3);
            if (addr.startsWith(body)) {
                return body;
            } else {
                body = name.substring(0, name.length() - 5);
                if (addr.startsWith(body))
                    return body;
            }
        }
        return null;
    }

    public boolean analysis(String addr, Address address) {

        String match = this.matches(addr);
        if (match != null) {
            address.setStreet(this);
            addr = addr.substring(match.length());
            address.setDetail(addr);
            return true;
        } else if (address.getDistrict() == null) {
            return false;
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
