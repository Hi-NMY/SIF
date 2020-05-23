package com.example.sif.ui.ib_viewpager;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sif.BaseFragment;
import com.example.sif.InterestingBlock;
import com.example.sif.Lei.MyBroadcastReceiver.BroadcastRec;
import com.example.sif.Lei.MyToolClass.IbFollow;
import com.example.sif.Lei.SearchFragment.SearchFragment;
import com.example.sif.Lei.ShiPeiQi.IbClassList;
import com.example.sif.MyApplication;
import com.example.sif.NeiBuLei.InterestingBlockClass;
import com.example.sif.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

public class FragmentIbMe extends BaseFragment implements View.OnClickListener {
    private View view;

    private RecyclerView ib_recyclerView;
    private InterestingBlock interestingBlock;
    private GridLayoutManager gridLayoutManager;
    private IbFollow ibFollow;
    private SearchFragment searchFragment;
    private View fragment;
    private SmartRefreshLayout mIbRefresh;
    private TextView mNullFrush;
    private IbClassList ibClassList;

    private LocalBroadcastManager localBroadcastManager;
    private SearchMyIbFollow searchMyIbFollow;
    private RefreshMyIbFollow refreshMyIbFollow;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ib,container,false);
        initView(view);

        localBroadcastManager = LocalBroadcastManager.getInstance(MyApplication.getContext());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("searchIbMy");
        searchMyIbFollow = new SearchMyIbFollow();
        localBroadcastManager.registerReceiver(searchMyIbFollow,intentFilter);

        refreshMyIbFollow = new RefreshMyIbFollow();
        BroadcastRec.obtainRecriver(MyApplication.getContext(),"trueFollow",refreshMyIbFollow);

        interestingBlock = (InterestingBlock) getActivity();

        searchFragment();
        initIbClass(1);
        ibFollow.sendHander(nullHanlder);

        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(interestingBlock, R.anim.layout_animation);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        controller.setDelay(0.1f);
        ib_recyclerView.setLayoutAnimation(controller);
        ib_recyclerView.startLayoutAnimation();
        gridLayoutManager = new GridLayoutManager(interestingBlock, 2);
        ib_recyclerView.setLayoutManager(gridLayoutManager);

        //initIbMore(ibFollow.interestingBlockClasses);

        return view;
    }

    @SuppressLint("ResourceType")
    private void searchFragment() {
        searchFragment = (SearchFragment) getChildFragmentManager().findFragmentById(R.layout.search_fragment);
        fragment.bringToFront();
    }

    private void initView(View v) {
        ib_recyclerView = (RecyclerView) v.findViewById(R.id.ib_recyclerView);
        fragment = (View) v.findViewById(R.id.ib_search_fragment);
        mIbRefresh = (SmartRefreshLayout)v.findViewById(R.id.ib_refresh);
        mNullFrush = (TextView)v.findViewById(R.id.null_frush);

        mNullFrush.setOnClickListener(this);

        initFrushListener(mIbRefresh);
    }

    public void initIbClass(int i){
        if (i == 1){
            ibFollow = interestingBlock.sendIbFollow(1,interestingBlock.getMyXueHao());
        }else {
            ibFollow = interestingBlock.sendIbFollow(2,interestingBlock.getMyXueHao());
        }
    }


    private void initFrushListener(SmartRefreshLayout m) {
        m.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                initIbClass(1);
                ibFollow.myFreshView(m);
                ibFollow.sendHander(nullHanlder);
            }
        });

        m.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ibFollow.myFreshView(m);
                ibFollow.sendHander(moreHander);
                ibFollow.moreFollow(ibClassList.ibId);
            }
        });
    }

    private void changeUI(int i){
        switch (i){
            case 1:
                mIbRefresh.setVisibility(View.VISIBLE);
                mNullFrush.setVisibility(View.INVISIBLE);
                break;
            case 2:
                //ToastZong.ShowToast(interestingBlock,"错误");
                mIbRefresh.setVisibility(View.INVISIBLE);
                mNullFrush.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void initIbMore(List<InterestingBlockClass> interestingBlockClasses){
        if (interestingBlockClasses != null && interestingBlockClasses.size() > 0){
            if (interestingBlockClasses.size() >= 15){
                mIbRefresh.setEnableLoadMore(true);
            }
            changeUI(1);
            ibClassList = new IbClassList(interestingBlockClasses, interestingBlock);
            ib_recyclerView.setAdapter(ibClassList);
            ibClassList.notifyDataSetChanged();
        }else {
            changeUI(2);
        }
    }

    private Handler nullHanlder = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            initIbMore(ibFollow.interestingBlockClasses);
            mIbRefresh.finishRefresh();
        }
    };

    private Handler moreHander = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mIbRefresh.finishLoadMore();
            ibClassList.addMoreIbList(ibFollow.interestingBlockClasses);
        }
    };



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.null_frush:
                initIbClass(1);
                ibFollow.sendHander(nullHanlder);
                break;
        }
    }
    private void searchIb(int id, String blockname) {
        if (id != -3 && blockname != null && !blockname.equals("")){
            ibFollow.sendHander(nullHanlder);
            ibFollow.searchMyIbMore(id,blockname);
            mIbRefresh.setEnableLoadMore(false);
            mIbRefresh.setEnableRefresh(false);
        }else {
            mIbRefresh.setEnableLoadMore(true);
            mIbRefresh.setEnableRefresh(true);
        }
    }

    class SearchMyIbFollow extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            int id = intent.getIntExtra("id",0);
            String blockname = intent.getStringExtra("blockname");
            if (blockname == null || blockname.equals("")){
                initIbClass(1);
                mIbRefresh.setEnableRefresh(true);
                ibFollow.sendHander(nullHanlder);
            }else {
                searchIb(id,blockname);
            }
        }
    }

    class RefreshMyIbFollow extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            initIbClass(1);
            ibFollow.myFreshView(mIbRefresh);
            ibFollow.sendHander(nullHanlder);
        }
    }

}
