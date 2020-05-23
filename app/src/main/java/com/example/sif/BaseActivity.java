package com.example.sif;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sif.Lei.LianJie.HttpUtil;
import com.example.sif.Lei.MyToolClass.IbClass;
import com.example.sif.Lei.MyToolClass.IbFollow;
import com.example.sif.Lei.MyToolClass.MyNetChange;
import com.example.sif.Lei.MyToolClass.ShowDiaLog;
import com.example.sif.Lei.MyToolClass.ToastZong;
import com.example.sif.Lei.MyToolClass.UserDynamicComment;
import com.example.sif.Lei.MyToolClass.UserDynamicThumb;
import com.example.sif.Lei.MyToolClass.UserFollow;
import com.example.sif.Lei.MyToolClass.UserFollowList;
import com.example.sif.Lei.MyToolClass.UserFollowToMe;
import com.example.sif.Lei.ShowActivityBar.FragmentActivityBar;
import com.example.sif.Lei.ZhuangTaiLan.StatusBarUtil;
import com.example.sif.NeiBuLei.GuangChangUserXinXi;
import com.example.sif.NeiBuLei.MyThumb;
import com.example.sif.NeiBuLei.User;
import com.example.sif.NeiBuLei.UserSchool;
import com.example.sif.NeiBuLei.UserSpace;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.igexin.sdk.PushManager;

import org.jetbrains.annotations.NotNull;
import org.litepal.LitePal;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;
import pl.droidsonroids.gif.GifImageView;


public class BaseActivity extends AppCompatActivity {

