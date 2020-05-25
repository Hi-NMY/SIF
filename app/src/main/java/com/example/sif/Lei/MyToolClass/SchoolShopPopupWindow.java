package com.example.sif.Lei.MyToolClass;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.sif.Lei.NiceImageView.SolidImageView;
import com.example.sif.R;
import com.zhy.view.flowlayout.TagFlowLayout;

public class SchoolShopPopupWindow extends PopupWindow implements View.OnClickListener {

    private Context context;
    private View view;

    public SchoolShopPopupWindow(Context c) {
        this.context = c;
        this.view = LayoutInflater.from(c).inflate(R.layout.shoppopupview, null);
        this.setContentView(view);
        initView(view);
        this.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        ColorDrawable colorDrawable = new ColorDrawable(0xb0000000);
        this.setBackgroundDrawable(colorDrawable);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.verypopupview);

    }

    private EditText mToshopNotice;
    private RecyclerView mToshopImageList;
    private SolidImageView mToshopAddimage;
    private TagFlowLayout mToshopLabel;
    private TextView mToshopAddlabel;
    private TextView mNowshop;
    private TextView mStopshop;
    private Button mCloseButton;
    private Button mRightButton;
    private void initView(View view) {
        mToshopNotice = (EditText)view.findViewById(R.id.toshop_notice);
        mToshopImageList = (RecyclerView)view.findViewById(R.id.toshop_image_list);
        mToshopAddimage = (SolidImageView)view.findViewById(R.id.toshop_addimage);
        mToshopLabel = (TagFlowLayout)view.findViewById(R.id.toshop_label);
        mToshopAddlabel = (TextView)view.findViewById(R.id.toshop_addlabel);
        mNowshop = (TextView)view.findViewById(R.id.nowshop);
        mStopshop = (TextView)view.findViewById(R.id.stopshop);
        mCloseButton = (Button)view.findViewById(R.id.close_button);
        mRightButton = (Button)view.findViewById(R.id.right_button);
        mCloseButton.setOnClickListener(this);
        mRightButton.setOnClickListener(this);
        mToshopNotice.setOnClickListener(this);
        mToshopAddimage.setOnClickListener(this);
        mToshopAddlabel.setOnClickListener(this);
        mNowshop.setOnClickListener(this);
        mStopshop.setOnClickListener(this);
    }


    private int shopstate;
    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.close_button:

                break;
            case R.id.right_button:

                break;
            case R.id.toshop_notice:

                break;
            case R.id.toshop_addimage:

                break;
            case R.id.toshop_addlabel:

                break;
            case R.id.nowshop:
                shopstate = 0;
                mNowshop.setTextColor(R.color.ziti);
                mStopshop.setTextColor(R.color.lightgray);
                break;
            case R.id.stopshop:
                shopstate = 1;
                mNowshop.setTextColor(R.color.lightgray);
                mStopshop.setTextColor(R.color.ziti);
                break;
        }
    }
}
