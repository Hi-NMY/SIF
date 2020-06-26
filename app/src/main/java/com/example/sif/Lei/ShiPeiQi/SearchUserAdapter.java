package com.example.sif.Lei.ShiPeiQi;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.sif.Lei.MyToolClass.ObtainUser;
import com.example.sif.Lei.MyToolClass.ToastZong;
import com.example.sif.Lei.NiceImageView.CircleImageView;
import com.example.sif.NeiBuLei.FollowList;
import com.example.sif.R;

import java.util.List;

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.ViewHolder> {

    private List<FollowList> followLists;
    private Activity activity;
    private View view;
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
            mConcernHeadimage = (CircleImageView) itemView.findViewById(R.id.concern_headimage);
            mConcernName = (TextView) itemView.findViewById(R.id.concern_name);
            mConcernSex = (ImageView) itemView.findViewById(R.id.concern_sex);
            mConcernHello = (TextView) itemView.findViewById(R.id.concern_hello);
            mConcernR = (RelativeLayout) itemView.findViewById(R.id.concern_R);
        }
    }

    public SearchUserAdapter(Activity a, List<FollowList> f) {
        this.activity = a;
        this.followLists = f;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_follow_list, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    private FollowList followList;
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
                .load("http://nmy1206.natapp1.cc/UserImageServer/"+followList.getXuehao()+"/HeadImage/myHeadImage.png")
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
