package com.example.sif;

import android.app.Application;
import android.content.Context;

import com.tamsiree.rxkit.RxTool;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import org.litepal.LitePal;

import io.rong.imkit.RongIM;

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
    }

    public static Context getContext() {
        return context;
    }
}
