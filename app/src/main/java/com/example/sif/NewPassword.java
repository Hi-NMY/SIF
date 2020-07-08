package com.example.sif;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import androidx.annotation.RequiresApi;
import com.example.sif.Lei.LianJie.HttpUtil;
import com.example.sif.Lei.MyToolClass.InValues;
import com.example.sif.Lei.MyToolClass.ToastZong;
import com.example.sif.Lei.ShowActivityBar.FragmentActivityBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import okhttp3.Call;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class NewPassword extends BaseActivity implements View.OnClickListener {

    private EditText mZhuceUserName;
    private EditText mZhuceUserZhanghao;
    private EditText mZhuceUserMima;
    private FloatingActionButton mZhuceCheck;
    private RelativeLayout relativeLayout;

    private int newpwdKey = 0;

    private Handler errorHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            closeDiaLog();
            ToastZong.ShowToast(MyApplication.getContext(),"修改错误");
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler succeed = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            closeDiaLog();
            switch (msg.what){
                case 0:
                    finish();
                    ToastZong.ShowToast(MyApplication.getContext(),"修改成功");
                    break;
                case 1:
                    ToastZong.ShowToast(MyApplication.getContext(),"修改错误");
                    break;
            }
        }
    };
    private Handler succeedRecret = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            closeDiaLog();
            switch (msg.what){
                case 0:
                    Intent intent = new Intent(MyApplication.getContext(),JinRuYe.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    MyApplication.getContext().startActivity(intent);
                    finish();
                    ToastZong.ShowToast(MyApplication.getContext(),"修改成功");
                    break;
                case 1:
                    ToastZong.ShowToast(MyApplication.getContext(),"修改错误");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        newpwdKey = getIntent().getIntExtra("newpwd",0);
        initView();
        //设置状态栏
        ZTL();
        //ActivityBar
        showActivityBar();

        //增加padding  避免状态栏遮挡
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.newpassword_r);
        setPadding(this, relativeLayout);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void showActivityBar() {
        FragmentActivityBar fragmentActivityBar = (FragmentActivityBar) getSupportFragmentManager().findFragmentById(R.id.new_password_bar);
        ShowActivityBars showActivityBars = new ShowActivityBars(this, fragmentActivityBar);
        showActivityBars.showKongJian(true, false, true, false, false, false, false);
        showActivityBars.showUI(R.drawable.zuo_black, null, "修改密码", null, 0, 0);
        showActivityBars.uiFunction(1, 0, 0, 0, 0, 0, 0);
    }


    private void initView() {
        mZhuceUserName = (EditText) findViewById(R.id.zhuce_user_name);
        mZhuceUserZhanghao = (EditText) findViewById(R.id.zhuce_user_zhanghao);
        mZhuceUserMima = (EditText) findViewById(R.id.zhuce_user_mima);
        mZhuceCheck = (FloatingActionButton) findViewById(R.id.zhuce_check);
        relativeLayout = (RelativeLayout)findViewById(R.id.zhuce_rll);

        mZhuceCheck.setOnClickListener(this);
        if (newpwdKey == 1){
            relativeLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.zhuce_check:
                if (newpwdKey == 0){
                    newPassword();
                }else {
                    recretPassword();
                }
                break;
        }
    }

    private void recretPassword() {
        final String userZhangHao = mZhuceUserZhanghao.getText().toString().trim();
        final String userMiMa = mZhuceUserMima.getText().toString().trim();
        while (true){
            if (TextUtils.isEmpty(userZhangHao)){
                ToastZong.ShowToast(this,"密码不能为空！");
                break;
            }
            if (TextUtils.isEmpty(userMiMa) || !userZhangHao.equals(userMiMa)){
                ToastZong.ShowToast(this,"请确认密码！");
                break;
            }
            if (userZhangHao!=null&&userMiMa!=null){
                showDiaLog(this,R.drawable.loading);
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            sleep(100);
                            HttpUtil.newPassword(InValues.send(R.string.NewPassword),getMyXueHao(),"**",userZhangHao, new okhttp3.Callback() {
                                @Override
                                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                    String a = response.body().string();
                                    Message message = new Message();
                                    message.what = Integer.valueOf(a);
                                    succeedRecret.sendMessage(message);
                                }
                                @Override
                                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                    Message message = new Message();
                                    errorHandler.sendMessage(message);
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                break;
            }
        }
    }

    private String path = "http://nmy1206.natapp1.cc/NewPassword.php";
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void newPassword(){
        final String userName = mZhuceUserName.getText().toString().trim();
        final String userZhangHao = mZhuceUserZhanghao.getText().toString().trim();
        final String userMiMa = mZhuceUserMima.getText().toString().trim();
        while (true){
            if (TextUtils.isEmpty(userName)){
                ToastZong.ShowToast(this,"旧密码不能为空！");
                break;
            }
            if (TextUtils.isEmpty(userZhangHao)){
                ToastZong.ShowToast(this,"新密码不能为空！");
                break;
            }
            if (TextUtils.isEmpty(userMiMa) || !userZhangHao.equals(userMiMa)){
                ToastZong.ShowToast(this,"请确认新密码！");
                break;
            }
            if (userName!=null&&userZhangHao!=null&&userMiMa!=null){
                showDiaLog(this,R.drawable.loading);
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            sleep(100);
                            HttpUtil.newPassword(InValues.send(R.string.NewPassword),getMyXueHao(),userName,userZhangHao, new okhttp3.Callback() {
                                @Override
                                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                    String a = response.body().string();
                                    Message message = new Message();
                                    message.what = Integer.valueOf(a);
                                    succeed.sendMessage(message);
                                }
                                @Override
                                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                    Message message = new Message();
                                    errorHandler.sendMessage(message);
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                break;
            }
        }
    }
}
