package com.example.sif;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import com.example.sif.Lei.LianJie.HttpUtil;
import com.example.sif.Lei.LianJie.ShuJuLianJie;
import com.example.sif.Lei.MyToolClass.InValues;
import com.example.sif.Lei.MyToolClass.ToastZong;
import com.example.sif.Lei.ShowActivityBar.FragmentActivityBar;
import com.example.sif.NeiBuLei.RongCloudClass;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import okhttp3.Call;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class ZhuCe extends BaseActivity implements View.OnClickListener {

    private TextView mZhuceTv;
    private EditText mZhuceUserName;
    private EditText mZhuceUserZhanghao;
    private EditText mZhuceUserMima;
    private FloatingActionButton mZhuceCheck;

    protected static final int ERROR = 2;
    protected static final int SUCCESS = 1;

    private boolean key = true;

    private String path = "";
    private String path5 = "";
    private String path2 = "";

    private Handler error = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            closeDiaLog();
            ToastZong.ShowToast(MyApplication.getContext(),"错误，请重试");
        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SUCCESS:
                    String I = (String) msg.obj;
                    switch (I) {
                        case "0":
                            obtainUserToken();
                            break;
                        case "1":
                            if (key){
                                closeDiaLog();
                                ToastZong.ShowToast(ZhuCe.this, "此账号已被注册！");
                            }else {
                                obtainUserToken();
                            }
                            break;
                        case "2":
                            closeDiaLog();
                            ToastZong.ShowToast(ZhuCe.this, "您非本校学生！");
                            break;
                        case "3":
                            closeDiaLog();
                            ToastZong.ShowToast(ZhuCe.this, "服务器错误！");
                            break;
                        case "4":
                            closeDiaLog();
                            ToastZong.ShowToast(ZhuCe.this, "用户名重复！");
                            break;
                        case "5":
                            closeDiaLog();
                            ToastZong.ShowToast(ZhuCe.this, "请输入正确的学号！");
                            break;
                    }
                    break;
                case ERROR:
                    closeDiaLog();
                    ToastZong.ShowToast(ZhuCe.this, "连接错误");
                    break;

            }
        }
    };

    private Handler successHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            closeDiaLog();
            ToastZong.ShowToast(ZhuCe.this, "注册成功");
            Intent intent = new Intent(ZhuCe.this, MyDengLu.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("zhanghao", mZhuceUserZhanghao.getText().toString());
            startActivity(intent);
            finish();
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhu_ce);
        initView();

        //设置状态栏
        ZTL();
        //ActivityBar
        showActBar();

        //改变字体
        Typeface SIF = Typeface.createFromAsset(this.getAssets(), "fonts/impact.ttf");
        mZhuceTv.setTypeface(SIF);

        //增加padding  避免状态栏遮挡
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.ZhuCe_R);
        setPadding(this, relativeLayout);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void showActBar() {
        FragmentActivityBar fragmentActivityBar = (FragmentActivityBar) getSupportFragmentManager().findFragmentById(R.id.ZhuCe_Frag_ActivityBar);
        ShowActivityBars showActivityBars = new ShowActivityBars(this, fragmentActivityBar);
        showActivityBars.showKongJian(true, false, true, false, false, false, false);
        showActivityBars.showUI(R.drawable.zuo_black, null, "请注册", null, null, 0, 0);
        showActivityBars.uiFunction(1, 0, 0, 0, 0, 0, 0);
    }

    private void initView() {
        mZhuceTv = (TextView) findViewById(R.id.zhuce_tv);
        mZhuceUserName = (EditText) findViewById(R.id.zhuce_user_name);
        mZhuceUserZhanghao = (EditText) findViewById(R.id.zhuce_user_zhanghao);
        mZhuceUserMima = (EditText) findViewById(R.id.zhuce_user_mima);
        mZhuceCheck = (FloatingActionButton) findViewById(R.id.zhuce_check);

        mZhuceCheck.setOnClickListener(this);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.zhuce_check:
                zhuCe();
                break;
        }
    }

    private void obtainUserToken(){
        HttpUtil.rongCloudServer(InValues.send(R.string.RongCloudUser),userZhangHao,userZhangHao,userName, new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                error.sendMessage(new Message());
                key = false;
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String a = response.body().string();
                RongCloudClass r = new Gson().fromJson(a,RongCloudClass.class);
                SharedPreferences sharedPreferences1 = getSharedPreferences("RongCloudUser",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences1.edit();
                editor.putString("userId",r.getUserId());
                editor.putString("token",r.getToken());
                editor.commit();
                HttpUtil.sendUserToken(InValues.send(R.string.SendToken),r.getToken(),r.getUserId(), new okhttp3.Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        error.sendMessage(new Message());
                        key = false;
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String a = response.body().string();
                        if (a.equals("0")){
                            successHandler.sendMessage(new Message());
                        }
                    }
                });
            }
        });
    }

    private String userName;
    private String userZhangHao;
    private String userMiMa;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void zhuCe() {
        userName = mZhuceUserName.getText().toString().trim();
        userZhangHao = mZhuceUserZhanghao.getText().toString().trim();
        userMiMa = mZhuceUserMima.getText().toString().trim();
        while (true) {
            if (TextUtils.isEmpty(userName)) {
                ToastZong.ShowToast(this, "用户名不能为空！");
                break;
            }
            if (TextUtils.isEmpty(userZhangHao)) {
                ToastZong.ShowToast(this, "学号不能为空！");
                break;
            }
            if (TextUtils.isEmpty(userMiMa)) {
                ToastZong.ShowToast(this, "密码不能为空！");
                break;
            }
            if (userName != null && userZhangHao != null && userMiMa != null) {
                showDiaLog(ZhuCe.this, R.drawable.loading);
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            sleep(100);
                            SharedPreferences sharedPreferences = getSharedPreferences("myid", MODE_PRIVATE);
                            String ip = sharedPreferences.getString("id", "");
                            ShuJuLianJie shuJuLianJie = new ShuJuLianJie(ZhuCe.this, InValues.send(R.string.YanZheng_ZhangHao), "POST", handler);
                            shuJuLianJie.chuanShu(userName, userZhangHao, userMiMa, ip);
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
