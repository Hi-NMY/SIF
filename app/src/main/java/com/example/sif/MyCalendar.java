package com.example.sif;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.MediaStoreSignature;
import com.example.sif.Lei.MyBroadcastReceiver.BroadcastRec;
import com.example.sif.Lei.MyToolClass.ObtainServerTime;
import com.example.sif.Lei.MyToolClass.ObtainUserSign;
import com.example.sif.Lei.NiceImageView.CircleImageView;
import com.example.sif.Lei.ShowActivityBar.FragmentActivityBar;
import com.example.sif.ui.sign.SignInFragment;

public class MyCalendar extends BaseActivity implements View.OnClickListener {

    private CircleImageView mSignUserimage;
    private TextView mSignUsername;
    private View mCalendarBar;
    private SignInFragment signInFragment;
    private StartAnimation startAnimation;
    private RelativeLayout mSignR;
    private static Animation animation;
    static {
        setAnimation();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_calendar);
        initView();
        startAnimation = new StartAnimation();
        BroadcastRec.obtainRecriver(this, "startAnimation", startAnimation);
        ObtainServerTime.obtainTime();
        ObtainUserSign.obtainSign(getMyXueHao());

        //设置状态栏
        ZTL();
        //ActivityBar
        showActivityBar();

        //增加padding  避免状态栏遮挡
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.calendar_r);
        setPadding(this, relativeLayout);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void showActivityBar() {
        FragmentActivityBar fragmentActivityBar = (FragmentActivityBar) getSupportFragmentManager().findFragmentById(R.id.calendar_bar);
        mCalendarBar.bringToFront();
        ShowActivityBars showActivityBars = new ShowActivityBars(this, fragmentActivityBar);
        showActivityBars.showKongJian(true, false, true, false, false, false, false);
        showActivityBars.showUI(R.drawable.zuo_white, null, "每日任务", null, 0, 0);
        showActivityBars.uiFunction(1, 0, 0, 0, 0, 0, 0);
        showActivityBars.barBackground1(Color.TRANSPARENT);
        showActivityBars.fontColor1(getColor(R.color.beijing));
    }

    private void initView() {
        signInFragment = (SignInFragment) getSupportFragmentManager().findFragmentById(R.id.sign_signin_frag);
        mSignUserimage = (CircleImageView) findViewById(R.id.sign_userimage);
        mSignUserimage.setOnClickListener(this);
        mSignUsername = (TextView) findViewById(R.id.sign_username);
        mSignUsername.setOnClickListener(this);
        SharedPreferences sharedPreferences = getSharedPreferences("userImageHeadDate", MODE_PRIVATE);
        userImageHeadDate = sharedPreferences.getString("userImageHeadDate", String.valueOf(System.currentTimeMillis()));
        Glide.with(MyApplication.getContext())
                .load("http://nmy1206.natapp1.cc/UserImageServer/" + getMyXueHao() + "/HeadImage/myHeadImage.png")
                .signature(new MediaStoreSignature(userImageHeadDate, 1, 1))
                .placeholder(R.drawable.nostartimage_three)
                .fallback(R.drawable.defaultheadimage)
                .error(R.drawable.defaultheadimage)
                .circleCrop()
                .into(mSignUserimage);

        mSignUsername.setText(getMyUserName());
        mCalendarBar = (View) findViewById(R.id.calendar_bar);
        mCalendarBar.setOnClickListener(this);
        mSignR = (RelativeLayout) findViewById(R.id.sign_r);
        mSignR.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_userimage:

                break;
        }
    }

    private static void setAnimation(){
        animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(600);
        animation.setFillAfter(true);
    }

    class StartAnimation extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            mSignR.setVisibility(View.VISIBLE);
            mSignR.startAnimation(animation);
        }
    }
}
