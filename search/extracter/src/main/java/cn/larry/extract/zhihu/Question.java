package cn.larry.extract.zhihu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by larry on 16-8-13.
 */
public class Question {

    private int id;

    private String title;

    private String description;

    private List<String> tags;

    private List<Answer> answers;

    public Question() {
        this.tags = new ArrayList<>();
        this.answers = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
}
