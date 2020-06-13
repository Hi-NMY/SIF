package com.example.sif.Lei.MyToolClass;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.sif.MyApplication;

public class UpdateShareTask {

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    static {
        getTaskShare();
    }

    private static void getTaskShare() {
        sharedPreferences = MyApplication.getContext().getSharedPreferences("taskNum", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static void updateTask(int a){
        switch (a){
            case 1:
                addSign();
                break;
            case 2:
                addDynamic();
                break;
            case 3:
                addTenDynamic();
                break;
            case 4:
                addGoodVoice();
                break;
            case 5:
                addGoToSpace();
                break;
        }
    }

    private static void addSign(){
        editor.putInt("sign",1);
        editor.commit();
    }

    private static void addDynamic(){
        editor.putInt("oneDynamic",1);
        editor.commit();
    }

    private static void addTenDynamic(){
        int a = sharedPreferences.getInt("thumbDynamic",0);
        editor.putInt("thumbDynamic",a + 1);
        editor.commit();
    }

    private static void addGoodVoice(){
        editor.putInt("goodVoice",1);
        editor.commit();
    }

    private static void addGoToSpace(){
        int a = sharedPreferences.getInt("goToSpace",0);
        editor.putInt("goToSpace",a + 1);
        editor.commit();
    }

    public static void over(String name){
        editor.putInt(name,-1);
        editor.commit();
    }
}
