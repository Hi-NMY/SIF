package com.example.sif.Lei.GeTuiServer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.sif.JinRuYe;
import com.example.sif.Lei.MyBroadcastReceiver.BroadcastRec;
import com.example.sif.Lei.MyToolClass.ActivityWindowState;
import com.example.sif.Lei.MyToolClass.SendUserIp;
import com.example.sif.MessageNotice;
import com.example.sif.NeiBuLei.NoticeClass;
import com.example.sif.R;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTNotificationMessage;
import com.igexin.sdk.message.GTTransmitMessage;

public class DemoIntentService extends GTIntentService {

    @Override
    public void onReceiveServicePid(Context context, int i) {

    }

    //接收cid
    @Override
    public void onReceiveClientId(Context context, String s) {
      //  Log.e(TAG, "onReceiveClientId -> " + "clientid = " + s);
        SharedPreferences sharedPreferences1 = getSharedPreferences("myid",MODE_PRIVATE);
        String a = sharedPreferences1.getString("id","");
        SharedPreferences sharedPreferences2 = getSharedPreferences("shoudeng",MODE_PRIVATE);
        boolean b = sharedPreferences2.getBoolean("SD",true);
        if ((TextUtils.isEmpty(s) && !s.equals(a))||TextUtils.isEmpty(a)){
            SharedPreferences sharedPreferences = getSharedPreferences("myid",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("id",s);
            editor.commit();
            SharedPreferences sharedPreferences3 = getSharedPreferences("userSchool",MODE_PRIVATE);
            String x = sharedPreferences3.getString("xuehao","");
            if (!b && !TextUtils.isEmpty(x)){
                SendUserIp.sendIp(x,s,null);
            }
        }
    }

    //处理透传消息  详见Demo
    private String content;
    private String title;
    private int key = 0;
    private int numkey = 0;
    private Intent intent1;
    private boolean a;
    private boolean b;
    private boolean c;
    private boolean d;
    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage gtTransmitMessage) {
        title = "您有新的消息";
        SharedPreferences setting = getSharedPreferences("settingMessage",MODE_PRIVATE);
        a = setting.getBoolean("thumb",true);
        b = setting.getBoolean("comment",true);
        c = setting.getBoolean("follow", true);
        d = setting.getBoolean("sound",true);

        saveNotice(gtTransmitMessage);
        sendContent();

        writeIntent(context);

        broadcastSend();
    }
    // cid 离线上线通知
    @Override
    public void onReceiveOnlineState(Context context, boolean b) {

    }
    // 各种事件处理回执
    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage gtCmdMessage) {
        Log.e(TAG, "bbbbbbbbbb -> " + "clientid = " + gtCmdMessage);
    }

    // 通知到达，只有个推通道下发的通知会回调此方法
    @Override
    public void onNotificationMessageArrived(Context context, GTNotificationMessage gtNotificationMessage) {
        Log.e(TAG, "aaaaaaaaaa -> " + "clientid = " + gtNotificationMessage.getContent());
    }

    // 通知点击，只有个推通道下发的通知会回调此方法
    @Override
    public void onNotificationMessageClicked(Context context, GTNotificationMessage gtNotificationMessage) {

    }

    private String message;
    private int fun;
    private String sendXuehao;
    private int see;
    private String dynamicId;
    private NoticeClass noticeClass;
    private String officialNotice;
    private String messageZong;

    private void saveNotice(GTTransmitMessage gtTransmitMessage){
        message = new String(gtTransmitMessage.getPayload());
        fun = Integer.valueOf(message.substring(0,1));
        if (fun != 5){
            sendXuehao = message.substring(1,10);
            see = Integer.valueOf(message.substring(10,11));
            dynamicId = message.substring(11);
            noticeClass = new NoticeClass();
            noticeClass.setFun(fun);
            noticeClass.setSendXueHao(sendXuehao);
            noticeClass.setDynamicId(dynamicId);
            noticeClass.setSee(see);
            noticeClass.save();
        }else {
            StringBuffer stringBuffer = new StringBuffer();
            officialNotice = message.substring(17);
            messageZong = message.substring(1);
            SharedPreferences sharedPreferences1 = getSharedPreferences("officialMessage",MODE_PRIVATE);
            String on = sharedPreferences1.getString("notice","");
            if (on != null) {
                stringBuffer.append(on);
            }
            stringBuffer.append(messageZong + "&");
            SharedPreferences sharedPreferences = getSharedPreferences("officialMessage",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("notice", String.valueOf(stringBuffer));
            editor.commit();
        }
    }

    private void sendContent(){
        switch (fun){
            case 1:
                if (!a){
                    content = "";
                }else {
                    content = "有人赞了你的动态";
                }
                break;
            case 2:
                if (!b){
                    content = "";
                }else {
                    content = "有人对你的动态进行了评论";
                }
                break;
            case 3:
                if (!c){
                    content = "";
                }else {
                    content = "有人关注了你";
                }
                break;
            case 4:
                content = "你的语音被Bui~了一下";
                break;
            case 5:
                title = "官方通知";
                content = officialNotice;
                break;
        }
    }

    private void writeIntent(Context context){
        boolean a = ActivityWindowState.activityState(context);
        if (content != null && !content.equals("") && content.length() > 0){
            if (a){
                intent1 = new Intent(this, MessageNotice.class);
                intent1.putExtra("key",0);
                if (fun == 5){
                    intent1.putExtra("ofkey",true);
                }
            }else {
                intent1 = new Intent(this, JinRuYe.class);
                intent1.putExtra("key",0);
            }
            sendIntent();
        }
    }

    private void sendIntent(){
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent1, 0);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        key += 1;
        if(Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            //只在Android O之上需要渠道，这里的第一个参数要和下面的channelId一样
            NotificationChannel notificationChannel = new NotificationChannel(String.valueOf(key),"name",NotificationManager.IMPORTANCE_HIGH);
            //如果这里用IMPORTANCE_NOENE就需要在系统的设置里面开启渠道，通知才能正常弹出
            notificationManager.createNotificationChannel(notificationChannel);
        }

        if (d){
            Notification notification = new NotificationCompat.Builder(this,String.valueOf(key))
                    .setContentTitle(title)
                    .setContentText(content)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.push_small)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.push))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .build();
            notificationManager.notify(key, notification);
        }else {
            Notification notification = new NotificationCompat.Builder(this,String.valueOf(key))
                    .setContentTitle(title)
                    .setContentText(content)
                    .setSound(null)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.push_small)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.push))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .build();
            notificationManager.notify(key, notification);
        }
    }


    private void broadcastSend(){
        if (fun != 5){
            BroadcastRec.sendReceiver(this,"newNotice",0,"");
        }else {
            BroadcastRec.sendReceiver(this,"newNotice",3,"");
        }

    }

}
