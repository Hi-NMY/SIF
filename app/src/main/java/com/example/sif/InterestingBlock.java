package com.example.sif;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.example.sif.ui.dashboard.FragmentViewPageAdapter;
import com.example.sif.ui.ib_viewpager.FragmentIb;
import com.example.sif.ui.ib_viewpager.FragmentIbMe;

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

public class InterestingBlock extends BaseActivity implements View.OnClickListener {

    private ImageView mIbFanHui;
    private MagicIndicator mIbIndicator;
    private RelativeLayout mIbR;
    private ViewPager mIbViewpager;

    private FragmentManager fragmentManager;
    private FragmentViewPageAdapter fragmentViewPageAdapter;
    private ArrayList<Fragment> fragments;
    private static final String[] title = new String[]{"全部", "我关注的"};
    private List<String> myTitleList = Arrays.asList(title);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interesting_block);

        ZTL();

        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.ib_RZong);
        setPadding(this,relativeLayout);

        initView();

        intFollowViewPager();

        highViewPager();
    }

    private void intFollowViewPager() {
        fragments = new ArrayList<>();
        fragments.add(new FragmentIb());
        fragments.add(new FragmentIbMe());

        fragmentManager = getSupportFragmentManager();

        fragmentViewPageAdapter = new FragmentViewPageAdapter(fragmentManager, fragments, 2);

        mIbViewpager.setAdapter(fragmentViewPageAdapter);
    }

    private void highViewPager() {
        CommonNavigator commonNavigator = new CommonNavigator(this);
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
                simplePagerTitleView.setNormalColor(Color.parseColor("#d3d3d3"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#363636"));
                simplePagerTitleView.setText(myTitleList.get(index));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mIbViewpager.setCurrentItem(index);
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

        mIbIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mIbIndicator, mIbViewpager);

    }

    public Fragment getFragment(){
        return fragmentViewPageAdapter.getThisFragment();
    }

    private void initView() {
        mIbFanHui = (ImageView) findViewById(R.id.ib_FanHui);
        mIbIndicator = (MagicIndicator) findViewById(R.id.ib_indicator);
        mIbR = (RelativeLayout) findViewById(R.id.ib_R);
        mIbViewpager = (ViewPager) findViewById(R.id.ib_viewpager);

        mIbFanHui.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_FanHui:
                this.finish();
                break;
        }
    }
}
