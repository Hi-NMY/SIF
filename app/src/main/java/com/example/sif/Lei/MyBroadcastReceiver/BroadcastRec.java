package com.example.sif.Lei.MyBroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class BroadcastRec {
    public static void sendReceiver(Context c,String intentName,int textone,String texttwo){
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(c);
        Intent intent = new Intent(intentName);
        intent.putExtra("textone",textone);
        intent.putExtra("texttwo",texttwo);
        localBroadcastManager.sendBroadcast(intent);
    }

    public static void obtainRecriver(Context c, String actionName, BroadcastReceiver receiver){
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(c);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(actionName);
        localBroadcastManager.registerReceiver(receiver,intentFilter);
    }
}
