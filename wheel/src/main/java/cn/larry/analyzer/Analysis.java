package cn.larry.analyzer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fugz on 2016/5/24.
 */
public class Analysis {

//    public Analysis(Trie trie) {
//        this.trie = trie;
//    }

    public static void main(String[] args) {
        //Analysis analysis = new Analysis(new Trie());
        trie = new Trie();
        trie.put("北京大学", "北京大学");
        trie.put("北京", "北京");
        trie.put("大学", "大学");
        trie.put("大学生", "大学生");
        trie.put("生活", "生活");
        String s = "北京大学生活";
        List<Interval> intervals = analysis(s);
        List<List<Interval>> results = Analyzer.getCombination(intervals);
        for (List<Interval> intervals1 : results) {
            for (Interval interval : intervals1) {
                System.out.print(s.substring(interval.getLower(), interval.getUpper() + 1) + ",");
            }
            System.out.println();
        }
    }

    private static Trie trie;

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
