package cn.larry.test;

/**
 * Created by larry on 16-7-10.
 */
public class Super {

    private String name;

    public Super(String name){
        this.name = name;
    }

    protected String getName(){
        return name;
    }
}
