package cn.larry.analyzer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fugz on 2016/5/24.
 */
public class Analysis {

//    public Analysis(Trie trie) {
//        this.trie = trie;
//    }

    public static void main(String[] args) throws IOException {
        //Analysis analysis = new Analysis(new Trie());
//        trie = new Trie();
//        trie.put("北京大学", "");
//        trie.put("北京", "");
//        trie.put("大学", "");
//        trie.put("大学生", "");
//        trie.put("生活", "");
        init();
        String s = "北京大学的生活很美好";
        List<Interval> intervals = analysis(s);
        List<List<Interval>> results = Analyzer.getCombination(intervals);
        List<List<String>> result = new ArrayList<>();
        for (List<Interval> intervals1 : results) {
            List<String> strings = new ArrayList<>();
            for (Interval interval : intervals1) {
                String w = s.substring(interval.getLower(), interval.getUpper() + 1);
                strings.add(w);
                System.out.print(w + ",");
            }
            result.add(strings);
            System.out.println();
        }

        //  Map<Integer,>
        int index = 0;
        double max = 0.0;
        LanModel.init(new FileInputStream("/home/larry/nlp/SogouR.txt"));
        for (int i = 0; i < result.size(); i++) {
            List<String> ss = result.get(i);
            double d = LanModel.wordProbability(ss.get(0));

            for (int j = 1; j < ss.size(); j++) {
                String start = ss.get(j - 1);
                String end = ss.get(j);
                d *= LanModel.combineProbability(start + "-" + end);
            }
            if (d > max) {
                max = d;
                index = i;
            }
        }
        System.out.println("best:");
        System.out.println(result.get(index));
    }

    private static Trie trie = new Trie();

    private static void init() {
        //  if (trie == null) {
        // trie = new Trie();
        InputStream is = Analysis.class.getResourceAsStream("/words.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        br.lines().forEach(l -> {
            trie.put(l, "");
        });
        //  }
        //return trie;

    }

    public static List<Interval> analysis(String text) {
        List<Interval> intervals = new ArrayList<>();
        for (int i = 0; i < text.length(); i++) {
            Interval in = trie.longestPrefixWithRange(text, i, text.length());
            while (in != null) {
                intervals.add(in);
                in = trie.longestPrefixWithRange(text, i, in.getUpper());
            }
        }
        return intervals;
    }
}
