package com.example.sif.Lei.MyToolClass;

import android.content.Context;
import android.util.DisplayMetrics;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sif.Lei.ShiPeiQi.GoodVoiceAdapter;
import com.example.sif.Lei.ZhuangTaiLan.StatusBarUtil;
import com.example.sif.MyApplication;

public class CenterLayoutManager extends LinearLayoutManager {

    private Context context;

    public CenterLayoutManager(Context c) {
        super(c);
        this.context = c;
    }

    private GoodVoiceAdapter goodVoiceAdapter;
    private int key = 0;
    public void setAdapter(GoodVoiceAdapter g){
        this.goodVoiceAdapter = g;
        key = 0;
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        super.smoothScrollToPosition(recyclerView, state, position);
        RecyclerView.SmoothScroller smoothScroller = new CenterSmoothScroller(recyclerView.getContext()){
            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return 80f / displayMetrics.densityDpi;
            }
        };
        smoothScroller.setTargetPosition(position);
        startSmoothScroll(smoothScroller);
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
    }

    private static class CenterSmoothScroller extends LinearSmoothScroller{
        public CenterSmoothScroller(Context context) {
            super(context);
        }

        @Override
        public int calculateDtToFit(int viewStart, int viewEnd, int boxStart, int boxEnd, int snapPreference) {
            int height = StatusBarUtil.getStatusBarHeight(MyApplication.getContext());
            return (boxStart - 2 * height + (boxEnd - boxStart) / 2) - (viewStart + (viewEnd - viewStart) / 2);
        }
    }
}
