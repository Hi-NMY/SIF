package com.example.sif.Lei.MyToolClass;

import android.app.Activity;
import android.text.TextUtils;

import java.io.File;

import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class ImageCompression {

    public String path = null;
    public void compression(Activity activity, String url,String imageName){
        File file = new File(imageName);
                try {
                    Luban.with(activity)
                            .load(url)
                            .ignoreBy(90)
                            .setTargetDir(file.getPath())
                            .filter(new CompressionPredicate() {
                                @Override
                                public boolean apply(String path) {
                                    return !(TextUtils.isEmpty(path)) || path.toLowerCase().endsWith(".gif");
                                }
                            })
                            .setCompressListener(new OnCompressListener() {
                                @Override
                                public void onStart() {

                                }

                                @Override
                                public void onSuccess(File file) {
                                    path = String.valueOf(file);
                                }

                                @Override
                                public void onError(Throwable e) {
                                    path = null;
                                }
                            });
                }catch (Exception e){
                    e.printStackTrace();
                }

    }
}
