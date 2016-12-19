package cn.larry.extract;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by larry on 16-7-17.
 */
public class XmlConvert {

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("/home/larry/nlp/sohunews");
        Gson gson = new Gson();
        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException {
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file.toFile()), "GBK"));
                    //List<String> lines = Files.newBufferedReader(file, Charset.forName("GBK")).lines().collect(Collectors.toList());
                    List<String> lines = br.lines().collect(Collectors.toList());
                    List<News> newsList = extractNews(lines);
                    List<String> jsonNews = newsList.stream().map(gson::toJson).collect(Collectors.toList());
                    String newPath = file.toString().replace(".txt", ".json");
                    Path path1 = Paths.get(newPath);
                    Files.createFile(path1);
                    Files.write(path1, jsonNews);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return FileVisitResult.CONTINUE;
            }
        });

    }

    public static List<News> extractNews(List<String> strings) {
        News news = new News();
        List<News> newsList = new ArrayList<>();
        for (String line : strings) {
            if (line.contains("<doc>")) {
                news = new News();
            } else if (line.contains("<url>")) {
                line = line.replace("<url>", "").replace("</url>", "").trim();
                news.setUrl(line);
            } else if (line.contains("<docno>")) {
                line = line.replace("<docno>", "").replace("</docno>", "").trim();
                news.setDocno(line);
            } else if (line.contains("<contenttitle>")) {
                line = line.replace("<contenttitle>", "").replace("</contenttitle>", "").trim();
                news.setTitle(line);
            } else if (line.contains("<content>")) {
                line = line.replace("<content>", "").replace("</content>", "").trim();
                news.setContent(line);
            } else if (line.contains("</doc>")) {
                newsList.add(news);
            }
        }
        return newsList;
    }


}
