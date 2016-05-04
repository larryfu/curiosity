package cn.larry.commons.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Created by larryfu on 2016/2/29.
 *
 * @author larryfu
 */
public class DES {

    public static void mains(String[] args) throws UnsupportedEncodingException {
        String msg = "hello";
        String key = "abcd1234";
        byte[] bytes = encrypt(msg.getBytes("UTF-8"), key);
        System.out.println(new String(Base64.getEncoder().encode(bytes)));
        byte[] debytes = decrypt(bytes, key);
        System.out.println(new String(debytes));
    }

    /**
     * 加密
     *
     * @param datasource byte[]
     * @param password   String
     * @return byte[]
     */
    public static byte[] encrypt(byte[] datasource, String password) {
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(password.getBytes());
//创建一个密匙工厂，然后用它把DESKeySpec转换成
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);
//Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance("DES");
//用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
//现在，获取数据并加密
//正式执行加密操作
            return cipher.doFinal(datasource);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String decrypt(String base64src, String key) {
        try {
            byte[] bytes = Base64.getDecoder().decode(base64src.getBytes("utf-8"));
            return new String(decrypt(bytes, key));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 解密
     *
     * @param src      byte[]
     * @param password String
     * @return byte[]
     * @throws Exception
     */
    public static byte[] decrypt(byte[] src, String password) {
        try {
            // DES算法要求有一个可信任的随机数源
            SecureRandom random = new SecureRandom();
// 创建一个DESKeySpec对象
            DESKeySpec desKey = new DESKeySpec(password.getBytes());
// 创建一个密匙工厂
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
// 将DESKeySpec对象转换成SecretKey对象
            SecretKey securekey = keyFactory.generateSecret(desKey);
// Cipher对象实际完成解密操作
            Cipher cipher = Cipher.getInstance("DES");
// 用密匙初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, securekey, random);
// 真正开始解密操作
            return cipher.doFinal(src);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
