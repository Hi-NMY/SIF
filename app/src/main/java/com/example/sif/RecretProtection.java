package com.example.sif;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.sif.Lei.MyToolClass.ToastZong;
import com.example.sif.Lei.MyToolClass.UserSecret;
import com.example.sif.Lei.ShowActivityBar.FragmentActivityBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class RecretProtection extends BaseActivity implements View.OnClickListener {

    private NiceSpinner mRecretSpinner;
    private List<String> recret = new LinkedList<>(Arrays.asList("我的学号", "我的生日", "我最爱的人的名字", "我的手机号"));
    private EditText mChangeText;
    private TextView mChangeNumber;
    private FloatingActionButton mRecretCheck;
    private StringBuffer stringBuffer;
    private int recretKey = 1;
    private int funKey = 0;

    private Handler secretHanlder = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            closeDiaLog();
            switch (msg.what){
                case 0:
                    if (funKey == 0){
                        finish();
                        ToastZong.ShowToast(MyApplication.getContext(),"修改成功");
                    }else {
                        ToastZong.ShowToast(MyApplication.getContext(),"验证成功");
                        Intent intent = new Intent(MyApplication.getContext(),NewPassword.class);
                        intent.putExtra("newpwd",1);
                        startActivity(intent);
                    }
                    break;
                case 1:
                    if (funKey == 0){
                        ToastZong.ShowToast(MyApplication.getContext(),"修改错误，请重试");
                    }else {
                        ToastZong.ShowToast(MyApplication.getContext(),"密保错误");
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recret_protection);
        initView();

        funKey = getIntent().getIntExtra("funKey",0);

        //设置状态栏
        ZTL();
        //ActivityBar
        showActivityBar();

        //增加padding  避免状态栏遮挡
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.recret_r);
        setPadding(this, relativeLayout);

        showText();

        mRecretSpinner.attachDataSource(recret);

        spinnerListener();
    }

    private void spinnerListener() {
        mRecretSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                recretKey = position + 1;
            }
        });
    }

    private void showText() {
        mChangeText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mChangeText.setSelection(mChangeText.length());
                mChangeNumber.setText(String.valueOf(mChangeText.getText().length()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void showActivityBar() {
        FragmentActivityBar fragmentActivityBar = (FragmentActivityBar) getSupportFragmentManager().findFragmentById(R.id.recret_protection_bar);
        ShowActivityBars showActivityBars = new ShowActivityBars(this, fragmentActivityBar);
        showActivityBars.showKongJian(true, false, true, false, false, false, false);
        showActivityBars.showUI(R.drawable.zuo_black, null, "密保", null, 0, 0);
        showActivityBars.uiFunction(1, 0, 0, 0, 0, 0, 0);
    }

    private void initView() {
        mRecretSpinner = (NiceSpinner) findViewById(R.id.recret_spinner);
        mChangeText = (EditText) findViewById(R.id.change_Text);
        mChangeText.setOnClickListener(this);
        mChangeNumber = (TextView) findViewById(R.id.change_number);
        mChangeNumber.setOnClickListener(this);
        mRecretCheck = (FloatingActionButton) findViewById(R.id.recret_check);
        mRecretCheck.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.recret_check:
                stringBuffer = new StringBuffer();
                stringBuffer.append(recretKey);
                if (mChangeText.getText().toString().trim().equals("")){
                    ToastZong.ShowToast(MyApplication.getContext(),"密保不能为空");
                }else {
                    showDiaLog(this, R.drawable.loading);
                    stringBuffer.append(mChangeText.getText().toString().trim());
                    UserSecret.sendSecred(funKey + 1,getMyXueHao(),String.valueOf(stringBuffer),secretHanlder);
                }
                break;
        }
    }
}
