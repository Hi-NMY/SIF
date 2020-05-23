package com.example.sif.Lei.MyToolClass;

import android.os.Handler;
import android.os.Message;

import com.example.sif.Lei.LianJie.HttpUtil;
import com.example.sif.MyApplication;
import com.example.sif.NeiBuLei.GuangChangUserXinXi;
import com.example.sif.NeiBuLei.InterestingBlockClass;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class BlockDetailedSend {

    private static String path = "http://nmy1206.natapp1.cc/BlockDetailed.php";
    private static InterestingBlockClass interestingBlockClass;
    private static List<GuangChangUserXinXi> guangChangUserXinXis;

    private static SmartRefreshLayout smartRefreshLayout;

    private static Handler longTimeHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (smartRefreshLayout != null){
                smartRefreshLayout.finishRefresh();
                smartRefreshLayout.finishLoadMore();
                smartRefreshLayout = null;
            }else {
                ToastZong.ShowToast(MyApplication.getContext(),"错误，请重试");
            }
        }
    };

    public static void freshView(SmartRefreshLayout s){
        smartRefreshLayout = s;
    }

    public static void detailedBlock(String blockname, int i, Handler handler){
        HttpUtil.blockSend(path,blockname,i,"", new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String a = response.body().string();
                try {
                    interestingBlockClass = new Gson().fromJson(a, InterestingBlockClass.class);
                    Message message = new Message();
                    message.obj = interestingBlockClass;
                    handler.sendMessage(message);
                }catch (Exception e){
                    Message message = new Message();
                    longTimeHandler.sendMessage(message);
                }
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                if (e instanceof SocketTimeoutException){
                    sendLongTime();
                }
                if (e instanceof ConnectException){
                    sendLongTime();
                }
            }
        });
    }

    public static void detailedList(String blockname,int i,String nowTime,Handler handler){
        String name = change(blockname);
        HttpUtil.blockSend(path,name,i,nowTime, new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    String a = response.body().string();
                    guangChangUserXinXis = new Gson().fromJson(a,new TypeToken<List<GuangChangUserXinXi>>()
                    {}.getType());
                    Message message = new Message();
                    message.obj = guangChangUserXinXis;
                    handler.sendMessage(message);
                }catch (Exception e){
                    Message message = new Message();
                    longTimeHandler.sendMessage(message);
                }

            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                if (e instanceof SocketTimeoutException){
                    sendLongTime();
                }
                if (e instanceof ConnectException){
                    sendLongTime();
                }
            }
        });
    }

    private static void sendLongTime(){
        if (smartRefreshLayout != null){
            Message message = new Message();
            longTimeHandler.sendMessage(message);
        }
    }

    private static String change(String name){
        String newName = name + ",";
        return newName;
    }

}
