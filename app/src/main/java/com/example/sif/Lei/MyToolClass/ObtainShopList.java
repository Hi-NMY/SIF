package com.example.sif.Lei.MyToolClass;

import android.os.Handler;
import android.os.Message;

import com.example.sif.Lei.LianJie.HttpUtil;
import com.example.sif.NeiBuLei.SchoolShopClass;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class ObtainShopList {

    private static String path = "http://nmy1206.natapp1.cc/ObtainSchoolShop.php";
    public static List<SchoolShopClass> schoolShopClasses;
    public static void obtainMoreShop(String nowTiem, Handler handler){
        HttpUtil.obtainSchoolShop(path,nowTiem, new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String a = response.body().string();
                try {
                    if (!a.equals("1")){
                        schoolShopClasses = new Gson().fromJson(a,new TypeToken<List<SchoolShopClass>>()
                        {}.getType());
                        handler.sendMessage(new Message());
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
