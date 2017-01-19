package cn.larry.nlp.analyzer;

import com.google.gson.*;
import redis.clients.jedis.Jedis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Created by larry on 16-12-20.
 */
public class RedisBasedLanModel implements LanModel {

    private static final Gson gson = new Gson();

    private static final Jedis jedis = new Jedis("127.0.0.1");

    private Map<String, Integer> wordNumMap;

    private Map<Integer, String> numWordMap;

    private String combineKey = "word-freq-total";
    private String wordKey = "single-word-freq";
    private String combineTotal = "total-combine";
    private String wordCombineNum = "word-combine-number";
    private String dir = "/home/larry/nlp/words-freq";


    public RedisBasedLanModel() {
        wordNumMap = getWordNumMap();
        numWordMap = convertMap(wordNumMap);
    }

    private <K, V> Map<V, K> convertMap(Map<K, V> wordNumMap) {
        Map<V, K> newMap = new HashMap<>();
        for (Map.Entry<K, V> entry : wordNumMap.entrySet())
            newMap.put(entry.getValue(), entry.getKey());
        return newMap;
    }

    public static void main(String[] args) throws IOException {
        RedisBasedLanModel lanModel = new RedisBasedLanModel();
        lanModel.caculate();
    }

    public void caculateCombineTotal() {
        long total = 0;
        Set<String> ss = jedis.hkeys(wordKey);
        for (String s : ss) {
            String v = jedis.hget(wordKey, s);
            total += Integer.parseInt(v);
        }
        jedis.set(combineTotal, total + "");
        System.out.println(combineTotal);
    }


    public void caculate() throws IOException {
        Map<String, Integer> wordNumMap = getWordNumMap();
        AtomicLong total = new AtomicLong(0);
        Map<Integer, Set<Integer>> combineMap = new HashMap<>();
        Files.walkFileTree(Paths.get(dir), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException {
                System.out.println("analysis:" + file);
                try (BufferedReader br = Files.newBufferedReader(file)) {
                    List<String> strings = br.lines().collect(Collectors.toList());
                    String str = String.join(" ", strings);
                    Map<String, Double> map = gson.fromJson(str, Map.class);

                    map.forEach((k, v) -> {
                        try {
                            String[] ss = k.split("-");
                            Integer n1 = wordNumMap.get(ss[0]);
                            Integer n2 = wordNumMap.get(ss[1]);
                            combineMap.putIfAbsent(n1, new HashSet<>());
                            combineMap.get(n1).add(n2);
                            short num = v.shortValue();
                            //jedis.sadd(n1 + "", n2 + "");
                            // jedis.hincrBy(wordKey, n1 + "", num);
                            total.addAndGet(num);
//                            if (n1 != null && n2 != null)
//                                if (n1 < 0xffff && n2 < 0xffff) {
//                                    int combine = (n1 << 16) | n2;
//                                    //  jedis.hincrBy(combineKey, combine + "", num);
//                                } else {
//                                    long combine = ((long) n1) << 32 | (long) n2;
//                                    //  jedis.hincrBy(combineKey, combine + "", num);
//                                }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });

                }
                return FileVisitResult.CONTINUE;
            }
        });
        //Map<Integer, Integer> wordCombineNumMap = new HashMap<>();
        List<String> strings = new ArrayList<>();
        combineMap.forEach((k, v) -> {
            // wordCombineNumMap.put(k, v.size());
            strings.add(k + "-" + v.size());
            // jedis.hset(wordCombineNumKey, k + "", v.size() + "");
        });
        String store = "/home/larry/nlp/word-combine-number.txt";
        Path path = Paths.get(store);
        Files.createFile(path);
        Files.write(path, strings);
        System.out.println(total.longValue());
        long totalNum = 2045190065;

        //jedis.set(combineTotal, total.longValue() + "");
        System.out.println("total statistic combine:" + total.longValue());
    }

    public double combineProbability(String start, String end) {
        int combineFreq = getCombineFreq(start + "-" + end);
        int singleFrep = getWordFreq(start);
        long wordsSize = getWordSize();
        int wordCombineNum = getWordCombineNum(start);

        //分配给在语料库中出现的词组的频率的比例 保证未出现的词组频率 < 出现一次的词组的频率
        double presentRate = (double) (10 * singleFrep) / (double) (10 * singleFrep + wordsSize - wordCombineNum);

        if (singleFrep == 0) {
            return getUnpresent();
        }

        if (combineFreq == 0)
            return (1 - presentRate) / (double) (wordsSize - wordCombineNum);

        return ((double) combineFreq / (double) singleFrep) * presentRate;
    }


    public double combineProbability(String combine) {
        String start = combine.trim().split("-")[0];
        int combineFreq = getCombineFreq(combine);
        int singleFrep = getWordFreq(start);
        int endFreq = getWordFreq(combine.trim().split("-")[1]);
        long wordsSize = getWordSize();
        long totalCombine = getCombineTotal();
        if (singleFrep == 0)
            singleFrep = (int) (totalCombine / (wordsSize * 10));
        if (endFreq == 0)
            endFreq = (int) (totalCombine / (wordsSize * 10));
        int totalFreq = singleFrep + endFreq;
        System.out.println(combine + ":" + combineFreq + ",total:" + totalFreq);
        //为避免出现概率为0的情况，将未出现的组合出现次数视为1，出现了的次数则视为原始的8倍
        if (combineFreq == 0) {
            return Math.sqrt(Math.sqrt(totalFreq)) / (wordsSize * 10);
        }
        return (double) (combineFreq * 4 + 1) / (double) (singleFrep * 4 + wordsSize);
    }

    private int getWordCombineNum(String start) {
        Integer num = wordNumMap.get(start);
        if (num == null)
            return 0;
        else {
            String result = jedis.hget(wordCombineNum, num + "");
            return parseInt(result);
        }
    }

    private long getWordSize() {
        return jedis.hlen(wordKey);
    }

    private long getCombineTotal() {
        return Long.parseLong(jedis.get(combineTotal));
    }

    public int getCombineFreq(String combine) {
        String[] ss = combine.split("-");
        if (ss.length < 2) {
            throw new IllegalArgumentException();
        }
        int num = combine2Int(combine);
        if (num == 0) {
            long l = combine2Long(combine);
            return parseInt(jedis.hget(combineKey, l + ""));
        } else {
            return parseInt(jedis.hget(combineKey, num + ""));
        }
    }

    private int parseInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return 0;
        }
    }

