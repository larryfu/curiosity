package cn.larry;

import java.util.concurrent.ConcurrentHashMap;

public class HashTest {

    public static void main(String[] args) {
        ConcurrentHashMap<String ,String> map = new ConcurrentHashMap<>();
        int h = Integer.MIN_VALUE  + 123121;
        System.out.println(h);
        System.out.println(spread(h));

    }
    static final int HASH_BITS = 0x7fffffff; // usable bits of normal node hash
    public static int  spread(int h){
        return   (h ^ (h >>> 16)) & HASH_BITS;
    }
}
