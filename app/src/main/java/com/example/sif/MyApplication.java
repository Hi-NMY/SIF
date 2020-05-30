package com.example.sif;

import android.app.Application;
import android.content.Context;

import com.example.sif.Lei.MyBroadcastReceiver.BroadcastRec;
import com.tamsiree.rxkit.RxTool;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import org.litepal.LitePal;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        String appKey = "bmdehs6pbasws";
        LitePal.initialize(this);
        context = getApplicationContext();
        RongIM.init(context, appKey);
        ZXingLibrary.initDisplayOpinion(this);
        RxTool.init(this);
        RongIM.setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageWrapperListener() {
            @Override
            public boolean onReceived(Message message, int i, boolean b, boolean b1) {
                BroadcastRec.sendReceiver(context,"newUserProtection",1,"");
                return false;
            }
        });
    }

    public static Context getContext() {
        return context;
    }
}
