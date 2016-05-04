package cn.larry.commons.util;

/**
 * Created by larryfu on 2015/12/21.
 * @author larryfu  all rights reserved
 *
 */
public class MathUtil {

    private MathUtil() {
    }

    /**
     * 向量的夹角cos值计算
     * @param d1
     * @param d2
     * @return
     * @throws IllegalArgumentException
     */
    public static double vectorCos(int[] d1, int[] d2) throws IllegalArgumentException {
        if (d1.length != d2.length)
            throw new IllegalArgumentException("length not equal");
        double l1 = 0.0, l2 = 0.0;
        long accumunate = 0;
        for (int i = 0; i < d1.length; i++) {
            l1 += d1[i] * d1[i];
            l2 += d2[i] * d2[i];
            accumunate +=d1[i]*d2[i];
        }
        l1 = Math.sqrt(l1);
        l2 = Math.sqrt(l2);
        return (double)accumunate/(l1*l2);
    }
}
