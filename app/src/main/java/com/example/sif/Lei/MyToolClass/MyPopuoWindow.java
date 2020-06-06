package com.example.sif.Lei.MyToolClass;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.sif.Lei.MyBroadcastReceiver.BroadcastRec;
import com.example.sif.MyApplication;
import com.example.sif.R;

public class MyPopuoWindow extends PopupWindow implements View.OnClickListener{

    private View view;
    private TextView mPopuowindowText1;
    private TextView mPopuowindowText2;
    private RelativeLayout mPopuowindowR;
    private LocalBroadcastManager localBroadcastManager;

    public MyPopuoWindow(Context context) {
        super(context);
        init(context);
        setMyWindow();
    }

    private void init(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.my_popuowindow, null);
        mPopuowindowR = (RelativeLayout)view.findViewById(R.id.popuowindow_R);
        mPopuowindowText1 = (TextView)view.findViewById(R.id.popuowindow_text1);
        mPopuowindowText2 = (TextView)view.findViewById(R.id.popuowindow_text2);

        mPopuowindowText1.setOnClickListener(this);
        mPopuowindowText2.setOnClickListener(this);
    }

    public void setMyWindow(){
        this.setContentView(view);
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.MyPopuoWindow);
        this.setBackgroundDrawable(new ColorDrawable(0x00000000));
        this.setOutsideTouchable(true);
        mPopuowindowR.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height = mPopuowindowR.getTop();
                int y = (int)event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP){
                    if (y < height){
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    private int funKey = -1;
    public void textFunction(int i){
        funKey = i;
        switch (i){
            case 1:
                mPopuowindowText1.setVisibility(View.VISIBLE);
                mPopuowindowText2.setVisibility(View.VISIBLE);
                mPopuowindowText1.setText("举报");
                break;
            case 2:
                mPopuowindowText1.setVisibility(View.VISIBLE);
                mPopuowindowText2.setVisibility(View.GONE);
                mPopuowindowText2.setPadding(0,0,0,10);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.popuowindow_text1:
                if (funKey != 1){
                    localBroadcastManager = LocalBroadcastManager.getInstance(MyApplication.getContext());
                    Intent intent = new Intent("deleteComment");
                    localBroadcastManager.sendBroadcast(intent);
                }else {
                    ToastZong.ShowToast(MyApplication.getContext(),"举报成功");
                }

                dismiss();
                break;
            case R.id.popuowindow_text2:
                dismiss();
                BroadcastRec.sendReceiver(MyApplication.getContext(),"replyComment",0,"");
                break;
        }
    }
}
