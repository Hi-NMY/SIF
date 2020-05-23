package com.example.sif.Lei.MyToolClass;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.example.sif.Lei.LianJie.HttpUtil;
import com.example.sif.MyApplication;
import com.example.sif.NeiBuLei.SchoolTable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class UserSchoolTable {
    private static String path = "http://nmy1206.natapp1.cc/UserSchoolTable.php";

    private static Handler errorHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ToastZong.ShowToast(MyApplication.getContext(),"错误");
        }
    };

    public static List<SchoolTable> schoolTables;
    public static void obtainSchoolTable(String zhuanye, String banji, String nianji,String nextDate, Handler handler){
        HttpUtil.obtainTimeTable(path,zhuanye,banji,nianji,nextDate, new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String a = response.body().string();
                try {
                    schoolTables = new Gson().fromJson(a,new TypeToken<List<SchoolTable>>()
                    {}.getType());
                    if (handler != null){
                        Message message = new Message();
                        handler.sendMessage(message);
                    }
                }catch (Exception e){
                    sendError();
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                sendError();
            }
        });
    }

    private static void sendError(){
        Message message = new Message();
        errorHandler.sendMessage(message);
    }
}
