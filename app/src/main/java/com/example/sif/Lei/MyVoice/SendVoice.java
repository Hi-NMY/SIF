package com.example.sif.Lei.MyVoice;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.example.sif.Lei.LianJie.HttpUtil;
import com.example.sif.Lei.MyToolClass.ToastZong;
import com.example.sif.MyApplication;
import com.example.sif.NeiBuLei.NiceVoice;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class SendVoice {

    private String path = "http://nmy1206.natapp1.cc/MyVoice.php";
    private String path1 = "http://nmy1206.natapp1.cc/ObtainVoice.php";

    private Handler error = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (smartRefreshLayout != null){
                smartRefreshLayout.finishLoadMore();
                smartRefreshLayout.finishRefresh();
                if (firstKey == 1){
                    smartRefreshLayout.setEnableLoadMore(false);
                    smartRefreshLayout.setEnableRefresh(false);
                }
            }
            ToastZong.ShowToast(MyApplication.getContext(),"阿欧..出错了");
        }
    };

    private Handler longTime = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (smartRefreshLayout != null){
                smartRefreshLayout.finishLoadMore();
                smartRefreshLayout.finishRefresh();
            }
            ToastZong.ShowToast(MyApplication.getContext(),"访问超时");
        }
    };

    public SendVoice(){

    }

    private Handler handler;
    private SmartRefreshLayout smartRefreshLayout;
    private int firstKey = 0;

    public SendVoice(Handler h, SmartRefreshLayout s){
        this.handler = h;
        this.smartRefreshLayout = s;
        firstKey = 1;
    }

    public void sendHandler(Handler h){
        this.handler = h;
    }

    public void sendMyVoice(String v,String n,String s,String l,String x){
        HttpUtil.sendVoice(path,v,n,s,l,x, new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String a = response.body().string();
                if (handler != null){
                    sendHandler(a);
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                sendLongTime();
            }
        });
    }

    public void sendMyVoice(String v,String n,String s,String l,String x,LongTime longTime){
        HttpUtil.sendVoice(path,v,n,s,l,x, new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String a = response.body().string();
                if (handler != null){
                    sendHandler(a);
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                sendLongTime();
                longTime.LongTimeRight();
            }
        });
    }

    public List<NiceVoice> niceVoices;
    public void obtainListVoice(int f,int i){
        HttpUtil.obtainVoice(path1,f,i, new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response){
                try {
                    String a = response.body().string();
                    niceVoices = new Gson().fromJson(a,new TypeToken<List<NiceVoice>>()
                    {}.getType());
                    if (handler != null){
                        sendHandler("");
                    }
                }catch (Exception e){
                    sendError();
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                sendLongTime();
            }
        });
    }

    private void sendError(){
        Message message = new Message();
        error.sendMessage(message);
    }

    private void sendLongTime(){
        Message message = new Message();
        longTime.sendMessage(message);
    }

    private void sendHandler(String a){
        Message message = new Message();
        message.obj = a;
        handler.sendMessage(message);
    }

    public interface LongTime{
        void LongTimeRight();
    }
}
