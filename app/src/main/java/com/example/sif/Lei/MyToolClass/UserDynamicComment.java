package com.example.sif.Lei.MyToolClass;

import android.os.Handler;
import android.os.Message;

import com.example.sif.Lei.LianJie.HttpUtil;
import com.example.sif.NeiBuLei.CommentMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class UserDynamicComment {

    private String dynamicid;
    private int commentNumber;

    public UserDynamicComment(){

    }

    public void dynamicXinxi(String d,int c){
        this.dynamicid = d;
        this.commentNumber = c;
    }

    String path = "http://nmy1206.natapp1.cc/CommentDetailed.php";
    private Handler handler;
    public List<CommentMessage> commentMessages;
    public void detailedComment(Handler h,int id,String nowTime){
        this.handler = h;
        if (commentNumber != 0){
            HttpUtil.dynamicDetailedComment(path,id,dynamicid,nowTime, new okhttp3.Callback() {
                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String a = response.body().string();
                    Gson gson = new Gson();
                    commentMessages = gson.fromJson(a,new TypeToken<List<CommentMessage>>(){}
                    .getType());
                    sendMessages(a,1);
                }
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }
            });
        }
    }


    String path1 = "http://nmy1206.natapp1.cc/SendComment.php";
    public void sendComment(int i,String dynamicId,String dynamiccomment,String commenttime,String commentusername,String tousername,String userheadimageurl,String commentxuehao,String userxuehao,int userid,Handler h){
        this.handler = h;
        HttpUtil.sendComment(path1,i,dynamicId,dynamiccomment,commenttime,commentusername,tousername,userheadimageurl,commentxuehao,null,userxuehao,userid, new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String a = response.body().string();
                if (h != null){
                    sendMessages(a,0);
                }
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }
        });
    }

    private void sendMessages(String a,int i){
        Message message = new Message();
        switch (i){
            case 0:
                message.what = 0;
                message.obj = a;
                handler.sendMessage(message);
                break;
            case 1:
                message.what = 1;
                message.obj = a;
                handler.sendMessage(message);
                break;
        }
    }
}
