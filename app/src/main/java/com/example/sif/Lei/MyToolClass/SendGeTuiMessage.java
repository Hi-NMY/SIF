package com.example.sif.Lei.MyToolClass;

import com.example.sif.Lei.LianJie.HttpUtil;
import com.example.sif.R;
import okhttp3.Call;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class SendGeTuiMessage {

    public SendGeTuiMessage() {

    }

    private static String path = "";
    public static void sendGeTuiMessage(int i,String xuehao,String sendxuehao,String ip,String dynamicid,int fun){
        HttpUtil.sendGeTuiMessage(InValues.send(R.string.getuiServer),i,xuehao,sendxuehao,ip,dynamicid,fun,new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String a = response.body().string();
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }
        });
    }

}
