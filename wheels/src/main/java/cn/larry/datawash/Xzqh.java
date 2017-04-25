package cn.larry.datawash;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by larry on 7/17/2016.
 */
public class Xzqh {

    private final List<Province> provices;

    public Xzqh(List<Province> provices) {
        this.provices = provices;
    }

    public List<Province> getProvices() {
        return provices;
    }

    public static void mains(String[] args) throws IOException {
        Gson gson = new Gson();
        String path = "/home/larry/nlp/xzqh-all.json";
        List<String> strings = Files.newBufferedReader(Paths.get(path)).lines().collect(Collectors.toList());
        List<Province> provinces = strings.stream().map(s -> gson.fromJson(s, Province.class)).collect(Collectors.toList());
        Map<String, Map> xzqh = new HashMap<>();
        provinces.forEach(p -> {
            xzqh.put(p.getName(), p.getInfos());
        });
        String path1 = "/home/larry/nlp/xzqh-simple.json";
        String xzqhinfo = gson.toJson(xzqh);
        Files.createFile(Paths.get(path1));
        Files.write(Paths.get(path1), Arrays.asList(xzqhinfo));
        System.out.println(xzqhinfo);
    }

    public static void main(String[] args) throws IOException {
        Gson gson = new Gson();
        String path = "/home/larry/nlp/xzqh-all.json";
        List<String> strings = Files.newBufferedReader(Paths.get(path)).lines().collect(Collectors.toList());
        List<Province> provinces = strings.stream().map(s -> gson.fromJson(s, Province.class)).collect(Collectors.toList());
        String address = "北京市东城区景山街道吉祥社区碧桂园5栋";
        Xzqh xzqh = new Xzqh(provinces);
        Address address1 = xzqh.analysis(address);
        System.out.println();
    }

    public Address analyze(String addr) {
        Address address = new Address();
        for (Province province : provices) {
            String match = province.matches(addr);
            if (match == null)
                continue;
            address.setProvince(province);
            address.setDetail(addr.substring(match.length()));
            break;
        }
        if (address.getProvince() != null && address.getDetail().length() > 1) {
            Province province = address.getProvince();
            String left = address.getDetail();
            for (City city : province.getCities()) {
                String match = city.matches(left);
                if (match != null) {
                    address.setCity(city);
                    address.setDetail(left.substring(match.length()));
                    break;
                }
            }
        }

        if (address.getCity() != null && address.getDetail().length() > 1) {
            City city = address.getCity();
            String left = address.getDetail();
            for (District district : city.getDistricts()) {
                String match = district.matches(left);
                if (match != null) {
                    address.setDistrict(district);
                    address.setDetail(left.substring(match.length()));
                    break;
                }
            }
        }

        if (address.getDistrict() != null && address.getDetail().length() > 1) {
            District district = address.getDistrict();
            String left = address.getDetail();
            for (Street street : district.getStreets()) {
                String match = street.matches(left);
                if (match != null) {
                    address.setStreet(street);
                    address.setDetail(left.substring(match.length()));
                    break;
                }
            }
        }

        return null;
    }

    public Address analysis(String addr) {
        for (Province province : provices) {
            Address address = province.analysis(addr);
            if (address != null) {
                return address;
            }
        }
        return null;
    }

}
