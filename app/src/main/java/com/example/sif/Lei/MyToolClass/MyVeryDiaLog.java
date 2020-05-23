package com.example.sif.Lei.MyToolClass;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;

import com.example.sif.R;
import com.tamsiree.rxui.view.dialog.RxDialog;
import com.tamsiree.rxui.view.dialog.RxDialogScaleView;

public class MyVeryDiaLog {

    public static void transparentDiaLog(Activity activity, RxDialog rxDialog){
        View view = LayoutInflater.from(activity).inflate(R.layout.transparent_log,null);
        rxDialog.setContentView(view);
        rxDialog.show();
    }

    public static void veryImageDiaLog(RxDialogScaleView rxDialogScaleView, String path, String path1, Handler handler){
        rxDialogScaleView.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                Bitmap bitmap = MyBitMapChange.myBitmap(path);
                if (bitmap == null) {
                    Bitmap bitmap2 = MyBitMapChange.myBitmap(path1);
                    message.what = 1;
                    message.obj = bitmap2;
                    handler.sendMessage(message);
                }else {
                    message.what = 0;
                    message.obj = bitmap;
                    handler.sendMessage(message);
                }
            }
        }).start();
    }

    public static void veryImageDiaLog(RxDialogScaleView rxDialogScaleView, String path, Handler handler){
        rxDialogScaleView.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                Bitmap bitmap = MyBitMapChange.myBitmapFile(path);
                if (bitmap == null) {
                    Bitmap bitmap2 = MyBitMapChange.myBitmapFile(path);
                    message.what = 1;
                    message.obj = bitmap2;
                    handler.sendMessage(message);
                }else {
                    message.what = 0;
                    message.obj = bitmap;
                    handler.sendMessage(message);
                }
            }
        }).start();
    }
}
