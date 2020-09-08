package com.example.sif.Lei.MyToolClass;

import com.example.sif.Lei.sun.misc.BASE64Decoder;
import com.example.sif.Lei.sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;

public class DataEncryption {
    private static boolean[] bcdLookup;
    private static final byte[] DES_KEY = { 19, 99, -12, -6, -126, -126, 126, 06 };
    //加密
    public static String encryptString(String s){
        try {
            SecureRandom sr = new SecureRandom();
            DESKeySpec deskey = new DESKeySpec(DES_KEY);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(deskey);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, key, sr);
            String encryptedData = new BASE64Encoder().encode(cipher.doFinal(s.getBytes()));
            return encryptedData;
        } catch (Exception e) {

        }
        return s;
    }

    //解密
    public static String decryptString(String s){
        try {
            String decryptedData = null;
            // 创建随机数源
            SecureRandom sr = new SecureRandom();
            DESKeySpec deskey = new DESKeySpec(DES_KEY);
            // 创建密钥工厂，并转化为对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(deskey);
            // 进行解密
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key, sr);
            decryptedData = new String(cipher.doFinal(new BASE64Decoder().decodeBuffer(s)));
            return decryptedData;
        } catch (Exception e) {

        }
        return "";
    }

}
