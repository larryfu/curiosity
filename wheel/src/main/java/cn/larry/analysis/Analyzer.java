package cn.larry.analysis;

import cn.larry.news.IKSpliter;
import org.jsoup.Jsoup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by larryfu on 16-5-21.
 */
public class Analyzer {

    private static final WordsTireSt wordsTireSt = new WordsTireSt();

    private Analyzer() {
    }

    public static void init() {
        try (InputStream is = Analyzer.class.getResourceAsStream("/main.dic");
             BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            List<String> strs = br.lines().collect(Collectors.toList());
            addWords(strs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        init();


        String s = Jsoup.parse(new URL("http://www.zhihu.com/question/46582590"), 3000).text();
        IKSpliter.splitNews(s);
        anlay(s);
        IKSpliter.splitNews(s);
        anlay(s);
        IKSpliter.splitNews(s);
        anlay(s);
        IKSpliter.splitNews(s);
        anlay(s);
        System.out.println("ik analyze start");
        long start = System.currentTimeMillis();
        System.out.println("ik analyze result:" + Arrays.asList(IKSpliter.splitNews(s)));
        System.out.println("ik time :" + (System.currentTimeMillis() - start) + "ms");
        System.out.println("my analyze start");
        start = System.currentTimeMillis();
        System.out.println("my analyze result:" + anlay(s));
        System.out.println("my time :" + (System.currentTimeMillis() - start) + "ms");

    }

    public static class Range {
        int start;
        int end;

        Range(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

    public static synchronized void addWords(Collection<String> words) {
        for (String word : words)
            wordsTireSt.put(word, word);
    }

    public static List<String> anlay(String text) {
        List<String> words = new ArrayList<>();
        for (int i = 0; i < text.length(); i++) {
            String word = wordsTireSt.longestPrefixWithRange(text, i, text.length());
            while (word != null && !word.isEmpty()) {
                words.add(word);
                word = wordsTireSt.longestPrefixWithRange(text, i, i + word.length() - 1);
            }
        }
        return words;
    }

    public static List<String> analyze(String text) {
        List<Range> words = new ArrayList<>();
        for (int i = 0; i < text.length(); i++) {
            int len = wordsTireSt.longestPrefixOfWithRange(text, i, text.length());
            while (len > 0) {
                words.add(new Range(i, i + len));
                len = wordsTireSt.longestPrefixOfWithRange(text, i, len - 1);
            }
        }
        List<String> ws = new ArrayList<>();
        for (Range r : words)
            ws.add(text.substring(r.start, r.end));
        return ws;
    }


    public static List<String> analysis(String text) {
        List<String> words = new ArrayList<>();
        for (int i = 0; i < text.length(); i++) {
            String s = text.substring(i);
            String lf = wordsTireSt.longestPrefixOf(s);
            while (lf != null && !lf.isEmpty()) {
                words.add(lf);
                String l = lf.substring(0, lf.length() - 1);
                lf = wordsTireSt.longestPrefixOf(l);
            }
        }
        return words;
    }


}
