package cn.larry.nlp.datawash;

/**
 * Created by fugz on 2016/7/18.
 */
public class Village {

    private int number;

    private String code;

    private String type;

    private String name;

    public Village(String code, String name, String type) {
        this.code = code;
        this.name = name;
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return "code:" + code + ",name:" + name + ",type:" + type;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
