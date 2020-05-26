package com.example.sif.Lei.ShiPeiQi;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.MediaStoreSignature;
import com.example.sif.GuangChangMessage;
import com.example.sif.Lei.MyToolClass.GlideRoundTransform;
import com.example.sif.Lei.MyToolClass.MyDateClass;
import com.example.sif.Lei.MyToolClass.SchoolShopPopupWindow;
import com.example.sif.MyApplication;
import com.example.sif.NeiBuLei.DouBleImagePath;
import com.example.sif.R;

import java.util.List;

public class GuangChangMessageImageList extends RecyclerView.Adapter<GuangChangMessageImageList.ViewHolder> {

    private List<DouBleImagePath> douBleImagePaths;
    private Activity activity;
    private GuangChangMessage guangChangMessage;
    private SchoolShopPopupWindow schoolShopPopupWindow;
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
        if (douBleImagePaths != null){
            douBleImagePaths.clear();
        }
        this.douBleImagePaths = d;
        notifyDataSetChanged();
    }

    public GuangChangMessageImageList(Activity a, SchoolShopPopupWindow s, List<DouBleImagePath> d) {
        this.activity = a;
        this.schoolShopPopupWindow = s;
        if (douBleImagePaths != null){
            douBleImagePaths.clear();
        }
        this.douBleImagePaths = d;
        notifyDataSetChanged();
    }

    public void addNewImage(DouBleImagePath d){
        douBleImagePaths.add(0, d);
       // notifyItemInserted(0);
        notifyItemRangeChanged(0, douBleImagePaths.size() + 1);
    }

    public void removeImage(int position){
     //   this.douBleImagePaths.remove(position);
        //notifyItemRemoved(position);
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

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        douBleImagePath = douBleImagePaths.get(position);
        String updateTime = MyDateClass.showNowDate();
        Glide.with(MyApplication.getContext())
                .load(douBleImagePath.getMaxPath())
                .override(70, 70)
                .centerCrop()
                .transform(new GlideRoundTransform(2))
                .signature(new MediaStoreSignature(updateTime, 1, 1))
                .into(holder.mImageItemView);

        holder.mImageItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (guangChangMessage != null){
                    guangChangMessage.lookPicture(position);
                }
                if (schoolShopPopupWindow != null){
                    schoolShopPopupWindow.lookPicture(position);
                }
            }
        });

        holder.mImageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (guangChangMessage != null){
                    guangChangMessage.removeList(position);
                }
                if (schoolShopPopupWindow != null){
                    schoolShopPopupWindow.removeList(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return douBleImagePaths.size();
    }
}
