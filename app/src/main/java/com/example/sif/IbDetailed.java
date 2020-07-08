package com.example.sif;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.sif.Lei.MyBroadcastReceiver.BroadcastRec;
import com.example.sif.Lei.MyToolClass.*;
import com.example.sif.Lei.ShiPeiQi.GuangChang;
import com.example.sif.Lei.ShowActivityBar.FragmentActivityBar;
import com.example.sif.NeiBuLei.GuangChangUserXinXi;
import com.example.sif.NeiBuLei.InterestingBlockClass;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

public class IbDetailed extends BaseActivity implements View.OnClickListener {

    private View mIbDetailedBar;
    private ImageView mIbImage;
    private TextView mIbNameDetailed;
    private TextView mDynamicNum;
    private TextView mFollowNum;
    private TextView mSlogan;
    private Button mFollowButton;
    private RecyclerView mIbDetailedList;
    private SmartRefreshLayout mIbDetailedFresh;
    private NestedScrollView mIbDetailedNest;
    private FragmentActivityBar fragmentActivityBar;
    private RelativeLayout mIbdetailedR;

    private GuangChang guangChang;
    private UserDynamicThumb userDynamicThumb;
    private LinearLayoutManager linearLayoutManager;
    private String ibname;
    private IbFollow ibFollow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ib_detailed);
        initView();

        ibFollow = sendIbFollow(2,"");

        ZTL2();

        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.ib_rrr);
        setPadding(this,relativeLayout);

        userDynamicThumb = sendUserThumb(0);

        showActivityBar();

        Intent intent = getIntent();
        ibname = intent.getStringExtra("ibname");
        sendIbName(ibname);

        linearLayoutManager = new LinearLayoutManager(this);
        mIbDetailedList.setLayoutManager(linearLayoutManager);

    }

    private Handler messageIbHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            writeMessage((InterestingBlockClass) msg.obj);
        }
    };

    private Handler ibListHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mIbDetailedFresh.finishRefresh();
            writeIbList((List<GuangChangUserXinXi>) msg.obj);
        }
    };

    private Handler moreIbMessageHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            guangChang.addGuangChangListbottom((List<GuangChangUserXinXi>) msg.obj);
            mIbDetailedFresh.finishLoadMore();
        }
    };

    private void writeMessage(InterestingBlockClass interestingBlockClass){
        mIbNameDetailed.setText(interestingBlockClass.getIbname());
        mDynamicNum.setText(MyDateClass.sendMath(Integer.valueOf(interestingBlockClass.getDynamicnum())));
        mFollowNum.setText(MyDateClass.sendMath(Integer.valueOf(interestingBlockClass.getFollownum())));
        if (ibFollow.noYesFollow(interestingBlockClass.getIbname())){
            mFollowButton.setBackgroundResource(R.drawable.nostartimage_three);
            mFollowButton.setText("已关注");
        }else {
            mFollowButton.setBackgroundResource(R.drawable.ib_detailed_button);
        }
        if (interestingBlockClass.getSlogan() != null && !interestingBlockClass.getSlogan().equals("")){
            mSlogan.setVisibility(View.VISIBLE);
            mSlogan.setText(interestingBlockClass.getSlogan());
        }
        Glide.with(this)
                .load(InValues.send(R.string.httpHeadert) + interestingBlockClass.getBgimage())
                .placeholder(R.drawable.nullblock)
                .fallback(R.drawable.nullblock)
                .error(R.drawable.nullblock)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .transform(new GlideRoundTransform(20))
                .into(mIbImage);
        mIbNameDetailed.postInvalidate();
        mDynamicNum.postInvalidate();
        mFollowNum.postInvalidate();
        mSlogan.postInvalidate();
    }

    private void writeIbList(List<GuangChangUserXinXi> g){
        listenFreshOn(g);
        guangChang = new GuangChang(this,g);
        guangChang.myuserDynamicThumb(userDynamicThumb);
        guangChang.sendUserFollow(sendUserFollow(2,getMyXueHao()));
        guangChang.notifyDataSetChanged();
        mIbDetailedList.setAdapter(guangChang);
        listenerFresh();
    }

    private void listenFreshOn(List<GuangChangUserXinXi> g){
        if (g.size() >= 4){
            mIbDetailedFresh.setEnableRefresh(true);
            mIbDetailedFresh.setEnableLoadMore(true);
            BlockDetailedSend.freshView(mIbDetailedFresh);
        }
    }

    private void listenerFresh(){
        mIbDetailedFresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                BlockDetailedSend.detailedList(ibname,2, MyDateClass.showNowDate(),ibListHandler);
            }
        });

        mIbDetailedFresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                BlockDetailedSend.detailedList(ibname,2, guangChang.date,moreIbMessageHandler);
            }
        });
    }

    private void sendIbName(String ibname) {
        BlockDetailedSend.detailedBlock(ibname,1,messageIbHandler);
        BlockDetailedSend.detailedList(ibname,2,MyDateClass.showNowDate(),ibListHandler);
    }

    private void showActivityBar() {
        fragmentActivityBar = (FragmentActivityBar) getSupportFragmentManager().findFragmentById(R.id.ib_detailed_bar);
        mIbDetailedBar.bringToFront();
        ShowActivityBars showActivityBars = new ShowActivityBars(this, fragmentActivityBar);
        showActivityBars.showKongJian(true, false, true, false, true, false, false);
        showActivityBars.showUI(R.drawable.zuo_black, (String) null, "", null, null, 0, 0);
        showActivityBars.uiFunction(1, 0, 0, 0, 0, 0, 0);
        showActivityBars.barBackground1(R.color.beijing);

        Rect rect = new Rect();
        mIbDetailedNest.getHitRect(rect);
        mIbDetailedNest.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (!mIbdetailedR.getLocalVisibleRect(rect)) {
                    showActivityBars.showUI(R.drawable.zuo_black, (String) null, mIbNameDetailed.getText().toString(), null, null, 0, 0);
                } else {
                    showActivityBars.showUI(R.drawable.zuo_black, (String) null, "", null, null, 0, 0);
                }
            }
        });
    }

    private void initView() {
        mIbDetailedBar = (View) findViewById(R.id.ib_detailed_bar);
        mIbImage = (ImageView) findViewById(R.id.ib_image);
        mIbNameDetailed = (TextView) findViewById(R.id.ib_name_detailed);
        mDynamicNum = (TextView) findViewById(R.id.dynamic_num);
        mFollowNum = (TextView) findViewById(R.id.follow_num);
        mSlogan = (TextView) findViewById(R.id.slogan);
        mFollowButton = (Button) findViewById(R.id.follow_button);
        mIbDetailedList = (RecyclerView) findViewById(R.id.ib_detailed_list);
        mIbDetailedFresh = (SmartRefreshLayout) findViewById(R.id.ib_detailed_fresh);
        mIbDetailedNest = (NestedScrollView) findViewById(R.id.ib_detailed_nest);

        mFollowButton.setOnClickListener(this);
        mIbdetailedR = (RelativeLayout) findViewById(R.id.ibdetailed_R);
    }

    private Handler followHanlder = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    if (msg.obj.equals("10")){
                        mFollowButton.setBackgroundResource(R.drawable.nostartimage_three);
                        mFollowButton.setText("已关注");
                        mFollowNum.setText(MyDateClass.sendMath(Integer.valueOf(mFollowNum.getText().toString()) + 1));
                        BroadcastRec.sendReceiver(MyApplication.getContext(),"trueFollow",1,null);
                    }
                    if (msg.obj.equals("11")){
                        mFollowButton.setBackgroundResource(R.drawable.ib_detailed_button);
                        mFollowButton.setText("关注");
                        ToastZong.ShowToast(MyApplication.getContext(),"取消关注");
                        BroadcastRec.sendReceiver(MyApplication.getContext(),"trueFollow",2,null);
                        mFollowNum.setText(MyDateClass.sendMath(Integer.valueOf(mFollowNum.getText().toString()) - 1));
                    }
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.follow_button:
                ibFollow.sendmyFollow(ibname,followHanlder);
                break;
        }
    }
}
