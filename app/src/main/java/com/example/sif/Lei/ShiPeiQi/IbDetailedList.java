package com.example.sif.Lei.ShiPeiQi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sif.Lei.NiceImageView.CircleImageView;
import com.example.sif.R;
import com.zhy.view.flowlayout.TagFlowLayout;

public class IbDetailedList extends RecyclerView.Adapter<IbDetailedList.ViewHolder> {

    private ViewHolder viewHolder;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView gcUserName;
        TextView gcUserShiJian;
        TextView gcUserXinXi;
        TextView gcUserThumb;
        TextView gcUserComment;
        ImageView imageView;
        ImageButton gcUserZan;
        ImageButton gcUserC;
        CircleImageView gcUserImage;
        RelativeLayout recyclerView;
        TagFlowLayout mGuangchangIb;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            gcUserName = (TextView) itemView.findViewById(R.id.guangchang_user_name);
            gcUserShiJian = (TextView) itemView.findViewById(R.id.guangchang_user_shijian);
            gcUserXinXi = (TextView) itemView.findViewById(R.id.guangchang_user_xinxi);
            gcUserImage = (CircleImageView) itemView.findViewById(R.id.guangchang_user_tou_image);
            gcUserZan = (ImageButton) itemView.findViewById(R.id.guangchang_user_dianzan);
            gcUserThumb = (TextView) itemView.findViewById(R.id.guangchang_user_thumb);
            imageView = (ImageView) itemView.findViewById(R.id.guangchang_user_messageimage);
            recyclerView = (RelativeLayout) itemView.findViewById(R.id.GuangChang_Message_detailed);
            gcUserComment = (TextView) itemView.findViewById(R.id.guangchang_user_message);
            gcUserC = (ImageButton) itemView.findViewById(R.id.guangchang_user_pinglun);
            mGuangchangIb = (TagFlowLayout)itemView.findViewById(R.id.guangchang_ib);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.guangchang_listitem, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
