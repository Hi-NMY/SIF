package com.example.sif;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;

import com.example.sif.Lei.MyToolClass.ShowDiaLog;
import com.example.sif.Lei.NewAppDownLoad.NewVersion;
import com.example.sif.Lei.ShowActivityBar.FragmentActivityBar;

public class SheZhi extends BaseActivity implements View.OnClickListener {

    private RelativeLayout mTuichuButton;
    private RelativeLayout version;
    private RelativeLayout mAccountSafe;
    private RelativeLayout mPrivacySettings;
    private RelativeLayout mMessageNotification;
    private RelativeLayout mHelp;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_she_zhi);
        initView();

        //设置状态栏
        ZTL();
        //ActivityBar
        showActivityBar();

        //增加padding  避免状态栏遮挡
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.SheZhi_R);
        setPadding(this, relativeLayout);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void showActivityBar() {
        FragmentActivityBar fragmentActivityBar = (FragmentActivityBar) getSupportFragmentManager().findFragmentById(R.id.SheZhi_Avtivitybar);
        ShowActivityBars showActivityBars = new ShowActivityBars(this, fragmentActivityBar);
        showActivityBars.showKongJian(true, false, true, false, false, false, false);
        showActivityBars.showUI(R.drawable.zuo_black, null, "设置", null, 0, 0);
        showActivityBars.uiFunction(1, 0, 0, 0, 0, 0, 0);
    }

    private void initView() {
        mTuichuButton = (RelativeLayout) findViewById(R.id.tuichu_button);

        mTuichuButton.setOnClickListener(this);
        version = (RelativeLayout) findViewById(R.id.version);
        version.setOnClickListener(this);
        mAccountSafe = (RelativeLayout) findViewById(R.id.account_safe);
        mAccountSafe.setOnClickListener(this);
        mPrivacySettings = (RelativeLayout) findViewById(R.id.privacy_settings);
        mPrivacySettings.setOnClickListener(this);
        mMessageNotification = (RelativeLayout) findViewById(R.id.message_notification);
        mMessageNotification.setOnClickListener(this);
        mHelp = (RelativeLayout) findViewById(R.id.help);
        mHelp.setOnClickListener(this);
    }

    private static boolean SHOUDENG = true;
    private ShowDiaLog showDiaLog;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.account_safe:
                Intent intentSettingAccountSafe = new Intent(this,SettingAccountSafe.class);
                startActivity(intentSettingAccountSafe);
                break;
            case R.id.privacy_settings:
                Intent intentSettingPrivacy = new Intent(this,SettingPrivacy.class);
                startActivity(intentSettingPrivacy);
                break;
            case R.id.message_notification:
                Intent intentMessageNotification = new Intent(this, SettingMessageNotification.class);
                startActivity(intentMessageNotification);
                break;
            case R.id.help:
                Intent intentSettingHelp = new Intent(this,SettingHelp.class);
                startActivity(intentSettingHelp);
                break;
            case R.id.version:
                NewVersion.inspectVerSion(this);
                break;
            case R.id.tuichu_button:
                View view = LayoutInflater.from(this).inflate(R.layout.dialog_two, null);
                view.setVerticalFadingEdgeEnabled(true);
                showDiaLog = new ShowDiaLog(this, R.style.AlertDialog_Function, view);
                showDiaLog.logWindow(new ColorDrawable(Color.TRANSPARENT));
                showDiaLog.showMyDiaLog();
                Button deleteDynamic = (Button) view.findViewById(R.id.delete_GuangChang_Dynamic);
                Button deleteCancel = (Button) view.findViewById(R.id.delete_cancel);

                deleteDynamic.setText("确认退出");
                deleteDynamic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences sharedPreferences = MyApplication.getContext().getSharedPreferences("shoudeng", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("SD", SHOUDENG);
                        editor.commit();

                        Intent intent = new Intent(MyApplication.getContext(), JinRuYe.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        setMyXueHao(null);
                        startActivity(intent);
                        finish();
                        showDiaLog.closeMyDiaLog();
                    }
                });

                deleteCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDiaLog.closeMyDiaLog();
                    }
                });
                break;
        }
    }
}
