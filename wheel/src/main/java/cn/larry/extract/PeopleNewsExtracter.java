package cn.larry.extract;

import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by larry on 16-11-6.
 */
public class PeopleNewsExtracter {
    public static void main(String[] args) throws IOException {
        String basePath = "/opt/data/webpages/people";
        Files.walkFileTree(Paths.get(basePath), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException {
                System.out.println("deal path:" + file);
                if (file.toString().contains(".htm"))
                    save(extractContent(file), file);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private static void save(String s, Path file) {
        if (s == null) return;
        try {
            String path = file.toString();
            String newePath = path.toString().replace("webpages", "rawcorpus").replace(".html", ".json").replace(".htm", ".json");
            String dir = newePath.substring(0, newePath.lastIndexOf('/'));
            new File(dir).mkdirs();
            Files.createFile(Paths.get(newePath));
            Files.write(Paths.get(newePath), Arrays.asList(s));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static String extractContent(Path file) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file.toFile()), "gbk"));
            String html = String.join("\n", br.lines().collect(Collectors.toList()));
            Document document = Jsoup.parse(html);
            Map<String, String> map = new HashMap<>();
            String content = document.select("#rwb_zw").text();
            if (StringUtils.isBlank(content)) {
                content = document.select("#p_content").text();
            }
            map.put("content", content);
            map.put("title", document.title());
            Gson gson = new Gson();
            return gson.toJson(map);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String extractContents(Path file) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file.toFile()), StandardCharsets.UTF_8));
            String html = String.join("\n", br.lines().collect(Collectors.toList()));
            Document document = Jsoup.parse(html);
            Map<String, String> map = new HashMap<>();

            List<String> selectors = Arrays.asList("#rwb_zw", "#p_content", "div.fl.text_con_left > div.box_con");
            String content = null;
            for (String selector : selectors) {
                content = document.select(selector).text();
                if (!StringUtils.isBlank(content)) {
                    break;
                }
            }
            if (StringUtils.isBlank(content)) {
                System.out.println("can not found content " + document.title());
                return null;
            }
            map.put("content", content);
            map.put("title", document.title());
            Gson gson = new Gson();
            return gson.toJson(map);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
