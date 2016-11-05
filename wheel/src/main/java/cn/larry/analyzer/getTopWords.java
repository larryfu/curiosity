package cn.larry.analyzer;

import com.google.gson.Gson;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by larry on 16-10-21.
 */
public class getTopWords {
    static String path_zhihu = "/opt/data/rawcorpus/zhihu.com/wordsummary.json";
    static String path_qq = "/opt/data/rawcorpus/qq.com/wordsummary.json";
    static String path_sogou = "/opt/data/rawcorpus/sogou/wordsummary.json";

    public static void main(String[] args) throws IOException {
        Gson gson = new Gson();
        Map<String, Double> zhihuMap = new HashMap<>();
        String zhihu = getStringFromFile(path_zhihu);
        zhihuMap = gson.fromJson(zhihu, zhihuMap.getClass());
        String qq = getStringFromFile(path_qq);
        String sogou = getStringFromFile(path_sogou);
        Map<String, Double> sogouMap = gson.fromJson(sogou, zhihuMap.getClass());
        Map<String, Double> qqMap = gson.fromJson(qq, zhihuMap.getClass());
        Map<String, Double> totalMap = new HashMap<>();
        zhihuMap.forEach((k, v) -> {
            totalMap.putIfAbsent(k, 0.0);
            totalMap.put(k, totalMap.get(k) + v);
        });
        qqMap.forEach((k, v) -> {
            totalMap.putIfAbsent(k, 0.0);
            totalMap.put(k, totalMap.get(k) + v);
        });
        sogouMap.forEach((k, v) -> {
            totalMap.putIfAbsent(k, 0.0);
            totalMap.put(k, totalMap.get(k) + v);
        });
        List<Map.Entry<String, Double>> entries = new ArrayList<>(totalMap.entrySet());
        Collections.sort(entries, (e1, e2) -> {
            return (int) (e2.getValue() - e1.getValue());
        });

        InputStream stream = getTopWords.class.getResourceAsStream("/main.dic");
        BufferedReader br = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
        Set<String> set = new HashSet<>(br.lines().collect(Collectors.toList()));

        entries = entries.stream().filter(e -> {
            return set.contains(e.getKey());
        }).filter(e -> e.getValue() >= 100).collect(Collectors.toList());
        List<String> strings = entries.stream().map(e -> e.getKey() + " " + e.getValue()).collect(Collectors.toList());


        //  strings = strings.stream().filter(s -> s.matches("[\u4e00-\u9fa5]+")).collect(Collectors.toList());


        String store = "/opt/data/rawcorpus/wordssummary.txt";
        Files.write(Paths.get(store), strings);
        strings.stream().forEach(System.out::println);
        System.out.println("size:"+strings.size());
    }

    public static String getStringFromFile(String path) throws IOException {
        Path path1 = Paths.get(path);
        BufferedReader br = Files.newBufferedReader(path1);
        return String.join("", br.lines().collect(Collectors.toList()));
    }
}

