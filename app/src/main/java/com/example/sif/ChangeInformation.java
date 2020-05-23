package com.example.sif;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.sif.Lei.MyToolClass.ToastZong;
import com.example.sif.Lei.ShowActivityBar.FragmentActivityBar;

public class ChangeInformation extends BaseActivity {

    public EditText mChangeText;
    public int i;
    public String text;
    public String xuehao1 = getMyXueHao();
    private TextView mChangeNumber;
    private TextView mChangeTv;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_information);
        initView();

        ZTL();

        Intent intent = getIntent();
        text = intent.getStringExtra("uText");
        i = intent.getIntExtra("id", 0);

        showActivityBar(i);

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.ChangeInfor_R);
        setPadding(this, relativeLayout);

        showText(text);
    }

    private void showText(String text) {
        mChangeText.setText(text);
        mChangeText.setSelection(text.length());
        mChangeNumber.setText(String.valueOf(mChangeText.getText().length()));
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
    private void showActivityBar(int i) {
        FragmentActivityBar fragmentActivityBar = (FragmentActivityBar) getSupportFragmentManager().findFragmentById(R.id.change_information);
        ShowActivityBars showActivityBars = new ShowActivityBars(this, fragmentActivityBar);
        if (i == 1) {
            mChangeText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
            showActivityBars.showKongJian(true, false, true, false, true, false, false);
            showActivityBars.showUI(R.drawable.zuo_black, null, "编辑昵称", null, "保存", 0, 0);
            showActivityBars.uiFunction(1, 0, 0, 0, 2, 0, 0);
        }
        if (i == 2) {
            mChangeText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
            mChangeTv.setText("/20");
            showActivityBars.showKongJian(true, false, true, false, true, false, false);
            showActivityBars.showUI(R.drawable.zuo_black, null, "编辑招呼语", null, "保存", 0, 0);
            showActivityBars.uiFunction(1, 0, 0, 0, 2, 0, 0);
        }
        if (i == 0) {
            ToastZong.ShowToast(this, "错误！");
        }

    }

    private void initView() {
        mChangeText = (EditText) findViewById(R.id.change_Text);
        mChangeNumber = (TextView) findViewById(R.id.change_number);
        mChangeTv = (TextView) findViewById(R.id.change_tv);
    }

}
