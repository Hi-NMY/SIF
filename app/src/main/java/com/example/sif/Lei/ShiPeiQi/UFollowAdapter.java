package com.example.sif.Lei.ShiPeiQi;

import android.app.Activity;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.sif.Lei.MyToolClass.InValues;
import com.example.sif.Lei.MyToolClass.ObtainUser;
import com.example.sif.Lei.MyToolClass.ToastZong;
import com.example.sif.Lei.NiceImageView.CircleImageView;
import com.example.sif.NeiBuLei.FollowList;
import com.example.sif.R;

import java.util.ArrayList;
import java.util.List;

public class UFollowAdapter extends RecyclerView.Adapter<UFollowAdapter.ViewHolder> {

    private List<FollowList> followLists = new ArrayList<>();
    private Activity activity;
    private View view;
    private FollowList followList;

    public int freshid;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    ToastZong.ShowToast(activity,"用户信息错误");
                    break;
                case 2:
                    ToastZong.ShowToast(activity,"用户信息错误");
                    break;
            }
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView mConcernHeadimage;
        TextView mConcernName;
        ImageView mConcernSex;
        TextView mConcernHello;
        RelativeLayout mConcernR;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mConcernHeadimage = (CircleImageView)itemView.findViewById(R.id.concern_headimage);
            mConcernName = (TextView)itemView.findViewById(R.id.concern_name);
            mConcernSex = (ImageView)itemView.findViewById(R.id.concern_sex);
            mConcernHello = (TextView)itemView.findViewById(R.id.concern_hello);
            mConcernR = (RelativeLayout)itemView.findViewById(R.id.concern_R);
        }
    }

    public UFollowAdapter(List<FollowList> f, Activity a) {
        if (f != null && f.size() > 0){
            this.followLists = f;
        }
        this.activity = a;

        if (followLists != null && followLists.size() > 0){
            freshid = followLists.get(followLists.size() - 1).getId();
        }

    }

    public void addFollowListbottom(List<FollowList> f){
        if (f.size() == 0){
            ToastZong.ShowToast(activity,"到底了\n快去发现更多有趣的人吧");
        }else {
            this.followLists.addAll(f);
            notifyItemRangeChanged(followLists.size()-f.size(),followLists.size()+1);
        }
        freshid = followLists.get(followLists.size() - 1).getId();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_follow_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        followList = followLists.get(position);
        holder.mConcernName.setText(followList.getUser_name());
        holder.mConcernHello.setText(followList.getUser_geqian());

        if (followList.getXingbie().equals("男")){
            holder.mConcernSex.setBackground(activity.getDrawable(R.drawable.boy));
        }
        if (followList.getXingbie().equals("女")){
            holder.mConcernSex.setBackground(activity.getDrawable(R.drawable.girl));
        }

        Glide.with(activity)
                .load(InValues.send(R.string.httpHeader) +"/UserImageServer/"+followList.getXuehao()+"/HeadImage/myHeadImage.png")
                .placeholder(R.drawable.nostartimage_three)
                .fallback(R.drawable.defaultheadimage)
                .error(R.drawable.defaultheadimage)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)//跳过内存缓存
                .transition(DrawableTransitionOptions.withCrossFade())
                .circleCrop()
                .into(holder.mConcernHeadimage);

        holder.mConcernR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObtainUser.obtainUser(activity,followLists.get(position).getXuehao(),handler);
            }
        });
    }

    @Override
    public int getItemCount() {
        return followLists.size();
    }

}
