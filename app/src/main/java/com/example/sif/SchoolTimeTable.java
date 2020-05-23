package com.example.sif;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sif.Lei.MyToolClass.MyDateClass;
import com.example.sif.Lei.MyToolClass.UserSchoolTable;
import com.example.sif.Lei.ShiPeiQi.SchoolTimeTableAdapter;
import com.example.sif.Lei.ShowActivityBar.FragmentActivityBar;
import com.example.sif.NeiBuLei.SchoolTable;

import java.util.Collections;
import java.util.List;

public class SchoolTimeTable extends BaseActivity {


    private RecyclerView mTimeTableList;
    private LinearLayoutManager linearLayoutManager;
    private Handler newSchoolTableHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            writeSchoolTable(UserSchoolTable.schoolTables);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_time_table);
        initView();

        //设置状态栏
        ZTL();
        //ActivityBar
        showActivityBar();

        //增加padding  避免状态栏遮挡
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.stt_r);
        setPadding(this, relativeLayout);

        String nextDate = MyDateClass.showYearMonthDayAddOneDay();
        UserSchoolTable.obtainSchoolTable(getMyZhuanYe(),getMyBanJi(),getMyNianJi(),nextDate,newSchoolTableHandler);

        linearLayoutManager = new LinearLayoutManager(this);
        mTimeTableList.setLayoutManager(linearLayoutManager);

    }

    private void setListAnimation(){
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        controller.setDelay(0.2f);
        mTimeTableList.setLayoutAnimation(controller);
        mTimeTableList.startLayoutAnimation();
    }

    private void writeSchoolTable(List<SchoolTable> schoolTables){
        setListAnimation();
        SchoolTimeTableAdapter schoolTimeTableAdapter = new SchoolTimeTableAdapter(this,schoolTables);

        mTimeTableList.setAdapter(schoolTimeTableAdapter);

        ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int swipFlag = 0;
                int dragFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                return makeMovementFlags(dragFlag,swipFlag);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                schoolTimeTableAdapter.notifyItemMoved(viewHolder.getAdapterPosition(),target.getAdapterPosition());
                Collections.swap(schoolTables,viewHolder.getAdapterPosition(),target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }

            @Override
            public boolean canDropOver(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder current, @NonNull RecyclerView.ViewHolder target) {
                return true;
            }

            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mTimeTableList);
        mTimeTableList.scrollToPosition(schoolTimeTableAdapter.scrollNum);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void showActivityBar() {
        FragmentActivityBar fragmentActivityBar = (FragmentActivityBar) getSupportFragmentManager().findFragmentById(R.id.school_timetable_bar);
        ShowActivityBars showActivityBars = new ShowActivityBars(this, fragmentActivityBar);
        showActivityBars.showKongJian(true, false, true, false, false, false, false);
        showActivityBars.showUI(R.drawable.zuo_black, null, "我的日程", null, 0, 0);
        showActivityBars.uiFunction(1, 0, 0, 0, 0, 0, 0);
    }

    private void initView() {
        mTimeTableList = (RecyclerView) findViewById(R.id.time_table_list);
    }
}