    private MessageNotice.NewOfficialNotice newOfficialNotice;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushManager.getInstance().initialize(this);
        initReceiver();
    }

    private MyNetChange myNetChange;
    private void initReceiver(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        myNetChange = new MyNetChange();
        registerReceiver(myNetChange, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myNetChange);
    }

    private int times = 0;
    //在处理权限时的回调
    private final int REQUEST_PHONE_PERMISSIONS = 0;
    //检查全新的核心方法
    public void checkPermission(){
        times++;
        final List<String> permissionsList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if ((checkSelfPermission(Manifest.permission.INTERNET)!= PackageManager.PERMISSION_GRANTED)) permissionsList.add(Manifest.permission.INTERNET);
            if ((checkSelfPermission(Manifest.permission.ACCESS_WIFI_STATE)!= PackageManager.PERMISSION_GRANTED)) permissionsList.add(Manifest.permission.ACCESS_WIFI_STATE);
            if ((checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE)!= PackageManager.PERMISSION_GRANTED)) permissionsList.add(Manifest.permission.ACCESS_NETWORK_STATE);
            if ((checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)) permissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if ((checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)) permissionsList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            if ((checkSelfPermission(Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED)) permissionsList.add(Manifest.permission.RECORD_AUDIO);
            if ((checkSelfPermission(Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)) permissionsList.add(Manifest.permission.CAMERA);
            if (permissionsList.size() != 0){
                if(times==1){
                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                            REQUEST_PHONE_PERMISSIONS);
                }else{
                    new AlertDialog.Builder(this)
                            .setCancelable(true)
                            .setTitle("提示")
                            .setMessage("获取不到授权，APP将无法正常使用，请允许APP获取权限！")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                                REQUEST_PHONE_PERMISSIONS);
                                    }
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    finish();//这里对该界面直接进行销毁，让用户从新进入该界面
                                }
                    }).show();
                }
            }else{
                //initData();//初始化数据
            }
        }else{
            //initData();//初始化数据
        }
    }

    private int t = 0;
    public void checkVoice(ShowDiaLog showDiaLog, String recordAudio){
        t++;
        final List<String> permissionsList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if ((checkSelfPermission(recordAudio)!= PackageManager.PERMISSION_GRANTED)) permissionsList.add(recordAudio);
            if (permissionsList.size() != 0){
                if(t == 1){
                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                            REQUEST_PHONE_PERMISSIONS);
                }else{
                    new AlertDialog.Builder(this)
                            .setCancelable(true)
                            .setTitle("提示")
                            .setMessage("获取不到授权，请允许APP获取权限！")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                                REQUEST_PHONE_PERMISSIONS);
                                    }
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    if (showDiaLog != null){
                                        showDiaLog.closeMyDiaLog();
                                    }
                                }
                            }).show();
                }
            }
        }
    }


    private static float MINALPHA = 0.3f;
    private static float MAXALPHA = 1.0f;

    public static String userImageHeadDate;

    private Handler handlerZong = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    ToastZong.ShowToast(MyApplication.getContext(),"连接超时");
                    break;
                case 1:
                    ToastZong.ShowToast(MyApplication.getContext(),"连接错误");
                    break;
                case 3:

                    break;
            }
        }
    };

    private Handler error = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ToastZong.ShowToast(MyApplication.getContext(),"错误");
        }
    };

    //状态栏设置
    public void ZTL(){
        //StatusBarUtil.setRootViewFitsSystemWindows(this,true);
        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        StatusBarUtil.setRootViewFitsSystemWindows(this,false);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
        //所以如果你是这种情况,请使用以下代码, 设置状态使用深色文字图标风格, 否则你可以选择性注释掉这个if内容
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
            //这样半透明+白=灰, 状态栏的文字能看得清
            StatusBarUtil.setStatusBarColor(this,0x55000000);
        }
    }

    public void ZTL2(){
        StatusBarUtil.setTranslucentStatus(this);
        //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
        //所以如果你是这种情况,请使用以下代码, 设置状态使用深色文字图标风格, 否则你可以选择性注释掉这个if内容
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
            //这样半透明+白=灰, 状态栏的文字能看得清
            StatusBarUtil.setStatusBarColor(this,0x55000000);
        }
    }

    public void setPadding(Activity activity,View view){
        int height = StatusBarUtil.getStatusBarHeight(activity);
        view.setPadding(0, height, 0, 0);
    }

    public void biaoTi(FragmentActivityBar fragmentActivityBar, Activity activity,String path,int size){
        TextView textView = (TextView) fragmentActivityBar.getView().findViewById(R.id.activitybar_title);
        TextView textView1 = (TextView) fragmentActivityBar.getView().findViewById(R.id.activitybar_title1);
        TextView textView2 = (TextView) fragmentActivityBar.getView().findViewById(R.id.activitybar_title2);
        Typeface SIF = Typeface.createFromAsset(activity.getAssets(), path);
        textView.setTypeface(SIF);
        textView.setTextSize(size);
        textView1.setTypeface(SIF);
        textView1.setTextSize(size);
        textView2.setTypeface(SIF);
        textView2.setTextSize(size);
    }

    private static UserDynamicThumb userDynamicThumb;
    public UserDynamicThumb sendUserThumb(int i){
        if (i == 1 || userDynamicThumb == null){
            userDynamicThumb = new UserDynamicThumb();
            return userDynamicThumb;
        }else {
            return userDynamicThumb;
        }
    }

    private UserDynamicComment userDynamicComment;
    public UserDynamicComment sendUserComment(){
        if (userDynamicComment == null){
            userDynamicComment = new UserDynamicComment();
            return userDynamicComment;
        }else {
            return userDynamicComment;
        }
    }

    private static UserFollow userFollow;
    public UserFollow sendUserFollow(int i,String xuehao){
        if (i == 1 || userFollow == null){
            userFollow = new UserFollow(xuehao);
            return userFollow;
        }else {
            return userFollow;
        }
    }

    private static UserFollowList userFollowList;
    public UserFollowList sendUserFollowList(int i,String xuehao){
        if (i == 1 || userFollowList == null){
            userFollowList = new UserFollowList(xuehao);
            return userFollowList;
        }else {
            return userFollowList;
        }
    }

    private static UserFollowToMe userFollowToMe;
    public UserFollowToMe sendUserFollowToMe(int i,String xuehao){
        if (i == 1 || userFollowToMe == null){
            userFollowToMe = new UserFollowToMe(xuehao);
            return userFollowToMe;
        }else {
            return userFollowToMe;
        }
    }

    private static IbFollow ibFollow;
    public IbFollow sendIbFollow(int i,String xuehao){
        if (i == 1 || ibFollow == null){
            ibFollow = new IbFollow(xuehao);
            return ibFollow;
        }else {
            return ibFollow;
        }
    }

    private static IbClass ibClass;
    public IbClass sendIb(int i){
        if (i == 1 || ibClass == null){
            ibClass = new IbClass(i);
            return ibClass;
        }else {
            return ibClass;
        }
    }

    public void noXiaYiBu(final EditText mDengluXueHao, final Button mXiaYiBu, final int MAXLENGTH) {

        mDengluXueHao.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @SuppressLint("ResourceAsColor")
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void afterTextChanged(Editable editable) {
                if (MAXLENGTH==0){
                    if (mDengluXueHao.length() != MAXLENGTH) {
                        mXiaYiBu.setAlpha(MAXALPHA);
                        mXiaYiBu.setClickable(true);
                        mXiaYiBu.setBackgroundResource(R.drawable.denglubackground2);
                    } else {
                        mXiaYiBu.setAlpha(MINALPHA);
                        mXiaYiBu.setClickable(false);
                        mXiaYiBu.setBackgroundResource(R.drawable.denglubackground);
                    }
                }else {
                    if (mDengluXueHao.length() == MAXLENGTH) {
                        mXiaYiBu.setAlpha(MAXALPHA);
                        mXiaYiBu.setClickable(true);
                        mXiaYiBu.setBackgroundResource(R.drawable.denglubackground2);
                    } else {
                        mXiaYiBu.setAlpha(MINALPHA);
                        mXiaYiBu.setClickable(false);
                        mXiaYiBu.setBackgroundResource(R.drawable.denglubackground);
                    }
                }

            }
        });
    }


    public List<GuangChangUserXinXi> guangChangUserXinXi;

    public void shuaXinGuangChang(String path, final Handler handlerShuaXin, String nowDate, final int i) {
        HttpUtil.addGuangChangXin(path,nowDate, new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String a = response.body().string();
                sendGuangChang2(a,handlerShuaXin,i);
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                if (e instanceof SocketTimeoutException){
                    Message message = new Message();
                    message.what = 0;
                    if (handlerShuaXin!=null){
                        message.what = 2;
                        handlerShuaXin.sendMessage(message);
                    }else {
                        sendMessageS(0);
                    }
                }
                if (e instanceof ConnectException){
                    Message message = new Message();
                    message.what = 1;
                    if (handlerShuaXin!=null){
                        message.what = 3;
                        handlerShuaXin.sendMessage(message);
                    }else {
                        sendMessageS(1);
                    }
                }
            }
        });
    }

    public boolean sendGuangChang2(String gCXinXi,Handler handler1,int i){
        Gson gson = new Gson();
        try{
            guangChangUserXinXi = gson.fromJson(gCXinXi,new TypeToken<List<GuangChangUserXinXi>>()
            {}.getType());

            if (i==1){
                LitePal.deleteAll(GuangChangUserXinXi.class);

                for (GuangChangUserXinXi guangChangUserXinXi1:guangChangUserXinXi){
                    boolean a = guangChangUserXinXi1.save();
                }
            }

            if (handler1!=null){
                if (i==1){
                    sendMessages(1,handler1);
                }
                if (i==2){
                    sendMessages(3,handler1);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            if (handler1!=null){
                sendMessages(2,handler1);
            }
        }
        return true;
    }

    private void sendMessages(int i,Handler handler){
        Message message = new Message();
        switch (i){
            case 1:
                message.what = 0;
                handler.sendMessage(message);
                break;
            case 2:
                message.what = 1;
                handler.sendMessage(message);
                break;
            case 3:
                message.what = 4;
                handler.sendMessage(message);
        }
    }

    private Handler userSpaceHandler;
    public List<UserSpace> userSpacesB;
    private int number;
    public void sendUserSpace(int number1, String path, String nowTime, String xuehao, final Handler handler){
        this.number = number1;
        HttpUtil.addUserSpace(path,nowTime,xuehao, new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String a = response.body().string();
                if (number == 1){
                    refreshUserSpace(a,handler);
                }
                if (number == 2||number == 3){
                    addUserSpace(a,handler);
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                if (e instanceof SocketTimeoutException){
                    sendMessageS(0);
                }
                if (e instanceof ConnectException){
                    sendMessageS(1);
                }
            }
        });
    }

    private void addUserSpace(String a, Handler handler1) {
        try{
            Gson gson = new Gson();
            userSpacesB = gson.fromJson(a,new TypeToken<List<UserSpace>>()
            {}.getType());

            if (userSpacesB.size() != 0){
                Message message = new Message();
                switch (number){
                    case 2:
                        message.what = 0;
                        message.obj = "0";
                        handler1.sendMessage(message);
                        break;
                    case 3:
                        message.what = 0;
                        message.obj = "1";
                        handler1.sendMessage(message);
                        break;
                }
            }

        }catch (Exception e){
            sendMessageS(3);
        }
    }

    private void refreshUserSpace(String a, Handler handler) {
        try{
            Gson gson = new Gson();
            userSpacesB = gson.fromJson(a,new TypeToken<List<UserSpace>>()
            {}.getType());

            LitePal.deleteAll(UserSpace.class);
            if (userSpacesB.equals("1")&&handler!=null){
                Message message = new Message();
                message.what = 1;
                message.obj = 1;
                handler.sendMessage(message);
            }
            for (UserSpace userSpace:userSpacesB){
                boolean b = userSpace.save();
                Log.d("bbb", String.valueOf(b));
            }
        }catch (Exception e){
            LitePal.deleteAll(UserSpace.class);
            sendMessageS(3);
        }

    }

    public List<MyThumb>  myThumbs;
    public void huoquThumb(String path,String xuehao){
        HttpUtil.Thumb(path,xuehao,new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String a = response.body().string();
                try{
                    Gson gson = new Gson();
                    myThumbs = gson.fromJson(a,new TypeToken<List<MyThumb>>()
                    {}.getType());

                    LitePal.deleteAll(MyThumb.class);
                    if (myThumbs.size()!=0){
                        for (MyThumb m:myThumbs){
                            boolean b = m.save();
                        }
                    }
                }catch (Exception e){

                }
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }
        });
    }

    private static String myXueHao;

    public String getMyXueHao() {
        return myXueHao;
    }

    public void setMyXueHao(String myXueHao) {
        this.myXueHao = myXueHao;
    }

    private static String myUserName;

    public String getMyUserName(){
        return myUserName;
    }

    public void setMyUserName(String n){
        this.myUserName = n;
    }

    private static String mySex;

    public String getMySex(){
        return mySex;
    }

    public void setMySex(String s){
        this.mySex = s;
    }

    private static String myZhuanYe;

    public String getMyZhuanYe(){
        return myZhuanYe;
    }

    public void setMyZhuanYe(String s){
        this.myZhuanYe = s;
    }

    private static String myNianJi;
    private static String myBanJi;

    public static String getMyNianJi() {
        return myNianJi;
    }

    public static void setMyNianJi(String myNianJi) {
        BaseActivity.myNianJi = myNianJi;
    }

    public static String getMyBanJi() {
        return myBanJi;
    }

    public static void setMyBanJi(String myBanJi) {
        BaseActivity.myBanJi = myBanJi;
    }

    private static String myToken;

    public static String getMyToken() {
        return myToken;
    }

    public static void setMyToken(String myToken) {
        BaseActivity.myToken = myToken;
    }

    public static String name;
    public static String xingbie;
    public static String nianji;
    public static String xibu;
    public static String zhuanye;
    public static String banji;
    public static String xuehao;
    public static String user_name;
    public static String user_birth;
    public static String user_geqian;
    public static String user_head_portrait;
    public static String user_background;
    public static String user_privacy;
    public static String user_ip;
    public static String user_token;

    public boolean geRenXinXiShare() {
        SharedPreferences sharedPreferences = MyApplication.getContext().getSharedPreferences("userSchool", Context.MODE_PRIVATE);
        name = sharedPreferences.getString("name","");
        xingbie = sharedPreferences.getString("xingbie","");
        nianji = sharedPreferences.getString("nianji","");
        xibu = sharedPreferences.getString("xibu","");
        zhuanye = sharedPreferences.getString("zhuanye","");
        banji = sharedPreferences.getString("banji","");
        xuehao = sharedPreferences.getString("xuehao","");
        setMyXueHao(xuehao);
        setMySex(xingbie);
        setMyZhuanYe(zhuanye);
        setMyNianJi(nianji);
        setMyBanJi(banji);
        SharedPreferences sharedPreferences2 = MyApplication.getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        user_name = sharedPreferences2.getString("user_name","");
        user_birth = sharedPreferences2.getString("user_birth","");
        user_geqian = sharedPreferences2.getString("user_geqian","");
        user_head_portrait = sharedPreferences2.getString("user_head_portrait","");
        user_background = sharedPreferences2.getString("user_background","");
        user_privacy = sharedPreferences2.getString("user_privacy","");
        user_ip = sharedPreferences2.getString("user_ip","");
        user_token = sharedPreferences2.getString("user_token","");
        setMyUserName(user_name);
        setMyToken(user_token);
        if(sharedPreferences==null||sharedPreferences2==null){
            return false;
        }
        if (xuehao!=getMyXueHao()&&getMyXueHao()!=null&&name.equals(userSchool1.name)&&user_name.equals(user1.user_name)&&banji.equals(userSchool1.banji)&&xibu.equals(userSchool1.xibu)){
            return false;
        }
        return true;
    }

    private UserSchool userSchool1;
    private User user1;
    public void huoQuGeRenXinXi(final Activity activity, String path1, String path2, String zhanghao, final Handler handler) {
        HttpUtil.sendOkHttpPost(path1,zhanghao,new okhttp3.Callback(){
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String userSchool = response.body().string();
                try {
                    userSchool1 = new Gson().fromJson(userSchool,UserSchool.class);
                    SharedPreferences sharedPreferences = MyApplication.getContext().getSharedPreferences("userSchool", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("id",userSchool1.id);
                    editor.putString("name",userSchool1.name);
                    editor.putString("xingbie",userSchool1.xingbie);
                    editor.putString("nianji",userSchool1.nianji);
                    editor.putString("xibu",userSchool1.xibu);
                    editor.putString("zhuanye",userSchool1.zhuanye);
                    editor.putString("banji",userSchool1.banji);
                    editor.putString("xuehao",userSchool1.xuehao);
                    editor.commit();
                }catch (Exception e){
                    sendError();
                }
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                if (e instanceof SocketTimeoutException){
                    sendMessageS(0);
                }
                if (e instanceof ConnectException){
                    sendMessageS(1);
                }
            }
        });

        HttpUtil.sendOkHttpPost(path2,zhanghao,new okhttp3.Callback(){
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String user = response.body().string();
                try{
                    user1 = new Gson().fromJson(user,User.class);
                    SharedPreferences sharedPreferences = MyApplication.getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("id",user1.id);
                    editor.putString("user_name",user1.user_name);
                    editor.putString("user_birth",user1.user_birth);
                    editor.putString("user_geqian",user1.user_geqian);
                    editor.putString("user_xuehao",user1.user_xuehao);
                    editor.putString("user_head_portrait", String.valueOf(user1.user_head_portrait));
                    editor.putString("user_background", String.valueOf(user1.user_background));
                    editor.putString("user_privacy",user1.user_privacy);
                    editor.putString("user_ip",user1.user_ip);
                    editor.putString("user_token",user1.user_token);
                    editor.commit();
                    setMyToken(user1.user_token);
                    setMyUserName(user1.user_name);
                }catch (Exception e){
                    Message message = new Message();
                    message.what = 1;
                    message.obj = "6";
                    handler.sendMessage(message);
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                if (e instanceof SocketTimeoutException){
                    sendMessageS(0);
                }
                if (e instanceof ConnectException){
                    sendMessageS(1);
                }
            }
        });

    }

    private void sendMessageS(int i){
        Message message = new Message();
        message.what = i;
        handlerZong.sendMessage(message);
    }

    private void sendError(){
        Message message = new Message();
        error.sendMessage(message);
    }


    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private Window window;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void showDiaLog(Activity activity, int drawable){
        builder = new AlertDialog.Builder(activity,R.style.AlertDialog);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_one,null);
        GifImageView gifImageView = (GifImageView)view.findViewById(R.id.gif_image_view);
        gifImageView.setImageResource(drawable);
        builder.setView(view);
        alertDialog = builder.create();
        window = alertDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        window.setLayout(300,300);
    }

    public void closeDiaLog(){
        alertDialog.dismiss();
    }

    public void cancelable(boolean c){
        alertDialog.setCancelable(c);
    }

}

