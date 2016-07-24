package cn.larry.extract;

import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by larry on 16-7-23.
 */
public class QQNewsExtracter {

    public static void main(String[] args) throws IOException {
        String basePath = "/opt/data/pages/qq.com/rawpages";
        Files.walkFileTree(Paths.get(basePath), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException {
                System.out.println("deal path:" + file);
                save(extractContent(file), file);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private static void save(String s, Path file) {
        if (s == null) return;
        try {
            String path = file.toString();
            String newePath = path.toString().replace("rawpages/", "").replace(".qq.com", "").replace(".htm", ".json").replace(".html", ".json");
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
            map.put("content", document.select("#Cnt-Main-Article-QQ>p").text());
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

    public static void maines(String[] args) throws IOException {
        String path = "/opt/data/pages/qq.com/news/a";
        //  String dest = "/opt/data/pages/qq.com/news/a/20160703/011343.htm";
        Path path1 = Paths.get(path);
        // Path path2 = Paths.get(dest);
        Gson gson = new Gson();
        Files.walkFileTree(path1, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException {
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file.toFile()), "gb2312"));
                    List<String> lines = br.lines().collect(Collectors.toList()); //Files.readAllLines(path1, Charset.forName("gb2312"));
                    String html = String.join("\n", lines);
                    Document document = Jsoup.parse(html);
                    Elements elements = document.select("script");
                    for (Element ele : elements) {
                        ele.remove();
                    }
                    Elements eles = document.select("style");
                    for (Element ele : eles) {
                        ele.remove();
                    }
                    String content = document.select("#Cnt-Main-Article-QQ>p").text();
                    String title = document.title();
                    Map<String, String> map = new HashMap<>();
                    map.put("title", title);
                    map.put("content", content);
                    String text = gson.toJson(map);

                    //saveContent(text,path);


                    String destPath = file.toString().replace("/a/", "/a3/");
                    String dir = destPath.substring(0, destPath.lastIndexOf('/'));
                    // Files.createDirectories(dir,new );
                    File file1 = new File(dir);
                    file1.mkdirs();
                    Files.createFile(Paths.get(destPath));
                    Files.write(Paths.get(destPath), Arrays.asList(text));
//                    file1.mkdirs();
//                    Path path4 = Paths.get(destPath);
//                    Files.createFile(path4);
//                    String str = document.html();
//                    String[] lins = str.split("\n");
//                    List<String> validLines = new ArrayList<>();
//                    for (String line : lins) {
//                        if (!(line.contains("<!--") && line.contains("-->"))) {
//                            validLines.add(line);
//                        }
//                    }
//                    Files.write(path4, validLines);

                    //  System.out.println("pre process file " + path4);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //System.out.println(document.text());
                //String html = String.join("/n", lines);
                //System.out.println(HtmlAnalys.parseHtml(html));
                //  lines.forEach(System.out::println);
//            System.out.println(path2);
//            if (Files.isRegularFile(path2)) {
//                System.out.println("a readable file :" + path2);
//                System.out.println(HtmlAnalys.removeExecludeTag(getContent(path2)));
//            }

                // });

                return FileVisitResult.CONTINUE;
            }
        });
    }


    public static void mains(String[] args) throws IOException {
        Document document = Jsoup.parse(new URL("http://news.qq.com/a/20160723/023609.htm"), 10000);
        String text = document.select("#Cnt-Main-Article-QQ>p").text();
        System.out.println();
    }
}
