package com.example.sif;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sif.Lei.MyToolClass.MyRecyclerviewAnimatior;
import com.example.sif.Lei.MyToolClass.ObtainUserShop;
import com.example.sif.Lei.ShiPeiQi.UserShopAdapter;
import com.example.sif.Lei.ShowActivityBar.FragmentActivityBar;

public class UserHomeShop extends BaseActivity {

    private Handler userShopHanlder = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            userShopAdapter = new UserShopAdapter(UserHomeShop.this,ObtainUserShop.schoolShopClasses);
            mUserallshopList.setAdapter(userShopAdapter);
        }
    };

    private RecyclerView mUserallshopList;
    private LinearLayoutManager linearLayoutManager;
    private UserShopAdapter userShopAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home_shop);
        initView();

        //设置状态栏
        ZTL();
        //ActivityBar
        showActivityBar();

        //增加padding  避免状态栏遮挡
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.user_shop);
        setPadding(this, relativeLayout);
        linearLayoutManager = new LinearLayoutManager(this);
        mUserallshopList.setLayoutManager(linearLayoutManager);
        mUserallshopList.setItemAnimator(new MyRecyclerviewAnimatior());
        ObtainUserShop.obtainUserShop(getMyXueHao(),userShopHanlder);

    }

    private void initView() {
        mUserallshopList = (RecyclerView) findViewById(R.id.userallshop_list);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void showActivityBar() {
        FragmentActivityBar fragmentActivityBar = (FragmentActivityBar) getSupportFragmentManager().findFragmentById(R.id.usershop_bar);
        ShowActivityBars showActivityBars = new ShowActivityBars(this, fragmentActivityBar);
        showActivityBars.showKongJian(true, false, true, false, false, false, false);
        showActivityBars.showUI(R.drawable.zuo_black, null, "我的发布", null, 0, 0);
        showActivityBars.uiFunction(1, 0, 0, 0, 0, 3, 2);
        showActivityBars.barBackground1(R.color.qianbai);
    }
}
