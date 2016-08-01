package cn.larry.analyzer;

import redis.clients.jedis.Jedis;

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

    private static final String WORD_NUM_KEY = "word_num_key";

    private static final String WORD_TOTAL_KEY = "word_total_key";

    private static Jedis jedis = new Jedis("127.0.0.1");

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
        //List<String> words = new ArrayList<>(wordsFrepMap.keySet());
        wordsFrepMap.forEach((k, v) -> {
            jedis.set(k, v + "");
        });
        combineFreqMap.forEach((k, v) -> {
            jedis.set(k, v + "");
        });
        jedis.set(WORD_NUM_KEY, wordsFrepMap.keySet().size() + "");
        long total = 0;
        for (Integer i : wordsFrepMap.values()) {
            total += i;
        }
        jedis.set(WORD_TOTAL_KEY, total + "");
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

    private static int getFrep(String key) {
        String s = jedis.get(key);
        if (s == null || s.equals("null"))
            return 0;
        return Integer.parseInt(s);
    }

    private static long getLong(String key) {
        String s = jedis.get(key);
        if (s == null || s.equals("null"))
            return 0;
        return Long.parseLong(s);
    }

    public static double combineProbability(String combine) {
        String start = combine.trim().split("-")[0];
        int combineFreq = getFrep(combine);
        int singleFrep = getFrep(start);
        int endFreq = getFrep(combine.trim().split("-")[1]);
        int totalFreq = singleFrep + endFreq;
        int wordsSize = getFrep(WORD_NUM_KEY);
        System.out.println(combine + ":" + combineFreq + ",total:" + totalFreq);
        //为避免出现概率为0的情况，将未出现的组合出现次数视为1，出现了的次数则视为原始的8倍
        if (combineFreq == 0) {
            return Math.sqrt(Math.sqrt(totalFreq)) / wordsSize;
        }
        return (double) (combineFreq * 4 + 1) / (double) (singleFrep * 4 + wordsSize);
    }

    public static double wordProbability(String word) {
        long total = getLong(WORD_TOTAL_KEY);
        int freq = getFrep(word);
        return (double) freq / (double) total;
    }


}
