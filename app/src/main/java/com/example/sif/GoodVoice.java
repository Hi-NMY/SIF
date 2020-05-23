package com.example.sif;

import android.Manifest;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sif.Lei.MyToolClass.CenterLayoutManager;
import com.example.sif.Lei.MyToolClass.ShowDiaLog;
import com.example.sif.Lei.MyToolClass.ToastZong;
import com.example.sif.Lei.MyVoice.SendVoice;
import com.example.sif.Lei.MyVoice.VoiceObtain;
import com.example.sif.Lei.NiceImageView.CircleImageView;
import com.example.sif.Lei.NiceImageView.CircleProgressBar;
import com.example.sif.Lei.ShiPeiQi.GoodVoiceAdapter;
import com.example.sif.NeiBuLei.NiceVoice;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.Pivot;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.io.IOException;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class GoodVoice extends BaseActivity implements View.OnClickListener {

    private ImageView mGvReturn;
    private DiscreteScrollView mGvList;
    private float mPosX, mPosY, mCurPosX, mCurPosY;
    private int index = 0;
    private RelativeLayout mRzong;
    private ImageView gv_return;
    private ImageView voice_send;
    private TextView mFaText;
    private SendVoice sendVoice;
    private CenterLayoutManager centerLayoutManager;
    private GoodVoiceAdapter goodVoiceAdapter;
    private SmartRefreshLayout mGvFreshLt;
    private NiceVoice newMyVoice;
    private LinearLayoutManager nullManager;

    private Handler newVoiceHanlder = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mGvFreshLt.finishRefresh();
            mGvFreshLt.finishLoadMore();
            writhList(sendVoice.niceVoices);
        }
    };

    private Handler moreVoiceHanlder = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mGvFreshLt.finishRefresh();
            mGvFreshLt.finishLoadMore();
            goodVoiceAdapter.addMoreVoiceList(sendVoice.niceVoices);
        }
    };

    private Handler newVoiceSendHnalder = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                String[] s = msg.obj.toString().split("&");
                newMyVoice = new NiceVoice();
                newMyVoice.setGv_username(s[0]);
                newMyVoice.setGv_usersex(s[1]);
                newMyVoice.setGv_label(s[2]);
                newMyVoice.setGv_voice(s[3]);
                newMyVoice.setGv_userxuehao(s[4]);
                goodVoiceAdapter.select = 0;
                index = 0;
                showDiaLog.closeMyDiaLog();
                goodVoiceAdapter.addNewVociceMy(newMyVoice);
                mGvList.setAdapter(goodVoiceAdapter);
            }catch (Exception e){
                ToastZong.ShowToast(MyApplication.getContext(),"阿欧，出错了，请重试");
            }
            closeDiaLog();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_voice);
        initView();

        ZTL();


        if (sendVoice == null) {
            sendVoice = new SendVoice(newVoiceHanlder,mGvFreshLt);
        }
        sendVoice.obtainListVoice(1, 0);

        //增加padding  避免状态栏遮挡
        setPadding(this, mRzong);

        refreshListener();

    }

    private void refreshListener() {
        mGvFreshLt.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                goodVoiceAdapter.player.release();
                sendVoice.sendHandler(newVoiceHanlder);
                sendVoice.obtainListVoice(1, 0);
            }
        });

        mGvFreshLt.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                goodVoiceAdapter.player.release();
                sendVoice.sendHandler(moreVoiceHanlder);
                sendVoice.obtainListVoice(2, goodVoiceAdapter.voiceId);
            }
        });
    }

    private void initView() {
        mGvReturn = (ImageView) findViewById(R.id.gv_return);
        mGvList = (DiscreteScrollView) findViewById(R.id.gv_list);
        mRzong = (RelativeLayout) findViewById(R.id.Rzong);
        gv_return = (ImageView) findViewById(R.id.gv_return);
        gv_return.setOnClickListener(this);
        voice_send = (ImageView) findViewById(R.id.voice_send);
        voice_send.setOnClickListener(this);
        mFaText = (TextView) findViewById(R.id.fa_text);

        gv_return.bringToFront();
        voice_send.bringToFront();
        mFaText.bringToFront();
        mGvList.bringToFront();

        mGvFreshLt = (SmartRefreshLayout) findViewById(R.id.gv_fresh_lt);
    }

    private void writhList(List<NiceVoice> niceVoices) {
//        centerLayoutManager = new CenterLayoutManager(this);
//        mGvList.setLayoutManager(centerLayoutManager);
        if (goodVoiceAdapter != null){
            goodVoiceAdapter = null;
        }
        goodVoiceAdapter = new GoodVoiceAdapter(niceVoices, mGvList, centerLayoutManager, this);
        goodVoiceAdapter.sendXueHao(getMyXueHao());
        mGvList.setAdapter(goodVoiceAdapter);
        listTransmate(goodVoiceAdapter);
    }

    private void listTransmate(GoodVoiceAdapter goodVoiceAdapter) {
        Animation animation3 = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f );
        animation3.setRepeatCount(Animation.INFINITE);
        animation3.setInterpolator(new LinearInterpolator());
        animation3.setDuration(10000);
        mGvList.addScrollStateChangeListener(new DiscreteScrollView.ScrollStateChangeListener<RecyclerView.ViewHolder>() {
            @Override
            public void onScrollStart(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                viewHolder.itemView.findViewById(R.id.voice_image).clearAnimation();
                viewHolder.itemView.setAlpha(0.5f);
                viewHolder.itemView.findViewById(R.id.gv_start_stop).setBackgroundResource(R.drawable.voicestart);
            }

            @Override
            public void onScrollEnd(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                viewHolder.itemView.findViewById(R.id.voice_image).startAnimation(animation3);
                viewHolder.itemView.setAlpha(1);
            }

            @Override
            public void onScroll(float v, int i, int i1, @Nullable RecyclerView.ViewHolder viewHolder, @Nullable RecyclerView.ViewHolder t1) {

            }
        });

        mGvList.addOnItemChangedListener(new DiscreteScrollView.OnItemChangedListener<RecyclerView.ViewHolder>() {
            @Override
            public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int i) {
             //  viewHolder.itemView.setAlpha(1);
               viewHolder.itemView.findViewById(R.id.voice_image).setAnimation(animation3);
               goodVoiceAdapter.buiKey = true;
               goodVoiceAdapter.startVoice(i, viewHolder.itemView.findViewById(R.id.voice_image),viewHolder.itemView.findViewById(R.id.gv_start_stop));
            }
        });

        mGvList.setItemTransformer(new ScaleTransformer.Builder().setMaxScale(1.0f).setMinScale(0.8f).setPivotX(Pivot.X.CENTER)
                .setPivotY(Pivot.Y.CENTER).build());
    }

    private ShowDiaLog showDiaLog;
    private CircleProgressBar circleProgressBar;
    private CircleImageView circleImageView;
    private CircleImageView circleImageView1;
    private ImageView close;
    private Button mLabel1;
    private Button mLabel2;
    private Button mLabel3;
    private Button mLabel4;
    private TextView mClickText;
    private GifImageView gifImageView;
    private ImageView gvStop;
    private ImageView deleteGv;
    private TextView gvTime;
    private Button sendgv;
    private TextView time;
    private LinearLayout linearLayout;
    private float totalProgress = 100;
    private float currentProgress;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gv_return:
                finish();
                break;
            case R.id.voice_send:
                View view = LayoutInflater.from(this).inflate(R.layout.send_goodvoice, null);
                showDiaLog = new ShowDiaLog(this, R.style.AlertDialog_qr, view);
                initDialogView(view);
                showDiaLog.Cancelable(false);
                showDiaLog.bottomrView();
                showDiaLog.logWindow(new ColorDrawable(Color.TRANSPARENT));
                showDiaLog.showMyDiaLog();
                if (goodVoiceAdapter != null){
                    goodVoiceAdapter.stopVoice();
                }
                a = 0;
                viewClick();
                checkVoice(showDiaLog, Manifest.permission.RECORD_AUDIO);
                break;
        }
    }

    private String labelName;

    private void labelTransmate(Button label) {
        label.setAlpha(1);
        for (int i = 0; i < labels.length; i++) {
            if (labels[i] != label) {
                if (labels[i].getAlpha() > 0.5) {
                    labels[i].setAlpha(0.4f);
                }
            } else {
                labelName = labels[i].getText().toString();
            }
        }
    }

    private Button[] labels;

    private void initDialogView(View view) {
        labels = new Button[4];
        mLabel1 = (Button) view.findViewById(R.id.label1);
        mLabel2 = (Button) view.findViewById(R.id.label2);
        mLabel3 = (Button) view.findViewById(R.id.label3);
        mLabel4 = (Button) view.findViewById(R.id.label4);
        labels[0] = mLabel1;
        labels[1] = mLabel2;
        labels[2] = mLabel3;
        labels[3] = mLabel4;
        circleProgressBar = (CircleProgressBar) view.findViewById(R.id.circleProgressbar);
        circleImageView = (CircleImageView) view.findViewById(R.id.circle_a);
        circleImageView1 = (CircleImageView) view.findViewById(R.id.circle_b);
        close = (ImageView) view.findViewById(R.id.gv_send_close);
        mClickText = (TextView) view.findViewById(R.id.click_text);
        gifImageView = (GifImageView) view.findViewById(R.id.gv_go);
        gvStop = (ImageView) view.findViewById(R.id.gv_stop);
        deleteGv = (ImageView) view.findViewById(R.id.gv_delete);
        gvTime = (TextView) view.findViewById(R.id.gv_time);
        sendgv = (Button) view.findViewById(R.id.send_voice);
        linearLayout = (LinearLayout) view.findViewById(R.id.time_llt);
        time = (TextView) view.findViewById(R.id.gv_time_math);

        labelClick();
    }

    private void labelClick() {
        for (int i = 0; i < labels.length; i++) {
            int finalI = i;
            labels[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    labelTransmate(labels[finalI]);
                }
            });
        }
    }

    private int a = 0;
    private ProgressRunable runable;
    private Animation animation;
    private Animation animation1;
    private Animation animation2;
    private String b;
    private String timeZong;
    private MediaPlayer player;

    private void viewClick() {
        animation = new ScaleAnimation(1.0f, 0.4f, 1.0f, 0.4f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        );
        animation.setDuration(70);
        animation.setFillAfter(true);

        animation1 = new ScaleAnimation(0.4f, 1.0f, 0.4f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        );
        animation1.setDuration(70);
        animation1.setFillAfter(true);

        animation2 = new ScaleAnimation(1.0f, 1.1f, 1.0f, 1.1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        );
        animation2.setRepeatCount(Animation.INFINITE);
        animation2.setInterpolator(new LinearInterpolator());
        animation2.setRepeatMode(Animation.REVERSE);
        animation2.setDuration(300);

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (a) {
                    case 0:
                        boolean key = VoiceObtain.startVoice();
                        if (key){
                            linearLayout.setVisibility(View.VISIBLE);
                            totalProgress = 100;
                            runable = new ProgressRunable();
                            runable.start();
                            mClickText.setText("录音中");
                            circleImageView.startAnimation(animation);
                            circleImageView1.startAnimation(animation2);
                            circleProgressBar.setVisibility(View.VISIBLE);
                            a = 1;
                        }
                        break;
                    case 1:
                        b = VoiceObtain.stopVoice();
                        timeZong = time.getText().toString();
                        circleImageView.startAnimation(animation1);
                        circleImageView1.clearAnimation();
                        mClickText.setText("点击播放");
                        totalProgress = currentProgress;
                        circleProgressBar.setVisibility(View.INVISIBLE);
                        animation1.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                gvStop.setVisibility(View.VISIBLE);
                                deleteGv.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        a = 2;
                        break;
                    case 2:
                        player = new MediaPlayer();
                        try {
                            player.setDataSource(b);
                            player.prepare();
                            player.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        DownTime downTime = new DownTime();
                        downTime.start();
                        time.setText(timeZong);
                        gvStop.setVisibility(View.GONE);
                        deleteGv.setVisibility(View.INVISIBLE);
                        gifImageView.setVisibility(View.VISIBLE);
                        mClickText.setText("正在播放");
                        a = 3;
                        break;
                    case 3:
                        a = 2;
                        time.setText(timeZong);
                        mClickText.setText("点击播放");
                        deleteGv.setVisibility(View.VISIBLE);
                        gvStop.setVisibility(View.VISIBLE);
                        gifImageView.setVisibility(View.GONE);
                        break;
                }
            }
        });

        showDiaLog.dismissListener(new ShowDiaLog.Dismiss() {
            @Override
            public void dismiss() {
                a = 0;
                currentProgress = 0;
                totalProgress = currentProgress;
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player != null){
                    player.release();
                }
                showDiaLog.closeMyDiaLog();

            }
        });

        deleteGv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gvStop.setVisibility(View.GONE);
                gifImageView.setVisibility(View.GONE);
                deleteGv.setVisibility(View.GONE);
                linearLayout.setVisibility(View.INVISIBLE);
                time.setText("0");
                circleProgressBar.setProgress(0);
                mClickText.setText("点击录音");
                a = 0;
            }
        });

        sendgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (labelName != null && b != null) {
                    if (player != null){
                        player.release();
                    }
                    showDiaLog(GoodVoice.this,R.drawable.loading);
                    cancelable(false);
                    sendVoice.sendHandler(newVoiceSendHnalder);
                    sendVoice.sendMyVoice(b, getMyUserName(), getMySex(), labelName, getMyXueHao(), new SendVoice.LongTime() {
                        @Override
                        public void LongTimeRight() {
                            closeDiaLog();
                        }
                    });
                } else {
                    ToastZong.ShowToast(MyApplication.getContext(), "请选择标签或录制音频");
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (goodVoiceAdapter != null){
            goodVoiceAdapter.stopVoice();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (goodVoiceAdapter != null){
            goodVoiceAdapter.pauseVoice();
        }
    }


    class ProgressRunable extends Thread {
        @Override
        public void run() {
            currentProgress = 0;
            while (currentProgress < totalProgress) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (currentProgress < totalProgress) {
                    currentProgress += 2.0;
                    circleProgressBar.setProgress(currentProgress);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            time.setText(String.valueOf(Integer.valueOf(time.getText().toString()) + 1));
                        }
                    });
                }
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (currentProgress == 100) {
                        a = 2;
                        mClickText.setText("点击播放");
                        b = VoiceObtain.stopVoice();
                        timeZong = time.getText().toString();
                        gvStop.setVisibility(View.VISIBLE);
                        deleteGv.setVisibility(View.VISIBLE);
                        circleImageView.startAnimation(animation1);
                        circleImageView1.clearAnimation();
                        circleProgressBar.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }
    }

    class DownTime extends Thread {
        @Override
        public void run() {
            super.run();
            while (true) {
                if (a == 2 || Integer.valueOf(time.getText().toString()) == 0) {
                    break;
                } else {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (a == 3) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (Integer.valueOf(time.getText().toString()) != 0) {
                                    time.setText(String.valueOf(Integer.valueOf(time.getText().toString()) - 1));
                                    time.postInvalidate();
                                } else {
                                    if (Integer.valueOf(time.getText().toString()) == 0) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                time.setText(timeZong);
                                                gvStop.setVisibility(View.VISIBLE);
                                                gifImageView.setVisibility(View.INVISIBLE);
                                                deleteGv.setVisibility(View.VISIBLE);
                                                mClickText.setText("点击播放");
                                                a = 2;
                                            }
                                        });
                                    }
                                }
                            }
                        });
                    }
                }
            }
        }
    }
}
