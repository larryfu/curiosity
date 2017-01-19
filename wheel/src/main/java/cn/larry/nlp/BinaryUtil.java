package cn.larry.nlp;

import java.util.Arrays;

/**
 * Created by larry on 16-12-23.
 */
public class BinaryUtil {

    public static void main(String[] args) {
        short s = 0x568d;
        System.out.println(s);
        byte[] bytes = short2ByteArray(s);
        for (int j = 0; j < bytes.length; j++) {
            System.out.println(Byte.toString(bytes[j]));
        }
        System.out.println(byteArray2Short(bytes));
        System.out.println();
    }


    public static byte[] short2ByteArray(short s) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) ((s >> 8) & 0xFF);
        bytes[1] = (byte) (s & 0xFF);
        return bytes;
    }

    public static short byteArray2Short(byte[] bytes) {
        if (bytes.length != 2)
            throw new IllegalArgumentException("not a short value");
        short s = 0;
        s += Byte.toUnsignedInt(bytes[0]) * (short) (1 << 8);
        s += Byte.toUnsignedInt(bytes[1]);
        return s;
    }

    /**
     * 把int装成byte数组 大端序
     *
     * @param i
     * @return
     */
    public static byte[] int2ByteArray(int i) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) ((i >> 24) & 0xFF);
        bytes[1] = (byte) ((i >> 16) & 0xFF);
        bytes[2] = (byte) ((i >> 8) & 0xFF);
        bytes[3] = (byte) (i & 0xFF);
        return bytes;
    }

    public static int byteArray2Int(byte[] bytes) {
        if (bytes.length != 4)
            throw new IllegalArgumentException("not a int value");
        int value = 0;
        for (int i = 0; i < 4; i++) {
            int offset = (3 - i) * 8;
            value += Byte.toUnsignedInt(bytes[i]) * (1 << offset);
        }
        return value;
    }

    /**
     * 把long装成byte数组 大端序
     *
     * @param l
     * @return
     */
    public static byte[] long2ByteArray(long l) {
        byte[] bytes = new byte[8];
        for (int i = 0; i < 8; i++) {
            int offset = 8 * (7 - i);
            bytes[i] = (byte) ((l >> offset) & 0xFF);
        }
        return bytes;
    }


    public static long byteArray2Long(byte[] bytes) {
        if (bytes.length != 8)
            throw new IllegalArgumentException("not a long value");
        long value = 0;
        for (int i = 0; i < 8; i++) {
            int offset = (7 - i) * 8;
            value += Byte.toUnsignedInt(bytes[i]) * (1L << offset);
        }
        return value;
    }
}
