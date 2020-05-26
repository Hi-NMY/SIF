package com.example.sif;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sif.Lei.MyToolClass.MyDateClass;
import com.example.sif.Lei.MyToolClass.ObtainShopList;
import com.example.sif.Lei.MyToolClass.SchoolShopPopupWindow;
import com.example.sif.Lei.ShiPeiQi.SchoolShopAdapter;
import com.example.sif.Lei.ShowActivityBar.FragmentActivityBar;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class SchoolShop extends BaseActivity {

    private Handler allShopHanlder = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mIbRefresh.finishRefresh();
            mIbRefresh.finishLoadMore();
            schoolShopAdapter = new SchoolShopAdapter(SchoolShop.this, ObtainShopList.schoolShopClasses);
            mAllshopList.setAdapter(schoolShopAdapter);
        }
    };

    private Handler moreShopHanlder = new Handler(){
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_shop);
        initView();

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
    }
}
