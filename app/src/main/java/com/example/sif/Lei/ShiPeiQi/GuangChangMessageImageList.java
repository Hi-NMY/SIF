package com.example.sif.Lei.ShiPeiQi;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.MediaStoreSignature;
import com.example.sif.GuangChangMessage;
import com.example.sif.Lei.MyToolClass.MyDateClass;
import com.example.sif.Lei.MyToolClass.ToastZong;
import com.example.sif.MyApplication;
import com.example.sif.NeiBuLei.DouBleImagePath;
import com.example.sif.R;
import com.tamsiree.rxui.view.dialog.RxDialog;
import com.tamsiree.rxui.view.dialog.RxDialogScaleView;

import java.util.List;

public class GuangChangMessageImageList extends RecyclerView.Adapter<GuangChangMessageImageList.ViewHolder> {

    public List<DouBleImagePath> douBleImagePaths;
    private Activity activity;
    private GuangChangMessage guangChangMessage;
    private View view;
    private ViewHolder viewHolder;


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageItemView;
        ImageView mImageClose;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageItemView = (ImageView)itemView.findViewById(R.id.image_item_view);
            mImageClose = (ImageView)itemView.findViewById(R.id.image_close);
        }
    }

    public GuangChangMessageImageList(Activity a,GuangChangMessage g, List<DouBleImagePath> d) {
        this.activity = a;
        this.guangChangMessage = g;
        this.douBleImagePaths = d;
    }

    public void addNewImage(DouBleImagePath d){
        this.douBleImagePaths.add(0, d);
        notifyItemInserted(0);
        notifyItemRangeChanged(0, douBleImagePaths.size() + 1);
    }

    public void removeImage(int position){
        this.douBleImagePaths.remove(position);
        notifyItemRangeChanged(position, douBleImagePaths.size() + 1);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.guangchang_message_imageitem, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    private DouBleImagePath douBleImagePath;
    private RxDialog rxDialog;
    private RxDialogScaleView rxDialogScaleView;
    private Handler bitMapHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            rxDialog.dismiss();
            if (msg.obj != null) {
                if (msg.what == 1) {
                    ToastZong.ShowToast(MyApplication.getContext(), "图片加载错误");
                    rxDialogScaleView.setImage((Bitmap) msg.obj);
                } else {
                    rxDialogScaleView.setImage((Bitmap) msg.obj);
                }
            } else {
                ToastZong.ShowToast(MyApplication.getContext(), "错误");
            }
        }
    };
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        douBleImagePath = douBleImagePaths.get(position);
        String updateTime = MyDateClass.showNowDate();
        Glide.with(MyApplication.getContext())
                .load(douBleImagePath.getMaxPath())
                .signature(new MediaStoreSignature(updateTime, 1, 1))
                .into(holder.mImageItemView);

        holder.mImageItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //接口回调查看图片

            }
        });

        holder.mImageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //接口回调进行删除

            }
        });
    }

    @Override
    public int getItemCount() {
        return douBleImagePaths.size();
    }
}
