package cn.larry.analyzer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by larry on 16-7-17.
 */
public class LanModel {

    private static final Map<String, Integer> wordsFrepMap = new TreeMap<>();

    private static final Map<String, Integer> combineFreqMap = new HashMap<>();

    public static void init(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        br.lines().forEach(l -> {
            try {
                String[] ss = l.split("\\s");
                int num = Integer.parseInt(ss[1]);
                combineFreqMap.put(ss[0], num);
                String startWord = ss[0].split("-")[0];
                wordsFrepMap.putIfAbsent(startWord, 0);
                wordsFrepMap.put(startWord, wordsFrepMap.get(startWord) + num);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        List<String> words = new ArrayList<>(wordsFrepMap.keySet());
        // String path1 = "/home/larry/nlp/words.txt";
        //Files.createFile(Paths.get(path1));
        //Files.write(Paths.get(path1), words);

        //words.forEach(System.out::println);
        System.out.println();
    }

    public static void main(String[] args) throws IOException {
        String path = "/home/larry/nlp/SogouR.txt";
        init(new FileInputStream(path));
    }

    public static double combineProbability(String combine) {
        String start = combine.trim().split("-")[0];
        Integer combineFreq = combineFreqMap.get(combine);
        if (combineFreq == null) {
            combineFreq = 0;
        }
        Integer singleFrep = wordsFrepMap.get(start);
        if (singleFrep == null) {
            singleFrep = 0;
        }
        int wordsSize = wordsFrepMap.keySet().size();
        //为避免出现概率为0的情况，将未出现的组合出现次数视为1，出现了的次数则视为原始的8倍
        return (double) (combineFreq * 8 + 1) / (double) (singleFrep * 8 + wordsSize);
    }

    public static double wordProbability(String word) {
        long total = 0;
        for (Integer i : wordsFrepMap.values()) {
            total += i;
        }
        int freq = wordsFrepMap.get(word);
        return (double) freq / (double) total;
    }


}
