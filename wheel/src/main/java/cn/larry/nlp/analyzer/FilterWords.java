package cn.larry.nlp.analyzer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by larry on 16-12-11.
 */
public class FilterWords {
    public static void main(String[] args) throws IOException {
        String path = "/opt/data/rawcorpus/words-filter.txt";
        List<String> words = Files.readAllLines(Paths.get(path));
        words = words.stream().map(s -> s.split("\\s+")[0]).filter(s -> s.length() < 6).collect(Collectors.toList());
        //  words = words.stream().map(s -> s.split("\\s+")[0]).collect(Collectors.toList());
        String storePath = "/home/larry/main2012.dic";
        Path path1 = Paths.get(storePath);
        Files.createFile(path1);
        Files.write(path1, words);
    }
}
