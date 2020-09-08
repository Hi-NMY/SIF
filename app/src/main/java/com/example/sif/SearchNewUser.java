package com.example.sif;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.sif.Lei.MyBroadcastReceiver.BroadcastRec;
import com.example.sif.Lei.MyToolClass.EasyLoading;
import com.example.sif.Lei.MyToolClass.ObtainSearchUser;
import com.example.sif.Lei.SearchFragment.UserSearchFragment;
import com.example.sif.Lei.ShiPeiQi.SearchUserAdapter;
import com.example.sif.Lei.ShowActivityBar.FragmentActivityBar;

public class SearchNewUser extends BaseActivity {

    private UserSearchFragment userSearchFragment;
    private View mSearchuserBar;
    private View mUserSearchFragment;
    private RecyclerView mSearchuserList;
    private LinearLayoutManager linearLayoutManager;
    private ObtainSearchUsers obtainSearchUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_new_user);
        obtainSearchUsers = new ObtainSearchUsers();
        BroadcastRec.obtainRecriver(MyApplication.getContext(),"searchNewUser",obtainSearchUsers);
        initView();
        searchFragment();
        //设置状态栏
        ZTL();
        //ActivityBar
        showActivityBar();

        //增加padding  避免状态栏遮挡
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.searchuser_r);
        setPadding(this, relativeLayout);

        linearLayoutManager = new LinearLayoutManager(this);
        mSearchuserList.setLayoutManager(linearLayoutManager);

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

    public void hideKeyboard(View v, int i) {
        InputMethodManager inputMethodManager = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (i == 1) {
            inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        } else {
            inputMethodManager.showSoftInput(v, 0);
        }
    }

    private SearchUserAdapter searchUserAdapter;
    private void sendUserList(){
        searchUserAdapter = new SearchUserAdapter(this, ObtainSearchUser.followLists);
        mSearchuserList.setAdapter(searchUserAdapter);
    }

    class ObtainSearchUsers extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            EasyLoading.dissmissLoading();
            if(intent.getIntExtra("textone",0) == 1000){
                sendUserList();
            }
        }
    }

}
