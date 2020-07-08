package com.example.sif.Lei.MyToolClass;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.example.sif.Lei.LianJie.HttpUtil;
import com.example.sif.MyApplication;
import com.example.sif.NeiBuLei.InterestingBlockClass;
import com.example.sif.NeiBuLei.UserIbFollow;
import com.example.sif.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import okhttp3.Call;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class IbFollow {
    private Handler error = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ToastZong.ShowToast(MyApplication.getContext(),"错误");
            if (smartRefreshLayout != null){
                smartRefreshLayout.finishLoadMore();
                smartRefreshLayout.finishRefresh();
            }
        }
    };

    private Handler reFreshHanlder = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (smartRefreshLayout != null){
                smartRefreshLayout.finishLoadMore();
                smartRefreshLayout.finishRefresh();
            }
        }
    };

    private String path = "http://nmy1206.natapp1.cc/FollowIb.php";
    private String path1 = "http://nmy1206.natapp1.cc/MyIbFollow.php";
    public Collection<UserIbFollow> userIbFollows;
    public List<String> followList;
    private SmartRefreshLayout smartRefreshLayout;

    private String xuehao;
    public IbFollow(String x) {
        this.xuehao = x;
        userIbFollows = new ArrayList<UserIbFollow>();
        followList = new ArrayList<String>();
        rightFollow();
    }

    private Handler frushHander;
    public void sendHander(Handler h){
        this.frushHander = h;
    }

    public void myFreshView(SmartRefreshLayout s){
        this.smartRefreshLayout = s;
    }

    private List<String> followListList;
    public void rightFollow() {
        followList = new ArrayList<>();
        followListList = new ArrayList<>();
        HttpUtil.myIbFollow(InValues.send(R.string.FollowIb),0,xuehao,"", new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String a = response.body().string();
                if (!a.equals("")&&a != null&&!a.equals("1")){
                    try {
                        userIbFollows = new Gson().fromJson(a,new TypeToken<List<UserIbFollow>>()
                        {}.getType());
                        for (UserIbFollow u:userIbFollows){
                            followList.add(u.getIbname());
                            followListList.add(u.getIbname());
                        }
                        moreFollow(0);
                    }catch (Exception e){
                        error.sendMessage(new Message());
                    }
                }else {
                    if (userIbFollows!=null){
                        userIbFollows.clear();
                    }
                    Message message = new Message();
                    if (frushHander != null){
                        frushHander.sendMessage(message);
                    }else {
                        reFreshHanlder.sendMessage(message);
                    }
                }
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                if (e instanceof SocketTimeoutException){
                    errorHandler();
                }
                if (e instanceof ConnectException){
                    errorHandler();
                }
            }
        });
    }

    private StringBuffer stringBuffer;
    public List<InterestingBlockClass> interestingBlockClasses;
    private List<String> removeList;

    private StringBuffer moreName(int i){
        StringBuffer s = new StringBuffer();
        removeList = new ArrayList<>();
        switch (i){
            case 1:
                int fu = 0;
                for (String f:followListList){
                    if (fu < 15){
                        if (fu == 0){
                            s.append(f);
                        }else {
                            s.append(String.valueOf(","+f));
                        }
                        removeList.add(f);
                    }
                    fu+=1;
                }
                removeList.remove(removeList.size() - 1);
                followListList.removeAll(removeList);
                break;
            case 2:
                int fu1 = 1;
                for (String f:f2){
                    if (fu1 == 1){
                        s.append(f);
                        fu1 += 1;
                    }else {
                        s.append(String.valueOf(","+f));
                    }
                }
                break;
        }
        return s;
    }

    public void moreFollow(int i){
        stringBuffer = new StringBuffer();
        if (i == 0){
            stringBuffer = moreName(1);
        }else {
            stringBuffer = moreName(2);
        }


        HttpUtil.followIb(InValues.send(R.string.MyIbFollow),stringBuffer,i, new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String a = response.body().string();
                if (!a.equals("")){
                    try {
                        interestingBlockClasses = new Gson().fromJson(a,new TypeToken<List<InterestingBlockClass>>()
                        {}.getType());
                        if (frushHander!=null){
                            Message message = new Message();
                            frushHander.sendMessage(message);
                        }
                    }catch (Exception e){
                        errorHandler();
                    }
                }
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                if (e instanceof SocketTimeoutException){
                    errorHandler();
                }
                if (e instanceof ConnectException){
                    errorHandler();
                }
            }
        });
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

    private String ibname;
    private Handler handler;
    public void sendmyFollow(String n, Handler h){
        this.ibname = n;
        this.handler = h;
        if (followList!=null && followList.contains(ibname)){
            noFollow();
        }else {
            yesFollow();
        }

    }

    public void yesFollow(){
        followList.add(ibname);
        HttpUtil.myIbFollow(InValues.send(R.string.FollowIb),1,xuehao,ibname, new okhttp3.Callback() {
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
                errorHandler();
            }
        });
    }

    public void noFollow(){
        followList.remove(ibname);
        HttpUtil.myIbFollow(InValues.send(R.string.FollowIb),2,xuehao,ibname, new okhttp3.Callback() {
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
                errorHandler();
            }
        });
    }

    private List<String> f2;
    public void searchMyIbMore(int id,String blockname){
        userIbFollows = new ArrayList<>();
        f2 = new ArrayList<>();
        HttpUtil.myIbFollow(InValues.send(R.string.FollowIb),-1,xuehao,blockname, new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String a = response.body().string();
                if (!a.equals("")&&a != null&&!a.equals("1")){
                    try {
                        userIbFollows = new Gson().fromJson(a,new TypeToken<List<UserIbFollow>>()
                        {}.getType());
                        for (UserIbFollow u:userIbFollows){
                            f2.add(u.getIbname());
                        }
                        moreFollow(1);
                    }catch (Exception e){
                        error.sendMessage(new Message());
                    }
                }
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                errorHandler();
            }
        });
    }


    private void errorHandler(){
        Message message = new Message();
        error.sendMessage(message);
    }

}
