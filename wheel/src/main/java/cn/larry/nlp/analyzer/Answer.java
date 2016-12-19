package cn.larry.nlp.analyzer;


import java.time.LocalDate;

/**
 * Created by larry on 16-8-13.
 */
public class Answer {

    private int id;

    private int token;

    private String author; //回答者

    private String authorHref; //作者链接

    private String content; //回答内容

    private LocalDate time;  //回答时间

    private int likes;  //点赞数

    public int getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorHref() {
        return authorHref;
    }

    public void setAuthorHref(String authorHref) {
        this.authorHref = authorHref;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getTime() {
        return time;
    }

    public void setTime(LocalDate time) {
        this.time = time;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
}