    public double wordProbability(String word) {
        long total = getLong(combineTotal);
        int freq = getWordFreq(word);
        if (freq == 0) {
            freq = 100;
        }
        return (double) freq / (double) total;
    }

    public int getWordFreq(String word) {
        Integer num = wordNumMap.get(word);
        if (num == null) {
            return 0;
        }
        String result = jedis.hget(wordKey, num + "");
        return parseInt(result);
    }

    private long getLong(String key) {
        String s = jedis.get(key);
        if (s == null || s.equals("null"))
            return 0;
        return Long.parseLong(s);
    }

    public String number2words(long num) {
        int n1, n2;
        if (num >= ((long) 1 << 32)) {
            n1 = (int) (num >> 32);
            n2 = (int) (num & 0xffffffffL);
        } else {
            n1 = (int) (num >> 16);
            n2 = (int) (num & 0xffffL);
        }
        String s1 = numWordMap.get(n1);
        String s2 = numWordMap.get(n2);
        return s1 + "-" + s2;
    }

    private int combine2Int(String words) {
        String[] ss = words.split("-");
        if (ss.length < 2)
            throw new IllegalArgumentException();
        Integer n1 = wordNumMap.get(ss[0]);
        Integer n2 = wordNumMap.get(ss[1]);
        if (n1 != null && n2 != null) {
            if (n1 < 0xffff && n2 < 0xffff) {
                int combine = (n1 << 16) | n2;
                return combine;
            }
        }
        return 0;
    }

    private long combine2Long(String words) {
        String[] ss = words.split("-");
        if (ss.length < 2)
            throw new IllegalArgumentException();
        Integer n1 = wordNumMap.get(ss[0]);
        Integer n2 = wordNumMap.get(ss[1]);
        if (n1 != null && n2 != null) {
            long combine = ((long) n1 << 32) | (long) n2;
            return combine;
        }
        return 0;
    }


    public static Map<String, Integer> getWordNumMap() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(RedisBasedLanModel.class.getResourceAsStream("/simplify.dic")));
        List<String> stringList = bufferedReader.lines().collect(Collectors.toList());
        Map<String, Integer> numMap = new HashMap<>();
        for (int i = 0; i < stringList.size(); i++)
            numMap.put(stringList.get(i), i);
        return numMap;
    }

    public double getUnpresent() {
        String result = jedis.hget(wordKey, "1");
        return (double) 1 / (double) (16 * parseInt(result));
    }
}
