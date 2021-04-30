package com.example.sif.Lei.MyToolClass;

import android.os.Handler;
import android.os.Message;
import com.example.sif.Lei.LianJie.HttpUtil;
import com.example.sif.R;
import okhttp3.Call;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class SendUserIp {

    public SendUserIp() {

    }

    private static String path = "";
    public static void sendIp(String xuehao, String ip, Handler handler){
        HttpUtil.sendUserIP(InValues.send(R.string.SendUserIP),xuehao,ip, new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String a = response.body().string();
                if (handler != null){
                    Message message = new Message();
                    message.what = 0;
                    handler.sendMessage(message);
                }
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }
        });
    }


}
