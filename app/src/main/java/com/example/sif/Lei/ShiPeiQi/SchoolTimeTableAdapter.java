package com.example.sif.Lei.ShiPeiQi;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sif.Lei.MyToolClass.MyDateClass;
import com.example.sif.Lei.MyToolClass.ShowDiaLog;
import com.example.sif.NeiBuLei.SchoolTable;
import com.example.sif.R;

import java.util.ArrayList;
import java.util.List;

public class SchoolTimeTableAdapter extends RecyclerView.Adapter<SchoolTimeTableAdapter.ViewHolder> {

    private List<SchoolTable> schoolTables;
    private Activity activity;
    private View view;
    private ViewHolder viewHolder;
    private SchoolTable schoolTable;
    private List<Integer> integers;

    public class ViewHolder extends RecyclerView.ViewHolder {
        View mCircleItem;
        TextView mTableTitle;
        TextView mClassName;
        TextView mClassRoom;
        TextView mClassTime;
        RelativeLayout mTimetableMessage;
        TextView mTableMonth;
        TextView mTableDay;
        ConstraintLayout mTableDate;
        TextView mTableWeek;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mCircleItem = (View) itemView.findViewById(R.id.circle_item);
            mTableTitle = (TextView) itemView.findViewById(R.id.table_title);
            mClassName = (TextView) itemView.findViewById(R.id.class_name);
            mClassRoom = (TextView) itemView.findViewById(R.id.class_room);
            mClassTime = (TextView) itemView.findViewById(R.id.class_time);
            mTimetableMessage = (RelativeLayout) itemView.findViewById(R.id.timetable_message);
            mTableMonth = (TextView) itemView.findViewById(R.id.table_month);
            mTableDay = (TextView) itemView.findViewById(R.id.table_day);
            mTableDate = (ConstraintLayout) itemView.findViewById(R.id.table_date);
            mTableWeek = (TextView)itemView.findViewById(R.id.table_week);
        }
    }

    private int num = -1;
    private int oneKey = 1;
    private String topDate;
    public int scrollNum = 0;

    public SchoolTimeTableAdapter(Activity a, List<SchoolTable> s) {
        integers = new ArrayList<>();
        this.activity = a;
        this.schoolTables = s;

        for (SchoolTable sch : s) {
            num++;
            if (oneKey == 1) {
                integers.add(num);
                topDate = sch.getClass_date();
                oneKey++;
                continue;
            }
            if (!sch.getClass_date().equals(topDate)) {
                integers.add(num);
                topDate = sch.getClass_date();
            }
        }

        scrollNum = integers.get(integers.size() - 2);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.school_time_list_item, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        schoolTable = schoolTables.get(position);
        holder.mClassName.setText(schoolTable.getClass_name());
        holder.mClassRoom.setText(schoolTable.getClass_room());
        holder.mClassTime.setText(schoolTable.getClass_time_start());
        holder.mTableMonth.setText(schoolTable.getClass_date().substring(5, 7) + "月");
        holder.mTableDay.setText(schoolTable.getClass_date().substring(8, 10) + "日");
        holder.mTableWeek.setText("星期" + MyDateClass.showWeekTable(schoolTable.getClass_date()));
        if (integers.contains(position)) {
            holder.mCircleItem.setVisibility(View.VISIBLE);
            holder.mTableDate.setVisibility(View.VISIBLE);
        } else {
            holder.mCircleItem.setVisibility(View.INVISIBLE);
            holder.mTableDate.setVisibility(View.INVISIBLE);
        }

        holder.mTimetableMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(activity).inflate(R.layout.schooltable_dialog, null);
                ShowDiaLog showDiaLog = new ShowDiaLog(activity, R.style.AlertDialog_qr, view);
                showDiaLog.logWindow(new ColorDrawable(Color.TRANSPARENT));
                showDiaLog.showMyDiaLog();
                ImageView close = (ImageView)view.findViewById(R.id.table_close);
                TextView title = (TextView)view.findViewById(R.id.schooltable_dialog_title);
                TextView time = (TextView)view.findViewById(R.id.schooltable_dialog_time);
                TextView message = (TextView)view.findViewById(R.id.schooltable_dialog_message);
                TextView moreOne = (TextView)view.findViewById(R.id.more_one);
                TextView moreTwo = (TextView)view.findViewById(R.id.more_two);
                TextView moreThree = (TextView)view.findViewById(R.id.more_three);
                time.setText(schoolTables.get(position).getClass_time_start() + " - " +schoolTables.get(position).getClass_time_over());
                message.setText(schoolTables.get(position).getClass_name());
                moreOne.setText(schoolTables.get(position).getClass_room());
                moreTwo.setText(schoolTables.get(position).getClass_teacher());
                moreThree.setText(schoolTables.get(position).getClass_num() + "节");
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDiaLog.closeMyDiaLog();
                    }
                });
            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return schoolTables.size();
    }
}