class ShowActivityBars extends AppCompatActivity{
    private FragmentActivityBar fragmentActivityBar;
    private Activity activity;
    public ShowActivityBars(Activity activity1,FragmentActivityBar fragmentActivityBar1) {
        this.fragmentActivityBar = fragmentActivityBar1;
        this.activity = activity1;
    }

    public void showKongJian(boolean left,boolean title,boolean title2,boolean title1,boolean textright,boolean right,boolean right1){
        fragmentActivityBar.showZuJian(activity,left,title,title2,title1,textright,right,right1);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void showUI(int leftUI, String titleUI,String title2UI, String title1UI, int rightUI, int right1UI){
        fragmentActivityBar.showZuJianUI(leftUI,titleUI,title2UI,title1UI,rightUI,right1UI);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void showUI(int leftUI, int titleUI, int title2UI,int title1UI, int rightUI, int right1UI){
        fragmentActivityBar.showZuJianUI(leftUI,titleUI,title2UI,title1UI,rightUI,right1UI);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void showUI(int leftUI, String titleUI, String title2UI,String title1UI, String textright,int rightUI, int right1UI){
        fragmentActivityBar.showZuJianUI(leftUI,titleUI,title2UI,title1UI,textright,rightUI,right1UI);
    }

    public void uiFunction(int leftFunction, int titleFunction, int title2Function,int title1Function,int textright, int rightFunction, int right1Function){
        fragmentActivityBar.showUiFunction(leftFunction,titleFunction,title2Function,title1Function,textright,rightFunction,right1Function);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void barBackground1(int color){
        fragmentActivityBar.barBackground(color);
    }

    public void fontColor1(int color){
        fragmentActivityBar.fontColor(color);
    }

    public void sendMessageNotice(boolean b){
        fragmentActivityBar.messageNotice(b);
    }
}
