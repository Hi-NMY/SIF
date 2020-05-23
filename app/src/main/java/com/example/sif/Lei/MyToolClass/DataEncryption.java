package com.example.sif.Lei.MyToolClass;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

public class DataEncryption {

    private static final String PASSWORD_ENC_SECRET = "usermessage";

    //加密
    public static String encryptString(String s){
        try {
            DESedeKeySpec keySpec = new DESedeKeySpec(PASSWORD_ENC_SECRET.getBytes("UTF-8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(keySpec);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE,secretKey);
            String encrypedPwd = Base64.encodeToString(cipher.doFinal(s.getBytes("UTF-8")),Base64.DEFAULT);
            return encrypedPwd;
        } catch (Exception e) {

        }
        return s;
    }

    //解密
    public static String decryptString(String s){
        try {
            DESedeKeySpec keySpec = new DESedeKeySpec(PASSWORD_ENC_SECRET.getBytes("UTF-8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(keySpec);

            byte[] encryptedWithoutB64 = Base64.decode(s,Base64.DEFAULT);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE,secretKey);
            byte[] plainTextPwdBytes = cipher.doFinal(encryptedWithoutB64);
            return new String(plainTextPwdBytes);
        } catch (Exception e) {

        }
        return s;
    }

}
