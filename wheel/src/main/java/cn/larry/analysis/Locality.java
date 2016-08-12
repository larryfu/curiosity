package cn.larry.analysis;

/**
 * Created by fugz on 2016/8/1.
 */
public class Locality {
    public static void main(String[] args) {
        int[][] ints = new int[10000][10000];
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            for (int j = 0; j < 10000; j++) {
                ints[j][i] = i * j;
            }
        }
        System.out.println("j i  cost:" + (System.currentTimeMillis() - start));
        start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            for (int j = 0; j < 10000; j++) {
                ints[i][j] = i * j;
            }
        }
        System.out.println(" i j cost:" + (System.currentTimeMillis() - start));
    }
}
