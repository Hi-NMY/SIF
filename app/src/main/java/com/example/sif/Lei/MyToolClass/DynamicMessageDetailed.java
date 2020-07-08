package com.example.sif.Lei.MyToolClass;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.example.sif.Lei.LianJie.HttpUtil;
import com.example.sif.MyApplication;
import com.example.sif.NeiBuLei.UserSpace;
import com.example.sif.R;
import com.google.gson.Gson;
import okhttp3.Call;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

public class DynamicMessageDetailed {
    private Activity activity;
    private String xuehao;
    private String dynamicid;
    private Handler handler;
    private String path = "http://nmy1206.natapp1.cc/DynamicDetailed.php";

    private Handler error = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ToastZong.ShowToast(MyApplication.getContext(),"错误");
        }
    };

    public DynamicMessageDetailed(Activity a,String x, String d,Handler h){
        this.activity = a;
        this.xuehao = x;
        this.dynamicid = d;
        this.handler = h;
    }

    public UserSpace userSpace;
    public void obtainDynamic(){
        HttpUtil.dynamicDetailedObtain(InValues.send(R.string.DynamicDetailed),xuehao,dynamicid, new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String a = response.body().string();
                Gson gson = new Gson();
                if (!a.equals("1")){
                    try{
                        userSpace = gson.fromJson(a,UserSpace.class);
                    }catch (Exception e){
                        error.sendMessage(new Message());
                    }
                }
                sendMessages(a,1);
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                if (e instanceof SocketTimeoutException){
                    sendMessages(null,2);
                }
                if (e instanceof ConnectException){
                    sendMessages(null,2);
                }
            }
        });
    }

    public void sendMessages(String A,int a){
        Message message = new Message();
        switch (a){
            case 1:
                message.what = 1;
                message.obj = A;
                handler.sendMessage(message);
                break;
            case 2:
                message.what = 2;
                handler.sendMessage(message);
                break;
        }
    }

}
