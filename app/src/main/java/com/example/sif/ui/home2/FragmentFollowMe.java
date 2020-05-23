package com.example.sif.ui.home2;

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
import com.example.sif.Lei.MyToolClass.ToastZong;
import com.example.sif.Lei.MyToolClass.UserFollowToMe;
import com.example.sif.Lei.ShiPeiQi.UFollowAdapter;
import com.example.sif.MyApplication;
import com.example.sif.MyZhuYe;
import com.example.sif.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

public class FragmentFollowMe extends BaseFragment {
    private View view;
    private LinearLayoutManager linearLayoutManager;
    private MyZhuYe myZhuYe;
    private UserFollowToMe userFollowToMe;
    private UFollowAdapter uFollowAdapter;
    private RecyclerView mFollowTomeReView;
    private SmartRefreshLayout mFollowtomeRefresh;
    private RelativeLayout mFollowtomeListNull;

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

    private Handler nullHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.obj != null && msg.obj.equals(1)) {
                nullList();
            } else if (msg.obj != null && msg.obj.equals(3) && myZhuYe == MyApplication.getContext()) {
                ToastZong.ShowToast(myZhuYe, "请求超时");
            } else {
                sendList();
            }
            mFollowtomeRefresh.finishRefresh();
        }
    };

    private Handler moreHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.obj != null && msg.obj.equals(3) && myZhuYe == MyApplication.getContext()) {
                ToastZong.ShowToast(myZhuYe, "请求超时");
            } else {
                try {
                    uFollowAdapter.addFollowListbottom(userFollowToMe.followLists);
                    mFollowtomeRefresh.finishLoadMore();
                } catch (Exception e) {
                    mFollowtomeRefresh.finishLoadMore();
                }
            }
            mFollowtomeRefresh.finishLoadMore();
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_follow_me, container, false);

        myZhuYe = (MyZhuYe) getActivity();
        initView();

        linearLayoutManager = new LinearLayoutManager(myZhuYe);
        mFollowTomeReView.setLayoutManager(linearLayoutManager);
        userFollowToMe = myZhuYe.sendUserFollowToMe(2, myZhuYe.getMyXueHao());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message message = new Message();
                if (userFollowToMe.followLists != null && userFollowToMe.followLists.size() != 0) {
                    message.what = 0;
                    followMeHandler.sendMessage(message);
                } else {
                    message.what = 1;
                    followMeHandler.sendMessage(message);
                }
            }
        }).start();

        mFollowtomeListNull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userFollowToMe.sendHander(nullHander);
                userFollowToMe.followSend(String.valueOf(-1));
            }
        });

        return view;
    }

    private void initView() {
        mFollowTomeReView = (RecyclerView) view.findViewById(R.id.follow_tome_ReView);
        mFollowtomeListNull = (RelativeLayout) view.findViewById(R.id.followtome_list_null);
        mFollowtomeRefresh = (SmartRefreshLayout) view.findViewById(R.id.followtome_refresh);

        setRefreshListener();
    }

    private void setRefreshListener() {
        mFollowtomeRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                userFollowToMe.sendHander(nullHander);
                userFollowToMe.followSend(String.valueOf(-1));
            }
        });

        mFollowtomeRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                userFollowToMe.sendHander(moreHander);
                userFollowToMe.followSend(String.valueOf(uFollowAdapter.freshid));
            }
        });
    }

    private void sendList() {
        if (userFollowToMe.followLists != null && userFollowToMe.followLists.size() > 0){
            mFollowtomeListNull.setVisibility(View.INVISIBLE);
            mFollowtomeRefresh.setVisibility(View.VISIBLE);
            uFollowAdapter = new UFollowAdapter(userFollowToMe.followLists, myZhuYe);
            mFollowTomeReView.setAdapter(uFollowAdapter);
            uFollowAdapter.notifyDataSetChanged();
            mFollowTomeReView.postInvalidate();
            if (userFollowToMe.followLists != null && userFollowToMe.followLists.size() >= 15) {
                mFollowtomeRefresh.setEnableLoadMore(true);
            } else {
                mFollowtomeRefresh.setEnableLoadMore(false);
            }
        }
    }

    private void nullList() {
        mFollowtomeListNull.setVisibility(View.VISIBLE);
        mFollowtomeRefresh.setVisibility(View.INVISIBLE);
    }

}
