package com.example.sif.Lei.MyToolClass;

import android.widget.TextView;
import com.example.sif.MyApplication;
import com.example.sif.R;

public class UserNameJudge {

    public static String judgeName(String commentXuehao,String dynamicXueHao){
        if (commentXuehao.substring(0,9).equals(dynamicXueHao)){
            return "楼主";
        }else {
            return commentXuehao.substring(9);
        }
    }

    public static String judgeName(TextView mDynamicCommentName, String commentName, String commentXuehao, String dynamicXueHao){
        if (commentXuehao.equals(dynamicXueHao)){
            mDynamicCommentName.setTextColor(MyApplication.getContext().getColor(R.color.bilan));
            return "楼主";
        }else {
            return commentName;
        }
    }
}
