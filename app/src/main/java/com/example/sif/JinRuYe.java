package com.example.sif;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.sif.Lei.MyToolClass.MyDateClass;
import com.example.sif.Lei.MyToolClass.ToastZong;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;
import org.litepal.LitePal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JinRuYe extends BaseActivity {
    private RelativeLayout mRL;
    private RelativeLayout mXiaYiBu1;
    private TextView mSIF;
    private TextView mSchoolIsFamily;
    private static int SIFTOP = 250;
    private ImageView mXiaYiBu2;

    private ExecutorService executorService;

    private static boolean SHOUDENG = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        //父类状态栏方法
        ZTL();

        SharedPreferences sharedPreferences = getSharedPreferences("settingMessage",MODE_PRIVATE);
        boolean a = sharedPreferences.getBoolean("key",false);
        if (!a){
            SharedPreferences sharedPreferences1 = getSharedPreferences("settingMessage",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences1.edit();
            editor.putBoolean("key",true);
            editor.putBoolean("thumb",true);
            editor.putBoolean("comment",true);
            editor.putBoolean("follow",true);
            editor.putBoolean("sound",true);
            editor.commit();
        }
        SharedPreferences sharedPreferences2 = getSharedPreferences("privacySetting",MODE_PRIVATE);
        boolean a1 = sharedPreferences2.getBoolean("key",false);
        if (!a1){
            SharedPreferences sharedPreferences1 = getSharedPreferences("privacySetting",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences1.edit();
            editor.putBoolean("key",true);
            editor.putBoolean("mYuanxiSwitch",true);
            editor.putBoolean("mZhuanyeSwitch",true);
            editor.putBoolean("mNianjiSwitch",true);
            editor.commit();
        }

        SharedPreferences sharedPreferences3 = getSharedPreferences("todayDate",MODE_PRIVATE);
        boolean a2 = sharedPreferences3.getBoolean("key",false);
        if (!a2){
           // SharedPreferences sharedPreferences1 = getSharedPreferences("todayDate",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences3.edit();
            editor.putBoolean("key",true);
            editor.putString("Date","2020-01-01");
            editor.commit();
        }

        SharedPreferences sharedPreferences4 = getSharedPreferences("taskNum",MODE_PRIVATE);
        boolean a3 = sharedPreferences4.getBoolean("key",false);
        if (!a3){
            // SharedPreferences sharedPreferences1 = getSharedPreferences("todayDate",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences4.edit();
            editor.putBoolean("key",true);
            editor.putInt("sign",0);
            editor.putInt("oneDynamic",0);
            editor.putInt("thumbDynamic",0);
            editor.putInt("goodVoice",0);
            editor.putInt("goToSpace",0);
            editor.commit();
        }



        int key = getIntent().getIntExtra("key",1);

        //修改字体
        Typeface SIF = Typeface.createFromAsset(getAssets(), "fonts/impact.ttf");
        mSIF.setTypeface(SIF);
        Typeface School = Typeface.createFromAsset(getAssets(), "fonts/ilsscript.ttf");
        mSchoolIsFamily.setTypeface(School);

        //动画
        final Animation animation1 = new TranslateAnimation(0, 0, 0, 0 - SIFTOP);
        animation1.setDuration(1000);
        animation1.setFillAfter(true);

        final Animation animation3 = new RotateAnimation(0f, 90f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f );
        animation3.setDuration(1000);
        animation3.setFillAfter(true);

//        SharedPreferences sharedPreferences = this.getSharedPreferences("createku",MODE_PRIVATE);
//        boolean aa = sharedPreferences.getBoolean("ku",false);
//        if (sharedPreferences==null||aa == false){
        LitePal.getDatabase();
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putBoolean("ku",true);
//            editor.commit();
//        }

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                mRL.startAnimation(animation1);
                animation1.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mXiaYiBu1.setVisibility(View.VISIBLE);
                        mXiaYiBu1.startAnimation(animation3);
                        mXiaYiBu2.animate().alpha(1).setDuration(1000).start();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
            }
        };

        Handler handler1 = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 6:
                        ToastZong.ShowToast(JinRuYe.this,"服务器连接错误,请关闭APP");
                        break;
                }
            }
        };
        String path = "http://nmy1206.natapp1.cc/shuaXinGuangChang.php";
        String path1 = "http://nmy1206.natapp1.cc/userSpace.php";
        String path2 = "http://nmy1206.natapp1.cc/Thumb.php";
        String path3 = "http://nmy1206.natapp1.cc/UserSchool.php";
        String path4 = "http://nmy1206.natapp1.cc/User.php";
        final Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    if (shouCiDengLu()){
                        sleep(1000);
                        Message message = new Message();
                        handler.sendMessage(message);
                        mXiaYiBu2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(JinRuYe.this,MyDengLu.class);
                                startActivity(intent);
                            }
                        });
                    }else {
                        geRenXinXiShare();
                        String nowDate = MyDateClass.showNowDate();
                        String xuehao = getMyXueHao();
                        executorService = Executors.newFixedThreadPool(15);
                        executorService.execute(new Runnable() {
                            @Override
                            public void run() {
                                huoQuGeRenXinXi(JinRuYe.this,path3,path4,xuehao,handler1);
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                shuaXinGuangChang(path,null,nowDate,1);
                                sendUserSpace(1,path1,nowDate,xuehao,null);
                                huoquThumb(path2,xuehao);
                                sendUserFollow(1,xuehao);
                                Thread.yield();
                                sendUserThumb(1);
                                Thread.yield();
                                sendUserFollowList(1,xuehao);
                                Thread.yield();
                                sendUserFollowToMe(1,xuehao);
                                sendIbFollow(1,xuehao);
                                SharedPreferences s = getSharedPreferences("RongCloudUser",MODE_PRIVATE);
                                String token = s.getString("token","");
                                RongIMClient.connect(token, new RongIMClient.ConnectCallback() {
                                    //token无效
                                    @Override
                                    public void onTokenIncorrect() {
                                        ToastZong.ShowToast(MyApplication.getContext(),"token无效");
                                        Log.d("rongooooooo","null");
                                    }
                                    //回调成功
                                    @Override
                                    public void onSuccess(String s) {
                                        UserInfo userInfo = new UserInfo(getMyXueHao(), getMyUserName(), Uri.parse("http://nmy1206.natapp1.cc/UserImageServer/"+getMyXueHao()+"/HeadImage/myHeadImage.png"));
                                        RongIM.getInstance().setCurrentUserInfo(userInfo);
//                                        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
//                                            @Override
//                                            public UserInfo getUserInfo(String s) {
//                                                UserInfo userInfo = new UserInfo(getMyXueHao(), getMyUserName(), Uri.parse("http://nmy1206.natapp1.cc/UserImageServer/"+getMyXueHao()+"/HeadImage/myHeadImage.png"));
//                                                RongIM.getInstance().refreshUserInfoCache(userInfo);
//
//                                                return userInfo;
//                                            }
//                                        },true);
                                        Log.d("rongooooooo",s);
                                    }
                                    //回调失败
                                    @Override
                                    public void onError(RongIMClient.ErrorCode errorCode) {
                                        ToastZong.ShowToast(MyApplication.getContext(),"error");
                                        Log.d("rongooooooo","error");
                                    }
                                });
                                try {
                                    Thread.sleep(1400);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        executorService.shutdown();
                        while (true){
                            if (executorService.isTerminated()){
                                Intent intent = new Intent(JinRuYe.this,MyZhuYe.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("key",key);
                                JinRuYe.this.startActivity(intent);
                                finish();
                                break;
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();

    }

    private boolean shouCiDengLu(){
        SharedPreferences sharedPreferences = this.getSharedPreferences("shoudeng", Context.MODE_PRIVATE);
        boolean shoudeng = sharedPreferences.getBoolean("SD",SHOUDENG);
        return shoudeng;
    }

    private void initView() {
        mSIF = (TextView) findViewById(R.id.SIF);
        mSchoolIsFamily = (TextView) findViewById(R.id.SchoolIsFamily);
        mRL = (RelativeLayout) findViewById(R.id.RL);
        mXiaYiBu1 = (RelativeLayout) findViewById(R.id.XiaYiBu1);
        mXiaYiBu2 = (ImageView) findViewById(R.id.XiaYiBu2);
    }
}
