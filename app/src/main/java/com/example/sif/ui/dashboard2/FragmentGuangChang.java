package com.example.sif.ui.dashboard2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sif.BaseFragment;
import com.example.sif.Lei.MyBroadcastReceiver.BroadcastRec;
import com.example.sif.Lei.MyToolClass.MyDateClass;
import com.example.sif.Lei.MyToolClass.ToastZong;
import com.example.sif.Lei.ShiPeiQi.GuangChang;
import com.example.sif.Lei.ShiPeiQi.ViewItemJianJu;
import com.example.sif.MyApplication;
import com.example.sif.MyZhuYe;
import com.example.sif.NeiBuLei.GuangChangUserXinXi;
import com.example.sif.R;
import com.google.android.material.snackbar.Snackbar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.litepal.LitePal;

import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class FragmentGuangChang extends BaseFragment {

    private View view;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private MyZhuYe myZhuYe;
    private boolean Aa;
    private GuangChang guangChang;

    private List<GuangChangUserXinXi> guangChangUserXinXis;

    private GifImageView mGuangchangGif;
    private RelativeLayout mGuangchangRt;
    private SmartRefreshLayout mGuangchangFreshLt;

    private Handler handler = new Handler(){
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 0:
                        guangChangUserXinXis = LitePal.findAll(GuangChangUserXinXi.class);
                        guangChang = new GuangChang(myZhuYe,guangChangUserXinXis);
                        guangChang.myuserDynamicThumb(myZhuYe.sendUserThumb(1));
                        guangChang.sendUserFollow(myZhuYe.sendUserFollow(2,myZhuYe.getMyXueHao()));
                        guangChang.notifyDataSetChanged();
                        recyclerView.setAdapter(guangChang);
                        mGuangchangFreshLt.finishRefresh();
                        Snackbar snackbar = Snackbar.make(view,"已为您更新最新数据",Snackbar.LENGTH_SHORT);
                        Snackbar.SnackbarLayout snackbarView = (Snackbar.SnackbarLayout)snackbar.getView();
                        snackbarView.setBackgroundColor(Color.parseColor("#70f3ff"));
                        TextView textView = (TextView) snackbarView.findViewById(R.id.snackbar_text);//获取文本View实例
                        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        textView.setTextColor(Color.parseColor("#FBFBFB"));
                        snackbar.show();
                       break;
                    case 1:
                        ToastZong.ShowToast(myZhuYe,"刷新错误,请重试");
                        mGuangchangFreshLt.finishRefresh();
                        mGuangchangFreshLt.finishLoadMore();
                        break;
                    case 2:
                        ToastZong.ShowToast(myZhuYe,"连接超时");
                        mGuangchangFreshLt.finishRefresh();
                        mGuangchangFreshLt.finishLoadMore();
                        break;
                    case 3:
                        ToastZong.ShowToast(myZhuYe,"连接错误");
                        mGuangchangFreshLt.finishRefresh();
                        mGuangchangFreshLt.finishLoadMore();
                        break;
                    case 4:
                        guangChang.addGuangChangListbottom(myZhuYe.guangChangUserXinXi);
                        mGuangchangFreshLt.finishLoadMore();
                        break;
                }
            }
        };

    private IntentFilter intentFilter;
    private ShuaG shuaG;
    private LocalBroadcastManager localBroadcastManager;
    private DeleteDynamic deleteDynamic;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_zhuye_guangchang, container, false);
        initFragmentView();

        localBroadcastManager = LocalBroadcastManager.getInstance(MyApplication.getContext());
        intentFilter = new IntentFilter();
        intentFilter.addAction("shuaG");
        shuaG = new ShuaG();
        localBroadcastManager.registerReceiver(shuaG,intentFilter);

        deleteDynamic = new DeleteDynamic();
        BroadcastRec.obtainRecriver(MyApplication.getContext(),"deleteDynamic",deleteDynamic);

        myZhuYe = (MyZhuYe) getActivity();

        layoutManager = new LinearLayoutManager(myZhuYe);
        recyclerView.setLayoutManager(layoutManager);

        guangChangUserXinXis = LitePal.findAll(GuangChangUserXinXi.class);
        showList(true);
        xiaLaShuaXin(mGuangchangFreshLt);

        recyclerView.addItemDecoration(new ViewItemJianJu(30));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

    private String path1 = "http://nmy1206.natapp1.cc/shuaXinGuangChang.php";
    private void xiaLaShuaXin(SmartRefreshLayout mGuangchangFreshLt) {
        mGuangchangFreshLt.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                String nowDate = MyDateClass.showNowDate();
                if (nowDate!=null){
                    myZhuYe.shuaXinGuangChang(path1,handler,nowDate,1);
                }else {
                    ToastZong.ShowToast(myZhuYe,"刷新失败!");
                }
            }
        });
        mGuangchangFreshLt.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (guangChang.date != null){
                    myZhuYe.shuaXinGuangChang(path1,handler,guangChang.date,2);
                }else {
                    mGuangchangFreshLt.finishLoadMore();
                }
            }
        });
    }

    private void initFragmentView(){
        recyclerView = (RecyclerView) view.findViewById(R.id.guangchang_zhu);
        mGuangchangGif = (GifImageView)view.findViewById(R.id.guangchang_gif);
        mGuangchangRt = (RelativeLayout)view.findViewById(R.id.guangchang_Rt);
        mGuangchangFreshLt = (SmartRefreshLayout)view.findViewById(R.id.guangchang_fresh_lt);
    }

    public void showList(boolean Aa1) {
        if (Aa1) {
            if (guangChang == null) {
                guangChang = new GuangChang(myZhuYe,guangChangUserXinXis);
                guangChang.myuserDynamicThumb(myZhuYe.sendUserThumb(0));
                guangChang.sendUserFollow(myZhuYe.sendUserFollow(2,myZhuYe.getMyXueHao()));
            }
            guangChang.notifyDataSetChanged();
            recyclerView.setAdapter(guangChang);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(shuaG);
    }

    private void newMessage(){
        guangChang.addGuangChangList(newguangchang);
        recyclerView.setAdapter(guangChang);
    }

    private void firstDeleteDynamic(String id){
        guangChang.deleteDynamicMy(id);
    }

    private GuangChangUserXinXi newguangchang;
    class ShuaG extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String newString = intent.getStringExtra("NEW");
            String[] s = newString.split("&");
            newguangchang = new GuangChangUserXinXi();
            newguangchang.setGc_user_name(s[0]);
            newguangchang.setGc_user_shijian(s[1]);
            newguangchang.setGc_user_xinxi(s[2]);
            newguangchang.setGc_user_xuehao(s[3]);
            newguangchang.setGc_user_image_url(s[4]);
            newguangchang.setGc_user_dynamic_id(s[5]);
            newguangchang.setGc_user_dynamic_thumb(Integer.parseInt(s[6]));
            newguangchang.setGc_user_dynamic_comment(Integer.parseInt(s[7]));
            if (s.length > 8){
                newguangchang.setBlock(s[8]);
            }
            newMessage();
        }
    }

    class DeleteDynamic extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            int a = intent.getIntExtra("textone",-1);
            String id = intent.getStringExtra("texttwo");
            if (a == 0){
                firstDeleteDynamic(id);
            }
        }
    }
}
