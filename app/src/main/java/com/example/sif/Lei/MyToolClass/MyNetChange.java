package com.example.sif.Lei.MyToolClass;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.sif.JinRuYe;

public class MyNetChange extends BroadcastReceiver {
    private int i = 1;
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()){
            if (i == 2){
                ToastZong.ShowToast(context,"网络已连接");
                Intent intent1 = new Intent(context, JinRuYe.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);
                i = 1;
            }
        }else {
            i = 2;
            ToastZong.ShowToast(context,"请检查网络连接");
        }
    }
}
