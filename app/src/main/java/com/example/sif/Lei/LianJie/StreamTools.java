package com.example.sif.Lei.LianJie;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamTools {
    public static String readStream(InputStream inputStream) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024*2];
            int len = -1;
            while ((len = inputStream.read(buffer))!= -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
            byteArrayOutputStream.close();
            return new String(byteArrayOutputStream.toByteArray());

        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
