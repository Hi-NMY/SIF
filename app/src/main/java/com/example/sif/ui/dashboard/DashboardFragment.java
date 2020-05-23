package com.example.sif.ui.dashboard;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.sif.BaseFragment;
import com.example.sif.Lei.ShowActivityBar.FragmentActivityBar;
import com.example.sif.MyZhuYe;
import com.example.sif.R;

import java.util.ArrayList;

public class DashboardFragment extends BaseFragment {

    private static int FONTSIZE = 16;
    private ViewPager viewPager;
    private ArrayList<Fragment> fragments;
    private FragmentPagerAdapter fragmentPagerAdapter;
    public boolean tishi;
    private View circle;
    private View circle1;
    private TextView mActivitybarTitle;
    private TextView mActivitybarTitle1;
    MyZhuYe myZhuYe;
    FragmentActivityBar fragmentActivityBar;

    public FragmentManager fragmentManager;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        viewPager = (ViewPager)root.findViewById(R.id.viewPage);

        MyZhuYe myZhuYe = (MyZhuYe)getActivity();

        fragmentManager = getChildFragmentManager();

        fragmentActivityBar = (FragmentActivityBar)getActivity().getSupportFragmentManager().findFragmentById(R.id.ZhuYe_Avtivitybar);
        View view = fragmentActivityBar.getView();
        circle = view.findViewById(R.id.activitybar_title_circle);
        circle1 = view.findViewById(R.id.activitybar_title_circle1);
        mActivitybarTitle = (TextView) view.findViewById(R.id.activitybar_title);
        mActivitybarTitle1 = (TextView) view.findViewById(R.id.activitybar_title1);

        tishi = selectonFragment(fragments,fragmentManager,viewPager,myZhuYe,circle,circle1,mActivitybarTitle,mActivitybarTitle1);
        tishiDian(0,circle,circle1);
        tishiFond(0,mActivitybarTitle,mActivitybarTitle1);

        return root;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            int a = viewPager.getCurrentItem();
            if (a == 1){
                tishiDian(1,circle,circle1);
            }
            if (a == 0){
                tishiDian(0,circle,circle1);
            }
        }else {

        }
    }
}