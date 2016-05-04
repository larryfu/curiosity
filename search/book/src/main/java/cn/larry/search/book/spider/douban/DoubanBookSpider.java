package cn.larry.search.book.spider.douban;

import cn.larry.commons.util.HttpAgent;
import cn.larry.commons.util.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

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

    public static void main(String[] args) {
        new DoubanBookSpider().climbInTag("https://book.douban.com/tag/散文", "散文");
    }

    public void climbInTag(String href, String tagName) {
        Document doc = Jsoup.parse(agent.getPage(href));
        String pageNumStr = doc.select("div.paginator>a").last().text();
        int pageNum = Integer.parseInt(pageNumStr);
        for (int i = 0; i < pageNum; i++) {
            climbOnepage(i * 20, tagName);
        }
    }

    private void climbOnepage(int start, String tagName) {
        String href = BOOK_HOME + tagName + "?start=" + start + "&type=T";
        Document document = Jsoup.parse(agent.getPage(href));
        Elements elements = document.select("ul.subject-list>li");
        for (Element element : elements) {
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
            System.out.println(name + "," + href1 + "," + pubInfo + "," + score + "," + evalutePerson + "," + des);
        }
    }
}
