package cn.larry.datawash;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Created by larry on 7/17/2016.
 */
public class xzqhWasher {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\Users\\larry\\Desktop\\city.json")));
        String json = String.join(" ", reader.lines().collect(Collectors.toList()));
        Gson gson = new Gson();
        Map<String, String> xzqhMap = new TreeMap<>();
        xzqhMap = gson.fromJson(json, xzqhMap.getClass());
        List<Province> provices = new ArrayList<>();
        List<City> cities = new ArrayList<>();
        List<District> districts = new ArrayList<>();
        xzqhMap.forEach((k, v) -> {
            if (k.endsWith("0000")) {
                provices.add(new Province(k, v));
            } else if (k.endsWith("00")) {
                cities.add(new City(k, v));
            } else {
                districts.add(new District(k, v));
            }
        });

        for (City city : cities) {
            String codePrefix = city.getCode().substring(0, 4);
            for (District district : districts) {
                if (district.getCode().startsWith(codePrefix)) {
                    city.getDistricts().add(district);
                }
            }
        }

        for (Province provice : provices) {
            String codePrefix = provice.getCode().substring(0, 2);
            for (City city : cities) {
                if (city.getCode().startsWith(codePrefix)) {
                    provice.getCities().add(city);
                }
            }
        }

        Xzqh xzqh = new Xzqh(provices);
        String xzqhJson = gson.toJson(xzqh);

        Map<String, Map<String, List<String>>> nameMap = new TreeMap<>();

        provices.forEach(p -> {
            Map<String, List<String>> proviceInfo = new TreeMap<>();
            p.getCities().forEach(c -> {
                List<String> districs = new ArrayList<>();
                c.getDistricts().forEach(d -> {
                    districs.add(d.getName());
                });
                proviceInfo.put(c.getName(), districs);
            });
            nameMap.put(p.getName(), proviceInfo);
        });

        System.out.println(xzqhJson);

        System.out.println(gson.toJson(nameMap));
    }
}
