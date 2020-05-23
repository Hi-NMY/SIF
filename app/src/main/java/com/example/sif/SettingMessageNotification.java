package com.example.sif;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;

import androidx.annotation.RequiresApi;

import com.example.sif.Lei.ShowActivityBar.FragmentActivityBar;

public class SettingMessageNotification extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    private Switch mThumbSwitch;
    private Switch mCommentSwitch;
    private Switch mFollowSwitch;
    private Switch mSoundSwitch;
    private SharedPreferences sharedPreferences;
    private boolean a;
    private boolean b;
    private boolean c;
    private boolean d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_message_notification);
        initView();

        sharedPreferences = getSharedPreferences("settingMessage",MODE_PRIVATE);
        a = sharedPreferences.getBoolean("thumb",false);
        b = sharedPreferences.getBoolean("comment",false);
        c = sharedPreferences.getBoolean("follow", false);
        d = sharedPreferences.getBoolean("sound",false);
        //设置状态栏
        ZTL();
        //ActivityBar
        showActivityBar();

        //增加padding  避免状态栏遮挡
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.setting_message_r);
        setPadding(this, relativeLayout);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void showActivityBar() {
        FragmentActivityBar fragmentActivityBar = (FragmentActivityBar) getSupportFragmentManager().findFragmentById(R.id.setting_message_bar);
        ShowActivityBars showActivityBars = new ShowActivityBars(this, fragmentActivityBar);
        showActivityBars.showKongJian(true, false, true, false, false, false, false);
        showActivityBars.showUI(R.drawable.zuo_black, null, "消息通知", null, 0, 0);
        showActivityBars.uiFunction(1, 0, 0, 0, 0, 0, 0);
    }

    private void initView() {
        mThumbSwitch = (Switch) findViewById(R.id.thumb_switch);
        mCommentSwitch = (Switch) findViewById(R.id.comment_switch);
        mFollowSwitch = (Switch) findViewById(R.id.follow_switch);
        mSoundSwitch = (Switch) findViewById(R.id.sound_switch);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedPreferences = getSharedPreferences("settingMessage",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("thumb",mThumbSwitch.isChecked());
        editor.putBoolean("comment",mCommentSwitch.isChecked());
        editor.putBoolean("follow",mFollowSwitch.isChecked());
        editor.putBoolean("sound",mSoundSwitch.isChecked());
        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mThumbSwitch.setOnCheckedChangeListener(null);
        mCommentSwitch.setOnCheckedChangeListener(null);
        mFollowSwitch.setOnCheckedChangeListener(null);
        mSoundSwitch.setOnCheckedChangeListener(null);
        mThumbSwitch.setChecked(a);
        mCommentSwitch.setChecked(b);
        mFollowSwitch.setChecked(c);
        mSoundSwitch.setChecked(d);
        mThumbSwitch.setOnCheckedChangeListener(this);
        mCommentSwitch.setOnCheckedChangeListener(this);
        mFollowSwitch.setOnCheckedChangeListener(this);
        mSoundSwitch.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }
}
