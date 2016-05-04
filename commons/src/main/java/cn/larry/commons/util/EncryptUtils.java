package cn.larry.commons.util;

/**
 * @author gzfu
 */

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class EncryptUtils {

    private EncryptUtils() {
    }

    public static String MD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] btInput = s.getBytes();
            // ���MD5ժҪ�㷨�� MessageDigest ����
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // ʹ��ָ�����ֽڸ���ժҪ
            mdInst.update(btInput);
            // �������
            byte[] md = mdInst.digest();
            // ������ת����ʮ�����Ƶ��ַ�����ʽ
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void mains(String[] args) {
//        String msg = "hello";
//        String key = "123";
//        System.out.println(AESEncrypt(msg, key));
        String content = "156156165152165156156";
        String password = "0123456789012345";
//加密
        System.out.println("加密前：" + content);
        byte[] encryptResult = AESEncrypt(content, password);
        int c = encryptResult[0] << 8 | encryptResult[1];
//解密
        System.out.println(new String(Base64.getEncoder().encode(encryptResult)));
        byte[] decryptResult = AESDecrypt(encryptResult, password);
        System.out.println("解密后：" + new String(decryptResult));
    }

    public static byte[] AESEncrypt(String content, String password) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(192, new SecureRandom(password.getBytes()));
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/ISO10126Padding");// 创建密码器
            byte[] byteContent = content.getBytes("utf-8");
            byte[] iv = password.getBytes("utf-8");
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, key,ivspec);// 初始化
            byte[] result = cipher.doFinal(byteContent);
            int i = result[0] << 24 | result[1] << 16 | result[2] << 8 | result[3];
            System.out.println(i);
            return result; // 加密
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] AESDecrypt(byte[] content, String password) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(192, new SecureRandom(password.getBytes()));
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/ISO10126Padding");// 创建密码器
             byte[] iv = password.getBytes("utf-8");
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, key,ivspec);// 初始化
            byte[] result = cipher.doFinal(content);
            return result; // 加密
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String SHA1(String decript) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(decript.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2)
                    hexString.append(0);
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}
