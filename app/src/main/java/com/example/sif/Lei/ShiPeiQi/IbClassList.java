package com.example.sif.Lei.ShiPeiQi;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.signature.MediaStoreSignature;
import com.example.sif.IbDetailed;
import com.example.sif.Lei.MyToolClass.GlideRoundTransform;
import com.example.sif.Lei.MyToolClass.MyDateClass;
import com.example.sif.Lei.MyToolClass.ToastZong;
import com.example.sif.Lei.NiceImageView.SolidImageView;
import com.example.sif.NeiBuLei.InterestingBlockClass;
import com.example.sif.R;

import java.util.List;

public class IbClassList extends RecyclerView.Adapter<IbClassList.ViewHolder> {

    private List<InterestingBlockClass> interestingBlockClasses;
    private View view;
    private InterestingBlockClass interestingBlockClass;
    private Activity activity;
    private ViewHolder viewHolder;
    private int frush;
    public int ibId;

    public class ViewHolder extends RecyclerView.ViewHolder {
        SolidImageView mIbListImage;
        View mIbListBlack;
        TextView mIbNumList;
        TextView mIbName;
        TextView mIbDate;
        RelativeLayout mIbRList;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mIbDate = (TextView)itemView.findViewById(R.id.ib_date);
            mIbListBlack = (View)itemView.findViewById(R.id.ib_list_black);
            mIbListImage = (SolidImageView) itemView.findViewById(R.id.ib_list_image);
            mIbName = (TextView)itemView.findViewById(R.id.ib_name);
            mIbNumList = (TextView)itemView.findViewById(R.id.ib_num_list);
            mIbRList = (RelativeLayout)itemView.findViewById(R.id.ib_R_List);
        }
    }

    public IbClassList(List<InterestingBlockClass> i, Activity a){
        this.interestingBlockClasses = i;
        this.activity = a;
        this.frush = 1;
        if (interestingBlockClasses.size() > 0){
            this.ibId = interestingBlockClasses.get(interestingBlockClasses.size() - 1).getId();
        }
    }

    public void addMoreIbList(List<InterestingBlockClass> i){
        i.remove(0);
        if (i.size() == 0){
            ToastZong.ShowToast(activity,"没有更多街区了");
        }else {
            this.interestingBlockClasses.addAll(i);
            notifyItemRangeChanged(interestingBlockClasses.size() - i.size(),interestingBlockClasses.size() + 1);
        }
        this.ibId = interestingBlockClasses.get(interestingBlockClasses.size() - 1).getId();
    }

    public void freshFollowNum(int i){
        if (freshPosition != -1){
            interestingBlockClass = interestingBlockClasses.get(freshPosition);
            if (i == 1){
                interestingBlockClass.setFollownum(String.valueOf(Integer.valueOf(interestingBlockClass.getFollownum()) + 1));
            }
            if (i == 2){
                interestingBlockClass.setFollownum(String.valueOf(Integer.valueOf(interestingBlockClass.getFollownum()) - 1));
            }
            notifyItemRangeChanged(freshPosition,freshPosition +1);
            freshPosition = -1;
        }
    }

    @NonNull
    @Override
    public IbClassList.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ib_list, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    private String updateTime;
    private int freshPosition = -1;
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        interestingBlockClass = interestingBlockClasses.get(position);
        holder.mIbNumList.setText(MyDateClass.sendMath(Integer.valueOf(interestingBlockClass.getFollownum())));
        holder.mIbName.setText(interestingBlockClass.getIbname());
        holder.mIbDate.setText(interestingBlockClass.getIbdate());

        if (frush == 1){
            updateTime = String.valueOf(System.currentTimeMillis());
            if (!interestingBlockClass.getBgimage().equals("")){
                if (!interestingBlockClasses.get(position).getBgimage().equals(holder.mIbListImage.getTag())){
                    holder.mIbListImage.setTag(null);
                    Glide.with(activity)
                            .load("http://nmy1206.natapp1.cc/"+interestingBlockClass.getBgimage())
                            .signature(new MediaStoreSignature(updateTime,1,1))
                            .placeholder(R.drawable.nullblock)
                            .fallback(R.drawable.nullblock)
                            .error(R.drawable.nullblock)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .transform(new MultiTransformation(new CenterCrop(),new GlideRoundTransform(20)))
                            .into(holder.mIbListImage);
                    holder.mIbListImage.setTag(interestingBlockClass.getBgimage());
                }
            }else {
                holder.mIbListImage.setBackgroundResource(R.drawable.nullblock);
            }
        }

        holder.mIbRList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                freshPosition = position;
                Intent intent = new Intent(activity, IbDetailed.class);
                intent.putExtra("ibname",interestingBlockClasses.get(position).getIbname());
                activity.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return interestingBlockClasses.size();
    }
}
