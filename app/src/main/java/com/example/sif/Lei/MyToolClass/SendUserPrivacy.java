package com.example.sif.Lei.MyToolClass;

import android.os.Handler;
import android.os.Message;
import com.example.sif.Lei.LianJie.HttpUtil;
import com.example.sif.R;
import okhttp3.Call;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class SendUserPrivacy {
    private static String path = "http://nmy1206.natapp1.cc/ModifyPrivacy.php";

    public static void userPrivacy(String xuehao,String privacy,Handler handler){
        HttpUtil.myPrivacy(InValues.send(R.string.ModifyPrivacy),xuehao,privacy, new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Message message = new Message();
                message.what = 2;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String a = response.body().string();
                if (a.equals("0") || a.equals(0)){
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                }else {
                    Message message = new Message();
                    message.what = 2;
                    handler.sendMessage(message);
                }
            }
        });
    }
}
