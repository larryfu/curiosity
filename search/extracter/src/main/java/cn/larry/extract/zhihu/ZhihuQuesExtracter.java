package cn.larry.extract.zhihu;

import cn.larry.commons.util.RegexUtils;
import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Exchanger;

/**
 * Created by larry on 16-8-13.
 */
public class ZhihuQuesExtracter {

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("/opt/data/webpages/www.zhihu.com/question");
        String dir = "/opt/data/rawcorpus/zhihu.com";
        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException {
                extractQuestion(file, dir);
                return FileVisitResult.CONTINUE;
            }
        });
        System.out.println();
    }

    public static Question extractQuestion(Path path, String soreDir) {
        try {
            if (path.toString().endsWith("collections"))
                return null;
            System.out.println("process :" + path);
            Gson gson = new Gson();
            Document document = Jsoup.parse(path.toFile(), "UTF-8");
            Question question = new Question();
            Elements tagElements = document.select("a.zm-item-tag");
            List<String> tags = new ArrayList<>();
            String id = path.getFileName().toString().split("\\?")[0];
            id = id.replace(".", "");
            question.setId(Integer.parseInt(id));
            tagElements.forEach(element -> {
                tags.add(element.text());
            });
            question.setTags(tags);
            question.setTitle(document.select("div#zh-question-title").text());
            question.setDescription(document.select("div#zh-question-detail>div").text());
            Elements answers = document.select("div.zm-item-answer");
            answers.forEach(element -> {
                Answer answer = extractAnswer(element);
                if (answer != null)
                    question.getAnswers().add(answer);
            });
            String store = soreDir + "/question/" + question.getId() + ".json";
            if (path.toString().contains("answer")) {
                store = soreDir + "/answer/" + path.toString().split("question")[1];
                Path path1 = Paths.get(store).getParent();
                Files.createDirectories(path1);
            }
            Files.createFile(Paths.get(store));
            Files.write(Paths.get(store), gson.toJson(question).getBytes(StandardCharsets.UTF_8));
            return question;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static LocalDate parseDate(String text) {
        String txt = RegexUtils.getFirstMatch("\\d{4}-\\d{2}-\\d{2}", text);
        return LocalDate.parse(txt);
    }

    private static Answer extractAnswer(Element element) {
        try {
            Answer answer = new Answer();
            answer.setToken(Integer.parseInt(element.attr("data-atoken")));
            answer.setId(Integer.parseInt(element.attr("data-aid")));
            answer.setAuthor(element.select("a.author-link").text());
            answer.setAuthorHref(element.select("a.author-link").attr("href"));
            String like = element.select("div.zm-votebar span.count").text();
            like = like.replaceAll("k|K", "000");
            answer.setLikes(Integer.parseInt(like));
            answer.setTime(parseDate(element.select("div.zm-item-meta a.answer-date-link").text()));
            answer.setContent(element.select("div.zm-item-rich-text div.zm-editable-content").text());
            return answer;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
