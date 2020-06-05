package com.example.sif.Lei.MyToolClass;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.sif.Lei.GPSServer.MyGpsClient;
import com.example.sif.Lei.MyBroadcastReceiver.BroadcastRec;
import com.example.sif.Lei.ShiPeiQi.MsgGpsAdapter;
import com.example.sif.MyApplication;
import com.example.sif.NeiBuLei.MsgGpsClass;
import com.example.sif.R;
import pl.droidsonroids.gif.GifImageView;

import java.util.List;

public class GPSVeryPopupwindow extends PopupWindow implements View.OnClickListener {

    private Context context;
    private Activity activity;
    private View view;
    public MyGpsClient myGpsClient;

    public GPSVeryPopupwindow(Context c,Activity a) {
        this.activity = a;
        this.context = c;
        this.view = LayoutInflater.from(c).inflate(R.layout.gcmsg_gps_popupview, null);
        initview();
        initSearch();
        this.setContentView(view);
        this.setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        ColorDrawable colorDrawable = new ColorDrawable(0xb0000000);
        this.setBackgroundDrawable(colorDrawable);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.verypopupview);
        initGps();
    }

    private void initGps() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (myGpsClient.obtainListener().msgGpsClasses == null || myGpsClient.obtainListener().msgGpsClasses.size() == 0){
                    BroadcastRec.sendReceiver(MyApplication.getContext(),"nearbyPlace",0,"");
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                myGpsClient = new MyGpsClient(MyApplication.getContext());
            }
        }).start();

        mErrorR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initGps();
                mLoagingGps.setVisibility(View.VISIBLE);
                mErrorR.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void initSearch() {

    }

    private TextView mGpsClose;
    private EditText mSearchMessage;
    private RecyclerView mGcmsgGpslist;
    private GifImageView mLoagingGps;
    private RelativeLayout mErrorR;

    private void initview() {
        mGpsClose = (TextView) view.findViewById(R.id.gps_close);
        mSearchMessage = (EditText)view.findViewById(R.id.search_message);
        mGcmsgGpslist = (RecyclerView)view.findViewById(R.id.gcmsg_gpslist);
        mLoagingGps = (GifImageView)view.findViewById(R.id.loaging_gps);
        mErrorR = (RelativeLayout)view.findViewById(R.id.error_r);
        mGpsClose.setOnClickListener(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        mGcmsgGpslist.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gps_close:
                dismissWindow();
                break;
        }
    }

    public void dismissWindow(){
        this.dismiss();
    }

    public void stopToGps(){
        myGpsClient.locationClient.stop();
    }

    public void addPlaceList(List<MsgGpsClass> classes){
        mLoagingGps.setVisibility(View.INVISIBLE);
        mErrorR.setVisibility(View.INVISIBLE);
        mGcmsgGpslist.setVisibility(View.VISIBLE);
        MsgGpsAdapter msgGpsAdapter = new MsgGpsAdapter(activity,classes);
        mGcmsgGpslist.setAdapter(msgGpsAdapter);
    }

    public void errorPlace(){
        mLoagingGps.setVisibility(View.INVISIBLE);
        mErrorR.setVisibility(View.VISIBLE);
        mGcmsgGpslist.setVisibility(View.INVISIBLE);
    }

}
