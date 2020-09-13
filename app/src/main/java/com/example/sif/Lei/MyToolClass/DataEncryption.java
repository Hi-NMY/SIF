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
    private static final byte[] KEY = { 19, 99, -12, -6, -126, -126, 126, 06 };
    //加密
    public static String encrypt(String string){
        try {
            SecureRandom secure = new SecureRandom();
            DESKeySpec Key = new DESKeySpec(KEY);
            SecretKeyFactory secret = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = secret.generateSecret(Key);
            Cipher c = Cipher.getInstance("DES");
            c.init(Cipher.ENCRYPT_MODE, secretKey, secure);
            String encrypted = new BASE64Encoder().encode(c.doFinal(string.getBytes()));
            return encrypted;
        } catch (Exception e) {
        }
        return string;
    }

    //解密
    public static String decrypt(String s){
        try {
            String decrypted = null;
            SecureRandom secure = new SecureRandom();
            DESKeySpec KeySpec = new DESKeySpec(KEY);
            SecretKeyFactory secretKey1 = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = secretKey1.generateSecret(KeySpec);
            Cipher c = Cipher.getInstance("DES");
            c.init(Cipher.DECRYPT_MODE, secretKey, secure);
            decrypted = new String(c.doFinal(new BASE64Decoder().decodeBuffer(s)));
            return decrypted;
        } catch (Exception e) {
        }
        return "";
    }

}
