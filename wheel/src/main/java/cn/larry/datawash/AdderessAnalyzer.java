package cn.larry.datawash;

import cn.larry.graph.Digraph;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by larry on 16-11-6.
 */
public class AdderessAnalyzer {
    public static void mains(String[] args) throws IOException {
        String path = "/home/larry/nlp/xzqh-simple.json";
        BufferedReader br = Files.newBufferedReader(Paths.get(path));
        List<String> stringList = br.lines().collect(Collectors.toList());
        String s = String.join(" ", stringList);
        Pattern pattern = Pattern.compile("\"[^\"]+\"");
        Matcher matcher = pattern.matcher(s);
        List<String> list = new ArrayList<>();
        while (matcher.find()) {
            list.add(matcher.group());
        }
        System.out.println();
        Map<Integer, String> nameMap = new HashMap<>();
        for (int i = 0; i < list.size(); i++)
            nameMap.put(i, list.get(i));
        for (int i = 0; i < list.size(); i++) {
            String str = list.get(i);
            s = s.replace(str, i + "");
        }
        System.out.println(s);
    }

    public static void main(String[] args) throws IOException {

        Gson gson = new Gson();
        String path = "/home/larry/nlp/xzqh-number.json";
        List<String> strings = Files.newBufferedReader(Paths.get(path)).lines().collect(Collectors.toList());
        List<Province> provinces = strings.stream().map(s -> gson.fromJson(s, Province.class)).collect(Collectors.toList());
        Digraph digraph = new Digraph(711827);
        for (Province province : provinces) {
            digraph.addEdge(0, province.getNumber());
            province.getCities().forEach(city -> {
                digraph.addEdge(province.getNumber(), city.getNumber());
                city.getDistricts().forEach(district -> {
                    digraph.addEdge(city.getNumber(), district.getNumber());
                    district.getStreets().forEach(street -> {
                        digraph.addEdge(district.getNumber(), street.getNumber());
                        street.getVillages().forEach(village -> {
                            digraph.addEdge(street.getNumber(), village.getNumber());
                        });
                    });
                });
            });
        }
        String s = gson.toJson(digraph);
        save(s, "/home/larry/nlp/digraph.json");
    }

    public static void save(String s, String pathStr) throws IOException {
        Path path = Paths.get(pathStr);
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
        Files.write(path, Arrays.asList(s));
    }
}
