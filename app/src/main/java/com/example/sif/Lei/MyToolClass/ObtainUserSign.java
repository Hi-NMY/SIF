package com.example.sif.Lei.MyToolClass;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.example.sif.Lei.LianJie.HttpUtil;
import com.example.sif.Lei.MyBroadcastReceiver.BroadcastRec;
import com.example.sif.MyApplication;
import com.example.sif.NeiBuLei.UserSignClass;
import com.google.gson.Gson;
import okhttp3.Call;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.litepal.LitePal;

import java.io.IOException;

public class ObtainUserSign {

    private static Handler errorHanlder = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ToastZong.ShowToast(MyApplication.getContext(),"阿欧，出错了");
        }
    };

    private static String path = "http://nmy1206.natapp1.cc/ObtainSign.php";
    private static String path1 = "http://nmy1206.natapp1.cc/StartSign.php";
    private static String path2 = "http://nmy1206.natapp1.cc/PatchSign.php";
    private static String path3 = "http://nmy1206.natapp1.cc/TaskAddSign.php";
    public static UserSignClass userSignClass;
    public static int keySign = -1;

    public static void obtainSign(String xuehao){
        HttpUtil.obtainSign(path,xuehao, new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    String a = response.body().string();
                    userSignClass = new Gson().fromJson(a,UserSignClass.class);
                    keySign = userSignClass.getSignday();
                    LitePal.deleteAll(UserSignClass.class);
                    if (userSignClass.save()){
                        BroadcastRec.sendReceiver(MyApplication.getContext(),"obtainWeek",-1,"");
                    }
                }catch (Exception e){
                    errorHanlder.sendMessage(new Message());
                }
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                errorHanlder.sendMessage(new Message());
            }
        });
    }

    public static void updateSign(String xuehao, int startSign,int coinSize, Handler handler){
        HttpUtil.updateSign(path1,startSign,xuehao,coinSize, new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    String a = response.body().string();
                    if (a.equals("0") && handler != null){
                        handler.sendMessage(new Message());
                    }
                }catch (Exception e){
                    errorHanlder.sendMessage(new Message());
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                errorHanlder.sendMessage(new Message());
            }
        });
    }

    public static void PatchSign(String xuehao, int startSign,int coinSize,Handler handler){
        HttpUtil.patchSign(path2,startSign,xuehao,coinSize, new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    String a = response.body().string();
                    if (a.equals("0") && handler != null){
                        handler.sendMessage(new Message());
                    }
                }catch (Exception e){
                    errorHanlder.sendMessage(new Message());
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                errorHanlder.sendMessage(new Message());
            }
        });
    }

    public static void updateTaskCoin(String xuehao,int coinSize, Handler handler){
        HttpUtil.updateTaskCoin(path3,xuehao,coinSize, new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    String a = response.body().string();
                    if (a.equals("0") && handler != null){
                        handler.sendMessage(new Message());
                    }
                }catch (Exception e){
                    errorHanlder.sendMessage(new Message());
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                errorHanlder.sendMessage(new Message());
            }
        });
    }

}
