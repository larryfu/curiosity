package cn.larry.nlp.analyzer;

import cn.larry.extract.News;
import cn.larry.extract.XmlConvert;
import cn.larry.extract.zhihu.Answer;
import cn.larry.extract.zhihu.Question;
import com.google.gson.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Created by larry on 16-12-13.
 */
public class WordCombinrFreqAnalyzer {

    private static final String punRegexp = getPunctuationReexp();

    private static final Gson gson = new GsonBuilder().
            registerTypeAdapter(Double.class, new JsonSerializer<Double>() {
                @Override
                public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
                    if (src == src.intValue())
                        return new JsonPrimitive(src.intValue());
                    if (src == src.longValue())
                        return new JsonPrimitive(src.longValue());
                    return new JsonPrimitive(src);
                }
            }).create();

    public static void main(String[] args) throws IOException {
        String qq_path = "/opt/data/rawcorpus/qq.com";
        String sina_path = "/opt/data/rawcorpus/sina";
        String people_path = "/opt/data/rawcorpus/people";
        String sogou_path = "/opt/data/rawcorpus/psogou";
        String zhihu_path = "/opt/data/rawcorpus/zhihu.com";
        // analysisSite(qq_path);
        //analysisSogouData();
        // analysisZhihuData();
        mergeData();
    }

    public static void mergeData() throws IOException {
        String path = "/home/larry/nlp/words-freq";
        Map<String, Integer> mergered = new HashMap<>();
        Files.walkFileTree(Paths.get(path), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException {
                System.out.println("deal file:" + file.toString());
                mergerWords(file, mergered);
                return FileVisitResult.CONTINUE;
            }
        });
        String store = "/home/larry/nlp/words-freq/total";
        Files.createFile(Paths.get(store));
        List<String> strings = new ArrayList<>();
        mergered.forEach((k, v) -> {
            strings.add(k + "   " + v);
            if (strings.size() > 10000) {
                System.out.println("write to file");
                try {
                    Files.write(Paths.get(store), strings, StandardOpenOption.APPEND);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                strings.clear();
            }
        });
    }

    private static void mergerWords(Path file, Map<String, Integer> mergered) {
        try (BufferedReader br = Files.newBufferedReader(file)) {
            List<String> strings = br.lines().collect(Collectors.toList());
            String str = String.join(" ", strings);
            Map<String, Double> map = gson.fromJson(str, Map.class);
            map.forEach((k, v) -> {
                mergered.putIfAbsent(k, 0);
                int val = v.intValue();
                mergered.put(k, mergered.get(k) + val);
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void analysisSite(String path) throws IOException {
        Map<String, Integer> freqMap = new HashMap<>();
        WordAnalysis.simplifyWords();
        AtomicInteger ai = new AtomicInteger();
        int step = 10000;
        Files.walkFileTree(Paths.get(path), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException {
                if (file.toString().endsWith(".json")) {
                    int current = ai.incrementAndGet();
                    System.out.println("analysis:" + file);
                    analysisNews(file, freqMap);
                    if (current % step == 0) {
                        saveData(freqMap, current / step, "qq");
                        freqMap.clear();
                    }
                }
                return FileVisitResult.CONTINUE;
            }
        });
        String store = path + "/combineWordFreq.json";
        String str = gson.toJson(freqMap);
        Files.createFile(Paths.get(store));
        Files.write(Paths.get(store), str.getBytes(StandardCharsets.UTF_8));
    }

    private static void saveData(Map<String, Integer> freqMap, int i, String site) throws IOException {
        System.out.println("save data:" + i);
        String baseDir = "/home/larry/nlp/words-freq";
        String file = baseDir + "/" + site + "-wordMap_" + i + ".json";
        Gson gson = new Gson();
        String data = gson.toJson(freqMap);
        Files.createFile(Paths.get(file));
        Files.write(Paths.get(file), data.getBytes(StandardCharsets.UTF_8));
    }

    public static String getPunctuationReexp() {
        List<String> punctuations = Arrays.asList("\\s+", "'", "。", ",", "，", "\\.", "、", "\\", "；", ";", "：", ":", "？", "\\?", "！", "!", "“", "”", "\"", "（", "）", "\\(", "\\)", "……", "…", "——", "-", "《", "》");
        return String.join("|", punctuations);
    }

    public static void analysisNews(Path path, Map<String, Integer> freqMap) throws IOException {
        try (BufferedReader br = Files.newBufferedReader(path)) {
            String str = String.join("", br.lines().collect(Collectors.toList()));
            Map<String, String> map = gson.fromJson(str, Map.class);
            String title = map.get("title");
            String content = map.get("content");
            String[] ss = content.split(punRegexp);
            for (String s : ss) {
                if (isNotBlank(s)) {
                    List<String> stringList = WordAnalysis.analysis(s);
                    for (int i = 0; i < stringList.size() - 1; i++) {
                        String key = stringList.get(i) + "-" + stringList.get(i + 1);
                        freqMap.putIfAbsent(key, 0);
                        freqMap.put(key, freqMap.get(key) + 1);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void analysisSogouData() throws IOException {
        Map<String, Integer> freqMap = new HashMap<>();
        String path = "/opt/data/rawcorpus/sogou";
        WordAnalysis.simplifyWords();
        AtomicInteger ai = new AtomicInteger();
        Files.walkFileTree(Paths.get(path), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException {
                int current = ai.incrementAndGet();
                System.out.println("analysis:" + file);
                analysisSogouNews(file, freqMap);
                if (freqMap.size() > 10000000) {
                    saveData(freqMap, current, "sogou");
                    freqMap.clear();
                }
                return FileVisitResult.CONTINUE;
            }
        });
        String store = path + "/combineWordFreq.json";
        String str = gson.toJson(freqMap);
        Files.createFile(Paths.get(store));
        Files.write(Paths.get(store), str.getBytes(StandardCharsets.UTF_8));
    }

    public static void analysisZhihuData() throws IOException {
        Map<String, Integer> freqMap = new HashMap<>();
        String path = "/opt/data/rawcorpus/zhihu.com";
        WordAnalysis.simplifyWords();
        AtomicInteger ai = new AtomicInteger();
        Files.walkFileTree(Paths.get(path), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException {
                int current = ai.incrementAndGet();
                System.out.println("analysis:" + file);
                analysisZhihu(file, freqMap);
                if (freqMap.size() > 10000000) {
                    saveData(freqMap, current, "zhihu");
                    freqMap.clear();
                }
                return FileVisitResult.CONTINUE;
            }
        });
        String store = path + "/combineWordFreq.json";
        String str = gson.toJson(freqMap);
        Files.createFile(Paths.get(store));
        Files.write(Paths.get(store), str.getBytes(StandardCharsets.UTF_8));
    }

    public static void analysisZhihu(Path path, Map<String, Integer> freqMap) throws IOException {
        try (BufferedReader br = Files.newBufferedReader(path)) {
            Gson gson = new Gson();
            List<String> strings = br.lines().collect(Collectors.toList());
            String str = String.join(" ", strings);
            Question question = gson.fromJson(str, Question.class);
            List<String> stringLst = new ArrayList<>();
            stringLst.addAll(question.getAnswers().stream().map(Answer::getContent).collect(Collectors.toList()));
            for (String string : stringLst) {
                String[] ss = string.split(punRegexp);
                for (String s : ss) {
                    if (isNotBlank(s)) {
                        List<String> stringList = WordAnalysis.analysis(s);
                        for (int i = 0; i < stringList.size() - 1; i++) {
                            String key = stringList.get(i) + "-" + stringList.get(i + 1);
                            freqMap.putIfAbsent(key, 0);
                            freqMap.put(key, freqMap.get(key) + 1);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void analysisSogouNews(Path path, Map<String, Integer> freqMap) throws IOException {
        try (BufferedReader br = Files.newBufferedReader(path, Charset.forName("GBK"))) {
            List<String> strings = br.lines().collect(Collectors.toList());
            List<News> newsList = XmlConvert.extractNews(strings);
            for (News news : newsList) {
                String[] ss = news.getContent().split(punRegexp);
                for (String s : ss) {
                    if (isNotBlank(s)) {
                        List<String> stringList = WordAnalysis.analysis(s);
                        for (int i = 0; i < stringList.size() - 1; i++) {
                            String key = stringList.get(i) + "-" + stringList.get(i + 1);
                            freqMap.putIfAbsent(key, 0);
                            freqMap.put(key, freqMap.get(key) + 1);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void mains(String[] args) {
        System.out.println(getPunctuationReexp());
        String ss = "百度网盘(原百度云)是由百度公司出品的一款个人云服务产品,不仅为用户提供免费存储空间,还可以将视频、照片、文档、通讯录数据在移动设备和PC端之间跨平台同步、备份等.还支持添加好友、创建群组,和伙伴们快乐分享,已上线：Android、iPhone、iPad、PC端、网页端等.";
        String[] sss = ss.split(getPunctuationReexp());
        System.out.println(Arrays.asList(sss));
    }


}
