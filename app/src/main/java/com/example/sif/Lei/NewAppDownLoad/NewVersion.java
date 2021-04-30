package com.example.sif.Lei.NewAppDownLoad;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import androidx.core.content.FileProvider;
import com.example.sif.Lei.MyToolClass.InValues;
import com.example.sif.Lei.MyToolClass.ShowDiaLog;
import com.example.sif.Lei.MyToolClass.ToastZong;
import com.example.sif.R;
import com.example.sif.SheZhi;
import com.tamsiree.rxui.view.RxProgressBar;
import com.tamsiree.rxui.view.dialog.RxDialogSureCancel;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class NewVersion{

    private static String path = "";
    private static String url = "";
    private static Context context;
    private static ShowDiaLog showDiaLog;

    private static Handler newVerSionHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.obj != null && !msg.obj.equals("0") && !msg.obj.equals("Tunnelnot found\n")){
                RxDialogSureCancel rxDialogSureCancel = new RxDialogSureCancel(context);
                rxDialogSureCancel.setTitle("检测到有新的版本,是否更新?\n此次版本更新内容如下");
                rxDialogSureCancel.getTitleView().setGravity(Gravity.CENTER);
                rxDialogSureCancel.getTitleView().setLineSpacing(1,1.2f);
                rxDialogSureCancel.setContent(msg.obj.toString());
                rxDialogSureCancel.setSure("取消");
                rxDialogSureCancel.setCancel("更新");
                rxDialogSureCancel.getCancelView().setTextColor(context.getResources().getColor(R.color.bilan));
                rxDialogSureCancel.getContentView().setGravity(Gravity.LEFT);
                rxDialogSureCancel.getContentView().setTextColor(context.getResources().getColor(R.color.gray));
                rxDialogSureCancel.getContentView().setTextSize(16);
                rxDialogSureCancel.getContentView().setLineSpacing(1,1.2f);
                rxDialogSureCancel.setCancelable(false);
                rxDialogSureCancel.setSureListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rxDialogSureCancel.cancel();
                    }
                });

                rxDialogSureCancel.setCancelListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rxDialogSureCancel.cancel();
                        View view = LayoutInflater.from(context).inflate(R.layout.rx_bar_newversion,null);
                        showDiaLog = new ShowDiaLog((Activity) context,R.style.AlertDialog_qr,view);
                        RxProgressBar progress_bar_new = (RxProgressBar)view.findViewById(R.id.progress_bar_new);
                        showDiaLog.logWindow(new ColorDrawable(Color.TRANSPARENT));
                        showDiaLog.Cancelable(false);
                        showDiaLog.showMyDiaLog();
                        DownLoadNewApp.get().downLoad(InValues.send(R.string.SIFAPK), "download", new DownLoadNewApp.OnDownloadListener() {
                            @Override
                            public void onDownloadSuccess(File file) {
                                showDiaLog.closeMyDiaLog();
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);
                                    i.setDataAndType(uri, "application/vnd.android.package-archive");
                                } else {
                                    i.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                                }
                                context.startActivity(i);
                            }

                            @Override
                            public void onDownloading(int progress) {
                                ((Activity) context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progress_bar_new.setProgress(progress);
                                    }
                                });
                            }

                            @Override
                            public void onDownloadError() {
                                ((Activity) context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastZong.ShowToast(context,"下载错误,请重新下载");
                                    }
                                });
                                showDiaLog.closeMyDiaLog();
                            }
                        });
                    }
                });
                rxDialogSureCancel.show();
            }else {
                if (context instanceof SheZhi){
                    ToastZong.ShowToast(context,"已是最新版本");
                }
            }
        }
    };

    public static void inspectVerSion(Context c){
        context = c;
        RequestBody requestBody = new FormBody.Builder()
                .add("versionCode", String.valueOf(ObtainVersion.versionCode(c)))
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(InValues.send(R.string.MyAppVersion))
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response){
                try {
                    String a = response.body().string();
                    String b = a.substring(0,1);
                    Message message = new Message();
                    if (b.equals("0")){
                        message.obj = b;
                    }else {
                        message.obj = a;
                    }
                    if (a.length() < 150){
                        newVerSionHandler.sendMessage(message);
                    }
                }catch (Exception e){

                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }
        });
    }

}
