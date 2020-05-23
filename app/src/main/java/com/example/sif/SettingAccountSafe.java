package com.example.sif;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;

import com.example.sif.Lei.ShowActivityBar.FragmentActivityBar;

public class SettingAccountSafe extends BaseActivity implements View.OnClickListener {

    private RelativeLayout mNewPassword;
    private RelativeLayout mRecretSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_account_safe);
        initView();

        //设置状态栏
        ZTL();
        //ActivityBar
        showActivityBar();

        //增加padding  避免状态栏遮挡
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.account_r);
        setPadding(this, relativeLayout);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void showActivityBar() {
        FragmentActivityBar fragmentActivityBar = (FragmentActivityBar) getSupportFragmentManager().findFragmentById(R.id.setting_account_bar);
        ShowActivityBars showActivityBars = new ShowActivityBars(this, fragmentActivityBar);
        showActivityBars.showKongJian(true, false, true, false, false, false, false);
        showActivityBars.showUI(R.drawable.zuo_black, null, "账户与安全", null, 0, 0);
        showActivityBars.uiFunction(1, 0, 0, 0, 0, 0, 0);
    }

    private void initView() {
        mNewPassword = (RelativeLayout) findViewById(R.id.new_password);
        mNewPassword.setOnClickListener(this);
        mRecretSetting = (RelativeLayout) findViewById(R.id.recret_setting);
        mRecretSetting.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_password:
                Intent intent = new Intent(this, NewPassword.class);
                intent.putExtra("newpwd",0);
                startActivity(intent);
                break;
            case R.id.recret_setting:
                Intent intent1 = new Intent(this, RecretProtection.class);
                intent1.putExtra("funKey",0);
                startActivity(intent1);
                break;
        }
    }
}
