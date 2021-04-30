package com.example.sif.Lei.MyToolClass;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.example.sif.Lei.LianJie.HttpUtil;
import com.example.sif.MyApplication;
import com.example.sif.NeiBuLei.InterestingBlockClass;
import com.example.sif.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.Call;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

public class IbClass {

    private Handler error = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ToastZong.ShowToast(MyApplication.getContext(),"错误");
        }
    };

    private Handler handler;

    public IbClass(int i) {
        sendIbMessage(-i);
    }

    public void myHandler(Handler h){
        this.handler = h;
    }

    private LocalBroadcastManager localBroadcastManager;

    public List<InterestingBlockClass> interestingBlockClasses;
    private String path = "";
    public void sendIbMessage(int id){
        HttpUtil.ibsend(InValues.send(R.string.InterestingBlockMore),id,"",new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String a = response.body().string();
                if (!a.equals("")){
                    try{
                        interestingBlockClasses = new Gson().fromJson(a, new TypeToken<List<InterestingBlockClass>>()
                        {}.getType());
                        if (handler != null){
                            Message message = new Message();
                            handler.sendMessage(message);
                        }else {
                            localBroadcastManager = LocalBroadcastManager.getInstance(MyApplication.getContext());
                            Intent intent = new Intent("initIb");
                            localBroadcastManager.sendBroadcast(intent);
                        }
                    }catch (Exception e){
                        error.sendMessage(new Message());
                    }
                }
           }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }
        });
    }

    public void searchIbMore(int id,String name){
        HttpUtil.ibsend(InValues.send(R.string.InterestingBlockMore),id,name,new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String a = response.body().string();
                if (!a.equals("")){
                    try{
                        interestingBlockClasses = new Gson().fromJson(a, new TypeToken<List<InterestingBlockClass>>()
                        {}.getType());
                        if (handler != null){
                            Message message = new Message();
                            handler.sendMessage(message);
                        }
                    }catch (Exception e){
                        error.sendMessage(new Message());
                    }
                }
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }
        });
    }
}
