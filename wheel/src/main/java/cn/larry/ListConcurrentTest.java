package cn.larry;

import java.util.List;

public class ListConcurrentTest {

    public static void main(String[] args) {


    }

    public void getAndAdd(List<String> strings) {
        strings.add("hello");
        for (int i = 0; i < 100; i++) {
            strings.get(strings.size() - 1);
        }
    }
}
