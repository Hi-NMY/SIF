package com.example.sif.ui.dashboard2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sif.BaseFragment;
import com.example.sif.Lei.MyBroadcastReceiver.BroadcastRec;
import com.example.sif.Lei.MyToolClass.MyDateClass;
import com.example.sif.Lei.MyToolClass.ToastZong;
import com.example.sif.Lei.MyToolClass.UserFollow;
import com.example.sif.Lei.ShiPeiQi.FollowDynamic;
import com.example.sif.Lei.ShiPeiQi.ViewItemJianJu;
import com.example.sif.MyApplication;
import com.example.sif.MyZhuYe;
import com.example.sif.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import pl.droidsonroids.gif.GifImageView;

public class FragmentGuanZhu extends BaseFragment {

    private View view;
    private TextView mSmarttext;
    private GifImageView mFollowGif;
    private RelativeLayout mFollowRt;
    private RecyclerView mFollowZhu;
    private TextView mFollowText;
    private RelativeLayout mFollowRt1;
    private SmartRefreshLayout mFollowFreshLt;
    private ImageView mFollowImage;
    private RelativeLayout mFollowNull;
    private MyZhuYe myZhuYe;

    private UserFollow userFollow;
    private LinearLayoutManager layoutManager;

    private RefreshFollow refreshFollow;

    private Handler handler = new Handler(){
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    if (userFollow.userSpaces!=null&&userFollow.userSpaces.size()!=0){
                        mFollowNull.setVisibility(View.INVISIBLE);
                        mFollowFreshLt.setVisibility(View.VISIBLE);
                        followDynamic = new FollowDynamic(myZhuYe,userFollow.userSpaces);
                        followDynamic.myuserDynamicThumb(myZhuYe.sendUserThumb(0));
                        followDynamic.sendUserFollow(myZhuYe.sendUserFollow(2,myZhuYe.getMyXueHao()));
                        followDynamic.notifyDataSetChanged();
                        mFollowZhu.setAdapter(followDynamic);
                        mFollowFreshLt.finishRefresh();
                    }else {
                        mFollowNull.setVisibility(View.VISIBLE);
                        mFollowFreshLt.setVisibility(View.INVISIBLE);
                    }
                    break;
                case 2:
                    followDynamic.addFollowListbottom(userFollow.userSpaces);
                    mFollowFreshLt.finishLoadMore();
                    break;
                case 3:
                    ToastZong.ShowToast(myZhuYe,"刷新错误");
                    mFollowFreshLt.finishRefresh();
                    mFollowFreshLt.finishLoadMore();
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_zhuye_guanzhu, container, false);
        initFragmentView();

        refreshFollow = new RefreshFollow();
        BroadcastRec.obtainRecriver(MyApplication.getContext(),"noyesFollowUser",refreshFollow);

        myZhuYe = (MyZhuYe) getActivity();

        userFollow = myZhuYe.sendUserFollow(2,myZhuYe.getMyXueHao());

        layoutManager = new LinearLayoutManager(myZhuYe);
        mFollowZhu.setLayoutManager(layoutManager);

        mFollowNull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userFollow.rightFollow();
                userFollow.sendHander(handler,1);
            }
        });

        try {
            if (userFollow.followList == null||userFollow.followList.equals("")||userFollow.followList.size() == 0||userFollow == null){
                mFollowNull.setVisibility(View.VISIBLE);
                mFollowFreshLt.setVisibility(View.INVISIBLE);
            }else {
                mFollowNull.setVisibility(View.INVISIBLE);
                mFollowFreshLt.setVisibility(View.VISIBLE);
                addMyFollow();
            }
        }catch (Exception e){
            ToastZong.ShowToast(MyApplication.getContext(),"错误");
        }


        xiaLaShuaXin(mFollowFreshLt);

        mFollowZhu.addItemDecoration(new ViewItemJianJu(30));

        mFollowZhu.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Glide.with(myZhuYe).resumeRequests();//恢复Glide加载图片
                }else {
                    Glide.with(myZhuYe).pauseRequests();//禁止Glide加载图片
                }
            }
        });

        return view;
    }

    private void xiaLaShuaXin(SmartRefreshLayout mFollowFreshLt) {
        mFollowFreshLt.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                String nowDate = MyDateClass.showNowDate();
                if (nowDate!=null){
                    userFollow.rightFollow();
                    userFollow.sendHander(handler,1);
                }else {
                    ToastZong.ShowToast(myZhuYe,"刷新失败!");
                }
            }
        });
        mFollowFreshLt.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                userFollow.sendHander(handler,2);
                userFollow.moreFollow(followDynamic.date);
            }
        });
    }

    private FollowDynamic followDynamic;
    private void addMyFollow() {
        if (followDynamic == null&&userFollow.userSpaces!=null) {
            followDynamic = new FollowDynamic(myZhuYe,userFollow.userSpaces);
            followDynamic.myuserDynamicThumb(myZhuYe.sendUserThumb(0));
            followDynamic.sendUserFollow(myZhuYe.sendUserFollow(2,myZhuYe.getMyXueHao()));
        }
        followDynamic.notifyDataSetChanged();
        mFollowZhu.setAdapter(followDynamic);
    }

    private void initFragmentView(){
        mSmarttext = (TextView)view.findViewById(R.id.smarttext);
        mFollowGif = (GifImageView)view.findViewById(R.id.follow_gif);
        mFollowRt = (RelativeLayout) view.findViewById(R.id.follow_Rt);
        mFollowZhu = (RecyclerView) view.findViewById(R.id.follow_zhu);
        mFollowText = (TextView) view.findViewById(R.id.follow_text);
        mFollowRt1 = (RelativeLayout) view.findViewById(R.id.follow_Rt1);
        mFollowFreshLt = (SmartRefreshLayout) view.findViewById(R.id.follow_fresh_lt);
        mFollowImage = (ImageView) view.findViewById(R.id.follow_image);
        mFollowNull = (RelativeLayout) view.findViewById(R.id.follow_null);
    }

    class RefreshFollow extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String nowDate = MyDateClass.showNowDate();
            if (nowDate!=null){
                userFollow.rightFollow();
                userFollow.sendHander(handler,1);
            }else {
                ToastZong.ShowToast(myZhuYe,"刷新失败!");
            }
        }
    }

}
