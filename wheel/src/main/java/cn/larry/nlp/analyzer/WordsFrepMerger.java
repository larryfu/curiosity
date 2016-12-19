package cn.larry.nlp.analyzer;

import com.google.gson.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by larry on 16-12-20.
 */
public class WordsFrepMerger {

    private static final Gson gson = new Gson();


    public static void main(String[] args) throws IOException {
        String dir = "/home/larry/nlp/words-freq";
        Map<String, Integer> wordNumMap = getWordNumMap();
        Map<Integer, Short> isMap = new HashMap<>();
        Map<Long, Short> lsMap = new HashMap<>();
        Files.walkFileTree(Paths.get(dir), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException {
                try (BufferedReader br = Files.newBufferedReader(file)) {
                    List<String> strings = br.lines().collect(Collectors.toList());
                    String str = String.join(" ", strings);
                    Map<String, Double> map = gson.fromJson(str, Map.class);
                    map.forEach((k, v) -> {
                        try {
                            String[] ss = k.split("-");
                            int n1 = wordNumMap.get(ss[0]);
                            int n2 = wordNumMap.get(ss[1]);
                            if (n1 < 0xffff & n2 < 0xffff) {
                                int combine = (n1 << 16) & n2;
                                Short orgin = isMap.get(combine);
                                if (orgin == null)
                                    orgin = 0;
                                short num = v.shortValue();
                                isMap.put(combine, (short) (orgin + num));
                            } else {
                                long combine = (long) n1 << 32 & (long) n2;
                                Short orgin = lsMap.get(combine);
                                if (orgin == null)
                                    orgin = 0;
                                short num = v.shortValue();
                                lsMap.put(combine, (short) (orgin + num));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }

    public static Map<String, Integer> getWordNumMap() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(WordsFrepMerger.class.getResourceAsStream("/simplify.dic")));
        List<String> stringList = bufferedReader.lines().collect(Collectors.toList());
        Map<String, Integer> numMap = new HashMap<>();
        for (int i = 0; i < stringList.size(); i++)
            numMap.put(stringList.get(i), i);
        return numMap;
    }
}
