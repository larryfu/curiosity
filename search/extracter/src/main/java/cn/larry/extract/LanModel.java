package cn.larry.extract;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by larry on 16-7-17.
 */
public class LanModel {

    private static final Map<String, Integer> wordsFrepMap = new TreeMap<>();

    private static final Map<String, Integer> combineFreqMap = new HashMap<>();

    public static void init(String path) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
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
        Set<String> words = wordsFrepMap.keySet();
        System.out.println();
    }

    public static void main(String[] args) throws FileNotFoundException {
        String path = "/home/larry/nlp/SogouR.txt";
        init(path);
    }

    public static double combineProbability(String combine) {
        String start = combine.trim().split("-")[0];
        int combineFreq = combineFreqMap.get(combine);

        int singleFrep = wordsFrepMap.get(start);
        int wordsSize = wordsFrepMap.keySet().size();
        //为避免出现概率为0的情况，将未出现的组合出现次数视为1，出现了的次数则视为原始的8倍
        return (double) (combineFreq * 8 + 1) / (double) (singleFrep * 8 + wordsSize);
    }


}
