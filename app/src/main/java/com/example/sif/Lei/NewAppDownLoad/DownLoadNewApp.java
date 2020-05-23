package com.example.sif.Lei.NewAppDownLoad;

import android.os.Environment;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownLoadNewApp {

    private static DownLoadNewApp downLoadNewApp;
    private final OkHttpClient okHttpClient;

    public static DownLoadNewApp get(){
        if (downLoadNewApp == null){
            downLoadNewApp = new DownLoadNewApp();
        }

        return downLoadNewApp;
    }

    public DownLoadNewApp(){
        okHttpClient = new OkHttpClient();
    }

    public void downLoad(String url,String saveDir,OnDownloadListener onDownloadListener){
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                onDownloadListener.onDownloadError();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response){
                InputStream i = null;
                byte[] bytes = new byte[2048];
                int len = 0;
                FileOutputStream fileOutputStream = null;
                try {
                    String savePath = isExisDir(saveDir);
                    i = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = new File(savePath,getNameFile(url));
                    fileOutputStream = new FileOutputStream(file);
                    long num = 0;
                    while ((len = i.read(bytes)) != -1){
                        fileOutputStream.write(bytes,0,len);
                        num += len;
                        int progress = (int)(num * 1.0f / total * 100);
                        onDownloadListener.onDownloading(progress);
                    }
                    fileOutputStream.flush();
                    onDownloadListener.onDownloadSuccess(file);
                }catch (Exception e){
                    onDownloadListener.onDownloadError();
                }finally {
                    try {
                        if (i != null)
                            i.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fileOutputStream != null)
                            fileOutputStream.close();
                    } catch (IOException e) {
                    }
                }
            }
        });
    }

    public String isExisDir(String saveDir) throws IOException {
        File downloadFile = new File(Environment.getExternalStorageDirectory(),saveDir);
        if (!downloadFile.mkdirs()){
            downloadFile.createNewFile();
        }

        String savePath = downloadFile.getAbsolutePath();
        return savePath;
    }

    private String getNameFile(String url){
        return url.substring(url.lastIndexOf("/") + 1);
    }

    public interface OnDownloadListener{
        void onDownloadSuccess(File file);
        void onDownloading(int progress);
        void onDownloadError();
    }
}
