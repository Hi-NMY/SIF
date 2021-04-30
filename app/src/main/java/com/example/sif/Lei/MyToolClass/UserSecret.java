package com.example.sif.Lei.MyToolClass;

import android.os.Handler;
import android.os.Message;
import com.example.sif.Lei.LianJie.HttpUtil;
import com.example.sif.R;
import okhttp3.Call;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class UserSecret {
    private static String path = "";
    public static void sendSecred(int fun, String xuehao, String secret, Handler handler){
        HttpUtil.sendSecret(InValues.send(R.string.SecretProtection),fun,xuehao,secret, new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String a = response.body().string();
                Message message = new Message();
                message.what = Integer.valueOf(a);
                handler.sendMessage(message);
            }
        });
    }
}
