package cn.larry.analyzer;

import com.google.gson.Gson;
import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by larry on 16-10-17.
 */
public class WordAnalysis {
    private static Gson gson = new Gson();

    public static void mains(String[] args) throws IOException {
        Gson gson = new Gson();
        String path = "/opt/data/rawcorpus/qq.com";
        Map<String, Integer> wordoccurency = new TreeMap<>();


        Files.walkFileTree(Paths.get(path), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException {
                try {
                    if (file.toString().contains(".json")) {
                        System.out.println(" process  " + file);
                        try (BufferedReader br = Files.newBufferedReader(file)) {
                            String str = String.join(" ", br.lines().collect(Collectors.toList()));
                            Map<String, String> map = new HashMap<>();
                            map = gson.fromJson(str, map.getClass());
                            String content = map.get("content");
                            List<String> list = analysis(content);
                            for (String s : list) {
                                wordoccurency.putIfAbsent(s, 0);
                                wordoccurency.put(s, wordoccurency.get(s) + 1);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return FileVisitResult.CONTINUE;
            }
        });
        Map<String, Integer> valid = new HashMap<>();
        wordoccurency.forEach((k, v) -> {
            if (v > 1) {
                valid.put(k, v);
            }
        });
        String store = "/opt/data/rawcorpus/qq.com/wordsummary.json";
        String json = gson.toJson(valid);
        Files.write(Paths.get(store), json.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);

    }

    public static void main(String[] args) throws IOException {
        String sogouroot = "/opt/data/rawcorpus/zhihu.com";
        executeExtract(sogouroot, path -> {
            try {
                return analysisZhihu(path);
            } catch (Exception e) {
                e.printStackTrace();
                return new ArrayList<>();
            }
        });
//        Map<String, Integer> wordsOccuMap = new HashMap<>();
//        Files.walkFileTree(Paths.get(sogouroot), new SimpleFileVisitor<Path>() {
//            @Override
//            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
//                    throws IOException {
//                try {
//                    System.out.println("process:" + file);
//                    List<String> words = analysisSogou(file);
//                    for (String s : words) {
//                        wordsOccuMap.putIfAbsent(s, 0);
//                        wordsOccuMap.put(s, wordsOccuMap.get(s) + 1);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                return FileVisitResult.CONTINUE;
//            }
//        });
//
//        Map<String, Integer> valid = new HashMap<>();
//        wordsOccuMap.forEach((k, v) -> {
//            if (v > 1) {
//                valid.put(k, v);
//            }
//        });
//        Gson gson = new Gson();
//        String store = "/opt/data/rawcorpus/sogou/wordsummary.json";
//        String json = gson.toJson(valid);
//        Files.write(Paths.get(store), json.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
    }

    public static void executeExtract(String root, Function<Path, List<String>> function) throws IOException {
        Map<String, Integer> wordsOccuMap = new HashMap<>();
        Files.walkFileTree(Paths.get(root), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException {
                try {
                    System.out.println("process:" + file);
                    List<String> words = function.apply(file);
                    for (String s : words) {
                        wordsOccuMap.putIfAbsent(s, 0);
                        wordsOccuMap.put(s, wordsOccuMap.get(s) + 1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return FileVisitResult.CONTINUE;
            }
        });

        Map<String, Integer> valid = new HashMap<>();
        wordsOccuMap.forEach((k, v) -> {
            if (v > 1) {
                valid.put(k, v);
            }
        });
        Gson gson = new Gson();
        String store = root + "/wordsummary.json";
        String json = gson.toJson(valid);
        Files.write(Paths.get(store), json.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);

    }

    public static void maines(String[] args) throws IOException {
        String sogouroot = "/opt/data/rawcorpus/zhihu.com";
        Map<String, Integer> wordsOccuMap = new HashMap<>();
        Files.walkFileTree(Paths.get(sogouroot), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException {
                try {
                    System.out.println("process:" + file);
                    List<String> words = analysisZhihu(file);
                    for (String s : words) {
                        wordsOccuMap.putIfAbsent(s, 0);
                        wordsOccuMap.put(s, wordsOccuMap.get(s) + 1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return FileVisitResult.CONTINUE;
            }
        });

        Map<String, Integer> valid = new HashMap<>();
        wordsOccuMap.forEach((k, v) -> {
            if (v > 1) {
                valid.put(k, v);
            }
        });
        Gson gson = new Gson();
        String store = "/opt/data/rawcorpus/zhihu.com/wordsummary.json";
        String json = gson.toJson(valid);
        Files.write(Paths.get(store), json.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
    }

    public static List<String> analysisSogou(Path path) throws IOException {
        BufferedReader br = Files.newBufferedReader(path, Charset.forName("GBK"));
        List<String> strings = br.lines().collect(Collectors.toList());
        List<String> words = new ArrayList<>();
        for (String s : strings) {
            try {
                if (s.contains("<contenttitle>")) {
                    s = s.replaceAll("</?contenttitle>", "");
                    words.addAll(analysis(s));
                }
                if (s.contains("<content>")) {
                    s = s.replaceAll("</?content>", "");
                    words.addAll(analysis(s));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return words;
    }

    public static List<String> analysisZhihu(Path path) throws IOException {
        try (BufferedReader br = Files.newBufferedReader(path)) {
            String s = String.join(" ", br.lines().collect(Collectors.toList()));
            List<String> words = new ArrayList<>();
            Question question = gson.fromJson(s, Question.class);
            words.addAll(analysis(question.getTitle()));
            for (Answer answer : question.getAnswers()) {
                words.addAll(analysis(answer.getContent()));
            }
            return words;
        }

    }

    public static List<String> analysis(String newstext) {
        try {
            List<String> wordsList = new ArrayList<String>();
            org.apache.lucene.analysis.Analyzer anal = new IKAnalyzer(true);
            StringReader reader = new StringReader(newstext);
            //分词
            TokenStream ts = anal.tokenStream("", reader);

            ts.reset();
            CharTermAttribute term = ts.getAttribute(CharTermAttribute.class);
            //遍历分词数据
            while (ts.incrementToken()) {
                wordsList.add(term.toString());
                //System.out.print(term.toString()+"|");
            }
            return wordsList;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
}
