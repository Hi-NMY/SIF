package com.example.sif.Lei.MyToolClass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import com.example.sif.R;
import com.tamsiree.rxui.view.dialog.RxDialog;

public class EasyLoading {

    private static RxDialog searchLoading;
    public static void startLoading(Context context){
        searchLoading = new RxDialog(context, R.style.tran_dialog);
        View loading = LayoutInflater.from(context).inflate(R.layout.sign_loading_lay,null);
        searchLoading.setContentView(loading);
        searchLoading.show();
    }

    public static void dissmissLoading(){
        searchLoading.dismiss();
    }

}
