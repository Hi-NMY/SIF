package com.example.sif;

import android.os.Build;
import android.os.Bundle;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;

import com.example.sif.Lei.ShowActivityBar.FragmentActivityBar;

public class SchoolShop extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_shop);

        //设置状态栏
        ZTL();
        //ActivityBar
        showActivityBar();

        //增加padding  避免状态栏遮挡
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.schsp_r);
        setPadding(this, relativeLayout);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void showActivityBar() {
        FragmentActivityBar fragmentActivityBar = (FragmentActivityBar) getSupportFragmentManager().findFragmentById(R.id.schoolshop_bar);
        ShowActivityBars showActivityBars = new ShowActivityBars(this, fragmentActivityBar);
        showActivityBars.showKongJian(true, false, true, false, false, true, true);
        showActivityBars.showUI(R.drawable.zuo_black, null, "淘商店", null, R.drawable.toshop, R.drawable.shophistory);
        showActivityBars.uiFunction(1, 0, 0, 0, 0, 3, 2);
    }


}
