package com.example.sif.Lei.MyToolClass;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.example.sif.Lei.LianJie.HttpUtil;
import com.example.sif.Lei.MyBroadcastReceiver.BroadcastRec;
import com.example.sif.MyApplication;
import com.example.sif.NeiBuLei.FollowList;
import com.example.sif.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.Call;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

public class ObtainSearchUser {

    private static Handler errorHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ToastZong.ShowToast(MyApplication.getContext(),"出错了");
        }
    };

    private static String path = "http://nmy1206.natapp1.cc/SearchUser.php";
    public static List<FollowList> followLists;
    public static void obtainUser(String key, Handler handler){
        HttpUtil.obtainUser(InValues.send(R.string.SearchUser),key, new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response){
                try {
                    String a = response.body().string();
                    followLists = new Gson().fromJson(a,new TypeToken<List<FollowList>>()
                    {}.getType());
                    BroadcastRec.sendReceiver(MyApplication.getContext(),"searchNewUser",0,"");
                }catch (Exception e){
                    handler.sendMessage(new Message());
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                errorHandler.sendMessage(new Message());
            }
        });
    }

}
