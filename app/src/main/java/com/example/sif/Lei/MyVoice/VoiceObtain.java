package com.example.sif.Lei.MyVoice;

import android.media.MediaRecorder;
import android.net.Uri;
import android.text.format.DateFormat;

import com.example.sif.Lei.MyToolClass.ToastZong;
import com.example.sif.Lei.NewAppDownLoad.DownLoadNewApp;
import com.example.sif.MyApplication;
import com.yalantis.ucrop.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

public class VoiceObtain {
    private static MediaRecorder mediaRecorder;
    private static String filePath;
    private static String fileName;
    private static String n;
    private static int key = 0;
    public static boolean startVoice(){
        if (mediaRecorder == null){
            //先实例化对象
            mediaRecorder = new MediaRecorder();
            try {
                //设置麦克风
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                //设置输出格式
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                //设置编码
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                fileName = DateFormat.format("yyyyMMdd_HHmmss", Calendar.getInstance(Locale.CHINA))+".m4a";
                String audioSaveDir = new DownLoadNewApp().isExisDir("mygoodvoice") + fileName;
                if (!FileUtils.isExternalStorageDocument(Uri.parse(FileUtils.getCreateFileName(audioSaveDir)))){
                    FileUtils.rename(audioSaveDir);
                }
                filePath = audioSaveDir;
                //准备
                mediaRecorder.setOutputFile(filePath);
                mediaRecorder.prepare();
                //开始
                mediaRecorder.start();
            } catch (IOException e) {
                ToastZong.ShowToast(MyApplication.getContext(),"请检查是否为SIF开启麦克风权限");
                key = 1;
            }catch (RuntimeException e){
                ToastZong.ShowToast(MyApplication.getContext(),"请检查是否为SIF开启麦克风权限");
                key = 1;
            }
        }
        if (key == 0){
            return true;
        }else {
            return false;
        }
    }

    public static String stopVoice(){
        n = filePath;
        try {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            filePath = "";
        }catch (Exception e){
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;

            File file = new File(filePath);
            if (file.exists()){
                file.delete();
                filePath = "";
            }
        }
        return n;
    }
}
