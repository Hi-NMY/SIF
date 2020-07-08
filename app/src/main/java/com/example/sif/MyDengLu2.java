package com.example.sif;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import com.example.sif.Lei.LianJie.HttpUtil;
import com.example.sif.Lei.LianJie.ShuJuLianJie;
import com.example.sif.Lei.MyToolClass.InValues;
import com.example.sif.Lei.MyToolClass.SendUserIp;
import com.example.sif.Lei.MyToolClass.ToastZong;
import com.example.sif.Lei.ShowActivityBar.FragmentActivityBar;
import com.tamsiree.rxui.view.dialog.RxDialogSure;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyDengLu2 extends BaseActivity implements View.OnClickListener {

    private EditText mDengluMiMa;
    private Button mDenglu;
    private String zhanghao;
    private TextView mForgetPassword;

    private static int MAXLENGTH = 0;

    protected static final int ERROR = 2;
    protected static final int SUCCESS = 1;
    protected static boolean SHOUDENG = false;

    private String path = "http://nmy1206.natapp1.cc/XueSheng_YanZheng2.php";
    private String path2 = "http://nmy1206.natapp1.cc/UserFollowToMe.php";


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SUCCESS:
                    // ToastZong.ShowToast(activity,(String)msg.obj);
                    String I = (String) msg.obj;
                    switch (I) {
                        case "0":
                            RongIMClient.connect(getMyToken(), new RongIMClient.ConnectCallback() {
                                //token无效
                                @Override
                                public void onTokenIncorrect() {
                                    ToastZong.ShowToast(MyApplication.getContext(),"token无效");
                                    Log.d("rongooooooo","null");
                                }
                                //回调成功
                                @Override
                                public void onSuccess(String s) {
                                    SharedPreferences sharedPreferences1 = getSharedPreferences("RongCloudUser",MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences1.edit();
                                    editor.putString("userId",getMyXueHao());
                                    editor.putString("token",getMyToken());
                                    editor.commit();
                                    UserInfo userInfo = new UserInfo(getMyXueHao(), getMyUserName(), Uri.parse(InValues.send(R.string.httpHeader) +"/UserImageServer/"+getMyXueHao()+"/HeadImage/myHeadImage.png"));
                                    RongIM.getInstance().setCurrentUserInfo(userInfo);
//                                    RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
//                                        @Override
//                                        public UserInfo getUserInfo(String s) {
//                                            UserInfo userInfo = new UserInfo(getMyXueHao(), getMyUserName(), Uri.parse("http://nmy1206.natapp1.cc/UserImageServer/"+getMyXueHao()+"/HeadImage/myHeadImage.png"));
//                                            RongIM.getInstance().refreshUserInfoCache(userInfo);
//                                            return userInfo;
//                                        }
//                                    },true);
                                    Log.d("rongooooooo",s);
                                }
                                //回调失败
                                @Override
                                public void onError(RongIMClient.ErrorCode errorCode) {
                                    if (errorCode == RongIMClient.ErrorCode.RC_CONN_REDIRECTED){
                                        SharedPreferences sharedPreferences1 = getSharedPreferences("RongCloudUser",MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences1.edit();
                                        editor.putString("userId",getMyXueHao());
                                        editor.putString("token",getMyToken());
                                        editor.commit();
                                    }
                                    ToastZong.ShowToast(MyApplication.getContext(),"error");
                                    Log.d("rongooooooo", String.valueOf(errorCode));
                                }
                            });
                            shouCiDengLu();
                            SharedPreferences sharedPreferences1 = getSharedPreferences("myid", MODE_PRIVATE);
                            String ip = sharedPreferences1.getString("id", "");
                            SendUserIp.sendIp(getMyXueHao(), ip, null);
                            Intent intent = new Intent(MyDengLu2.this, MyZhuYe.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            closeDiaLog();
                            startActivity(intent);
                            finish();
                            break;
                        case "6":
                            closeDiaLog();
                            ToastZong.ShowToast(MyDengLu2.this, "账号或密码错误！");
                            break;
                    }
                    break;
                case ERROR:
                    closeDiaLog();
                    ToastZong.ShowToast(MyDengLu2.this, "连接错误");
                    break;
            }
        }
    };
    private TextView mTokenObtain;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_deng_lu2);
        initView();

        //设置状态栏
        ZTL();
        //ActivityBar
        showActivityBar();

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.DengLu2_R);
        setPadding(this, relativeLayout);

        //登录按钮
        noXiaYiBu(mDengluMiMa, mDenglu, MAXLENGTH);

        //获取账号
        Intent intent = getIntent();
        zhanghao = intent.getStringExtra("zhanghao");

        RxDialogSure rxDialogSure = new RxDialogSure(this);
        rxDialogSure.setTitle("产品声明");
        rxDialogSure.setContent("此APP目前未完工，为测试版本，有任何问题请联系：\nQQ：944273286\nWeChat：18736423860\n版权声明：内部UI图标以及部分样式素材均为百度收集\n盗版必究！请使用正版软件！");
        rxDialogSure.setSureListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rxDialogSure.cancel();
            }
        });
        rxDialogSure.show();
    }

    private void initView() {
        mDengluMiMa = (EditText) findViewById(R.id.denglu_MiMa);
        mDenglu = (Button) findViewById(R.id.denglu);

        mDenglu.setOnClickListener(this);
        mForgetPassword = (TextView) findViewById(R.id.forget_password);
        mForgetPassword.setOnClickListener(this);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.denglu:
                final String userMima = mDengluMiMa.getText().toString().trim();
                while (true) {
                    if (TextUtils.isEmpty(userMima)) {
                        ToastZong.ShowToast(this, "学号不能为空！");
                        break;
                    }
                    if (userMima != null) {
                        showDiaLog(this, R.drawable.loading2);
                        ShuJuLianJie shuJuLianJie = new ShuJuLianJie(MyDengLu2.this, InValues.send(R.string.XueSheng_YanZheng2), "POST", handler);
                        ExecutorService executorService = Executors.newFixedThreadPool(15);
                        executorService.execute(new Runnable() {
                            @Override
                            public void run() {
                                shuJuLianJie.chuanShu(zhanghao, userMima);
                                sendUserThumb(1);
                                sendUserFollow(1, zhanghao);
                                sendUserFollowList(1, zhanghao);
                                sendUserFollowToMe(1, zhanghao);
                                HttpUtil.myFollow(InValues.send(R.string.UserFollowToMe), 0, getMyXueHao(), "", new Callback() {
                                    @Override
                                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                                    }

                                    @Override
                                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                                    }
                                });
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        break;
                    }
                }
                break;
            case R.id.forget_password:
                Intent intent1 = new Intent(this, RecretProtection.class);
                intent1.putExtra("funKey", 1);
                startActivity(intent1);
                break;
        }
    }

    private void shouCiDengLu() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("shoudeng", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("SD", SHOUDENG);
        editor.commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void showActivityBar() {
        FragmentActivityBar fragmentActivityBar = (FragmentActivityBar) getSupportFragmentManager().findFragmentById(R.id.Denglu_Frag_ActivityBar2);
        ShowActivityBars showActivityBars = new ShowActivityBars(this, fragmentActivityBar);
        showActivityBars.showKongJian(true, false, false, false, false, false, false);
        showActivityBars.showUI(R.drawable.zuo_white, (String) null, null, null, 0, 0);
        showActivityBars.uiFunction(1, 0, 0, 0, 0, 0, 0);
        showActivityBars.barBackground1(0);
    }

}
