package cn.larry.nlp.analyzer;

import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by larry on 16-10-17.
 */
public class WordAnalysis {
    private static Gson gson = new Gson();

    public static void mainse(String[] args) throws IOException {
        Gson gson = new Gson();
        String path = "/opt/data/rawcorpus/sina";
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
        String store = "/opt/data/rawcorpus/sina/wordsummary.json";
        String json = gson.toJson(valid);
        Files.write(Paths.get(store), json.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);

    }

    public static void mains(String[] args) throws IOException {
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

    public static void mainew(String[] args) {
        System.out.println(analysis("数据递增率"));
        simplifyWords();
        System.out.println(analysis("数据递增率"));
    }


    public static Set<String> getFileAsStringList(String path) {
        InputStream inputStream = WordAnalysis.class.getResourceAsStream(path);
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        return br.lines().collect(Collectors.toSet());
    }

    public static void simplifyWords() {
        analysis("我们");
        Set<String> toRemove = getWordsToRemove();
        org.wltea.analyzer.dic.Dictionary.getSingleton().disableWords(toRemove);
    }

    public static Set<String> getWordsToRemove() {
        Set<String> total = getFileAsStringList("/main.dic");
        Set<String> remain = getFileAsStringList("/simplify.dic");
        total.removeAll(remain);
        return total;
    }

    public static List<String> analysis(String newstext) {
        try {
            List<String> list = new ArrayList<>();
            if (StringUtils.isNotBlank(newstext)) {
                StringReader reader = new StringReader(newstext);
                IKSegmenter ik = new IKSegmenter(reader, true);
                Lexeme lexeme;
                while ((lexeme = ik.next()) != null) {
                    String word = lexeme.getLexemeText();
                    list.add(word);
                }
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

    }

    public static void main(String[] args) {
        String text = "单位“建议捐款”、“号召捐款”时，即使你不太想捐，但你能不捐吗？当领导和同事捐出数百元，即使你囊中羞涩，你好意思只捐几十元吗？7月20日，在广东省慈善总会召开的专题研究会议上，副省长刘昆直指“建议捐款”、“号召捐款”、为“向领导看齐”而捐的款都属于“变相强捐”";
        System.out.println(analysis(text));
    }
}
