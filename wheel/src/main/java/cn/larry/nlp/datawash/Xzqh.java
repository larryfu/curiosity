package cn.larry.nlp.datawash;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
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
        List<String> names = new ArrayList<>();

        for (Province province : provinces) {
            names.add(province.getName());
            province.setNumber(names.size());
            for (City city : province.getCities()) {
                names.add(city.getName());
                city.setNumber(names.size());
                for (District district : city.getDistricts()) {
                    names.add(district.getName());
                    district.setNumber(names.size());
                    for (Street street : district.getStreets()) {
                        names.add(street.getName());
                        street.setNumber(names.size());
                        for (Village village : street.getVillages()) {
                            names.add(village.getName());
                            village.setNumber(names.size());
                        }
                    }
                }
            }
        }
        List<String> ss = provinces.stream().map(province -> gson.toJson(province)).collect(Collectors.toList());
        String spath = "/home/larry/nlp/xzqh-number.json";
        //String listPath = gson.toJson(names);
        String listStore = "/home/larry/nlp/xzqh-names.txt";
        //Files.createFile(Paths.get(listStore));
        Files.write(Paths.get(listStore), names);
        Files.write(Paths.get(spath), ss);
        Map<Integer, Map> xzqh = new HashMap<>();
        provinces.forEach(p -> {
            xzqh.put(p.getNumber(), p.getNumbers());
        });
        String path1 = "/home/larry/nlp/xzqh-number-simple.json";
        String xzqhinfo = gson.toJson(xzqh);
        //Files.createFile(Paths.get(path1));
        Files.write(Paths.get(path1), Arrays.asList(xzqhinfo));
        System.out.println(xzqhinfo);
    }

    public static void maines(String[] args) throws IOException {
        Gson gson = new Gson();
        String path = "/home/larry/nlp/xzqh-all.json";
        List<String> strings = Files.newBufferedReader(Paths.get(path)).lines().collect(Collectors.toList());
        List<Province> provinces = strings.stream().map(s -> gson.fromJson(s, Province.class)).collect(Collectors.toList());
        String address = "北京市东城区景山街道吉祥社区碧桂园5栋";
        String addr = "湖南株洲";
        Xzqh xzqh = new Xzqh(provinces);
        Address address1 = xzqh.analysis(addr);
        System.out.println();
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
