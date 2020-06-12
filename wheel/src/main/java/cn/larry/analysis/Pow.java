package cn.larry.analysis;

import java.util.HashMap;
import java.util.Map;

public class Pow {


    public static void main(String[] args) {
        double d = pow(2,30);
        System.out.println(d);
    }

    public static double pow(double a,int n){
        if(n == 0)return 1;
        Map<Integer,Double> cache = new HashMap<>();
        double d = a;
        for(int i = 1;i<=n;i*=2){
            cache.put(i,d);
            d*=d;
        }
        int k = 0;
        double result = 1;
        while(n>0){
            int left = n%2;
            n = n/2;
            if(left ==1){
                result = result * cache.get(1<<k);
            }
            k++;
        }
        return result;
    }
}
