package cn.larry.search.book.spider.douban;

/**
 * Created by larryfu on 16-5-4.
 */
public class BookTag {
    public String href;
    public String superTag;
    public String tag;

    public BookTag() {

    }

    public BookTag(String href, String superTag, String tag) {
        this.href = href;
        this.superTag = superTag;
        this.tag = tag;
    }
}
