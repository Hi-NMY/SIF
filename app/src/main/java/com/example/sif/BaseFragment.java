package com.example.sif;

import android.app.Activity;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.sif.Lei.ShowActivityBar.FragmentActivityBar;
import com.example.sif.ui.dashboard.FragmentViewPageAdapter;
import com.example.sif.ui.dashboard2.FragmentGuanZhu;
import com.example.sif.ui.dashboard2.FragmentGuangChang;

import java.util.ArrayList;

public class BaseFragment extends Fragment {

    public void biaoTi(FragmentActivityBar fragmentActivityBar, Activity activity,String path,int size){
        TextView textView = (TextView) fragmentActivityBar.getView().findViewById(R.id.activitybar_title);
        TextView textView1 = (TextView) fragmentActivityBar.getView().findViewById(R.id.activitybar_title1);
        TextView textView2 = (TextView) fragmentActivityBar.getView().findViewById(R.id.activitybar_title2);
        Typeface SIF = Typeface.createFromAsset(activity.getAssets(), path);
        textView.setTypeface(SIF);
        textView.setTextSize(size);
        textView1.setTypeface(SIF);
        textView1.setTextSize(size);
        textView2.setTypeface(SIF);
        textView2.setTextSize(size);

    }

    private static ViewPager viewPager;
    private ArrayList<Fragment> fragments;
    private FragmentPagerAdapter fragmentPagerAdapter;
    private FragmentActivityBar fragmentActivityBar;
    private FragmentManager fragmentManager;
    private Activity activity;

    public boolean selectonFragment(ArrayList<Fragment> f,FragmentManager fm,ViewPager v,Activity a,View circle,View circle1,TextView mActivitybarTitle,TextView mActivitybarTitle1){
        this.fragments = f;
        this.viewPager = v;
        this.activity = a;
        this.fragmentManager = fm;

        fragments = new ArrayList<Fragment>();
        fragments.add(new FragmentGuangChang());
        fragments.add(new FragmentGuanZhu());

        fragmentPagerAdapter = new FragmentViewPageAdapter(fragmentManager,fragments,2);
        viewPager.setAdapter(fragmentPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tishiDian(position,circle,circle1,mActivitybarTitle,mActivitybarTitle1);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return tishi;
    }

    public boolean tishi = false;
    public void tishiDian(int i,View view, View view1,TextView mActivitybarTitle,TextView mActivitybarTitle1) {
        switch (i) {
            case 0:
                if (view != null && view1 != null) {
                    view.setVisibility(View.VISIBLE);
                    view1.setVisibility(View.INVISIBLE);
                    tishiFond(0,mActivitybarTitle,mActivitybarTitle1);
                    tishi = false;
                }
                break;
            case 1:
                view.setVisibility(View.INVISIBLE);
                view1.setVisibility(View.VISIBLE);
                tishiFond(1,mActivitybarTitle,mActivitybarTitle1);
                tishi = true;
                break;
        }
    }
    public void tishiDian(int i,View view, View view1) {
        switch (i) {
            case 0:
                if (view != null && view1 != null) {
                    view.setVisibility(View.VISIBLE);
                    view1.setVisibility(View.INVISIBLE);
                    tishi = false;
                }
                break;
            case 1:
                view.setVisibility(View.INVISIBLE);
                view1.setVisibility(View.VISIBLE);
                tishi = true;
                break;
        }
    }

    public void tishiDianclick(int i,View view, View view1) {
        switch (i) {
            case 0:
                viewPager.setCurrentItem(0);
                view.setVisibility(View.VISIBLE);
                view1.setVisibility(View.INVISIBLE);
                tishi = false;
                break;
            case 1:
                viewPager.setCurrentItem(1);
                view.setVisibility(View.INVISIBLE);
                view1.setVisibility(View.VISIBLE);
                tishi = true;
                break;
        }
    }

    public void tishiFond(int i,TextView mActivitybarTitle,TextView mActivitybarTitle1){
        TextPaint textPaint = mActivitybarTitle.getPaint();
        TextPaint textPaint1 = mActivitybarTitle1.getPaint();
        switch (i){
            case 0:
                textPaint.setFakeBoldText(true);
                textPaint1.setFakeBoldText(false);
                break;
            case 1:
                textPaint.setFakeBoldText(false);
                textPaint1.setFakeBoldText(true);
                break;
        }
        mActivitybarTitle.postInvalidate();
        mActivitybarTitle1.postInvalidate();
    }
}
