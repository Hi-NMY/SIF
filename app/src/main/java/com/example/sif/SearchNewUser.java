package com.example.sif;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import com.example.sif.Lei.SearchFragment.UserSearchFragment;
import com.example.sif.Lei.ShowActivityBar.FragmentActivityBar;

public class SearchNewUser extends BaseActivity {

    private UserSearchFragment userSearchFragment;
    private View mSearchuserBar;
    private View mUserSearchFragment;
    private RecyclerView mSearchuserList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_new_user);
        initView();
        searchFragment();
        //设置状态栏
        ZTL();
        //ActivityBar
        showActivityBar();

        //增加padding  避免状态栏遮挡
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.searchuser_r);
        setPadding(this, relativeLayout);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void showActivityBar() {
        FragmentActivityBar fragmentActivityBar = (FragmentActivityBar) getSupportFragmentManager().findFragmentById(R.id.searchuser_bar);
        ShowActivityBars showActivityBars = new ShowActivityBars(this, fragmentActivityBar);
        showActivityBars.showKongJian(true, false, true, false, false, false, false);
        showActivityBars.showUI(R.drawable.zuo_black, null, "新关注", null, 0, 0);
        showActivityBars.uiFunction(1, 0, 0, 0, 0, 0, 0);
        showActivityBars.barBackground1(R.color.beijing);
    }

    private void searchFragment() {
        userSearchFragment = (UserSearchFragment) getSupportFragmentManager().findFragmentById(R.id.user_search_fragment);
        mUserSearchFragment.bringToFront();
    }

    private void initView() {
        mSearchuserBar = (View) findViewById(R.id.searchuser_bar);
        mUserSearchFragment = (View) findViewById(R.id.user_search_fragment);
        mSearchuserList = (RecyclerView) findViewById(R.id.searchuser_list);
    }
}
