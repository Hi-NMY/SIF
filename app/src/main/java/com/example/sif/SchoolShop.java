package com.example.sif;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sif.Lei.LianJie.HttpUtil;
import com.example.sif.Lei.MyBroadcastReceiver.BroadcastRec;
import com.example.sif.Lei.MyToolClass.MyDateClass;
import com.example.sif.Lei.MyToolClass.ObtainShopList;
import com.example.sif.Lei.MyToolClass.SchoolShopPopupWindow;
import com.example.sif.Lei.MyToolClass.ShowDiaLog;
import com.example.sif.Lei.MyToolClass.ToastZong;
import com.example.sif.Lei.ShiPeiQi.SchoolShopAdapter;
import com.example.sif.Lei.ShowActivityBar.FragmentActivityBar;
import com.example.sif.NeiBuLei.SchoolShopClass;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class SchoolShop extends BaseActivity implements View.OnClickListener {

    private Handler allShopHanlder = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mIbRefresh.finishRefresh();
            mIbRefresh.finishLoadMore();
            schoolShopAdapter = new SchoolShopAdapter(SchoolShop.this,SchoolShop.this, ObtainShopList.schoolShopClasses);
            mAllshopList.setAdapter(schoolShopAdapter);
            mShopSearch.attachToRecyclerView(mAllshopList);
        }
    };

    private Handler moreShopHanlder = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mIbRefresh.finishRefresh();
            mIbRefresh.finishLoadMore();
            schoolShopAdapter.addSchoolShopAdapter(ObtainShopList.schoolShopClasses);
        }
    };

    private RelativeLayout mSchspR;
    private LinearLayoutManager linearLayoutManager;
    private SchoolShopAdapter schoolShopAdapter;
    private RecyclerView mAllshopList;
    private SmartRefreshLayout mIbRefresh;

    private NewSendShop newSendShop;
    private FloatingActionButton mShopSearch;
    private TextView mT1;
    private ImageView mImg1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_shop);
        initView();

        newSendShop = new NewSendShop();
        BroadcastRec.obtainRecriver(MyApplication.getContext(), "newSendShop", newSendShop);

        //设置状态栏
        ZTL();
        //ActivityBar
        showActivityBar();

        //增加padding  避免状态栏遮挡
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.schsp_r);
        setPadding(this, relativeLayout);
        linearLayoutManager = new LinearLayoutManager(this);
        mAllshopList.setLayoutManager(linearLayoutManager);
        ObtainShopList.obtainMoreShop(MyDateClass.showNowDate(), allShopHanlder);

        freshListener();
    }

    private void freshListener() {
        mIbRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                ObtainShopList.obtainMoreShop(MyDateClass.showNowDate(), allShopHanlder);
            }
        });

        mIbRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ObtainShopList.obtainMoreShop(schoolShopAdapter.date, moreShopHanlder);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void showActivityBar() {
        FragmentActivityBar fragmentActivityBar = (FragmentActivityBar) getSupportFragmentManager().findFragmentById(R.id.schoolshop_bar);
        ShowActivityBars showActivityBars = new ShowActivityBars(this, fragmentActivityBar);
        showActivityBars.showKongJian(true, false, true, false, false, true, true);
        showActivityBars.showUI(R.drawable.zuo_black, null, "淘商店", null, R.drawable.toshop, R.drawable.shophistory);
        showActivityBars.uiFunction(1, 0, 0, 0, 0, 3, 2);
        showActivityBars.barBackground1(R.color.qianbai);
    }

    private SchoolShopPopupWindow schoolShopPopupWindow;

    public void setPopupWindow(SchoolShopPopupWindow s) {
        this.schoolShopPopupWindow = s;
    }

    private List<LocalMedia> localMedia = new ArrayList<>();
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
                                        schoolShopPopupWindow.sendSmallImage(m.getCompressPath(), imageApath);
                                    } else {
                                        schoolShopPopupWindow.sendSmallImage(m.getCutPath(), imageApath);
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
        Luban.with(SchoolShop.this)
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
                        schoolShopPopupWindow.sendSmallImage(path, imageApath);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                }).launch();
    }


    private void initView() {
        mAllshopList = (RecyclerView) findViewById(R.id.allshop_list);
        mIbRefresh = (SmartRefreshLayout) findViewById(R.id.ib_refresh);
        mShopSearch = (FloatingActionButton) findViewById(R.id.shop_search);
        mShopSearch.setOnClickListener(this);
        mT1 = (TextView) findViewById(R.id.t1);
        mT1.setOnClickListener(this);
        mImg1 = (ImageView) findViewById(R.id.img1);
        mImg1.setOnClickListener(this);
    }

    private void newShopMessage() {
        schoolShopAdapter.addNewShop(schoolShopClass);
        mAllshopList.setAdapter(schoolShopAdapter);
    }


    private ShowDiaLog showDiaLog1;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shop_search:
                View view1 = LayoutInflater.from(this).inflate(R.layout.shop_searchdialog, null);
                initShopView(view1);
                showDiaLog1 = new ShowDiaLog(this, R.style.AlertDialog_qr, view1);
                showDiaLog1.logWindow(new ColorDrawable(Color.TRANSPARENT));
                showDiaLog1.showMyDiaLog();
                break;
            case R.id.t1:
                mIbRefresh.setEnableRefresh(true);
                mIbRefresh.setEnableLoadMore(true);
                mT1.setVisibility(View.GONE);
                mImg1.setVisibility(View.GONE);
                ObtainShopList.obtainMoreShop(MyDateClass.showNowDate(), allShopHanlder);
                break;
        }
    }

    private EditText searchE;
    private Button searchBtn;
    private String path = "http://nmy1206.natapp1.cc/SearchSchoolShop.php";
    private List<SchoolShopClass> searchSchools;
    private Handler searchHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    closeDiaLog();
                    if (showDiaLog1 != null){
                        showDiaLog1.closeMyDiaLog();
                    }
                    mIbRefresh.setEnableRefresh(false);
                    mIbRefresh.setEnableLoadMore(false);
                    mT1.setVisibility(View.VISIBLE);
                    mImg1.setVisibility(View.VISIBLE);
                    schoolShopAdapter = new SchoolShopAdapter(SchoolShop.this,SchoolShop.this, searchSchools);
                    mAllshopList.setAdapter(schoolShopAdapter);
                    mShopSearch.attachToRecyclerView(mAllshopList);
                    break;
                case 1:
                    closeDiaLog();
                    ToastZong.ShowToast(MyApplication.getContext(), "阿欧，出错了");
                    break;
            }
        }
    };

    public void initShopView(View view1) {
        searchE = (EditText) view1.findViewById(R.id.search_shopett);
        searchBtn = (Button) view1.findViewById(R.id.search_shop_btn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDiaLog(SchoolShop.this, R.drawable.searchshop_loading);
                if (!searchE.getText().toString().equals("")) {
                    searchStart(searchE.getText().toString());
                } else {
                    ToastZong.ShowToast(MyApplication.getContext(), "搜索内容不能为空哦");
                    closeDiaLog();
                }
            }
        });
    }

    public void searchStart(String keywork){
        searchSchools = new ArrayList<>();
        Message message = new Message();
        HttpUtil.searchSchoolShop(path, keywork, new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String a = response.body().string();
                try {
                    searchSchools = new Gson().fromJson(a, new TypeToken<List<SchoolShopClass>>() {
                    }.getType());
                    message.what = 0;
                    searchHandler.sendMessage(message);
                } catch (Exception e) {
                    message.what = 1;
                    searchHandler.sendMessage(message);
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                message.what = 1;
                searchHandler.sendMessage(message);
            }
        });
    }

    private SchoolShopClass schoolShopClass;

    class NewSendShop extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String a = intent.getStringExtra("texttwo");
            String[] s = a.split("&");
            schoolShopClass = new SchoolShopClass();
            schoolShopClass.setXuehao(s[0]);
            schoolShopClass.setSendtime(s[1]);
            schoolShopClass.setLabel(s[2]);
            schoolShopClass.setNotice(s[3]);
            schoolShopClass.setCommodityimage(s[4]);
            schoolShopClass.setShopstate(Integer.valueOf(s[5]));
            newShopMessage();
        }
    }
}
