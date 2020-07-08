package com.example.sif.Lei.MyToolClass;

import android.os.Handler;
import android.os.Message;
import com.example.sif.Lei.LianJie.HttpUtil;
import com.example.sif.NeiBuLei.SchoolShopClass;
import com.example.sif.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.Call;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ObtainUserShop {
    private static String path = "http://nmy1206.natapp1.cc/ObtainUserShop.php";
    public static List<SchoolShopClass> schoolShopClasses;
    public static void obtainUserShop(String xuehao, Handler handler){
        schoolShopClasses = new ArrayList<>();
        HttpUtil.obtainUserShop(InValues.send(R.string.ObtainUserShop),xuehao, new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String a = response.body().string();
                schoolShopClasses = new Gson().fromJson(a,new TypeToken<List<SchoolShopClass>>()
                {}.getType());
                handler.sendMessage(new Message());
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }
        });
    }
}
