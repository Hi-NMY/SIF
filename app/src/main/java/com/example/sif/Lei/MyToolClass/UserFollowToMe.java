package com.example.sif.Lei.MyToolClass;

import android.os.Handler;
import android.os.Message;
import com.example.sif.Lei.LianJie.HttpUtil;
import com.example.sif.NeiBuLei.FollowList;
import com.example.sif.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.Call;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.List;

public class UserFollowToMe {

    private String xuehao;
    private Handler handler;
    public UserFollowToMe(String x) {
        this.xuehao = x;
        followSend(String.valueOf(-1));
    }

    public void sendHander(Handler h){
        this.handler = h;
    }

    private static String path = "";
    public List<FollowList> followLists;

    public void followSend(String i){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(xuehao);
        HttpUtil.followDynamic(InValues.send(R.string.SendFollowToMe),stringBuffer,i,new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String a = response.body().string();
                if (a != null&&!a.equals("1")&&!a.equals("")){
                    try {
                        Gson gson = new Gson();
                        followLists = gson.fromJson(a,new TypeToken<List<FollowList>>()
                        {}.getType());
                        toMessage(1);
                    }catch (Exception e){

                    }
                }else {
                    if (followLists != null && followLists.size() >0){
                        followLists.clear();
                    }
                    toMessage(2);
                }
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                if (e instanceof SocketTimeoutException){
                    toMessage(3);
                }
                if (e instanceof ConnectException){
                    toMessage(3);
                }
            }
        });
    }

    private void toMessage(int i){
        if (handler != null){
            Message message = new Message();
            switch (i){
                case 1:
                    handler.sendMessage(message);
                    break;
                case 2:
                    message.obj = 1;
                    handler.sendMessage(message);
                    break;
                case 3:
                    message.obj = 3;
                    handler.sendMessage(message);
                    break;
            }
        }
    }


}
