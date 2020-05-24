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
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.sif.Lei.MyToolClass.GlideRoundTransform;
import com.example.sif.Lei.MyToolClass.MyVeryDiaLog;
import com.example.sif.Lei.MyToolClass.ToastZong;
import com.example.sif.NeiBuLei.DouBleImagePath;
import com.example.sif.R;
import com.tamsiree.rxui.view.dialog.RxDialog;
import com.tamsiree.rxui.view.dialog.RxDialogScaleView;

import java.util.List;

public class GuangChangImageAdapter extends RecyclerView.Adapter<GuangChangImageAdapter.ViewHolder> {

    private List<DouBleImagePath> douBleImagePaths;
    private Activity activity;
    private View view;
    private ViewHolder viewHolder;
    private int fun = -1;

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mGuangchangImagelist;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mGuangchangImagelist = (ImageView)itemView.findViewById(R.id.guangchang_imagelist);
        }
    }

    public GuangChangImageAdapter(Activity a,List<DouBleImagePath> d){
        this.activity = a;
        this.douBleImagePaths = d;
        if (douBleImagePaths.size() == 3){
            fun = 3;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.guangchang_imagelist, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    private DouBleImagePath douBleImagePath;
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
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        douBleImagePath = douBleImagePaths.get(position);

        if (!douBleImagePath.getMinPath().equals(holder.mGuangchangImagelist.getTag())) {
            holder.mGuangchangImagelist.setTag(null);
            if (!douBleImagePaths.get(position).getMinPath().equals("")) {
                holder.mGuangchangImagelist.setVisibility(View.VISIBLE);
                switch (fun){
                    case 3:
                        Glide.with(activity)
                                .load(douBleImagePath.getMinPath())
                                .placeholder(R.drawable.nostartimage_two)
                                .fallback(R.drawable.nostartimage_two)
                                .error(R.drawable.nostartimage_two)
                                .override(200, 200)
                                .centerCrop()
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .transform(new GlideRoundTransform(6))
                                .into(holder.mGuangchangImagelist);
                        break;
                    default:
                        Glide.with(activity)
                                .load(douBleImagePath.getMinPath())
                                .placeholder(R.drawable.nostartimage_two)
                                .fallback(R.drawable.nostartimage_two)
                                .error(R.drawable.nostartimage_two)
                                .override(300, 300)
                                .centerCrop()
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .transform(new GlideRoundTransform(6))
                                .into(holder.mGuangchangImagelist);
                        break;
                }
                holder.mGuangchangImagelist.setTag(douBleImagePath.getMinPath());
            }
        }

        holder.mGuangchangImagelist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rxDialogScaleView = new RxDialogScaleView(activity);
                rxDialog = new RxDialog(activity, R.style.tran_dialog);
                rxDialog.setCanceledOnTouchOutside(false);
                MyVeryDiaLog.veryImageDiaLog(rxDialogScaleView, douBleImagePath.getMaxPath(), douBleImagePath.getMinPath(), bitMapHandler);
                MyVeryDiaLog.transparentDiaLog(activity, rxDialog);
            }
        });
    }

    @Override
    public int getItemCount() {
        return douBleImagePaths.size();
    }
}
