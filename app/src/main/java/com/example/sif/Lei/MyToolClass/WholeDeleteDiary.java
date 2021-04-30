package com.example.sif.Lei.MyToolClass;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.example.sif.Lei.LianJie.HttpUtil;
import com.example.sif.MyApplication;
import com.example.sif.NeiBuLei.UserDiaryClass;
import com.example.sif.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.Call;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

public class WholeDeleteDiary {

    private static String path = "";
    private static Handler errorHanlder = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ToastZong.ShowToast(MyApplication.getContext(),"超时，请重试");
        }
    };


    public static List<UserDiaryClass> userDiaryClasses;
    public static void wholeDiary(int id,String xuehao,String diarydate,Handler handler){
        HttpUtil.wrightDiary(InValues.send(R.string.UserDiary),0,id,xuehao,diarydate, new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    String a = response.body().string();
                    userDiaryClasses = new Gson().fromJson(a,new TypeToken<List<UserDiaryClass>>()
                    {}.getType());
                    if (handler != null){
                        Message message = new Message();
                        handler.sendMessage(message);
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                sendError();
            }
        });
    }

    public static void deleteDiary(int id,String xuehao,String diarydate,Handler handler){
        HttpUtil.wrightDiary(InValues.send(R.string.UserDiary),1,id,xuehao,diarydate, new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String a = response.body().string();
                if (handler != null){
                    Message message = new Message();
                    message.obj = a;
                    handler.sendMessage(message);
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
        errorHanlder.sendMessage(message);
    }
}
