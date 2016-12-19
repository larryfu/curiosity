package cn.larry.nlp.datawash;

import cn.larry.graph.Digraph;
import cn.larry.graph.DirectedDFS;
import cn.larry.nlp.analyzer.Trie;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
    public static Map<Integer, String> genNumberMap() throws IOException {
        Gson gson = new Gson();
        String path = "/home/larry/nlp/xzqh-number.json";
        List<String> strings = Files.newBufferedReader(Paths.get(path)).lines().collect(Collectors.toList());
        List<Province> provinces = strings.stream().map(s -> gson.fromJson(s, Province.class)).collect(Collectors.toList());
        Map<Integer, String> numNameMap = new HashMap<>();
        for (Province province : provinces) {
            numNameMap.put(province.getNumber(), province.getName());
            province.getCities().forEach(city -> {
                numNameMap.put(city.getNumber(), city.getName());
                city.getDistricts().forEach(district -> {
                    numNameMap.put(district.getNumber(), district.getName());
                    district.getStreets().forEach(street -> {
                        numNameMap.put(street.getNumber(), street.getName());
                        street.getVillages().forEach(village -> {
                            numNameMap.put(village.getNumber(), village.getName());
                        });
                    });
                });
            });
        }
        return numNameMap;
    }

    public static void analysis(String addr) throws IOException {
        Digraph digraph = genDigraph();
        Map<Integer, String> numNameMap = genNumberMap();
        Trie xzqhTrie = generacteTrie();
        DirectedDFS dfs = new DirectedDFS(digraph, 0);
        String prefix;
        String leftAddr = addr;
        List<Object> objects = new ArrayList<>();
        while ((prefix = xzqhTrie.longestPrefixOf(leftAddr)) != null) {
            Set<Integer> pc = new HashSet<>();
            for (int i = 0; i < digraph.V(); i++) {
                if (dfs.marked(i)) pc.add(i);
            }
            String name = xzqhTrie.get(prefix);
            int number = -1;
            for (Map.Entry<Integer, String> entry : numNameMap.entrySet())
                if (entry.getValue().equals(name)) {
                    number = entry.getKey();
                    break;
                }
            if (pc.contains(number)) {
                objects.add(name);
                leftAddr = leftAddr.substring(prefix.length());
                dfs = new DirectedDFS(digraph, number);
            } else break;
        }
        objects.add(leftAddr);
        for (Object object : objects) {
            System.out.println(object);
        }
    }

    public static Digraph genDigraph() throws IOException {
        Gson gson = new Gson();
        String path = "/home/larry/nlp/xzqh-number.json";
        List<String> strings = Files.newBufferedReader(Paths.get(path)).lines().collect(Collectors.toList());
        List<Province> provinces = strings.stream().map(s -> gson.fromJson(s, Province.class)).collect(Collectors.toList());
        Digraph digraph = new Digraph(711828);
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
        return digraph;
    }

    private static String readFileToString(String path) throws IOException {
        BufferedReader reader = Files.newBufferedReader(Paths.get(path), StandardCharsets.UTF_8);
        List<String> strings = reader.lines().collect(Collectors.toList());
        return String.join(" ", strings);
    }


    public static void main(String[] args) throws IOException {
        String str = "湖南省株洲县仙井龙凤冲";
        analysis(str);
    }

    public static Trie generacteTrie() throws IOException {
        Trie trie = new Trie();
        String path = "/home/larry/nlp/xzqh-simple.json";
        String xzqh = readFileToString(path);
        Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]+");
        Matcher matcher = pattern.matcher(xzqh);
        while (matcher.find()) {
            String str = matcher.group();
            trie.put(str, str);
            if (str.endsWith("省") || str.endsWith("市") || str.endsWith("区") || str.endsWith("县") || str.endsWith("乡")||str.endsWith("村")) {
                String simpleName = str.substring(0, str.length() - 1);
                if (!simpleName.isEmpty() && trie.get(simpleName) == null)
                    trie.put(simpleName, str);
            }
            if (str.endsWith("村委会")) {
                String simpleName = str.substring(0, str.length() - 2);
                if (!simpleName.isEmpty() && trie.get(simpleName) == null)
                    trie.put(simpleName, str);
                String simpleName2 = str.substring(0, str.length() - 3);
                if (!simpleName2.isEmpty() && trie.get(simpleName2) == null)
                    trie.put(simpleName2, str);
            }
            if (str.endsWith("社区居委会")) {
                String simpleName = str.substring(0, str.length() - 5);
                if (!simpleName.isEmpty() && trie.get(simpleName) == null)
                    trie.put(simpleName, str);
            }
            if (str.endsWith("居委会")) {
                String simpleName = str.substring(0, str.length() - 3);
                if (!simpleName.isEmpty() && trie.get(simpleName) == null)
                    trie.put(simpleName, str);
            }
        }
        return trie;
    }

    private int getNumber(String name, Map<Integer, String> numberMap) {
        for (Map.Entry<Integer, String> entry : numberMap.entrySet())
            if (entry.getValue().equals(name))
                return entry.getKey();
        return -1;
    }

    public static void save(String s, String pathStr) throws IOException {
        Path path = Paths.get(pathStr);
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
        Files.write(path, Arrays.asList(s));
    }
}
