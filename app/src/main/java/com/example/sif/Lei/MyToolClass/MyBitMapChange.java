package com.example.sif.Lei.MyToolClass;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.example.sif.MyApplication;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MyBitMapChange {

    private static Handler error = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ToastZong.ShowToast(MyApplication.getContext(),"错误");
        }
    };

    public static Bitmap myBitmap(String path){
        URL imageUrl = null;
        Bitmap bitmap = null;
        try {
            imageUrl = new URL(path);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection)imageUrl.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();
            InputStream inputStream = httpURLConnection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
            return bitmap;
        } catch (Exception e){
            Message message = new Message();
            error.sendMessage(message);
            return null;
        }
    }

    public static Bitmap myBitmapFile(String path){
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeFile(path);
            return bitmap;
        } catch (Exception e){
            Message message = new Message();
            error.sendMessage(message);
            return null;
        }
    }
}
