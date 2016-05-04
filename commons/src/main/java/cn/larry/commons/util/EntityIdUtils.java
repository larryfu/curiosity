package cn.larry.commons.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author larryfu
 */
public class EntityIdUtils {

    private EntityIdUtils() {
    }

    public static String generateResumeId() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 2; i++) {
            int l = RandomUtils.nextInt(26);
            char c = (char) (l + 'a');
            sb.append(c);
        }
        for (int i = 0; i < 5; i++) {
            int n = RandomUtils.nextInt(10);
            sb.append(n + "");
        }
        return sb.toString();
    }

//    public static void main(String[] args) {
//        for (int i = 0; i < 100; i++)
//            System.out.println(transferId(generateId()));
//    }

    private static char toLowerChar(char c) {
        while (c > 'z')
            c -= 26;
        while (c < 'a')
            c += 26;
        return c;
    }

    public static String transferId(String id) {
        char c1 = id.charAt(1);
        char c2 = id.charAt(2);
        c1 = toLowerChar(c1);
        c2 = toLowerChar(c2);
        StringBuilder sb = new StringBuilder(c1 + "" + c2);

        for (int i = 0; i < 5; i++)
            sb.append(RandomUtils.nextInt(10) + "");
        return sb.toString();
    }


    public static long getSecondLength() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date2014 = sdf.parse("2014-01-01");
            Date now = new Date();
            long length = now.getTime() - date2014.getTime();
            length = length / 1000;
            return length;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    public static String generateId() {
        long length = getSecondLength();
        StringBuilder sb = new StringBuilder();
        while (length > 0) {
            int index = (int) (length % 64);
            length = length / 64;
            sb.append(RandomUtils.BASE_CHAR.charAt(index));
        }
        sb.reverse();
        for (int i = 0; i < 4; i++) {
            int number = RandomUtils.nextInt(62);
            sb.append(RandomUtils.BASE_CHAR.charAt(number));
        }
        return sb.toString();
    }


}
