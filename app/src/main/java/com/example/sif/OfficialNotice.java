package com.example.sif;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sif.Lei.ShiPeiQi.OfficialAdapter;
import com.example.sif.Lei.ShowActivityBar.FragmentActivityBar;
import com.example.sif.NeiBuLei.OfficialNoticeList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OfficialNotice extends AppCompatActivity {

    private FragmentActivityBar fragmentActivityBar;
    private View mOfficialNoticeBar;
    private RecyclerView mOfficialList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official_notice);
        initView();

        writeOfficialNotice();

        showActivityBar();
    }

    private List<String> officialN;
    private List<OfficialNoticeList> officialNoticeLists;
    private void writeOfficialNotice() {
        officialNoticeLists = new ArrayList<>();
        SharedPreferences sharedPreferences1 = getSharedPreferences("officialMessage",MODE_PRIVATE);
        String on = sharedPreferences1.getString("notice","");
        if (on != null && on.length() > 0){
            officialN = Arrays.asList(on.split("&"));
            for (String s:officialN){
                OfficialNoticeList officialNoticeList = new OfficialNoticeList();
                officialNoticeList.setTime(s.substring(0,16));
                officialNoticeList.setMessage(s.substring(16));
                officialNoticeLists.add(officialNoticeList);
            }
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            mOfficialList.setLayoutManager(linearLayoutManager);
            OfficialAdapter officialAdapter = new OfficialAdapter(officialNoticeLists);
            mOfficialList.setAdapter(officialAdapter);
        }
    }

    private void showActivityBar() {
        fragmentActivityBar = (FragmentActivityBar) getSupportFragmentManager().findFragmentById(R.id.official_notice_bar);
        mOfficialNoticeBar.bringToFront();
        ShowActivityBars showActivityBars = new ShowActivityBars(this, fragmentActivityBar);
        showActivityBars.showKongJian(true, false, true, false, true, false, false);
        showActivityBars.showUI(R.drawable.zuo_black, (String) null, "官方通知", null, null, 0, 0);
        showActivityBars.uiFunction(1, 0, 0, 0, 0, 0, 0);
        showActivityBars.barBackground1(R.color.beijing);
    }

    private void initView() {
        mOfficialNoticeBar = (View) findViewById(R.id.official_notice_bar);
        mOfficialList = (RecyclerView) findViewById(R.id.official_list);
    }
}
