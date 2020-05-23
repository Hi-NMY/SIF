package com.example.sif;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;

import androidx.annotation.RequiresApi;

import com.example.sif.Lei.MyToolClass.SendUserPrivacy;
import com.example.sif.Lei.MyToolClass.ToastZong;
import com.example.sif.Lei.ShowActivityBar.FragmentActivityBar;

import java.util.ArrayList;
import java.util.List;

public class SettingPrivacy extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    private Switch mYuanxiSwitch;
    private Switch mZhuanyeSwitch;
    private Switch mNianjiSwitch;
    private SharedPreferences sharedPreferences;
    private boolean a;
    private boolean b;
    private boolean c;
    private Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    SharedPreferences sharedPreferences = MyApplication.getContext().getSharedPreferences("privacySetting",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("mYuanxiSwitch",mYuanxiSwitch.isChecked());
                    editor.putBoolean("mZhuanyeSwitch",mZhuanyeSwitch.isChecked());
                    editor.putBoolean("mNianjiSwitch",mNianjiSwitch.isChecked());
                    editor.commit();
                    ToastZong.ShowToast(MyApplication.getContext(),"设置成功");
                    break;
                case 2:
                    ToastZong.ShowToast(MyApplication.getContext(),"设置失败");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_privacy);
        initView();

        sharedPreferences = getSharedPreferences("privacySetting",MODE_PRIVATE);
        a = sharedPreferences.getBoolean("mYuanxiSwitch",false);
        b = sharedPreferences.getBoolean("mZhuanyeSwitch",false);
        c = sharedPreferences.getBoolean("mNianjiSwitch", false);
        //设置状态栏
        ZTL();
        //ActivityBar
        showActivityBar();

        //增加padding  避免状态栏遮挡
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.privacy_r);
        setPadding(this, relativeLayout);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void showActivityBar() {
        FragmentActivityBar fragmentActivityBar = (FragmentActivityBar) getSupportFragmentManager().findFragmentById(R.id.setting_privacy_bar);
        ShowActivityBars showActivityBars = new ShowActivityBars(this, fragmentActivityBar);
        showActivityBars.showKongJian(true, false, true, false, false, false, false);
        showActivityBars.showUI(R.drawable.zuo_black, null, "隐私设置", null, 0, 0);
        showActivityBars.uiFunction(1, 0, 0, 0, 0, 0, 0);
    }

    private List<Switch> switches;
    private void initView() {
        switches = new ArrayList<>();
        mYuanxiSwitch = (Switch) findViewById(R.id.yuanxi_switch);
        mZhuanyeSwitch = (Switch) findViewById(R.id.zhuanye_switch);
        mNianjiSwitch = (Switch) findViewById(R.id.nianji_switch);
        switches.add(mYuanxiSwitch);
        switches.add(mZhuanyeSwitch);
        switches.add(mNianjiSwitch);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mYuanxiSwitch.isChecked() != a || mZhuanyeSwitch.isChecked() != b || mNianjiSwitch.isChecked() != c){
            StringBuffer stringBuffer = new StringBuffer();
            for (Switch s:switches){
                if (s.isChecked()){
                    stringBuffer.append("0");
                }else {
                    stringBuffer.append("1");
                }
            }
            SendUserPrivacy.userPrivacy(getMyXueHao(), String.valueOf(stringBuffer),handler);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mYuanxiSwitch.setOnCheckedChangeListener(null);
        mZhuanyeSwitch.setOnCheckedChangeListener(null);
        mNianjiSwitch.setOnCheckedChangeListener(null);
        mYuanxiSwitch.setChecked(a);
        mZhuanyeSwitch.setChecked(b);
        mNianjiSwitch.setChecked(c);
        mYuanxiSwitch.setOnCheckedChangeListener(this);
        mZhuanyeSwitch.setOnCheckedChangeListener(this);
        mNianjiSwitch.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

}
