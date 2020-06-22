package com.example.sif;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.sif.Lei.GPSServer.MyLocationListener;
import com.example.sif.Lei.GPSServer.SearchGps;
import com.example.sif.Lei.MyBroadcastReceiver.BroadcastRec;
import com.example.sif.Lei.MyToolClass.*;
import com.example.sif.Lei.ShiPeiQi.GuangChangMessageImageList;
import com.example.sif.Lei.ShowActivityBar.FragmentActivityBar;
import com.example.sif.NeiBuLei.DouBleImagePath;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.tamsiree.rxui.view.dialog.RxDialog;
import com.tamsiree.rxui.view.dialog.RxDialogScaleView;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GuangChangMessage extends BaseActivity implements View.OnLayoutChangeListener, View.OnClickListener {

    private ImageButton mDynamicMessageImage;
    private ImageView mDynamicMessageImageTwo;
    private TagFlowLayout mIdFlowlayout;
    private String[] values = new String[]{"我的位置", "选择街区"};
    private EditText mGuangchangMessageContent;

    private LocalBroadcastManager localBroadcastManager;
    private IntentFilter intentFilter;
    private LabelListener labelListener;
    private TagFlowLayout mIbSelect;
    private RecyclerView mImageList;
    private GPSVeryPopupwindow gpsVeryPopupwindow;
    private ObtainPlace obtainPlace;
    private SelectMyPlace selectMyPlace;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guang_chang_message);
        obtainPlace = new ObtainPlace();
        selectMyPlace = new SelectMyPlace();
        BroadcastRec.obtainRecriver(this,"selectPlace",selectMyPlace);
        BroadcastRec.obtainRecriver(this,"nearbyPlace",obtainPlace);
        initView();
        strings = new ArrayList<>();

        localBroadcastManager = LocalBroadcastManager.getInstance(MyApplication.getContext());
        intentFilter = new IntentFilter();
        intentFilter.addAction("BlockLabel");
        labelListener = new LabelListener();
        localBroadcastManager.registerReceiver(labelListener, intentFilter);

        showActivityBar();

        stringBuffer = new StringBuffer();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //键盘
        windowView = findViewById(R.id.GuangChang_R);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        windowHeight = metrics.heightPixels;
        keyHeight = windowHeight / 3;
        gcMsgTag();
    }

    private void gcMsgTag(){
        LayoutInflater mInflater = LayoutInflater.from(this);
        mIdFlowlayout.setAdapter(new TagAdapter<String>(values) {
            @Override
            public View getView(FlowLayout parent, int position, String o) {
                View v = mInflater.inflate(R.layout.lable_list, mIdFlowlayout, false);
                TextView t = (TextView) v.findViewById(R.id.text);
                ImageView i = (ImageView) v.findViewById(R.id.image);
                if (position == 0) {
                    i.setBackground(getDrawable(R.drawable.gps));
                    if (!o.equals("我的位置")){
                        t.setTextColor(getColor(R.color.navyblue));
                    }else {
                        t.setTextColor(getColor(R.color.gray));
                    }
                } else {
                    i.setBackground(getDrawable(R.drawable.blockimage));
                }
                t.setText(o);
                return v;
            }
        });

        mIdFlowlayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (position == 0){
                    hideKeyboard(mGuangchangMessageContent, 1);
                    checkVoice(null, Manifest.permission.ACCESS_COARSE_LOCATION);
                    gpsVeryPopupwindow = new GPSVeryPopupwindow(MyApplication.getContext(),GuangChangMessage.this);
                    gpsVeryPopupwindow.showAtLocation(findViewById(R.id.id_flowlayout), Gravity.BOTTOM, 0, 0);
                }
                if (position == 1) {
                    hideKeyboard(mGuangchangMessageContent, 1);
                    VeryPopupWindow veryPopupWindow = new VeryPopupWindow(MyApplication.getContext(), strings);
                    veryPopupWindow.showAtLocation(findViewById(R.id.id_flowlayout), Gravity.BOTTOM, 0, 0);
                }
                return true;
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void showActivityBar() {
        FragmentActivityBar fragmentActivityBar = (FragmentActivityBar) getSupportFragmentManager().findFragmentById(R.id.guangchang_message_frag);
        ShowActivityBars showActivityBars = new ShowActivityBars(this, fragmentActivityBar);
        showActivityBars.showKongJian(true, false, false, false, true, false, false);
        showActivityBars.showUI(R.drawable.zuo_black, (String) null, null, null, "发布", 0, 0);
        showActivityBars.uiFunction(1, 0, 0, 0, 1, 0, 0);
        showActivityBars.barBackground1(0);
    }

    private void hideKeyboard(View v, int i) {
        InputMethodManager inputMethodManager = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (i == 1) {
            inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        } else {
            inputMethodManager.showSoftInput(v, 0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        windowView.addOnLayoutChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private View windowView;
    private int windowHeight;
    private int keyHeight;

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
            mImageList.setVisibility(View.INVISIBLE);
            mDynamicMessageImageTwo.setVisibility(View.INVISIBLE);
        } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
            mImageList.setVisibility(View.VISIBLE);
            mDynamicMessageImageTwo.setVisibility(View.VISIBLE);
        }
    }

    private void initView() {
        mDynamicMessageImage = (ImageButton) findViewById(R.id.dynamic_message_image);
        mDynamicMessageImageTwo = (ImageView) findViewById(R.id.dynamic_message_image_two);
        mDynamicMessageImageTwo.setOnClickListener(this);

        mDynamicMessageImage.setOnClickListener(this);
        mIdFlowlayout = (TagFlowLayout) findViewById(R.id.id_flowlayout);
        mIdFlowlayout.setOnClickListener(this);
        mGuangchangMessageContent = (EditText) findViewById(R.id.guangchang_message_content);
        mIbSelect = (TagFlowLayout) findViewById(R.id.ib_select);
        mImageList = (RecyclerView) findViewById(R.id.image_list);
    }

    private String imageName = "";

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dynamic_message_image:
                imageName = System.currentTimeMillis() +
                        (int) (Math.random() * 1000) + ".png";
                SelectImage selectImage = new SelectImage(this);
                selectImage.showSelectImage(1, imageName);
                break;
            case R.id.dynamic_message_image_two:
                imageName = System.currentTimeMillis() +
                        (int) (Math.random() * 1000) + ".png";
                SelectImage selectImage1 = new SelectImage(this);
                selectImage1.showSelectImage(1, imageName);
                break;
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
        Luban.with(GuangChangMessage.this)
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

    public List<DouBleImagePath> ds;
    private GuangChangMessageImageList guangChangMessageImageList;
    private LinearLayoutManager linearLayoutManager;
    private DouBleImagePath douBleImagePath;
    private void sendSmallImage(String path) {
        if (ds == null){
            ds = new ArrayList<>();
            linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            mImageList.setLayoutManager(linearLayoutManager);
        }
        if (path != null) {
            imagepath = path;
            if (ds.size() < 4){
                mImageList.setVisibility(View.VISIBLE);
                mDynamicMessageImageTwo.setVisibility(View.VISIBLE);
                douBleImagePath = new DouBleImagePath();
                douBleImagePath.setMinPath(imagepath);
                douBleImagePath.setMaxPath(imageApath);
                ds.add(0,douBleImagePath);
                guangChangMessageImageList = new GuangChangMessageImageList(this,GuangChangMessage.this,ds);
                mImageList.setAdapter(guangChangMessageImageList);
                if (ds.size() == 4){
                    mDynamicMessageImageTwo.setVisibility(View.GONE);
                }
            }
        } else {
            ToastZong.ShowToast(GuangChangMessage.this, "图片加载失败,请重新选择");
        }
    }

    public void removeList(int position){
        ds.remove(position);
        if (ds == null || ds.size() <= 0 ){
            mImageList.setVisibility(View.INVISIBLE);
            mDynamicMessageImageTwo.setVisibility(View.GONE);
        }else {
            mDynamicMessageImageTwo.setVisibility(View.VISIBLE);
            guangChangMessageImageList.removeImage(position);
        }
    }

    public void lookPicture(int position){
        rxDialogScaleView = new RxDialogScaleView(GuangChangMessage.this);
        rxDialog = new RxDialog(GuangChangMessage.this, R.style.tran_dialog);
        rxDialog.setCanceledOnTouchOutside(false);
        MyVeryDiaLog.veryImageDiaLog(rxDialogScaleView, ds.get(position).getMaxPath(), bitMapHandler);
        MyVeryDiaLog.transparentDiaLog(GuangChangMessage.this, rxDialog);
    }

    public StringBuffer stringBuffer;
    public void sendMySelectIb(List<String> strings) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        stringBuffer = new StringBuffer();
        if (strings.size() != 0) {
            for (String f : strings) {
                stringBuffer.append(f + ",");
            }
        } else {
            stringBuffer.append("");
        }

        mIbSelect.setAdapter(new TagAdapter<String>(strings) {
            @Override
            public View getView(FlowLayout parent, int position, String o) {
                View view = layoutInflater.inflate(R.layout.label_list1, mIbSelect, false);
                TextView t = (TextView) view.findViewById(R.id.text);
                t.setText(o);
                return view;
            }
        });
    }

    public void removeAllList() {
        mIbSelect.removeAllViews();
    }

    private List<String> strings;

    class LabelListener extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            strings = intent.getStringArrayListExtra("B");
            if (strings != null && strings.size() != 0) {
                sendMySelectIb(strings);
            } else {
                stringBuffer.append("");
                removeAllList();
            }
        }
    }

    private MyLocationListener myLocationListener;
    class ObtainPlace extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            int a = intent.getIntExtra("textone",0);
            if (gpsVeryPopupwindow != null){
                if (a == 1){
                    myLocationListener = gpsVeryPopupwindow.myGpsClient.obtainListener();
                    gpsVeryPopupwindow.addPlaceList(myLocationListener.msgGpsClasses);
                }else if (a == 2){
                    gpsVeryPopupwindow.addPlaceList(SearchGps.searchMsgGps);
                }else if (a != 1 && a!= 2){
                    gpsVeryPopupwindow.errorPlace();
                }
            }
        }
    }

    public String placeString = "";
    class SelectMyPlace extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            placeString = intent.getStringExtra("texttwo");
            if (placeString.equals("不显示位置信息")){
                placeString = "";
                values = new String[]{"我的位置", "选择街区"};
            }else {
                values = new String[]{placeString, "选择街区"};
            }
            gcMsgTag();
            gpsVeryPopupwindow.dismissWindow();
        }
    }

}

