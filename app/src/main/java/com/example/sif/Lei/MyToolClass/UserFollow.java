package com.example.sif.Lei.MyToolClass;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.example.sif.Lei.LianJie.HttpUtil;
import com.example.sif.MyApplication;
import com.example.sif.NeiBuLei.UserFollowPeople;
import com.example.sif.NeiBuLei.UserSpace;
import com.example.sif.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.Call;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserFollow {

    private Handler error = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ToastZong.ShowToast(MyApplication.getContext(),"错误");
        }
    };

    public Collection<UserFollowPeople> userFollowPeople;
    public Collection<String> followList;
    private String path = "";
    public void rightFollow() {
        followList = new ArrayList<>();
        HttpUtil.myFollow(InValues.send(R.string.UserFollow),0,xuehao,"", new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String a = response.body().string();
                if (!a.equals("")&&a != null&&!a.equals("1")){
                    try {
                        userFollowPeople = new Gson().fromJson(a,new TypeToken<List<UserFollowPeople>>()
                        {}.getType());
                        for (UserFollowPeople u:userFollowPeople){
                            followList.add(u.getUserxuehao());
                        }
                        String nowTime = MyDateClass.showNowDate();
                        moreFollow(nowTime);
                    }catch (Exception e){
                        error.sendMessage(new Message());
                    }
                }else {
                    if (frushHander!=null){
                        if (userSpaces!=null){
                            userSpaces.clear();
                        }
                        Message message = new Message();
                        message.what = 1;
                        frushHander.sendMessage(message);
                    }
                }
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                if (e instanceof SocketTimeoutException){
                    if (frushHander!=null){
                        Message message = new Message();
                        message.what = 3;
                        frushHander.sendMessage(message);
                    }
                }
                if (e instanceof ConnectException){
                    if (frushHander!=null){
                        Message message = new Message();
                        message.what = 3;
                        frushHander.sendMessage(message);
                    }
                }
            }
        });
    }

    private Handler frushHander;
    private int fun;
    public void sendHander(Handler h,int i){
        this.frushHander = h;
        this.fun = i;
    }

    public boolean noYesFollow(String u){
        if (followList!=null){
            if (followList.contains(u)){
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }

    private String xuehao;
    public UserFollow(String x) {
        this.xuehao = x;
        userFollowPeople = new ArrayList<UserFollowPeople>();
        followList = new ArrayList<String>();
        rightFollow();
    }

    private Handler handler;
    private String userxuehao;
    public void sendmyFollow(String u, Handler h){
        this.userxuehao = u;
        this.handler = h;
        if (followList!=null && followList.contains(userxuehao)){
            noFollow();
        }else {
            yesFollow();
        }

    }

    private String pathto = "";
    public void yesFollow(){
        followList.add(userxuehao);
        HttpUtil.myFollow(InValues.send(R.string.UserFollow),1,xuehao,userxuehao, new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String a = response.body().string();
                if (handler!=null){
                    Message message = new Message();
                    message.what = 1;
                    message.obj = a;
                    handler.sendMessage(message);
                }
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }
        });

        HttpUtil.myFollow(InValues.send(R.string.UserFollowToMe),1,userxuehao,xuehao, new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

            }
        });
    }

    public void noFollow(){
        followList.remove(userxuehao);
        HttpUtil.myFollow(InValues.send(R.string.UserFollow),2,xuehao,userxuehao, new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String a = response.body().string();
                if (handler!=null){
                    Message message = new Message();
                    message.what = 1;
                    message.obj = a;
                    handler.sendMessage(message);
                }
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }
        });

        HttpUtil.myFollow(InValues.send(R.string.UserFollowToMe),2,userxuehao,xuehao, new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

            }
        });
    }

    private String path1 = "";
    private StringBuffer stringBuffer;
    public List<UserSpace> userSpaces;
    public void moreFollow(String nowTime){
        int fu = 1;
        stringBuffer = new StringBuffer();
        for (String f:followList){
            if (fu == 1){
                stringBuffer.append(f);
                fu+=1;
            }else {
                stringBuffer.append(String.valueOf(","+f));
            }
        }
        HttpUtil.followDynamic(InValues.send(R.string.MyFollow),stringBuffer,nowTime, new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String a = response.body().string();
                if (!a.equals("")){
                    try {
                        userSpaces = new Gson().fromJson(a,new TypeToken<List<UserSpace>>()
                        {}.getType());
                        if (frushHander!=null){
                            Message message = new Message();
                            message.what = fun;
                            frushHander.sendMessage(message);
                        }
                    }catch (Exception e){
                        if (frushHander != null){
                            Message message = new Message();
                            message.what = 3;
                            frushHander.sendMessage(message);
                        }
                    }
                }
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                if (e instanceof SocketTimeoutException){
                    if (frushHander != null){
                        Message message = new Message();
                        message.what = 3;
                        frushHander.sendMessage(message);
                    }
                }
                if (e instanceof ConnectException){
                    if (frushHander != null){
                        Message message = new Message();
                        message.what = 3;
                        frushHander.sendMessage(message);
                    }
                }
            }
        });
    }


}
