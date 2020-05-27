package com.example.sif.Lei.ShiPeiQi;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sif.BaseActivity;
import com.example.sif.Lei.MyToolClass.GuangChangImageToClass;
import com.example.sif.Lei.MyToolClass.ShowDiaLog;
import com.example.sif.NeiBuLei.SchoolShopClass;
import com.example.sif.R;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

public class UserShopAdapter extends RecyclerView.Adapter<UserShopAdapter.ViewHolder> {

    private List<SchoolShopClass> schoolShopClasses;
    private Activity activity;
    private View view;
    private ViewHolder viewHolder;
    public String date;
    private BaseActivity baseActivity;

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout mShopState;
        TextView mShopStateText;
        TextView mShopNotice;
        RecyclerView mShopImagelist;
        TagFlowLayout mShopLabellist;
        TextView mShopTime;
        Button mShopTomessage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mShopState = (RelativeLayout)itemView.findViewById(R.id.usershop_state);
            mShopStateText = (TextView) itemView.findViewById(R.id.usershopstate_text);
            mShopNotice = (TextView)itemView.findViewById(R.id.usershop_notice);
            mShopImagelist = (RecyclerView)itemView.findViewById(R.id.usershop_imagelist);
            mShopLabellist = (TagFlowLayout)itemView.findViewById(R.id.usershop_labellist);
            mShopTime = (TextView)itemView.findViewById(R.id.usershop_time);
            mShopTomessage = (Button)itemView.findViewById(R.id.usershop_tomessage);
        }
    }

    public UserShopAdapter(Activity a,BaseActivity b,List<SchoolShopClass> s){
        this.activity = a;
        this.schoolShopClasses = s;
        this.baseActivity = b;
        if (schoolShopClasses.size() > 0) {
            date = schoolShopClasses.get(schoolShopClasses.size() - 1).getSendtime();
        }
    }

    public void addSchoolShopAdapter(List<SchoolShopClass> s) {

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.usershop_listitem, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    private SchoolShopClass schoolShopClass;
    private ShowDiaLog showDiaLog1;
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        schoolShopClass = schoolShopClasses.get(position);

        holder.mShopNotice.setText(schoolShopClass.getNotice());
        holder.mShopTime.setText(schoolShopClass.getSendtime());
        switch (schoolShopClass.getShopstate()) {
            case 0:
                holder.mShopState.setBackgroundResource(R.drawable.shop_state_bg);
                holder.mShopStateText.setText("正  售");
                break;
            case 1:
                holder.mShopState.setBackgroundResource(R.drawable.shop_state_bg2);
                holder.mShopStateText.setText("已售出");
                break;
            case 2:
                holder.mShopState.setBackgroundResource(R.drawable.shop_state_bg1);
                holder.mShopStateText.setText("暂  挂");
                break;
        }

        if (!schoolShopClasses.get(position).getCommodityimage().equals("")) {
            holder.mShopImagelist.setVisibility(View.VISIBLE);
            holder.mShopImagelist.setLayoutManager(GuangChangImageToClass.newView(activity,schoolShopClass.getCommodityimage(),schoolShopClass.getXuehao()));
            GuangChangImageAdapter guangChangImageAdapter = new GuangChangImageAdapter(activity, GuangChangImageToClass.imageToClass(schoolShopClass.getCommodityimage(),schoolShopClass.getXuehao()));
            holder.mShopImagelist.setAdapter(guangChangImageAdapter);
        }

        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        if (schoolShopClasses.get(position).getLabel() != null && !schoolShopClasses.get(position).getLabel().equals("")) {
            List<String> strings = new ArrayList<>();
            String[] s = schoolShopClasses.get(position).getLabel().split(",");
            for (int a = 0; a < s.length; a++) {
                strings.add(s[a]);
            }
            holder.mShopLabellist.setAdapter(new TagAdapter<String>(strings) {
                @Override
                public View getView(FlowLayout parent, int position, String o) {
                    View view = layoutInflater.inflate(R.layout.shop_label_item, holder.mShopLabellist, false);
                    TextView selet = (TextView) view.findViewById(R.id.text);
                    selet.setText(o);
                    return view;
                }
            });
        }

        holder.mShopTomessage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                oldState = schoolShopClass.getShopstate();
                View view1 = LayoutInflater.from(activity).inflate(R.layout.update_shopstate, null);
                initShopView(view1,schoolShopClass.getShopstate());
                showDiaLog1 = new ShowDiaLog(activity, R.style.AlertDialog_qr, view1);
                showDiaLog1.logWindow(new ColorDrawable(Color.TRANSPARENT));
                showDiaLog1.showMyDiaLog();
            }
        });
    }

    private TextView t1;
    private TextView t2;
    private TextView t3;
    private Button right;
    private int oldState = -1;
    private int newState = -1;
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initShopView(View view1, int state) {
        t1 = (TextView)view1.findViewById(R.id.nowshop);
        t2 = (TextView)view1.findViewById(R.id.outshop);
        t3 = (TextView)view1.findViewById(R.id.stopshop);
        right = (Button)view1.findViewById(R.id.right_button);
        if (state == 0){
            t1.setTextColor(activity.getColor(R.color.ziti));
            t2.setTextColor(activity.getColor(R.color.lightgray));
            t3.setTextColor(activity.getColor(R.color.lightgray));
        }else if (state == 1){
            t1.setTextColor(activity.getColor(R.color.lightgray));
            t2.setTextColor(activity.getColor(R.color.ziti));
            t3.setTextColor(activity.getColor(R.color.lightgray));
        }else if (state == 2){
            t1.setTextColor(activity.getColor(R.color.lightgray));
            t2.setTextColor(activity.getColor(R.color.lightgray));
            t3.setTextColor(activity.getColor(R.color.ziti));
        }

        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newState = 0;
                t1.setTextColor(activity.getColor(R.color.ziti));
                t2.setTextColor(activity.getColor(R.color.lightgray));
                t3.setTextColor(activity.getColor(R.color.lightgray));
            }
        });
        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newState = 1;
                t1.setTextColor(activity.getColor(R.color.lightgray));
                t2.setTextColor(activity.getColor(R.color.ziti));
                t3.setTextColor(activity.getColor(R.color.lightgray));
            }
        });
        t3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newState = 2;
                t1.setTextColor(activity.getColor(R.color.lightgray));
                t2.setTextColor(activity.getColor(R.color.lightgray));
                t3.setTextColor(activity.getColor(R.color.ziti));
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseActivity.showDiaLog(activity, R.drawable.loading2);
                if (oldState == newState){
                    baseActivity.closeDiaLog();
                }else {

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return schoolShopClasses.size();
    }
}
