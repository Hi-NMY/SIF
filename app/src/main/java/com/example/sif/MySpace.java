package com.example.sif;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.signature.MediaStoreSignature;
import com.example.sif.Lei.LianJie.HttpUtil;
import com.example.sif.Lei.MyBroadcastReceiver.BroadcastRec;
import com.example.sif.Lei.MyToolClass.*;
import com.example.sif.Lei.NiceImageView.CircleImageView;
import com.example.sif.Lei.ShiPeiQi.MySpacePack;
import com.example.sif.NeiBuLei.UserSpace;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tamsiree.rxui.view.dialog.RxDialog;
import com.tamsiree.rxui.view.dialog.RxDialogScaleView;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;
import okhttp3.Call;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.litepal.LitePal;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class MySpace extends BaseActivity implements View.OnClickListener {

    private ImageView mMySpaceImage;
    private ImageView mMySpaceToolbarFanHui;
    private TextView mMySpaceToolbarTitle;
    private Toolbar mMySpaceToolbar;
    private CollapsingToolbarLayout mMySpaceCollLayout;
    private AppBarLayout mAppBarLayout;
    private RecyclerView mMySpaceListMessage;
    private RelativeLayout mMySoaceMessageR;
    private NestedScrollView mMyspaceNestedscrollview;
    private CircleImageView mMySpaceUserImage;
    private CoordinatorLayout mMySpaceCoor;
    private TextView mMySpaceUserName;
    private ImageView mMySpaceUserSEX;
    private TextView mMySpaceUserZhuanYe;
    private TextView mMySpaceUserZhaoHu;
    private RelativeLayout mR;
    private LinearLayoutManager layoutManager;
    private SmartRefreshLayout smartRefreshLayout;
    private ImageView imageFollow;
    private TextView textUserYuan;
    private TextView textUserBan;
    private RelativeLayout privacyYuan;
    private RelativeLayout privacyZhuan;
    private RelativeLayout privacyNian;
    private RelativeLayout privater;
    private Button privateUserButton;
    private TextView mlongDayText;

    private String uName;
    private String uXingBie;
    private String uGeQian;
    private String uZhuanYe;
    private String uXueHao;
    private String uprivacy;
    private String uYuan;
    private String uNianJi;
    private int id;
    private List<UserSpace> userSpaces;
    private RelativeLayout mMypaceNull;
    private UserFollow userFollow;

    private Handler messagehandler = new Handler(){
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    ToastZong.ShowToast(MySpace.this,"读取超时");
                    break;
                case 2:
                    SharedPreferences sharedPreferences = getSharedPreferences("userSpaceZ",MODE_PRIVATE);
                    uName = sharedPreferences.getString("uName","");
                    uXingBie = sharedPreferences.getString("uXingBie","男");
                    uGeQian = sharedPreferences.getString("uGeQian","这个人很懒，什么也没写");
                    uZhuanYe = sharedPreferences.getString("uZhuanYe","未知");
                    uNianJi = sharedPreferences.getString("uNianJi","****");
                    uYuan = sharedPreferences.getString("uXiBu","");
                    uprivacy = sharedPreferences.getString("uprivacy","1111");
                    if (!uXueHao.equals(getMyXueHao())){
                        userPrivacy(uprivacy);
                    }
                    if(uZhuanYe.equals("未知")){
                        ToastZong.ShowToast(MySpace.this,"信息错误请重试");
                    }
                    boolean a = tianJiaUserSpace();
                    if (userSpaces != null && userSpaces.size() > 0){
                        if (uXueHao.equals(getMyXueHao())){
                            if (a){
                                mySpacePack.moreFunction(1,getMyXueHao(),uName);
                            }
                        }else {
                            if (a){
                                mySpacePack.moreFunction(2,uXueHao,uName);
                            }
                        }
                    }

                    xieru();
                    break;
                case 3:
                    ToastZong.ShowToast(MySpace.this,"请检查网络或重试");
                    break;
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_space);
        initView();

        ZTL();

        //增加padding  避免状态栏遮挡
        setPadding(this, mMySpaceCoor);

        //移动监听
        moveListener(mAppBarLayout);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        uXueHao = intent.getStringExtra("uXueHao");

        if (id == 1) {
            uName = intent.getStringExtra("uName");
            uXingBie = intent.getStringExtra("uXingBie");
            uGeQian = intent.getStringExtra("uGeQian");
            uZhuanYe = intent.getStringExtra("uZhuanYe");
            uYuan = intent.getStringExtra("xibu");
            uNianJi = intent.getStringExtra("nianji");
            xieru();
            boolean a = tianJiaMySpace();
            if (a){
                mySpacePack.moreFunction(id,getMyXueHao(),uName);
            }
        }
        if (id == 2){
            if (!uXueHao.equals(getMyXueHao())){
                privater.setVisibility(View.VISIBLE);
                imageFollow.setVisibility(View.VISIBLE);
                userFollow = sendUserFollow(2,getMyXueHao());
                if (userFollow.noYesFollow(uXueHao)){
                    imageFollow.setImageResource(R.drawable.alreadyfollow);
                }else {
                    imageFollow.setImageResource(R.drawable.follow);
                }
            }
                String nowTime = MyDateClass.showNowDate();
                String pathSpace = "http://nmy1206.natapp1.cc/userSpace.php";
                HttpUtil.addUserSpace(pathSpace,nowTime,uXueHao, new okhttp3.Callback() {
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String a = response.body().string();
                        Gson gson = new Gson();
                        try {
                            userSpaces = gson.fromJson(a,new TypeToken<List<UserSpace>>()
                            {}.getType());
                            sendMessages(2);
                        }catch (Exception e){
                            Message message = new Message();
                            message.what = 2;
                            messagehandler.sendMessage(message);
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        if (e instanceof SocketTimeoutException){
                            sendMessages(1);
                        }
                        if (e instanceof ConnectException){
                            sendMessages(1);
                        }
                    }
                });
        }
        //信息写入
        //xieru();
        updateTime = String.valueOf(System.currentTimeMillis());
        Glide.with(MyApplication.getContext())
                .load("http://nmy1206.natapp1.cc/UserImageServer/"+uXueHao+"/HeadImage/myHeadImage.png")
                .signature(new MediaStoreSignature(updateTime,1,1))
                .placeholder(R.drawable.nostartimage_three)
                .fallback(R.drawable.defaultheadimage)
                .error(R.drawable.defaultheadimage)
                .priority(Priority.HIGH)
                .transition(DrawableTransitionOptions.withCrossFade())
                .circleCrop()
                .into(mMySpaceUserImage);
        Glide.with(MyApplication.getContext())
                .load("http://nmy1206.natapp1.cc/UserImageServer/"+uXueHao+"/BackgroundImage/myBackgroundImage.png")
                .signature(new MediaStoreSignature(updateTime,1,1))
                .placeholder(R.drawable.nostartimage)
                .fallback(R.drawable.celan_geren)
                .error(R.drawable.celan_geren)
                .priority(Priority.HIGH)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(mMySpaceImage);

        //刷新smart
        smartRefreshLayout.setEnableNestedScroll(true);
        startrefresh(smartRefreshLayout);

    }
    private void sendMessages(int i){
        if (i == 1){
            Message message = new Message();
            message.what = 1;
            messagehandler.sendMessage(message);
        }
        if (i == 2){
            Message message = new Message();
            message.what = 2;
            messagehandler.sendMessage(message);
        }
    }

    private Handler spacehandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    if (msg.obj == "0"){
                        mySpacePack.addGuangChangListbottom(userSpacesB);
                        smartRefreshLayout.finishLoadMore();
                    }
                    if (msg.obj == "1"){
                        mySpacePack = new MySpacePack(MySpace.this,userSpacesB);
                        mySpacePack.myuserDynamicThumb(sendUserThumb(0));
                        if (uXueHao.equals(getMyXueHao())){
                            mySpacePack.moreFunction(1,getMyXueHao(),uName);
                        }else {
                            mySpacePack.moreFunction(2,uXueHao,uName);
                        }
                        mySpacePack.notifyDataSetChanged();
                        mMySpaceListMessage.setAdapter(mySpacePack);
                        smartRefreshLayout.finishRefresh();
                    }
                    break;
            }
        }
    };

    private String path = "http://nmy1206.natapp1.cc/userSpace.php";
    private void startrefresh(SmartRefreshLayout smartRefreshLayout) {
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                final String nowTime = MyDateClass.showNowDate();
                sendUserSpace(3,path,nowTime,uXueHao,spacehandler);
            }
        });

        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                sendUserSpace(2,path,mySpacePack.date,uXueHao,spacehandler);
            }
        });
    }

    private boolean Y = true;
    private boolean Z = true;
    private boolean N = true;
    private void userPrivacy(String privacy){
        Y = (privacy.substring(0,1).equals("0"))? true : false;
        Z = (privacy.substring(1,2).equals("0"))? true : false;
        N = (privacy.substring(2,3).equals("0"))? true : false;
    }

    private Handler newLongDayHanlder = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mlongDayText.setVisibility(View.VISIBLE);
            mlongDayText.setText(longDay+"天");
        }
    };
    private String longDay;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void xieru() {
        mMySpaceUserName.setText(uName);
        mMySpaceToolbarTitle.setText(uName);
        if (uXingBie.equals("男")) {
            mMySpaceUserSEX.setBackground(getDrawable(R.drawable.boy));
        }
        if (uXingBie.equals("女")) {
            mMySpaceUserSEX.setBackground(getDrawable(R.drawable.girl));
        }

        if (Y){
            privacyYuan.setVisibility(View.VISIBLE);
            textUserYuan.setText(uYuan);
        }

        if (Z){
            privacyZhuan.setVisibility(View.VISIBLE);
            mMySpaceUserZhuanYe.setText(uZhuanYe);
        }

        if (N){
            privacyNian.setVisibility(View.VISIBLE);
            textUserBan.setText(uNianJi);
        }

        HttpUtil.obtainUserLongDay("http://nmy1206.natapp1.cc/ObtainUserLongDay.php",uXueHao, new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                longDay = response.body().string();
                newLongDayHanlder.sendMessage(new Message());
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }
        });


        mMySpaceUserZhaoHu.setText(uGeQian);
        mMySpaceUserName.postInvalidate();
        mMySpaceToolbarTitle.postInvalidate();
        mMySpaceUserSEX.postInvalidate();
        mMySpaceUserZhuanYe.postInvalidate();
        mMySpaceUserZhaoHu.postInvalidate();
        textUserYuan.postInvalidate();
        textUserBan.postInvalidate();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initView() {
        mMySpaceImage = (ImageView) findViewById(R.id.my_space_image);
        mMySpaceToolbarFanHui = (ImageView) findViewById(R.id.mySpace_Toolbar_FanHui);
        mMySpaceToolbarTitle = (TextView) findViewById(R.id.mySpace_Toolbar_title);
        mMySpaceToolbar = (Toolbar) findViewById(R.id.mySpace_Toolbar);
        mMySpaceCollLayout = (CollapsingToolbarLayout) findViewById(R.id.mySpace_CollLayout);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        mMySpaceListMessage = (RecyclerView) findViewById(R.id.my_space_list_message);
        mMySoaceMessageR = (RelativeLayout) findViewById(R.id.mySpace_Message_R);
        mMyspaceNestedscrollview = (NestedScrollView) findViewById(R.id.myspace_nestedscrollview);
        mMySpaceUserImage = (CircleImageView) findViewById(R.id.my_space_user_image);
        mMySpaceCoor = (CoordinatorLayout) findViewById(R.id.MySpace_Coor);
        mMySpaceUserName = (TextView) findViewById(R.id.MySpace_UserName);
        mMySpaceUserSEX = (ImageView) findViewById(R.id.MySpace_UserSEX);
        mMySpaceUserZhuanYe = (TextView) findViewById(R.id.MySpace_UserZhuanYe);
        mMySpaceUserZhaoHu = (TextView) findViewById(R.id.MySpace_UserZhaoHu);
        mR = (RelativeLayout) findViewById(R.id.R);
        smartRefreshLayout = (SmartRefreshLayout)findViewById(R.id.smart);
        imageFollow = (ImageView)findViewById(R.id.mySpace_Toolbar_follow);
        textUserBan = (TextView)findViewById(R.id.MySpace_UserBan);
        textUserYuan = (TextView)findViewById(R.id.MySpace_UserYuan);
        privacyNian = (RelativeLayout)findViewById(R.id.privacy_ban);
        privacyYuan = (RelativeLayout)findViewById(R.id.privacy_yuan);
        privacyZhuan = (RelativeLayout)findViewById(R.id.privacy_zhuan);
        privater = (RelativeLayout)findViewById(R.id.private_r);
        privateUserButton = (Button) findViewById(R.id.private_user_button);
        mlongDayText = (TextView)findViewById(R.id.longDay_text);

        privateUserButton.setOnClickListener(this);
        imageFollow.setOnClickListener(this);
        mMySpaceToolbarFanHui.setOnClickListener(this);

        mMypaceNull = (RelativeLayout) findViewById(R.id.Mypace_null);

        mMypaceNull.setOnClickListener(this);
        mMySpaceUserImage.setOnClickListener(this);
        mMySpaceImage.setOnClickListener(this);
    }

    private List<UserSpace> userSpacesss;
    private MySpacePack mySpacePack;

    private boolean tianJiaMySpace() {
        userSpacesss = LitePal.findAll(UserSpace.class);
        if (userSpacesss.size()>=7){
            smartRefreshLayout.setDragRate(1.6f);
            smartRefreshLayout.setEnableLoadMore(true);
        }else {
            smartRefreshLayout.setEnableLoadMore(false);
        }
        if (userSpacesss.size() == 0){
            mMySpaceListMessage.setVisibility(View.INVISIBLE);
            mMypaceNull.setVisibility(View.VISIBLE);
            return false;
        }else {
            mMySpaceListMessage.setVisibility(View.VISIBLE);
            mMypaceNull.setVisibility(View.INVISIBLE);
            layoutManager = new LinearLayoutManager(this);
            mMySpaceListMessage.setLayoutManager(layoutManager);
            if (mySpacePack == null) {
                mySpacePack = new MySpacePack(this,userSpacesss);
                mySpacePack.myuserDynamicThumb(sendUserThumb(0));
            }
            mySpacePack.notifyDataSetChanged();
            mMySpaceListMessage.setAdapter(mySpacePack);
            return true;
        }
    }
    private boolean tianJiaUserSpace() {
        if (userSpaces != null && userSpaces.size()>=7){
            smartRefreshLayout.setDragRate(1.6f);
            smartRefreshLayout.setEnableLoadMore(true);
        }else {
            smartRefreshLayout.setEnableLoadMore(false);
        }
        if (userSpaces == null || userSpaces.size() == 0){
            mMySpaceListMessage.setVisibility(View.INVISIBLE);
            mMypaceNull.setVisibility(View.VISIBLE);
            return false;
        }else {
            mMySpaceListMessage.setVisibility(View.VISIBLE);
            mMypaceNull.setVisibility(View.INVISIBLE);
            layoutManager = new LinearLayoutManager(this);
            mMySpaceListMessage.setLayoutManager(layoutManager);
            mySpacePack = new MySpacePack(this,userSpaces);
            mySpacePack.myuserDynamicThumb(sendUserThumb(0));
            mySpacePack.notifyDataSetChanged();
            mMySpaceListMessage.setAdapter(mySpacePack);
            return true;
        }
    }

    private ShowDiaLog showDiaLog;
    private Handler followHander = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    if (msg.obj.equals("10")){
                        imageFollow.setImageResource(R.drawable.alreadyfollow);
                        SendGeTuiMessage.sendGeTuiMessage(1,uXueHao,getMyXueHao(),"1","-1",2);
                        BroadcastRec.sendReceiver(MyApplication.getContext(),"noyesFollowUser",0,null);
                    }
                    if (msg.obj.equals("11")){
                        ToastZong.ShowToast(MySpace.this,"取消关注");
                        imageFollow.setImageResource(R.drawable.follow);
                        BroadcastRec.sendReceiver(MyApplication.getContext(),"noyesFollowUser",0,null);
                    }
                    break;
            }
        }
    };


    private RxDialogScaleView rxDialogScaleView;
    private RxDialog rxDialog;
    private Handler bitMapHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            rxDialog.dismiss();
            if (msg.obj != null){
                if (msg.what == 1){
                    ToastZong.ShowToast(MyApplication.getContext(),"图片加载错误");
                    rxDialogScaleView.setImage((Bitmap) msg.obj);
                }else {
                    rxDialogScaleView.setImage((Bitmap) msg.obj);
                }
            }else {
                ToastZong.ShowToast(MyApplication.getContext(),"错误");
            }
        }
    };

    private UserInfo userInfo;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mySpace_Toolbar_FanHui:
                finish();
                break;
            case R.id.mySpace_Toolbar_follow:
                userFollow.sendmyFollow(uXueHao,followHander);
                break;
            case R.id.my_space_user_image:
                if (id == 1){
                    View view = LayoutInflater.from(this).inflate(R.layout.dialog_three,null);
                    showDiaLog = new ShowDiaLog(this,R.style.AlertDialog_Function,view);
                    showDiaLog.logWindow(new ColorDrawable(Color.TRANSPARENT));
                    showDiaLog.showMyDiaLog();
                    TextView title = (TextView)view.findViewById(R.id.dialog_text);
                    Button buttonOne = (Button)view.findViewById(R.id.button_one);
                    Button buttonTwo = (Button)view.findViewById(R.id.button_two);
                    Button buttonThree = (Button)view.findViewById(R.id.button_three);
                    title.setText("修改头像");
                    buttonOne.setText("从相册选取");
                    buttonTwo.setText("拍一张吧");
                    imageName = "myHeadImage.png";
                    function = 1;
                    startButton(buttonOne,buttonTwo,buttonThree);
                }else {
                    rxDialogScaleView = new RxDialogScaleView(this);
                    rxDialog = new RxDialog(this,R.style.tran_dialog);
                    rxDialog.setCanceledOnTouchOutside(false);
                    String NewName = "http://nmy1206.natapp1.cc/UserImageServer/"+uXueHao+"/HeadImage/myHeadImage.png";
                    MyVeryDiaLog.veryImageDiaLog(rxDialogScaleView,NewName,null,bitMapHandler);
                    MyVeryDiaLog.transparentDiaLog(this,rxDialog);
                }
                break;
            case R.id.my_space_image:
                if (id == 1){
                    View view = LayoutInflater.from(this).inflate(R.layout.dialog_three,null);
                    showDiaLog = new ShowDiaLog(this,R.style.AlertDialog_Function,view);
                    showDiaLog.logWindow(new ColorDrawable(Color.TRANSPARENT));
                    showDiaLog.showMyDiaLog();
                    TextView title = (TextView)view.findViewById(R.id.dialog_text);
                    Button buttonOne = (Button)view.findViewById(R.id.button_one);
                    Button buttonTwo = (Button)view.findViewById(R.id.button_two);
                    Button buttonThree = (Button)view.findViewById(R.id.button_three);
                    title.setText("修改背景");
                    buttonOne.setText("从相册选取");
                    buttonTwo.setText("拍一张吧");
                    imageName = "myBackgroundImage.png";
                    function = 2;
                    startButton(buttonOne,buttonTwo,buttonThree);
                }
                break;
            case R.id.Mypace_null:
                final String nowTime = MyDateClass.showNowDate();
                sendUserSpace(3,path,nowTime,uXueHao,spacehandler);
                break;
            case R.id.private_user_button:
                if (uXueHao.equals("") || uName.equals("")){
                    ToastZong.ShowToast(this,"信息错误，请重新拉取");
                }else {
//                    RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
//                        @Override
//                        public UserInfo getUserInfo(String s) {
//                            userInfo = new UserInfo(uXueHao, uName, Uri.parse("http://nmy1206.natapp1.cc/UserImageServer/"+uXueHao+"/HeadImage/myHeadImage.png"));
//                            return userInfo;
//                        }
//                    },false);
//                    RongIM.getInstance().refreshUserInfoCache(userInfo);
                    Conversation.ConversationType conversationType  = Conversation.ConversationType.PRIVATE;
                    RongIM.getInstance().startConversation(this , conversationType, uXueHao, uName);
                }
                break;
        }
    }

    private int function;
    private String imageName;
    private SelectImage selectImage;
    private void startButton(Button buttonOne, Button buttonTwo, Button buttonThree) {
        selectImage = new SelectImage(MySpace.this);
        buttonOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage.showSelectImage(1,imageName);
            }
        });

        buttonTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MySpace.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MySpace.this,new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else {
                    aaaa();
                }
                if (ContextCompat.checkSelfPermission(MySpace.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MySpace.this,new String[]{ Manifest.permission.CAMERA},2);
                }else {
                    aaaa();
                }

            }
        });

        buttonThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDiaLog.closeMyDiaLog();
            }
        });
    }

    private void aaaa(){
        selectImage.showSelectImage(2,imageName);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if (grantResults.length>0&&grantResults[0] == PackageManager.PERMISSION_GRANTED){

                }else {
                    ToastZong.ShowToast(MySpace.this,"SD卡权限获取失败");
                }
                break;
            case 2:
                if (grantResults.length>0&&grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    aaaa();
                }else {
                    ToastZong.ShowToast(MySpace.this,"相机权限获取失败");
                }
                break;
        }
    }

    private List<LocalMedia> localMedia = new ArrayList<>();
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case PictureConfig.CHOOSE_REQUEST:
                    sendUrlImage(data,1);
                    break;
                case PictureConfig.REQUEST_CAMERA:
                    sendUrlImage(data,2);
                    break;
            }
        }
    }

    private String updateTime = null;
    private Handler userImageHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    if (msg.obj.equals("0")){
                        closeDiaLog();
                        userImageHeadDate = String.valueOf(System.currentTimeMillis());
                        SharedPreferences sharedPreferences = MyApplication.getContext().getSharedPreferences("userImageHeadDate",MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("a",userImageHeadDate);
                        editor.commit();
                        Glide.with(MyApplication.getContext())
                                .load("http://nmy1206.natapp1.cc/UserImageServer/"+getMyXueHao()+"/HeadImage/myHeadImage.png")
                                .signature(new MediaStoreSignature(userImageHeadDate,1,1))
                                .placeholder(R.drawable.nostartimage_three)
                                .fallback(R.drawable.defaultheadimage)
                                .error(R.drawable.defaultheadimage)
                                .circleCrop()
                                .into(mMySpaceUserImage);
                        Glide.with(MyApplication.getContext())
                                .load("http://nmy1206.natapp1.cc/UserImageServer/"+getMyXueHao()+"/BackgroundImage/myBackgroundImage.png")
                                .signature(new MediaStoreSignature(userImageHeadDate,1,1))
                                .placeholder(R.drawable.nostartimage)
                                .fallback(R.drawable.celan_bg)
                                .error(R.drawable.celan_bg)
                                .into(mMySpaceImage);
                        ToastZong.ShowToast(MySpace.this,"保存成功");
                    }
                    if (msg.obj.equals("1")){
                        closeDiaLog();
                        ToastZong.ShowToast(MySpace.this,"保存错误请重试");
                    }
                    break;
            }
        }
    };

    private String pathimage = "http://nmy1206.natapp1.cc/KeepImage.php";
    private void sendUrlImage(Intent data,int i){
        localMedia = PictureSelector.obtainMultipleResult(data);
        for (LocalMedia m:localMedia){
            if (m.isCut()){
                if (m.isCompressed()) {
                    double a = m.getSize();
                    switch (i){
                        case 1:
                            if (a / 1024 <= 100) {
                                if (a / 1024 >=50){
                                    urlImage(m.getCompressPath());
                                }else {
                                    urlImage(m.getCutPath());
                                }
                            } else {
                                smallImage(m.getCompressPath());
                            }
                            break;
                        case 2:
                            smallImage(m.getCompressPath());
                            break;
                    }
                }
            }
            break;
        }
    }

    private void smallImage(String m) {
        Luban.with(MySpace.this)
                .load(m)
                .ignoreBy(0)
                .setTargetDir(null)
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(File file) {
                        String path = String.valueOf(file);
                        urlImage(path);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                }).launch();
    }

    private void urlImage(String urlPath){
        showDiaLog.closeMyDiaLog();
        showDiaLog(this,R.drawable.loading2);
        if (urlPath!=null){
            HttpUtil.sendMyHeadImage(pathimage,function,getMyXueHao(),urlPath, new okhttp3.Callback() {
                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String a = response.body().string();
                    Message message = new Message();
                    message.what = 1;
                    message.obj = a;
                    userImageHandler.sendMessage(message);
                }

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    if (e instanceof SocketTimeoutException){
                        Message message = new Message();
                        message.what = 1;
                        message.obj = "1";
                        userImageHandler.sendMessage(message);
                    }
                    if (e instanceof ConnectException){
                        Message message = new Message();
                        message.what = 1;
                        message.obj = "1";
                        userImageHandler.sendMessage(message);
                    }
                }
            });
        }
    }

    int A = 1;
    private void moveListener(final AppBarLayout mAppBarLayout) {
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                Rect rectangle = new Rect();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(rectangle);
                int a = mMySpaceToolbar.getHeight();
                int[] position = new int[2];
                mMySpaceListMessage.getLocationOnScreen(position);
                if (mMySpaceUserImage.getTop() <= rectangle.top + a && A != 1) {
                    mMySpaceUserImage.setVisibility(View.INVISIBLE);
                    mMySpaceUserName.setVisibility(View.INVISIBLE);
                    mMySpaceUserSEX.setVisibility(View.INVISIBLE);
                    if (i <= mMyspaceNestedscrollview.getHeight() && A != 1) {
                        mMySpaceToolbarTitle.setVisibility(View.VISIBLE);
                    } else {

                    }
                } else {
                    mMySpaceToolbarTitle.setVisibility(View.INVISIBLE);
                    mMySpaceUserImage.setVisibility(View.VISIBLE);
                    mMySpaceUserName.setVisibility(View.VISIBLE);
                    mMySpaceUserSEX.setVisibility(View.VISIBLE);
                    A = 2;
                }
            }
        });
    }
}
