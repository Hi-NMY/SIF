package com.example.sif.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.example.sif.BaseFragment;
import com.example.sif.GoodVoice;
import com.example.sif.InterestingBlock;
import com.example.sif.Lei.MyToolClass.ShowDiaLog;
import com.example.sif.Lei.ShowActivityBar.FragmentActivityBar;
import com.example.sif.MyApplication;
import com.example.sif.MyZhuYe;
import com.example.sif.R;
import com.example.sif.SchoolEducationalAdministration;
import com.example.sif.SchoolShop;
import com.example.sif.ui.dashboard.FragmentViewPageAdapter;
import com.example.sif.ui.home2.FragmentFollowMe;
import com.example.sif.ui.home2.FragmentMyFollow;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends BaseFragment implements View.OnClickListener{

    private HomeViewModel homeViewModel;
    private static MyZhuYe myZhuYe;
    private static FragmentActivityBar fragmentActivityBar;
    private CollapsingToolbarLayout mHomeCollapsinglayout;
    private MagicIndicator mHomeMagicIndicator;
    private AppBarLayout mHomeAppbarlayout;
    private ViewPager mHomeViewpager;
    private NestedScrollView mHomeScrollview;
    private CoordinatorLayout mHomeCoorlayout;
    private RelativeLayout homeR2;
    private LinearLayout funBottonllt;

    private FragmentManager fragmentManager;
    private FragmentViewPageAdapter fragmentViewPageAdapter;
    private ArrayList<Fragment> fragments;

    private static final String[] title = new String[]{"我的关注", "关注我的"};
    private List<String> myTitleList = Arrays.asList(title);
    private ImageButton mButton1;
    private ImageButton mButton2;
    private ImageButton mButton3;
    private ImageButton mButton4;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        myZhuYe = (MyZhuYe) getActivity();
        initView(root);

        intFollowViewPager();

        highViewPager();

        listenViewMove();

        return root;
    }

    private void listenViewMove() {
        mHomeAppbarlayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (i != 0) {
                    float topA = 1 - (1 - i) / 250f;
                    funBottonllt.setAlpha(topA);
                }
            }
        });

    }

    private void highViewPager() {
        CommonNavigator commonNavigator = new CommonNavigator(myZhuYe);
        commonNavigator.setScrollPivotX(0.65f);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return myTitleList == null ? 0 : myTitleList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, int index) {
                SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);
                simplePagerTitleView.setTextSize(16);
                simplePagerTitleView.setNormalColor(Color.parseColor("#808080"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#3eede7"));
                simplePagerTitleView.setText(myTitleList.get(index));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mHomeViewpager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setLineHeight(UIUtil.dip2px(context, 6));
                indicator.setLineWidth(UIUtil.dip2px(context, 10));
                indicator.setRoundRadius(UIUtil.dip2px(context, 3));
                indicator.setStartInterpolator(new AccelerateInterpolator());
                indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
                indicator.setColors(Color.parseColor("#3eede7"));
                return indicator;
            }
        });

        mHomeMagicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mHomeMagicIndicator, mHomeViewpager);

    }

    private void intFollowViewPager() {
        fragments = new ArrayList<>();
        fragments.add(new FragmentMyFollow());
        fragments.add(new FragmentFollowMe());

        fragmentManager = getChildFragmentManager();

        fragmentViewPageAdapter = new FragmentViewPageAdapter(fragmentManager, fragments, 2);

        mHomeViewpager.setAdapter(fragmentViewPageAdapter);
    }

    private void initView(View root) {
        mHomeMagicIndicator = (MagicIndicator) root.findViewById(R.id.home_magic_indicator);
        mHomeViewpager = (ViewPager) root.findViewById(R.id.home_viewpager);
        mHomeScrollview = (NestedScrollView) root.findViewById(R.id.home_scrollview);
        homeR2 = (RelativeLayout) root.findViewById(R.id.home_R2);
        mHomeAppbarlayout = (AppBarLayout) root.findViewById(R.id.home_appbarlayout);
        funBottonllt = (LinearLayout) root.findViewById(R.id.fun_button_llt);
        mButton1 = (ImageButton) root.findViewById(R.id.button1);
        mButton2 = (ImageButton) root.findViewById(R.id.button2);
        mButton3 = (ImageButton) root.findViewById(R.id.button3);
        mButton4 = (ImageButton) root.findViewById(R.id.button4);
        mButton1.setOnClickListener(this);
        mButton2.setOnClickListener(this);
        mButton3.setOnClickListener(this);
        mButton4.setOnClickListener(this);

        mButton1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                myOnTouch(event,mButton1);
                return false;
            }
        });

        mButton2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                myOnTouch(event,mButton2);
                return false;
            }
        });

        mButton3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                myOnTouch(event,mButton3);
                return false;
            }
        });

        mButton4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                myOnTouch(event,mButton4);
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button1:
                Intent intent = new Intent(MyApplication.getContext(), SchoolEducationalAdministration.class);
                myZhuYe.startActivity(intent);
                break;
            case R.id.button2:
                View view = LayoutInflater.from(myZhuYe).inflate(R.layout.goodvioce_tips, null);
                ShowDiaLog showDiaLog = new ShowDiaLog(myZhuYe, R.style.AlertDialog_qr, view);
                showDiaLog.logWindow(new ColorDrawable(Color.TRANSPARENT));
                showDiaLog.showMyDiaLog();
                Button button = (Button)view.findViewById(R.id.tips_button);
                ImageView imageView = (ImageView)view.findViewById(R.id.close_tips);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDiaLog.closeMyDiaLog();
                        Intent intent2 = new Intent(MyApplication.getContext(), GoodVoice.class);
                        myZhuYe.startActivity(intent2);
                    }
                });
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDiaLog.closeMyDiaLog();
                    }
                });

                break;
            case R.id.button3:
                myZhuYe.sendIb(1);
                Intent intent1 = new Intent(MyApplication.getContext(), InterestingBlock.class);
                myZhuYe.startActivity(intent1);
                break;
            case R.id.button4:
                View view1 = LayoutInflater.from(myZhuYe).inflate(R.layout.schoolshop_tips, null);
                ShowDiaLog showDiaLog1 = new ShowDiaLog(myZhuYe, R.style.AlertDialog_qr, view1);
                showDiaLog1.logWindow(new ColorDrawable(Color.TRANSPARENT));
                showDiaLog1.showMyDiaLog();
                Button button1 = (Button)view1.findViewById(R.id.tips_button);
                ImageView imageView1 = (ImageView)view1.findViewById(R.id.close_tips);
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDiaLog1.closeMyDiaLog();
                        Intent intent2 = new Intent(MyApplication.getContext(), SchoolShop.class);
                        myZhuYe.startActivity(intent2);
                    }
                });
                imageView1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDiaLog1.closeMyDiaLog();
                    }
                });
                break;
        }
    }

    public void myOnTouch(MotionEvent event,ImageButton i) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            viewScaleTransmation(1,i);
        }else {
            viewScaleTransmation(2,i);
        }
    }

    private void viewScaleTransmation(int i,ImageButton mButton){
        ScaleAnimation scaleAnimation = null;
        if (i == 1){
            scaleAnimation = new ScaleAnimation(1.0f,0.95f,1.0f,0.95f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
            scaleAnimation.setDuration(90);
            scaleAnimation.setFillAfter(true);
        }
        if (i == 2){
            scaleAnimation = new ScaleAnimation(0.95f,1.0f,0.95f,1.0f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
            scaleAnimation.setDuration(70);
            scaleAnimation.setFillAfter(true);
        }
        mButton.startAnimation(scaleAnimation);
    }
}
