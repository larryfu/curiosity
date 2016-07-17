package cn.larry;

import cn.larry.test.Sub2;
import cn.larry.test.Super;

/**
 * Created by larry on 16-7-10.
 */
public class Sub1 extends Super {
    public Sub1(String name) {
        super(name);
    }

    public static void main(String[] args) {
        int a = 128;
        for(int i=31;i>=0;i--){
            System.out.print((a >> i) %2);
        }
        System.out.println();
        int b = -128;
        for(int i=31;i>=0;i--){
            System.out.print(Math.abs((b >> i) % 2));
        }
    }
}
