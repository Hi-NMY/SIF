package com.example.sif;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sif.Lei.MyBroadcastReceiver.BroadcastRec;
import com.example.sif.Lei.ShiPeiQi.NoticeAdapter;
import com.example.sif.Lei.ShowActivityBar.FragmentActivityBar;
import com.example.sif.NeiBuLei.NoticeClass;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.litepal.LitePal;

import java.util.List;

public class MessageNotice extends BaseActivity implements View.OnClickListener {

    private FragmentActivityBar fragmentActivityBar;
    private View mMessagenoticeBar;
    private RelativeLayout mNotiveR;
    private RecyclerView mMessageNoticeList;
    private LinearLayoutManager linearLayoutManager;
    private List<NoticeClass> noticeClasses;
    private TextView mNullNotice;
    private int key;
    private SmartRefreshLayout mNoticeFreshLt;
    private View mViewNoticeOfficial;
    private static boolean officialKey = false;
    private RelativeLayout mOfficialnotice;
    private NewOfficialNotice newOfficialNotice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_notice);
        initView();

        newOfficialNotice = new NewOfficialNotice();
        BroadcastRec.obtainRecriver(this,"newNotice",newOfficialNotice);

        key = getIntent().getIntExtra("key", 1);

        officialKey = getIntent().getBooleanExtra("ofkey",false);

        ZTL2();

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.notive_r);
        setPadding(this, relativeLayout);
        showActivityBar();
        writeNotice();
        nowOfficialNotice();
        noticeRefresh();

    }

    private void showActivityBar() {
        fragmentActivityBar = (FragmentActivityBar) getSupportFragmentManager().findFragmentById(R.id.messagenotice_bar);
        mMessagenoticeBar.bringToFront();
        ShowActivityBars showActivityBars = new ShowActivityBars(this, fragmentActivityBar);
        showActivityBars.showKongJian(true, false, true, false, true, false, false);
        showActivityBars.showUI(R.drawable.zuo_black, (String) null, "最近通知", null, null, 0, 0);
        showActivityBars.uiFunction(1, 0, 0, 0, 0, 0, 0);
        showActivityBars.barBackground1(R.color.beijing);
    }

    private void initView() {
        mMessagenoticeBar = (View) findViewById(R.id.messagenotice_bar);
        mNotiveR = (RelativeLayout) findViewById(R.id.notive_r);
        mMessageNoticeList = (RecyclerView) findViewById(R.id.message_notice_list);
        mNullNotice = (TextView) findViewById(R.id.null_notice);
        mNoticeFreshLt = (SmartRefreshLayout) findViewById(R.id.notice_fresh_lt);
        mViewNoticeOfficial = (View) findViewById(R.id.view_notice_official);
        mOfficialnotice = (RelativeLayout) findViewById(R.id.officialnotice);
        mOfficialnotice.setOnClickListener(this);
    }

    private NoticeAdapter noticeAdapter;

    public void writeNotice() {
        linearLayoutManager = new LinearLayoutManager(this);
        mMessageNoticeList.setLayoutManager(linearLayoutManager);
        noticeClasses = LitePal.order("id desc").limit(30).find(NoticeClass.class);
        if (noticeClasses != null && noticeClasses.size() > 0) {
            mNullNotice.setVisibility(View.INVISIBLE);
            mNoticeFreshLt.setVisibility(View.VISIBLE);
            noticeAdapter = new NoticeAdapter(this, noticeClasses);
            noticeAdapter.myXuehao(getMyXueHao(), getMyUserName());
            mMessageNoticeList.setAdapter(noticeAdapter);
            if (noticeClasses.size() >= 30) {
                mNoticeFreshLt.setEnableLoadMore(true);
            }
        }

        if (key == 0) {
            mNoticeFreshLt.autoLoadMore(100, 100, 1, false);
        }
        mNoticeFreshLt.finishRefresh();
    }

    public void addMoreNotice() {
        noticeClasses = LitePal.where("id <= ?", String.valueOf(noticeAdapter.noticeId)).order("id desc").limit(30).find(NoticeClass.class);
        noticeAdapter.addNoticebottom(noticeClasses);
        mNoticeFreshLt.finishLoadMore();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            noticeAdapter.inspectNotice(officialKey);
        }catch (Exception e){
            BroadcastRec.sendReceiver(this,"newNotice",1,"");
        }
    }

    private void noticeRefresh() {
        mNoticeFreshLt.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                writeNotice();
            }
        });

        mNoticeFreshLt.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                addMoreNotice();
            }
        });
    }

    private void nowOfficialNotice() {
        if (officialKey) {
            mViewNoticeOfficial.setVisibility(View.VISIBLE);
        } else {
            mViewNoticeOfficial.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.officialnotice:
                Intent intent = new Intent(this,OfficialNotice.class);
                startActivity(intent);
                officialKey = false;
                nowOfficialNotice();
                break;
        }
    }

    class NewOfficialNotice extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getIntExtra("textone", 0) == 3) {
                officialKey = true;
                nowOfficialNotice();
            }
        }
    }
}
