package com.example.sif.Lei.LianJie;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ShuJuLianJie {
    protected static final int ERROR = 2;
    protected static final int SUCCESS = 1;

    private Activity activity;
    private String path;
    private URL url;
    private HttpURLConnection httpURLConnection;
    private String fangshi;

    private Handler handler;


    public ShuJuLianJie(){

    }

    public ShuJuLianJie(Activity activity1,String path1,String fangshi1,Handler handler1) {
        this.activity = activity1;
        this.path = path1;
        this.fangshi = fangshi1;
        this.handler = handler1;
        try {
            url = new URL(path);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void chuanShu(String username,String zhanghao,String mima,String ip){
            try {
                httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod(fangshi);
                String data = "yonghuming="+username+"&xuehao="+zhanghao+"&mima="+mima+"&ip="+ip+"&button=";
                httpURLConnection.setDoOutput(true);
                byte[] bytes = data.getBytes();
                httpURLConnection.getOutputStream().write(bytes);
                int code = httpURLConnection.getResponseCode();
                if (code==200){
                    messages(0);
                }else {
                    messages(1);
                }
            } catch (IOException e) {
                messages(1);
                //e.printStackTrace();
            }

    }

    public void chuanShu(String zhanghao,String mima){
            try {
                httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod(fangshi);
                String data = "xuehao="+zhanghao+"&mima="+mima+"&button=";
                httpURLConnection.setDoOutput(true);
                byte[] bytes = data.getBytes();
                httpURLConnection.getOutputStream().write(bytes);
                int code = httpURLConnection.getResponseCode();
                if (code==200){
                    messages(0);
                }else {
                    messages(1);
                }
            } catch (IOException e) {
                messages(1);
                //e.printStackTrace();
            }
    }

    public void chuanShu(String zhanghao){
            try {
                httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod(fangshi);
                String data = "xuehao="+zhanghao+"&button=";
                httpURLConnection.setDoOutput(true);
                byte[] bytes = data.getBytes();
                httpURLConnection.getOutputStream().write(bytes);
                int code = httpURLConnection.getResponseCode();
                if (code==200){
                    messages(0);
                }else {
                    messages(1);
                }
            } catch (IOException e) {
                messages(1);
                //e.printStackTrace();
            }
    }

    private void messages(int i){
        InputStream inputStream = null;
        if (i == 0){
            try {
                inputStream = httpURLConnection.getInputStream();
                String resule = StreamTools.readStream(inputStream);
                Message message = Message.obtain();
                message.what =SUCCESS;
                message.obj = resule;
                handler.sendMessage(message);

            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        if (i == 1){
            Message message = Message.obtain();
            message.what =ERROR;
            handler.sendMessage(message);
        }
    }
}
