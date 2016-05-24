package cn.larry.search.book.spider.douban;

import cn.larry.commons.util.HttpAgent;
import cn.larry.commons.util.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

/**
 * Created by larryfu on 16-4-27.
 */
public class DoubanBookSpider {

    public static final String BOOK_HOME = "https://book.douban.com/tag/";

    public static final String DOUBAN_HOME = "https://book.douban.com";

    private HttpAgent agent = new HttpAgent();


    private static BlockingQueue<String> bookTagQueue = new ArrayBlockingQueue<>(100);

    public void climbTags(String[] args) throws IOException {
        Document doc = Jsoup.parse(agent.getPage(BOOK_HOME));
        Elements elements = doc.select("div.article>div>div");
        System.out.println();
        List<BookTag> tags = new ArrayList<>();
        elements.forEach(element -> {
            String superTag = element.select("a.tag-title-wrapper").attr("name");
            element.select("table a.tag").forEach(ele -> {
                BookTag bt = new BookTag(DOUBAN_HOME + ele.attr("href"), superTag, ele.text());
                tags.add(bt);
            });
        });
        Path path = Paths.get("/home/larryfu/Desktop/booktag.json");
        List<String> tagStrs = new ArrayList<>();
        tags.forEach(bookTag -> {
            try {
                tagStrs.add(JsonUtils.writeValueAsString(bookTag));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
        Files.createFile(path);
        Files.write(path, tagStrs);
    }

    public static void main(String[] args) throws IOException {
        List<String> strs = Files.readAllLines(Paths.get("/home/larryfu/Desktop/booktag.json"));
        DoubanBookSpider spider = new DoubanBookSpider();
        strs.stream().map(s -> {
            try {
                return JsonUtils.readValue(s, BookTag.class);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }).filter(b -> b != null).forEach(b -> {
            try {
                spider.climbInTag(b.href, b.tag);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void climbInTag(String href, String tagName) throws IOException {
        System.out.println("climb in tag :" + tagName);
        Document doc = Jsoup.parse(agent.getPage(href));
        String pageNumStr = doc.select("div.paginator>a").last().text();
        int pageNum = Integer.parseInt(pageNumStr);
        List<DoubanBookInfo> infos = new ArrayList<>();
        for (int i = 0; i < pageNum; i++) {
            infos.addAll(climbOnepage(i * 20, tagName));
        }
        Path file = Paths.get("/home/larryfu/books/" + tagName + ".json");
        Path path = Files.createFile(file);

        List<String> lines = infos.stream().map(bi -> {
            try {
                return JsonUtils.writeValueAsString(bi);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        }).filter(i -> i != null).collect(Collectors.toList());
        Files.write(path, lines, StandardCharsets.UTF_8);
    }

    private List<DoubanBookInfo> climbOnepage(int start, String tagName) {

        String href = BOOK_HOME + tagName + "?start=" + start + "&type=T";
        System.out.println();
        System.out.println("climb one page :" + href);
        Document document = Jsoup.parse(agent.getPage(href));
        Elements elements = document.select("ul.subject-list>li");
        List<DoubanBookInfo> bookInfos = new ArrayList<>();
        for (Element element : elements) {
            try {
                Elements title = element.select("div.info>h2>a");
                Elements pub = element.select("div.info>div.pub");
                Elements evalute = element.select("div.info>div.star");
                Elements description = element.select("div.info>p");
                String name = title.text();
                String href1 = title.attr("href");
                String pubInfo = pub.text();
                String score = evalute.select("span.rating_nums").text();
                String evalutePerson = evalute.select("span.pl").text();
                String des = description.text();
                DoubanBookInfo bi = new DoubanBookInfo();
                bi.name = StringUtils.trim(name);
                bi.introduction = StringUtils.trim(des);
                bi.score = StringUtils.trim(score);
                String[] infos = pubInfo.split("/");
                String price = StringUtils.trim(infos[infos.length - 1]);
                bi.price = StringUtils.trim(price);
                String pubTime = infos[infos.length - 2];
                bi.pressYear = StringUtils.trim(pubTime);
                String press = StringUtils.trim(infos[infos.length - 3]);
                bi.press = press;
                bi.authors = new ArrayList<>();
                for (int i = infos.length - 4; i >= 0; i--) {
                    bi.authors.add(infos[i]);
                }
                bi.detailHref = StringUtils.trim(href1);
                bi.evalutePerson = StringUtils.trim(evalutePerson);
                System.out.print(name+",");
               // System.out.println(name + "," + href1 + "," + pubInfo + "," + score + "," + evalutePerson + "," + des);
                bookInfos.add(bi);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return bookInfos;
    }
}
