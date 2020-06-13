package com.example.sif.Lei.MyToolClass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import com.example.sif.Lei.LianJie.HttpUtil;
import com.example.sif.MyApplication;
import com.example.sif.MySpace;
import com.example.sif.NeiBuLei.User;
import com.example.sif.NeiBuLei.UserSchool;
import com.google.gson.Gson;
import okhttp3.Call;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ObtainUser {
    private static Activity activity;
    private static int position;
    private static String xuehao;
    private static Handler handler;
    public ObtainUser(Activity a,int p) {

    }

    public static void obtainUser(Activity a, String x, Handler h){
        activity = a;
        xuehao = x;
        handler = h;
        users = null;
        userSchools = null;
        try{
            ExecutorService executorService = Executors.newFixedThreadPool(6);
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        sendUserSpace(xuehao);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            executorService.shutdown();
            while (true){
                if (executorService.isTerminated()){
                    try{
                        Intent intent = new Intent(activity,MySpace.class);
                        intent.putExtra("id",2);
                        intent.putExtra("uXueHao",xuehao);
                        activity.startActivity(intent);
                        UpdateShareTask.updateTask(5);
                        break;
                    }catch (Exception e) {
                        ToastZong.ShowToast(activity,"连接错误,请重试");
                        break;
                    }
                }
            }
        }catch (Exception e){
            ToastZong.ShowToast(activity,"出错了");
        }
    }

    private static User users;
    private static UserSchool userSchools;
    public static void sendUserSpace(String xuehao){
        SharedPreferences sharedPreferences = MyApplication.getContext().getSharedPreferences("userSpaceZ", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        String path2 = "http://nmy1206.natapp1.cc/User.php";
        HttpUtil.sendOkHttpPost(path2,xuehao, new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String a = response.body().string();
                Gson gson = new Gson();
                try{
                    users = gson.fromJson(a, User.class);
                    editor.putString("uName",users.user_name);
                    editor.putString("uGeQian",users.user_geqian);
                    editor.putString("uprivacy",users.user_privacy);
                    editor.commit();
                }catch (Exception e){
                    sendMessages(1);
                }
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                if (e instanceof SocketTimeoutException){
                    sendMessages(1);
                }
                if (e instanceof ConnectException){
                    sendMessages(2);
                }
            }

        });

        String path3 = "http://nmy1206.natapp1.cc/UserSchool.php";
        HttpUtil.sendOkHttpPost(path3,xuehao, new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String a = response.body().string();
                Gson gson = new Gson();
                try{
                    userSchools = gson.fromJson(a, UserSchool.class);
                    editor.putString("uXingBie",userSchools.xingbie);
                    editor.putString("uZhuanYe",userSchools.zhuanye);
                    editor.putString("uXueHao",userSchools.xuehao);
                    editor.putString("uNianJi",userSchools.nianji);
                    editor.putString("uXiBu",userSchools.xibu);
                    editor.commit();
                }catch (Exception e){
                    sendMessages(1);
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                if (e instanceof SocketTimeoutException){
                    sendMessages(1);
                }
                if (e instanceof ConnectException){
                    sendMessages(2);
                }
            }
        });
    }
    public static void sendMessages(int i){
        if (handler != null) {
            if (i == 1) {
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
            if (i == 2) {
                Message message = new Message();
                message.what = 2;
                handler.sendMessage(message);
            }
        }
    }
}
