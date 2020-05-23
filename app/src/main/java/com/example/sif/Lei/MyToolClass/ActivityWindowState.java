package com.example.sif.Lei.MyToolClass;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

public class ActivityWindowState {
    public static boolean activityState(Context context){
        ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcessInfos = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo r:appProcessInfos){
            String a = context.getPackageName();
            if (r.processName.equals(a)){
                if (r.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    return true;
                }else{
                    return false;
                }
            }
        }
        return false;
    }
}
