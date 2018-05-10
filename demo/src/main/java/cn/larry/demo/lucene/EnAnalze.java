package cn.larry.demo.lucene;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class EnAnalze {
    public static void main(String[] args) throws IOException {
        String path = "";
        Path path1 = Paths.get(path);
        List<String> strings = new ArrayList<>();
        Files.lines(path1).forEach(s->{
               s = s.toLowerCase();
               s.replaceAll("\\. "," . ");
               s.replaceAll("\\?"," ? ");
               s.replaceAll(","," , ");
        });

       // List<String> stringList = Files.readAllLines(Paths.get(path));
        //for()
    }
}
