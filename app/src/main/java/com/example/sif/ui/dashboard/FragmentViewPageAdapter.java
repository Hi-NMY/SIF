package com.example.sif.ui.dashboard;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentViewPageAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;

    public FragmentViewPageAdapter(FragmentManager fragmentManager, ArrayList<Fragment> f, int sum){
        super(fragmentManager,sum);
        this.fragments = f;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    private Fragment fragment;
    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        fragment = (Fragment)object;
        super.setPrimaryItem(container, position, object);
    }

    public Fragment getThisFragment(){
        return fragment;
    }

}
