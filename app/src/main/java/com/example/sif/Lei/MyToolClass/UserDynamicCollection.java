package com.example.sif.Lei.MyToolClass;

import android.os.Handler;
import android.os.Message;
import com.example.sif.Lei.LianJie.HttpUtil;
import com.example.sif.NeiBuLei.UserCollection;
import com.example.sif.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.Call;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

public class UserDynamicCollection {


    public UserDynamicCollection() {

    }

    public static List<UserCollection> userCollections;
    private static String path1 = "";
    public static void Collection(String xuehao,int id,Handler handler){
        HttpUtil.inquireCollection(InValues.send(R.string.InquireCollection),xuehao,id, new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String a = response.body().string();
                Gson gson = new Gson();
                Message message = new Message();
                if (a.equals("1")||a == null){
                    message.what = 1;
                    handler.sendMessage(message);
                }else{
                    try {
                        userCollections = gson.fromJson(a,new TypeToken<List<UserCollection>>(){}
                                .getType());
                        message.what = 0;
                        handler.sendMessage(message);
                    }catch (Exception e){
                        message.what = 1;
                        handler.sendMessage(message);
                    }
                }
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }
        });
    }

    public static void noYesCollection(int i, String dynamicid, String xuehao, Handler handler){
        HttpUtil.dynamicCollection(InValues.send(R.string.UserCollection),i,dynamicid,xuehao, new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String a = response.body().string();
                Message message = new Message();
                if (a.equals("0")){
                    message.what = 0;
                    handler.sendMessage(message);
                }
                if (a.equals("1")){
                    message.what = 1;
                    handler.sendMessage(message);
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }
        });
    }


    private static String path = "";
    public static void detailedCollection(int id,String dynamicid,String headimage,String xuehao,String username,String message,String image,String myxuehao,Handler handler){
        HttpUtil.dynamicCollection(InValues.send(R.string.UserCollection),id,dynamicid,headimage,xuehao,username,message,image,myxuehao, new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String a = response.body().string();
                Message message = new Message();
                if (a.equals("10")){
                    message.what = 10;
                    handler.sendMessage(message);
                }else if (a.equals("0")){
                    message.what = 0;
                    handler.sendMessage(message);
                }else{
                    message.what = 100;
                    handler.sendMessage(message);
                }
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }
        });
    }

    public static void detailedCollection(int id,String dynamicid,String myxuehao,Handler handler){
        HttpUtil.dynamicCollection(InValues.send(R.string.UserCollection),id,dynamicid,myxuehao, new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String a = response.body().string();
                Message message = new Message();
                if (handler!=null){
                    if (a.equals("0")){
                        message.what = 0;
                        handler.sendMessage(message);
                    }else {
                        message.what = 100;
                        handler.sendMessage(message);
                    }
                }
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }
        });
    }
}
