package cn.larry.search.book.index;


import cn.larry.commons.util.RegexUtils;
import cn.larry.search.book.bean.Book;
import cn.larry.search.book.bean.BookData;
import com.google.gson.Gson;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Created by larryfu on 16-6-7.
 */
public class TransferData {

    private static final Gson gson = new Gson();

    private static final Random random = new Random();

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("/home/larryfu/books/文学.json");
        List<Book> books = new TransferData().getBookFromFile(path);
        System.out.println();
    }

    public List<Book> getBooksFromDir(String dir) throws IOException {
        List<Book> books = new ArrayList<>();
        Files.list(Paths.get(dir)).forEach(file -> {
            try {
                books.addAll(getBookFromFile(file));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return books;
    }

    private List<Book> getBookFromFile(Path path) throws IOException {
        List<String> lines = Files.readAllLines(path);
        List<Book> books = lines.stream()
                .map(TransferData::fromJson).filter(d -> d != null)
                .map(TransferData::data2Book).filter(b -> b != null)
                .collect(Collectors.toList());
        String category = path.toFile().getName().split("\\.")[0];
        books.forEach(b -> b.setCategory(category));
        return books;
    }

    private static BookData fromJson(String s) {
        try {
            return gson.fromJson(s, BookData.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Book data2Book(BookData data) {
        try {
            Book book = new Book();
            book.setId(random.nextInt(Integer.MAX_VALUE));
            List<String> aus = new ArrayList<>();
            if (data.authors != null)
                for (String au : data.authors)
                    if (au != null)
                        aus.add(au.trim());
            book.setAuthors(aus);
            book.setMark(Double.parseDouble(data.score));
            book.setName(data.name.trim());
            book.setDescription(data.introduction.trim());
            book.setPress(data.press.trim());
            book.setPrice(extractDouble(data.price));
            book.setEvaluaters(extractInt(data.evalutePerson));
            book.setDetailHref(data.detailHref.trim());
            book.setPressTime(parsePressTime(data.pressYear));
            return book;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static double extractDouble(String s) {
        Pattern pattern = Pattern.compile("\\d+\\.\\d+");
        Matcher matcher = pattern.matcher(s);
        if (matcher.find()) {
            return Double.parseDouble(matcher.group());
        }
        return 0.0;
    }

    private static int parsePressTime(String time) {
        LocalDate date = getDate(time);
        if (date == null)
            return 0;
        return Integer.parseInt(date.format(DateTimeFormatter.ofPattern("yyyyMM")));
    }

    public static LocalDate getDate(String date) {
        if (date == null || date.equals("至今") || isBlank(date))
            return null;
        if (date.length() >= 10) {
            int year = parseInt(date.substring(0, 4));
            int month = parseInt(date.substring(5, 7));
            int dat = parseInt(date.substring(8, 10));
            return LocalDate.of(year, month, dat);
        } else {
            String year = RegexUtils.getFirstMatch("\\d{4}\\D?", date);
            if (year == null)
                return null;
            date = date.substring(year.length());
            year = year.substring(0, 4);
            List<String> timeNums = RegexUtils.getMatch("\\d{1,2}", date);
            int len = timeNums.size();
            String[] nums = {"1", "1"};
            for (int i = 0; i < len && i < 5; i++) {
                String str = timeNums.get(i);
                nums[i] = str;
            }
            return LocalDate.of(parseInt(year), parseInt(nums[0]), parseInt(nums[1]));
        }
    }

    private static int extractInt(String s) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(s);
        if (matcher.find()) {
            return parseInt(matcher.group());
        }
        return 0;
    }

}
