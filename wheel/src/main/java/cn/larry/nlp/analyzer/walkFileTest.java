package cn.larry.nlp.analyzer;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Created by larry on 16-12-25.
 */
public class walkFileTest {
    public static void main(String[] args) throws IOException {
        String path = "/home/larry/nlp/words-freq";
        Files.walkFileTree(Paths.get(path), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException {
                System.out.println(file.getFileName());
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
