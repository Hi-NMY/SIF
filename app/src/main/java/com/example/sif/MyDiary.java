package com.example.sif;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.MediaStoreSignature;
import com.example.sif.Lei.LianJie.HttpUtil;
import com.example.sif.Lei.MyToolClass.*;
import com.example.sif.Lei.ShiPeiQi.UserDiaryAdapter;
import com.example.sif.Lei.ShowActivityBar.FragmentActivityBar;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.melnykov.fab.FloatingActionButton;
import com.tamsiree.rxui.view.dialog.RxDialog;
import com.tamsiree.rxui.view.dialog.RxDialogScaleView;
import okhttp3.Call;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyDiary extends BaseActivity implements View.OnClickListener {

    private LinearLayoutManager linearLayoutManager;
    private RecyclerView mDiaryList;
    private FloatingActionButton mFab;
    private UserDiaryAdapter userDiaryAdapter;
    private int[] weatherNum = new int[]{R.drawable.weather_sun, R.drawable.weather_night, R.drawable.weather_cloudy
            , R.drawable.weather_overcast, R.drawable.weather_rain, R.drawable.weather_snow};

    private Handler selectHanlder = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            writeMoreDiary();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_diary);
        initView();


        //设置状态栏
        ZTL();
        //ActivityBar
        showActivityBar();

        //增加padding  避免状态栏遮挡
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.udiary_r);
        setPadding(this, relativeLayout);

        linearLayoutManager = new LinearLayoutManager(this);
        mDiaryList.setLayoutManager(linearLayoutManager);

        readNewDiary();

    }

    private void readNewDiary(){
        WholeDeleteDiary.wholeDiary(0,getMyXueHao(),"",selectHanlder);
    }

    private void writeMoreDiary(){
        userDiaryAdapter = new UserDiaryAdapter(this, WholeDeleteDiary.userDiaryClasses);
        userDiaryAdapter.sendMyXuehao(getMyXueHao());
        mDiaryList.setAdapter(userDiaryAdapter);
        mFab.attachToRecyclerView(mDiaryList);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void showActivityBar() {
        FragmentActivityBar fragmentActivityBar = (FragmentActivityBar) getSupportFragmentManager().findFragmentById(R.id.diary_bar);
        ShowActivityBars showActivityBars = new ShowActivityBars(this, fragmentActivityBar);
        showActivityBars.showKongJian(true, false, true, false, false, false, false);
        showActivityBars.showUI(R.drawable.zuo_black, null, "我的日记", null, 0, 0);
        showActivityBars.uiFunction(1, 0, 0, 0, 0, 0, 0);
    }

    private void initView() {
        mDiaryList = (RecyclerView) findViewById(R.id.diary_list);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(this);
    }


    private int weatherKey = -1;
    private String imageName = getMyXueHao() + "Diary.png";
    private String path = "http://nmy1206.natapp1.cc/AddUserDiary.php";
    private Handler longTimeHanlder = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ToastZong.ShowToast(MyApplication.getContext(),"超时，请重试");
        }
    };
    private Handler successHanlder = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.obj.equals("0")){
                readNewDiary();
                showDiaLog.closeMyDiaLog();
            }else {
                ToastZong.ShowToast(MyApplication.getContext(),"错误，请重试");
            }
        }
    };

    private ShowDiaLog showDiaLog;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                View view = LayoutInflater.from(this).inflate(R.layout.send_new_diary_dialog, null);
                showDiaLog = new ShowDiaLog(this, R.style.AlertDialog_qr, view);
                showDiaLog.logWindow(new ColorDrawable(Color.TRANSPARENT));
                showDiaLog.Cancelable(false);
                showDiaLog.showMyDiaLog();
                showDiaLog.dismissListener(new ShowDiaLog.Dismiss() {
                    @Override
                    public void dismiss() {
                        imagepath = null;
                        imageApath = null;
                        weatherKey = -1;
                    }
                });
                initDialogView(view);
                diaryDate.setText(MyDateClass.showYearMonthDay());
                diaryWeek.setText("星期" + MyDateClass.showWeekTable(MyDateClass.showYearMonthDay(),0));
                mWeatherGroup.setOnCheckedChangeListener(new OnOneWeatherGroupListener());
                mWeatherGroup1.setOnCheckedChangeListener(new OnTwoWeatherGroupListener());
                mWeatherGroup2.setOnCheckedChangeListener(new OnThreeWeatherGroupListener());
                diaryImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (imagepath == null){
                            SelectImage selectImage = new SelectImage(MyDiary.this);
                            selectImage.showSelectImage(1, imageName);
                        }else {
                            rxDialogScaleView = new RxDialogScaleView(MyDiary.this);
                            //      rxDialogScaleView.setContentView(LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.tipes_gv,null));
                            rxDialog = new RxDialog(MyDiary.this, R.style.tran_dialog);
                            rxDialog.setCanceledOnTouchOutside(false);
                            MyVeryDiaLog.veryImageDiaLog(rxDialogScaleView, imagepath, bitMapHandler);
                            MyVeryDiaLog.transparentDiaLog(MyDiary.this, rxDialog);
                        }
                    }
                });

                writeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (message.getText().toString().equals("")){
                            ToastZong.ShowToast(MyApplication.getContext(),"日记内容不能为空");
                        }else if (weatherKey == -1){
                            ToastZong.ShowToast(MyApplication.getContext(),"请选择今日天气");
                        }else if (imagepath != null){
                            HttpUtil.sendUserDiary(1,InValues.send(R.string.AddUserDiary),getMyXueHao(),weatherKey,message.getText().toString(),diaryDate.getText().toString()
                                    ,imagepath,imageApath, new okhttp3.Callback() {
                                        @Override
                                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                            String a = response.body().string();
                                            sendMessage(a);
                                        }

                                        @Override
                                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                            sendMessage("-1");
                                        }
                                    });
                        }else {
                            HttpUtil.sendUserDiary(0,InValues.send(R.string.AddUserDiary),getMyXueHao(),weatherKey,message.getText().toString(),diaryDate.getText().toString()
                                    ,"","", new okhttp3.Callback() {
                                        @Override
                                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                            String a = response.body().string();
                                            sendMessage(a);
                                        }

                                        @Override
                                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                            sendMessage("-1");
                                        }
                                    });
                        }
                    }
                });

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDiaLog.closeMyDiaLog();
                    }
                });
                break;
        }
    }

    private void sendMessage(String a){
        Message message = new Message();
        if (a.equals("-1")){
            longTimeHanlder.sendMessage(message);
        }else {
            message.obj = a;
            successHanlder.sendMessage(message);
        }
    }

    private List<LocalMedia> localMedia = new ArrayList<>();
    private String updateTime = null;
    public String imagepath;
    public String imageApath;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    localMedia = PictureSelector.obtainMultipleResult(data);
                    for (LocalMedia m : localMedia) {
                        if (m.isCut()) {
                            if (m.isCompressed()) {
                                double a = m.getSize();
                                imageApath = m.getCutPath();
                                if (a / 1024 <= 100) {
                                    if (a / 1024 >= 50) {
                                        sendSmallImage(m.getCompressPath());
                                    } else {
                                        sendSmallImage(m.getCutPath());
                                    }
                                } else {
                                    smallImage(m.getCompressPath());
                                }
                            }
                        }
                    }
                    break;
            }
        }
    }

    private void smallImage(String m) {
        Luban.with(MyDiary.this)
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
                        sendSmallImage(path);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                }).launch();
    }

    private RxDialog rxDialog;
    private RxDialogScaleView rxDialogScaleView;
    private Handler bitMapHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            rxDialog.dismiss();
            if (msg.obj != null) {
                if (msg.what == 1) {
                    ToastZong.ShowToast(MyApplication.getContext(), "图片加载错误");
                    rxDialogScaleView.setImage((Bitmap) msg.obj);
                } else {
                    rxDialogScaleView.setImage((Bitmap) msg.obj);
                }
            } else {
                ToastZong.ShowToast(MyApplication.getContext(), "错误");
            }
        }
    };
    private void sendSmallImage(String path) {
        if (path != null) {
            imagepath = path;
            updateTime = String.valueOf(System.currentTimeMillis());
            imageClose.setVisibility(View.VISIBLE);
            imageClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imagepath = null;
                    imageApath = null;
                    Glide.with(MyApplication.getContext()).clear(diaryImage);
                    imageClose.setVisibility(View.GONE);
                }
            });

            Glide.with(MyApplication.getContext())
                    .load(imagepath)
                    .signature(new MediaStoreSignature(updateTime, 1, 1))
                    .into(diaryImage);
        } else {
            ToastZong.ShowToast(MyApplication.getContext(), "图片加载失败,请重新选择");
        }
    }


    private ImageView diaryImage;
    private ImageView close;
    private TextView diaryDate;
    private TextView diaryWeek;
    private EditText message;
    private Button writeButton;
    private ImageView imageClose;
    private RadioButton mSun;
    private RadioButton mNight;
    private RadioButton mCloudy;
    private RadioButton mOvercast;
    private RadioButton mRain;
    private RadioButton mSnow;
    private RadioGroup mWeatherGroup;
    private RadioGroup mWeatherGroup1;
    private RadioGroup mWeatherGroup2;
    private List<RadioButton> radioButtons;

    private void initDialogView(View view) {
        radioButtons = new ArrayList<>();
        diaryImage = (ImageView) view.findViewById(R.id.newdiary_image);
        close = (ImageView) view.findViewById(R.id.newdiary_close);
        diaryDate = (TextView) view.findViewById(R.id.newdiary_date);
        diaryWeek = (TextView) view.findViewById(R.id.newdiary_week);
        message = (EditText) view.findViewById(R.id.newdiary_message);
        writeButton = (Button) view.findViewById(R.id.write_button);
        mSun = (RadioButton) view.findViewById(R.id.sun);
        mNight = (RadioButton) view.findViewById(R.id.night);
        mCloudy = (RadioButton) view.findViewById(R.id.cloudy);
        mOvercast = (RadioButton) view.findViewById(R.id.overcast);
        mRain = (RadioButton) view.findViewById(R.id.rain);
        mSnow = (RadioButton) view.findViewById(R.id.snow);
        mWeatherGroup = (RadioGroup) view.findViewById(R.id.weather_group);
        mWeatherGroup1 = (RadioGroup) view.findViewById(R.id.weather_group1);
        mWeatherGroup2 = (RadioGroup) view.findViewById(R.id.weather_group2);
        imageClose = (ImageView)view.findViewById(R.id.image_close);
        radioButtons.add(mSun);
        radioButtons.add(mNight);
        radioButtons.add(mCloudy);
        radioButtons.add(mOvercast);
        radioButtons.add(mRain);
        radioButtons.add(mSnow);

        for (int i = 0; i < weatherNum.length; i++) {
            AddRadioImage.resetRadioButtonImage(weatherNum[i], radioButtons.get(i));
        }
    }

    private class OnOneWeatherGroupListener implements RadioGroup.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId){
                case R.id.sun:
                    if (mSun.isChecked()){
                        weatherKey = 0;
                        mWeatherGroup1.clearCheck();
                        mWeatherGroup2.clearCheck();
                    }
                    break;
                case R.id.night:
                    if (mNight.isChecked()){
                        weatherKey = 1;
                        mWeatherGroup1.clearCheck();
                        mWeatherGroup2.clearCheck();
                    }
                    break;
            }
        }
    }
    private class OnTwoWeatherGroupListener implements RadioGroup.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId){
                case R.id.cloudy:
                    if (mCloudy.isChecked()){
                        weatherKey = 2;
                        mWeatherGroup.clearCheck();
                        mWeatherGroup2.clearCheck();
                    }
                    break;
                case R.id.overcast:
                    if (mOvercast.isChecked()){
                        weatherKey = 3;
                        mWeatherGroup.clearCheck();
                        mWeatherGroup2.clearCheck();
                    }
                    break;
            }
        }
    }
    private class OnThreeWeatherGroupListener implements RadioGroup.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId){
                case R.id.rain:
                    if (mRain.isChecked()){
                        weatherKey = 4;
                        mWeatherGroup.clearCheck();
                        mWeatherGroup1.clearCheck();
                    }
                    break;
                case R.id.snow:
                    if (mSnow.isChecked()){
                        weatherKey = 5;
                        mWeatherGroup.clearCheck();
                        mWeatherGroup1.clearCheck();
                    }
                    break;
            }
        }
    }
}
