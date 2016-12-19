package cn.larry.nlp.datawash;

import com.google.gson.Gson;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by larry on 7/17/2016.
 */
public class WordsCombinationFreq {

    public static void mains(String[] args) throws IOException {
        String path = "C:\\Users\\larry\\Desktop\\SougouR\\SogouR.txt";
        Gson gson = new Gson();
        Map<String, Integer> wordNumMap = new HashMap<>();
        AtomicInteger i = new AtomicInteger(1);
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
        br.lines().forEach(l -> {
            try {
                String[] words = l.split("-|\\s");
                String word1 = words[0];
                String word2 = words[1];
                if (!wordNumMap.containsKey(word1)) {
                    wordNumMap.put(word1.trim(), i.getAndIncrement());
                }
                if (!wordNumMap.containsKey(word2)) {
                    wordNumMap.put(word2.trim(), i.getAndIncrement());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        Set<String> words = wordNumMap.keySet();
        Path path1 = Paths.get("C:\\Users\\larry\\Desktop\\SougouR\\SogouF.json");
        Files.createFile(path1);
        Files.write(path1, Arrays.asList(gson.toJson(wordNumMap)));
        System.out.println(gson.toJson(wordNumMap));

    }

    public static void main(String[] args) throws IOException {
        Gson gson = new Gson();
        InputStream is = WordsCombinationFreq.class.getResourceAsStream("/wordNum.json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        Map<String, Double> wordsNumMap = gson.fromJson(reader, new HashMap<String, Double>().getClass());
        String path = "C:\\Users\\larry\\Desktop\\SougouR\\SogouR.txt";
        Map<WordsCombine, Integer> combineFreq = new HashMap<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
        br.lines().forEach(l -> {
            try {
                String[] words = l.split("-|\\s");
                String word1 = words[0].trim();
                String word2 = words[1].trim();
                int num = Integer.parseInt(words[2]);
                combineFreq.put(new WordsCombine((int) (double) wordsNumMap.get(word1), (int) (double) wordsNumMap.get(word2)), num);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // String s = gson.toJson(combineFreq);
        List<String> strings = new ArrayList<>();
        combineFreq.forEach((k, v) -> {
            strings.add(gson.toJson(k) + " " + v);
        });

        //  System.gc();
        Path path1 = Paths.get("C:\\Users\\larry\\Desktop\\SougouR\\combineFreq.json");
        Files.createFile(path1);
        Files.write(path1, strings);
    }

}
