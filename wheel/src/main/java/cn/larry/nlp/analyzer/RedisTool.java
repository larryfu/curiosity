package cn.larry.nlp.analyzer;

import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by larry on 17-1-16.
 */
public class RedisTool {

    public static void main(String[] args) throws IOException {
        loadData();
    }

    public static void loadData() throws IOException {
        String store = "/home/larry/nlp/word-combine-number.txt";
        Jedis jedis = new Jedis("127.0.0.1");
        String wordCombineNumKey = "word-combine-number";
        List<String> stringList = Files.newBufferedReader(Paths.get(store)).lines().collect(Collectors.toList());
        stringList.forEach(s -> {
            String[] ss = s.split("-");
            if (ss.length > 1) {
                jedis.hset(wordCombineNumKey, ss[0], ss[1]);
            }
        });
    }

    public static void clearKey() {
        Jedis jedis = new Jedis("127.0.0.1");
        for (int i = 0; i < 200000; i++) {
            System.out.println("del:" + i);
            jedis.del(i + "");
        }
    }
}
