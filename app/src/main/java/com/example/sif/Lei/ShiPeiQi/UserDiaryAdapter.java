package com.example.sif.Lei.ShiPeiQi;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.signature.MediaStoreSignature;
import com.example.sif.Lei.MyToolClass.GlideRoundTransform;
import com.example.sif.Lei.MyToolClass.MyDateClass;
import com.example.sif.Lei.MyToolClass.MyVeryDiaLog;
import com.example.sif.Lei.MyToolClass.ShowDiaLog;
import com.example.sif.Lei.MyToolClass.ToastZong;
import com.example.sif.Lei.MyToolClass.WholeDeleteDiary;
import com.example.sif.MyApplication;
import com.example.sif.NeiBuLei.UserDiaryClass;
import com.example.sif.R;
import com.tamsiree.rxui.view.dialog.RxDialog;
import com.tamsiree.rxui.view.dialog.RxDialogScaleView;

import java.util.List;

public class UserDiaryAdapter extends RecyclerView.Adapter<UserDiaryAdapter.ViewHolder> {

    private Activity activity;
    private List<UserDiaryClass> userDiaryClasses;
    private View view;
    private ViewHolder viewHolder;
    private UserDiaryClass userDiaryClass;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mDiaryItemDate;
        TextView mDiaryItemWeek;
        ImageView mDiaryItemWeather;
        TextView mDiaryItemMessage;
        ImageView mDiaryItemImage;
        ConstraintLayout mDiaryItemR;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mDiaryItemDate = (TextView)itemView.findViewById(R.id.diary_item_date);
            mDiaryItemWeek = (TextView)itemView.findViewById(R.id.diary_item_week);
            mDiaryItemWeather = (ImageView)itemView.findViewById(R.id.diary_item_weather);
            mDiaryItemMessage = (TextView)itemView.findViewById(R.id.diary_item_message);
            mDiaryItemImage = (ImageView)itemView.findViewById(R.id.diary_item_image);
            mDiaryItemR = (ConstraintLayout)itemView.findViewById(R.id.diary_item_r);
        }
    }

    public UserDiaryAdapter(Activity a,List<UserDiaryClass> u){
        this.activity = a;
        this.userDiaryClasses = u;
    }

    private String myXuehao;
    public void sendMyXuehao(String x){
        this.myXuehao = x;
    }


    public void deleteDiary(int id){
        userDiaryClasses.remove(id);
        notifyItemRangeChanged(id, userDiaryClasses.size() + 1);
        notifyItemInserted(id);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.diary_list_item, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    private String nowTime;
    private RxDialogScaleView rxDialogScaleView;
    private RxDialog rxDialog;
    private Handler bitMapHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            rxDialog.dismiss();
            if (msg.obj != null) {
                if (msg.what == 1) {
                    ToastZong.ShowToast(activity, "图片加载错误");
                    rxDialogScaleView.setImage((Bitmap) msg.obj);
                } else {
                    rxDialogScaleView.setImage((Bitmap) msg.obj);
                }
            } else {
                ToastZong.ShowToast(activity, "错误");
            }
        }
    };

    private Handler deleteHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.obj.equals("0")){
                deleteDiary(deleteId);
                showDiaLog.closeMyDiaLog();
                ToastZong.ShowToast(MyApplication.getContext(),"已删除此日记");
            }else {
                ToastZong.ShowToast(MyApplication.getContext(),"请重试");
            }
        }
    };

    private ShowDiaLog showDiaLog;
    private int deleteId;
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        userDiaryClass = userDiaryClasses.get(position);
        holder.mDiaryItemDate.setText(userDiaryClass.getDiarydate());
        holder.mDiaryItemWeek.setText("星期" + MyDateClass.showWeekTable(userDiaryClass.getDiarydate()));
        switch (userDiaryClass.getWeathernum()){
            case 0:
                holder.mDiaryItemWeather.setBackgroundResource(R.drawable.weather_sun);
                break;
            case 1:
                holder.mDiaryItemWeather.setBackgroundResource(R.drawable.weather_night);
                break;
            case 2:
                holder.mDiaryItemWeather.setBackgroundResource(R.drawable.weather_cloudy);
                break;
            case 3:
                holder.mDiaryItemWeather.setBackgroundResource(R.drawable.weather_overcast);
                break;
            case 4:
                holder.mDiaryItemWeather.setBackgroundResource(R.drawable.weather_rain);
                break;
            case 5:
                holder.mDiaryItemWeather.setBackgroundResource(R.drawable.weather_snow);
                break;
        }

        holder.mDiaryItemMessage.setText(userDiaryClass.getMessagediary());
        if (!userDiaryClass.getImagepath().equals(holder.mDiaryItemImage.getTag())) {
            holder.mDiaryItemImage.setTag(null);
            nowTime = MyDateClass.showNowDate();
            if (!userDiaryClasses.get(position).getImagepath().equals("")) {
                holder.mDiaryItemImage.setVisibility(View.VISIBLE);
                Glide.with(activity)
                        .load("http://nmy1206.natapp1.cc/" + userDiaryClass.getImagepath())
                        .signature(new MediaStoreSignature(nowTime, 1, 1))
                        .placeholder(R.drawable.nostartimage_two)
                        .fallback(R.drawable.nostartimage_two)
                        .error(R.drawable.nostartimage_two)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .override(300, 300)
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .transform(new GlideRoundTransform(6))
                        .into(holder.mDiaryItemImage);
                holder.mDiaryItemImage.setTag(userDiaryClass.getImagepath());
            }
        }

        holder.mDiaryItemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rxDialogScaleView = new RxDialogScaleView(activity);
                rxDialog = new RxDialog(activity, R.style.tran_dialog);
                rxDialog.setCanceledOnTouchOutside(false);
                String name = String.valueOf(userDiaryClasses.get(position).getImagepath()).substring(String.valueOf(userDiaryClasses.get(position).getImagepath()).lastIndexOf("/"));
                String NewName = "http://nmy1206.natapp1.cc/UserImageServer/" + myXuehao + "/ADiary/" + name;
                String path1 = "http://nmy1206.natapp1.cc/" + userDiaryClasses.get(position).getImagepath();
                MyVeryDiaLog.veryImageDiaLog(rxDialogScaleView, NewName, path1, bitMapHandler);
                MyVeryDiaLog.transparentDiaLog(activity, rxDialog);
            }
        });

        holder.mDiaryItemR.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                View view = LayoutInflater.from(activity).inflate(R.layout.dialog_two, null);
                view.setVerticalFadingEdgeEnabled(true);
                showDiaLog = new ShowDiaLog(activity, R.style.AlertDialog_Function, view);
                showDiaLog.logWindow(new ColorDrawable(Color.TRANSPARENT));
                showDiaLog.showMyDiaLog();
                Button followDynamic = (Button) view.findViewById(R.id.delete_GuangChang_Dynamic);
                Button Cancel = (Button) view.findViewById(R.id.delete_cancel);
                followDynamic.setText("删除");
                followDynamic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WholeDeleteDiary.deleteDiary(userDiaryClasses.get(position).getId(),myXuehao,userDiaryClasses.get(position).getDiarydate(),deleteHandler);
                        deleteId = position;
                    }
                });
                Cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDiaLog.closeMyDiaLog();
                    }
                });
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return userDiaryClasses.size();
    }
}
