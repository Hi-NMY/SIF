package com.example.sif.Lei.MyToolClass;

import com.example.sif.MyApplication;

public class InValues {

    public static String send(int i) {
        return MyApplication.getContext().getResources().getString(i);
    }
}
