package com.example.sif.Lei.MyToolClass;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;

import com.example.sif.R;

public class ShowDiaLog extends AlertDialog {
    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;
    private Window window;
    public ShowDiaLog(Activity activity,int a,View view) {
        super(activity,false,null);
        builder = new AlertDialog.Builder(activity,a);
        builder.setView(view);
        alertDialog = builder.create();
    }

    public void logWindow(Drawable color){
        window = alertDialog.getWindow();
        window.setBackgroundDrawable(color);
    }

    public void showMyDiaLog(){
        alertDialog.show();
    }

    public void closeMyDiaLog(){
        alertDialog.dismiss();
    }

    public void bottomrView(){
        Window window = alertDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.verypopupview);
    }

    public void Cancelable(boolean a){
        alertDialog.setCancelable(a);
    }

    public void dismissListener(Dismiss dismiss){
        alertDialog.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dismiss.dismiss();
            }
        });
    }

    public interface Dismiss{
        void dismiss();
    }
}
