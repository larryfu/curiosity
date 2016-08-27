package cn.larry.demo.guice;

/**
 * Created by larry on 16-8-27.
 */
public class SimpleAdd implements Add {
    @Override
    public int add(int a, int b) {
        return a + b;
    }
}
