package com.example.sif.Lei.NewAppDownLoad;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class ObtainVersion {
    public static int versionCode(Context context){
        PackageManager manager = context.getPackageManager();
        int code = 0;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(),0);
            code = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return code;
    }

    public static String versionName(Context context){
        PackageManager manager = context.getPackageManager();
        String name = null;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(),0);
            name = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return name;
    }
}
