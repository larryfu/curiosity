package cn.larry.commons.util;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Created by larryfu on 2016/2/29.
 *
 * @author larryfu
 */
public class AESEncrypt {

    /**
     * 加密
     *
     * @param content 需要加密的内容
     * @param key     加密密码
     * @param md5Key  是否对key进行md5加密
     * @param iv      加密向量
     * @return 加密后的字节数据
     */
    public static byte[] encrypt(byte[] content, byte[] key, boolean md5Key, byte[] iv) {
        try {
            if (md5Key) {
                MessageDigest md = MessageDigest.getInstance("MD5");
                key = md.digest(key);
            }
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/ISO10126Padding"); //"算法/模式/补码方式"
            IvParameterSpec ivps = new IvParameterSpec(iv);//使用CBC模式，需要一个向量iv，可增加加密算法的强度
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivps);
            return cipher.doFinal(content);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException ex) {
            // .error(ex.getLocalizedMessage());
            ex.printStackTrace();
        }
        return null;
    }

    public static void mains(String[] args) throws UnsupportedEncodingException {
        String msg = "hello";
        String key = "0123456789123456";
        byte[] bytes = encrypt(msg.getBytes("UTF-8"), key.getBytes("UTF-8"), false, key.getBytes("UTF-8"));
        System.out.println(new String(Base64.getEncoder().encode(bytes)));
        byte[] debytes = decrypt(bytes, key.getBytes("UTF-8"), false, key.getBytes("UTF-8"));
        System.out.println(new String(bytes));
    }

    public static byte[] decrypt(byte[] content, byte[] key, boolean md5Key, byte[] iv) {
        try {
            if (md5Key) {
                MessageDigest md = MessageDigest.getInstance("MD5");
                key = md.digest(key);
            }
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/ISO10126Padding"); //"算法/模式/补码方式"
            IvParameterSpec ivps = new IvParameterSpec(iv);//使用CBC模式，需要一个向量iv，可增加加密算法的强度
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivps);
            return cipher.doFinal(content);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException ex) {
            // logger.error(ex.getLocalizedMessage());
            ex.printStackTrace();
        }
        return null;
    }
}
/*
01 算法/模式/填充                16字节加密后数据长度        不满16字节加密后长度
02 AES/CBC/NoPadding             16                          不支持
03 AES/CBC/PKCS5Padding          32                          16
04 AES/CBC/ISO10126Padding       32                          16
05 AES/CFB/NoPadding             16                          原始数据长度
06 AES/CFB/PKCS5Padding          32                          16
07 AES/CFB/ISO10126Padding       32                          16
08 AES/ECB/NoPadding             16                          不支持
09 AES/ECB/PKCS5Padding          32                          16
10 AES/ECB/ISO10126Padding       32                          16
11 AES/OFB/NoPadding             16                          原始数据长度
12 AES/OFB/PKCS5Padding          32                          16
13 AES/OFB/ISO10126Padding       32                          16
14 AES/PCBC/NoPadding            16                          不支持
15 AES/PCBC/PKCS5Padding         32                          16
16 AES/PCBC/ISO10126Padding      32                          16



CryptoJS supports the following padding schemes:

    Pkcs7 (the default)
    Iso97971
    AnsiX923
    Iso10126
    ZeroPadding
    NoPadding
*/
//}
