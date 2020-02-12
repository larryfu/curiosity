package cn.larry.demo;

import java.util.ArrayList;
import java.util.List;

/**
 * 从0到n中有多少个包含了1的数字
 */
public class OneNumber {

    public static void main(String[] args) {

        System.out.println(oneNum(69865391));
    }

    private static int oneNum(int num){
        if(num <1)return 0;
        if(num<10)return 1;
        int n = (int)Math.log10(num);
        int base = (int)Math.pow(10,n);
        int h =  num / base;
        if(h==1) {
            return  num - base + 1 +baseNum(n-1);
        } else {
            int left = num % base;
            return (h-1)*baseNum(n-1)+ base + oneNum(left);
        }
    }

    static final List<Integer>  baseNums = new ArrayList<>();
    private static int baseNum(int n){
        if(baseNums.size() == 0){
            baseNums.add(0,1);
        }
        if(baseNums.size()<=n){
            for(int i = baseNums.size();i<=n;i++){
                baseNums.add(i,9*baseNums.get(i-1)+ (int)Math.pow(10,i));
            }
        }
        return baseNums.get(n);
    }
}
