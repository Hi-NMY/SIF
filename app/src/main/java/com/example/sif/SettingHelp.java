package com.example.sif;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;

import com.example.sif.Lei.ShowActivityBar.FragmentActivityBar;
import com.ms.square.android.expandabletextview.ExpandableTextView;

public class SettingHelp extends BaseActivity implements View.OnClickListener {

    private ExpandableTextView mExpandTextView;
    private ExpandableTextView mExpandTextView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_help);
        initView();

        //设置状态栏
        ZTL();
        //ActivityBar
        showActivityBar();

        //增加padding  避免状态栏遮挡
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.helpRzong);
        setPadding(this, relativeLayout);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void showActivityBar() {
        FragmentActivityBar fragmentActivityBar = (FragmentActivityBar) getSupportFragmentManager().findFragmentById(R.id.setting_help_bar);
        ShowActivityBars showActivityBars = new ShowActivityBars(this, fragmentActivityBar);
        showActivityBars.showKongJian(true, false, true, false, false, false, false);
        showActivityBars.showUI(R.drawable.zuo_black, null, "帮助", null, 0, 0);
        showActivityBars.uiFunction(1, 0, 0, 0, 0, 0, 0);
    }

    private void initView() {
        mExpandTextView = (ExpandableTextView) findViewById(R.id.expand_text_view);
        mExpandTextView.setText("遇到BUG怎么办\n请以以下方式联系\n1：QQ->944273286\n2：WeChat->18736423860");
        mExpandTextView1 = (ExpandableTextView) findViewById(R.id.expand_text_view1);
        mExpandTextView1.setText("特别提醒\n本软件为测试娱乐软件，功能未完善，请勿将其用于其\n他用途");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
}
