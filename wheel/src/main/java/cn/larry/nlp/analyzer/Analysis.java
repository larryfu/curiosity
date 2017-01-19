package cn.larry.nlp.analyzer;

import org.apache.commons.lang3.StringUtils;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by fugz on 2016/5/24.
 */
public class Analysis {

    List<List<String>> analyzer(String text) {
        List<List<String>> result = new ArrayList<>();
        List<List<Interval>> results = Analyzer.getCombination(analysis(text));
        for (List<Interval> ins1 : results) {
            result.add(ins1.stream()
                    .map(in -> text.substring(in.getLower(), in.getUpper() + 1))
                    .collect(Collectors.toList()));
        }
        return result;
    }

    public static List<String> analyzeWithIK(String text) throws IOException {
        List<String> list = new ArrayList<>();
        if (StringUtils.isNotBlank(text)) {
            StringReader reader = new StringReader(text);
            IKSegmenter ik = new IKSegmenter(reader, true);
            Lexeme lexeme;
            while ((lexeme = ik.next()) != null) {
                String word = lexeme.getLexemeText();
                list.add(word);
            }
        }
        return list;
    }

    public static Set<String> getWords() {
        try (InputStream is = Analysis.class.getResourceAsStream("/simplify.dic");
             BufferedReader br = new BufferedReader(new InputStreamReader(is));) {
            Set<String> stringSet = new HashSet<>();
            br.lines().forEach(stringSet::add);
            return stringSet;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashSet<>();
    }


    public static void main(String[] args) throws IOException {
        String s = "赵诗环面试得很好";
        Set<String> stopWords = new HashSet<>(Arrays.asList("的", "是", "了", "之", "啊", "呀", "吧", "呢", "吗", "在", "儿", "于", "和", "与", "来", "去"));
        LanModel lanModel = new RedisBasedLanModel();
        System.out.println(analyzeWithIK(s));
        init();
        Set<String> dictWords = getWords();
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
        int index = 0;
        double max = 0.0;
        List<List<String>> resultFiltered = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            resultFiltered.add(result.get(i).stream().filter(s1 -> !stopWords.contains(s1)).collect(Collectors.toList()));
            //resultFiltered.add(result.get(i));
        }
        for (int i = 0; i < resultFiltered.size(); i++) {
            List<String> ss = resultFiltered.get(i);
            double d = lanModel.wordProbability(ss.get(0));
            for (int j = 1; j < ss.size(); j++) {
                String start = ss.get(j - 1);
                String end = ss.get(j);
                double rate = lanModel.combineProbability(start, end);
                d *= rate;
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
        InputStream is = Analysis.class.getResourceAsStream("/simplify.dic");
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        br.lines().forEach(l -> {
            trie.put(l, "");
        });
    }

    public static List<Interval> analysis(String text) {
        List<Interval> intervals = new ArrayList<>();
        for (int i = 0; i < text.length(); i++) {
            Interval in = trie.longestPrefixWithRange(text, i, text.length());
            if (in == null) in = new Interval(i, i);
            while (in != null) {
                intervals.add(in);
                in = trie.longestPrefixWithRange(text, i, in.getUpper());
            }
        }
        return intervals;
    }
}
