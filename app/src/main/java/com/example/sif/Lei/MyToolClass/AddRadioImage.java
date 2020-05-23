package com.example.sif.Lei.MyToolClass;

import android.graphics.drawable.Drawable;
import android.widget.RadioButton;

import com.example.sif.MyApplication;

public class AddRadioImage {
    public static void resetRadioButtonImage(int drawableId, RadioButton radioButton) {
        //定义底部标签图片大小和位置
        Drawable drawable_news = MyApplication.getContext().getResources().getDrawable(drawableId);
        //当这个图片被绘制时，给他绑定一个矩形 ltrb规定这个矩形
        drawable_news.setBounds(0, 0, 70, 70);
        //设置图片在文字的哪个方向
        radioButton.setCompoundDrawables(drawable_news, null, null, null);
    }
}
