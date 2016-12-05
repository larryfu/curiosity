package cn.larry.datawash;

import cn.larry.graph.Digraph;
import cn.larry.graph.DirectedDFS;
import cn.larry.sample.compress.Trie;
import com.google.gson.Gson;

import javax.print.DocFlavor;
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
        String store = "/home/larry/nlp/addrNumberMap.json";
        System.out.println();
        Map<Integer, String> nameMap = new HashMap<>();
        for (int i = 0; i < list.size(); i++)
            nameMap.put(i + 1, list.get(i));
        return nameMap;
        // Gson gson = new Gson();
        //String mapStr = gson.toJson(nameMap);
        //  Files.write(Paths.get(store), mapStr.getBytes(StandardCharsets.UTF_8));
    }

    public static void analysis(String addr) throws IOException {
        Digraph digraph = genDigraph();
        Map<Integer, String> numNameMap = genNumberMap();
        Trie<String> xzqhTrie = generacteTrie();
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
                if (entry.getValue().equals(name))
                    number = entry.getKey();
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
        // save(s, "/home/larry/nlp/digraph.json");
    }

    private static String readFileToString(String path) throws IOException {
        BufferedReader reader = Files.newBufferedReader(Paths.get(path), StandardCharsets.UTF_8);
        List<String> strings = reader.lines().collect(Collectors.toList());
        return String.join(" ", strings);
    }


    public static void main(String[] args) throws IOException {
        String str = "福建省龙岩市长汀县铁长乡铁长村";
        analysis(str);
    }

    public static Trie<String> generacteTrie() throws IOException {

        Trie<String> trie = new Trie<>();
        //String addressc = address;
        String path = "/home/larry/nlp/xzqh-simple.json";
        //String numberPath = "/home/larry/nlp/addrNumberMap.json";
        // String numberMapStr = readFileToString(numberPath);
        // Map<Integer, String> numberMap = new HashMap<>();
        //  numberMap = gson.fromJson(numberMapStr, numberMap.getClass());
        String xzqh = readFileToString(path);
        Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]+");
        Matcher matcher = pattern.matcher(xzqh);
        while (matcher.find()) {
            String str = matcher.group();
            //  System.out.println("add address name to trie :" + str);
            trie.put(str, str);
            if (str.endsWith("省") || str.endsWith("市") || str.endsWith("区") || str.endsWith("县")) {
                String simpleName = str.substring(0, str.length() - 1);
                trie.put(simpleName, str);
            }
            if (str.endsWith("村委会")) {
                String simpleName = str.substring(0, str.length() - 2);
                trie.put(simpleName, str);
            }
            if (str.endsWith("社区居委会")) {
                String simpleName = str.substring(0, str.length() - 5);
                trie.put(simpleName, str);
            }
            if (str.endsWith("居委会")) {
                String simpleName = str.substring(0, str.length() - 3);
                trie.put(simpleName, str);
            }

        }
        return trie;
//        String trieJson = gson.toJson(trie);
//        save(trieJson, "/home/larry/nlp/xzqhtrie.json");
//        System.out.println(trieJson);
        //String pre = trie.longestPrefixOf(addressc);
        // addressc = addressc.substring(pre.length());
        //String name = trie.get(pre);
        //int number = getNumber(name, numberMap);
        // DirectedDFS dfs = new DirectedDFS(digraph, 0);
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
