package cn.larry.extract;

import cn.larry.commons.util.HtmlAnalys;
import cn.larry.commons.util.RegexUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by larry on 16-7-10.
 */
public class HtmlContentExtracter {

    public static void mains(String[] args) throws IOException {
        Path file = Paths.get("/opt/data/pages/qq.com/news/a/20160629/005855.htm");
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file.toFile()), "gb2312"));
        List<String> lines = br.lines().collect(Collectors.toList()); //Files.readAllLines(path1, Charset.forName("gb2312"));
        List<String> validLines = new ArrayList<>();
        for (String line : lines) {
            if (!(line.contains("<!--") && line.contains("-->"))) {
                validLines.add(line);
            }
        }
        String html = String.join("\n", validLines);
        Document document = Jsoup.parse(html);
        Elements elements = document.select("script");
        for (Element ele : elements) {
            ele.remove();
        }
        Elements eles = document.select("style");
        for (Element ele : eles) {
            ele.remove();
        }
        String destPath = file.toString().replace("/a/", "/a2/");

        String dir = destPath.substring(0, destPath.lastIndexOf('/'));
        // Files.createDirectories(dir,new );
        File file1 = new File(dir);
        file1.mkdirs();
        Path path4 = Paths.get(destPath);
        Files.createFile(path4);
        Files.write(path4, Arrays.asList(document.html()));
    }

    public static void main(String[] args) throws IOException {
        String path = "/opt/data/pages/qq.com/news/a";
        String dest = "/opt/data/pages/qq.com/news/a/20160703/011343.htm";
        Path path1 = Paths.get(path);
        Path path2 = Paths.get(dest);
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
                    String destPath = file.toString().replace("/a/", "/a2/");

                    String dir = destPath.substring(0, destPath.lastIndexOf('/'));
                    // Files.createDirectories(dir,new );
                    File file1 = new File(dir);
                    file1.mkdirs();
                    Path path4 = Paths.get(destPath);
                    Files.createFile(path4);
                    String str = document.html();
                    String[] lins = str.split("\n");
                    List<String> validLines = new ArrayList<>();
                    for (String line : lins) {
                        if (!(line.contains("<!--") && line.contains("-->"))) {
                            validLines.add(line);
                        }
                    }
                    Files.write(path4, validLines);

                    System.out.println("pre process file " + path4);
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

    public static String getContent(Path path) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(path.toFile()), "ISO-8859-1"))) {
            in.lines().forEach(sb::append);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return transferCharset(sb.toString());
    }

    private static String transferCharset(String html) {
        String charset = RegexUtils.getFirstMatch("(?:charset=)[^\"^;^']*", html);
        if (charset != null && charset.length() > 8) {
            charset = charset.substring(8);
        } else {
            charset = RegexUtils.getFirstMatch("charset=\"[^\"^;^']*\"", html);
            if (charset != null && charset.length() > 10)
                charset = charset.substring(8).replace("\"", "");
        }
        if (charset == null)
            charset = "UTF-8";

        //  logger.info("transfer charset:" + charset);
        byte[] bytes = html.getBytes(StandardCharsets.ISO_8859_1);
        try {
            return new String(bytes, charset);
        } catch (UnsupportedEncodingException e) {
            //   logger.info("unsuppoted  charset:" + charset + ",return as UTF-8");
            e.printStackTrace();
            return new String(bytes, StandardCharsets.UTF_8);
        }
    }
}
