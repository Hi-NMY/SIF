package com.example.sif.Lei.MyToolClass;

import com.example.sif.Lei.MyBroadcastReceiver.BroadcastRec;
import com.example.sif.MyApplication;
import com.example.sif.NeiBuLei.ServerTimeNow;
import com.google.gson.Gson;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ObtainServerTime {

    public static ServerTimeNow serverTimeNow;
    public static void obtainTime(){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url("http://quan.suning.com/getSysTime.do")
                .build();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String a = response.body().string();
                serverTimeNow = new Gson().fromJson(a,ServerTimeNow.class);
                int week = Integer.parseInt(MyDateClass.showWeekTable(serverTimeNow.getSysTime2(),1));
                BroadcastRec.sendReceiver(MyApplication.getContext(),"obtainWeek",week,"");
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }
        });
    }

}
