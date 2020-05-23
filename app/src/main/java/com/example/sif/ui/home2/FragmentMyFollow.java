package com.example.sif.ui.home2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sif.BaseFragment;
import com.example.sif.Lei.MyBroadcastReceiver.BroadcastRec;
import com.example.sif.Lei.MyToolClass.ToastZong;
import com.example.sif.Lei.MyToolClass.UserFollowList;
import com.example.sif.Lei.ShiPeiQi.UFollowAdapter;
import com.example.sif.MyApplication;
import com.example.sif.MyZhuYe;
import com.example.sif.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

public class FragmentMyFollow extends BaseFragment {
    private View view;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView mFollowMeReView;
    private MyZhuYe myZhuYe;
    private UserFollowList userFollowList;
    private RelativeLayout mFollowListNull;
    private SmartRefreshLayout mMyfollolistRefresh;
    private UFollowAdapter uFollowAdapter;

    private RefreshFollowUserList refreshFollowUserList;

    private Handler followMeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    sendList();
                    break;
                case 1:
                    nullList();
                    break;
            }

        }
    };

    private Handler nullHander = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.obj != null && msg.obj.equals(1)){
                nullList();
            }else if (msg.obj != null && msg.obj.equals(3) && myZhuYe == MyApplication.getContext()){
                ToastZong.ShowToast(myZhuYe,"请求超时");
            }else {
                sendList();
            }
            mMyfollolistRefresh.finishRefresh();
        }
    };

    private Handler moreHander = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.obj != null && msg.obj.equals(3) && myZhuYe == MyApplication.getContext()){
                ToastZong.ShowToast(myZhuYe,"请求超时");
            }else {
                try {
                    uFollowAdapter.addFollowListbottom(userFollowList.followLists);
                    mMyfollolistRefresh.finishLoadMore();
                }catch (Exception e){
                    mMyfollolistRefresh.finishLoadMore();
                }
            }
            mMyfollolistRefresh.finishLoadMore();
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_myfollow, container, false);
        myZhuYe = (MyZhuYe) getActivity();
        initView();

        refreshFollowUserList = new RefreshFollowUserList();
        BroadcastRec.obtainRecriver(MyApplication.getContext(),"noyesFollowUser",refreshFollowUserList);

        linearLayoutManager = new LinearLayoutManager(myZhuYe);
        mFollowMeReView.setLayoutManager(linearLayoutManager);
        userFollowList = myZhuYe.sendUserFollowList(2, myZhuYe.getMyXueHao());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message message = new Message();
                if (userFollowList.followLists != null && userFollowList.followLists.size() != 0) {
                    message.what = 0;
                    followMeHandler.sendMessage(message);
                } else {
                    message.what = 1;
                    followMeHandler.sendMessage(message);
                }
            }
        }).start();

        mFollowListNull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userFollowList.sendHander(nullHander);
                userFollowList.followSend(String.valueOf(-1));
            }
        });

        return view;
    }

    private void initView() {
        mFollowMeReView = (RecyclerView) view.findViewById(R.id.follow_me_ReView);
        mFollowListNull = (RelativeLayout) view.findViewById(R.id.follow_list_null);
        mMyfollolistRefresh = (SmartRefreshLayout) view.findViewById(R.id.myfollolist_refresh);

        setRefreshListener();
    }

    private void setRefreshListener() {
        mMyfollolistRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                userFollowList.sendHander(nullHander);
                userFollowList.followSend(String.valueOf(-1));
            }
        });

        mMyfollolistRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                userFollowList.sendHander(moreHander);
                userFollowList.followSend(String.valueOf(uFollowAdapter.freshid));
            }
        });
    }

    private void sendList(){
        mFollowListNull.setVisibility(View.INVISIBLE);
        mMyfollolistRefresh.setVisibility(View.VISIBLE);
        uFollowAdapter = new UFollowAdapter(userFollowList.followLists, myZhuYe);
        mFollowMeReView.setAdapter(uFollowAdapter);
        uFollowAdapter.notifyDataSetChanged();
        mFollowMeReView.postInvalidate();
        if (userFollowList.followLists != null && userFollowList.followLists.size() >= 15){
            mMyfollolistRefresh.setEnableLoadMore(true);
        }else {
            mMyfollolistRefresh.setEnableLoadMore(false);
        }
    }

    private void nullList(){
        mFollowListNull.setVisibility(View.VISIBLE);
        mMyfollolistRefresh.setVisibility(View.INVISIBLE);
    }

    class RefreshFollowUserList extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            userFollowList.sendHander(nullHander);
            userFollowList.followSend(String.valueOf(-1));
        }
    }
}
