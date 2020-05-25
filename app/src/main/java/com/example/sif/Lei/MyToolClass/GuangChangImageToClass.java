package com.example.sif.Lei.MyToolClass;

import android.app.Activity;

import androidx.recyclerview.widget.GridLayoutManager;

import com.example.sif.NeiBuLei.DouBleImagePath;

import java.util.ArrayList;
import java.util.List;

public class GuangChangImageToClass {

    public static GridLayoutManager newView(Activity a,String d,String xuehao){
        if (imageToClass(d,xuehao).size() == 3){
            GridLayoutManager gridLayoutManager = new GridLayoutManager(a, 3);
            return gridLayoutManager;
        }else {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(a, 2);
            return gridLayoutManager;
        }
    }

    public static GridLayoutManager shopNewView(Activity a,String d,String xuehao){
        if (shopimageToClass(d,xuehao).size() == 3){
            GridLayoutManager gridLayoutManager = new GridLayoutManager(a, 3);
            return gridLayoutManager;
        }else {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(a, 2);
            return gridLayoutManager;
        }
    }

    public static List imageToClass(String paths,String xuehao){
        String[] ss = paths.split("@");
        List<DouBleImagePath> douBleImagePaths = new ArrayList<>();
        for (String s : ss){
            DouBleImagePath douBleImagePath = new DouBleImagePath();
            douBleImagePath.setMinPath("http://nmy1206.natapp1.cc/" + s);
            douBleImagePath.setMaxPath("http://nmy1206.natapp1.cc/UserImageServer/" + xuehao + "/ADynamicImage/" + s.substring(39));
            douBleImagePaths.add(douBleImagePath);
        }
        return douBleImagePaths;
    }

    public static List shopimageToClass(String paths,String xuehao){
        String[] ss = paths.split("@");
        List<DouBleImagePath> douBleImagePaths = new ArrayList<>();
        for (String s : ss){
            DouBleImagePath douBleImagePath = new DouBleImagePath();
            douBleImagePath.setMinPath("http://nmy1206.natapp1.cc/" + s);
            douBleImagePath.setMaxPath("http://nmy1206.natapp1.cc/UserImageServer/" + xuehao + "/ASchoolShop/" + s.substring(39));
            douBleImagePaths.add(douBleImagePath);
        }
        return douBleImagePaths;
    }
}
