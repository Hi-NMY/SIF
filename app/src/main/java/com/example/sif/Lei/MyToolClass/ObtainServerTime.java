package com.example.sif.Lei.MyToolClass;

import android.content.SharedPreferences;
import com.example.sif.Lei.MyBroadcastReceiver.BroadcastRec;
import com.example.sif.MyApplication;
import com.example.sif.NeiBuLei.ServerTimeNow;
import com.example.sif.R;
import com.google.gson.Gson;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;

public class ObtainServerTime {

    public static ServerTimeNow serverTimeNow;

    static {
        obtainIsMonday();
    }

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    private static void obtainIsMonday() {
        sharedPreferences = MyApplication.getContext().getSharedPreferences("isMonday",MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static void obtainTime(){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(InValues.send(R.string.getSysTime))
                .build();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String a = response.body().string();
                serverTimeNow = new Gson().fromJson(a,ServerTimeNow.class);
                int week = Integer.parseInt(MyDateClass.showWeekTable(serverTimeNow.getSysTime2(),1));
                boolean firstKey = sharedPreferences.getBoolean("firstKey",false);
                if (week >= 2 && !firstKey){
                    editor.putBoolean("Monday",true);
                    editor.commit();
                }else {
                    editor.putBoolean("Monday",false);
                    editor.commit();
                }
                BroadcastRec.sendReceiver(MyApplication.getContext(),"obtainWeek",week,"");
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }
        });
    }

    public static void compareDate(){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(InValues.send(R.string.getSysTime))
                .build();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String a = response.body().string();
                serverTimeNow = new Gson().fromJson(a,ServerTimeNow.class);
                boolean compare = MyDateClass.compareDate(serverTimeNow.getSysTime2());
                if(!compare){
                    SharedPreferences sharedPreferences = MyApplication.getContext().getSharedPreferences("taskNum",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("key",true);
                    editor.putInt("sign",0);
                    editor.putInt("oneDynamic",0);
                    editor.putInt("thumbDynamic",0);
                    editor.putInt("goodVoice",0);
                    editor.putInt("goToSpace",0);
                    editor.commit();
                }
                BroadcastRec.sendReceiver(MyApplication.getContext(),"obtainTask",0,"");
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }
        });
    }

}
