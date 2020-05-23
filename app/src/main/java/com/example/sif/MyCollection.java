package com.example.sif;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sif.Lei.MyToolClass.UserDynamicCollection;
import com.example.sif.Lei.ShiPeiQi.CollectionList;
import com.example.sif.Lei.ShowActivityBar.FragmentActivityBar;

public class MyCollection extends BaseActivity {

    private RecyclerView mMycollectionRecy;
    private FragmentActivityBar fragmentActivityBar;
    private CollectionList collectionList;
    private LinearLayoutManager linearLayoutManager;
    private TextView mNullCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collection);
        initView();

        ZTL();

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.colleation_R);
        setPadding(this, relativeLayout);

        showActivitybar();

    }

    private Handler collectionHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    mNullCollection.setVisibility(View.INVISIBLE);
                    mMycollectionRecy.setVisibility(View.VISIBLE);
                    if (collectionList == null) {
                        linearLayoutManager = new LinearLayoutManager(MyApplication.getContext());
                        collectionList = new CollectionList(MyCollection.this,getMyXueHao());
                    }
                    collectionList.addCollection(UserDynamicCollection.userCollections);
                    mMycollectionRecy.setLayoutManager(linearLayoutManager);
                    mMycollectionRecy.setAdapter(collectionList);
                    collectionList.notifyDataSetChanged();
                    break;
                case 1:
                    mMycollectionRecy.setVisibility(View.INVISIBLE);
                    mNullCollection.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        UserDynamicCollection.Collection(getMyXueHao(), 1, collectionHander);
    }

    private void showActivitybar() {
        fragmentActivityBar = (FragmentActivityBar) getSupportFragmentManager().findFragmentById(R.id.mycollectin_bar);
        ShowActivityBars showActivityBars = new ShowActivityBars(this, fragmentActivityBar);
        showActivityBars.showKongJian(true, false, true, false, true, false, false);
        showActivityBars.showUI(R.drawable.zuo_black, (String) null, "我的收藏", null, null, 0, 0);
        showActivityBars.uiFunction(1, 0, 0, 0, 0, 0, 0);
        showActivityBars.barBackground1(R.color.beijing);
    }

    private void initView() {
        mMycollectionRecy = (RecyclerView) findViewById(R.id.mycollection_recy);
        mNullCollection = (TextView) findViewById(R.id.null_collection);
    }
}
