package com.example.sif.Lei.ShiPeiQi;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.sif.DynamicDetailed;
import com.example.sif.Lei.MyToolClass.DynamicMessageDetailed;
import com.example.sif.Lei.MyToolClass.GuangChangImageToClass;
import com.example.sif.Lei.MyToolClass.ObtainUser;
import com.example.sif.Lei.MyToolClass.ToastZong;
import com.example.sif.Lei.MyToolClass.UserDynamicCollection;
import com.example.sif.Lei.NiceImageView.CircleImageView;
import com.example.sif.MyApplication;
import com.example.sif.NeiBuLei.UserCollection;
import com.example.sif.R;

import java.io.Serializable;
import java.util.List;

public class CollectionList extends RecyclerView.Adapter<CollectionList.ViewHolder> {

    private View view;
    private List<UserCollection> userCollections;
    private UserCollection userCollection;
    private Activity activity;

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView mCollectHeadimage;
        TextView mCollectionUname;
        TextView mCollectionMessage;
        RecyclerView mGuangchangUserMessageimagelist;
        LinearLayout mCollectionRZong;
        CardView mCollectionCardview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mCollectHeadimage = (CircleImageView)itemView.findViewById(R.id.collect_headimage);
            mCollectionUname = (TextView)itemView.findViewById(R.id.collection_uname);
            mCollectionMessage = (TextView)itemView.findViewById(R.id.collection_message);
            mGuangchangUserMessageimagelist = (RecyclerView)itemView.findViewById(R.id.collection_image);
            mCollectionRZong = (LinearLayout)itemView.findViewById(R.id.collection_RZong);
            mCollectionCardview = (CardView)itemView.findViewById(R.id.collection_cardview);
        }
    }

    public CollectionList(Activity a,String Myxuehao) {
        this.activity = a;
        this.myxuehao = Myxuehao;
    }

    public void addCollection(List<UserCollection> u) {
        if (userCollections != null){
            userCollections.clear();
        }
        this.userCollections = u;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.collection_list, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    private String uxuehao;
    private String uname;
    private String udyanmic;
    private String myxuehao;
    private DynamicMessageDetailed dynamicMessageDetailed;
    private int key;
    private Handler dynamicHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Intent intent = new Intent(activity, DynamicDetailed.class);
                    if (msg.obj.equals("1")){
                        UserDynamicCollection.detailedCollection(2,udyanmic,myxuehao,null);
                        ToastZong.ShowToast(MyApplication.getContext(),"em...动态被删除了");
                    }else{
                        intent.putExtra("id",key);
                        intent.putExtra("uname",uname);
                        intent.putExtra("uxuehao",uxuehao);
                        intent.putExtra("dynamic", (Serializable) dynamicMessageDetailed.userSpace);
                    }
                    activity.startActivity(intent);
                    break;
                case 2:
                    ToastZong.ShowToast(MyApplication.getContext(),"超时.请重试");
                    break;
            }
        }
    };
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
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        userCollection = userCollections.get(position);
        holder.mCollectionUname.setText(userCollection.getUser_name());
        holder.mCollectionMessage.setText(userCollection.getMessages());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.mCollectionCardview.setClipToOutline(false);
        }

        if (userCollections.get(position).getHead_image()!=null){
            Glide.with(activity)
                    .load(userCollections.get(position).getHead_image())
                    .placeholder(R.drawable.nostartimage_three)
                    .fallback(R.drawable.defaultheadimage)
                    .error(R.drawable.defaultheadimage)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)//跳过内
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .circleCrop()
                    .into(holder.mCollectHeadimage);
        }

        if (!userCollections.get(position).getImages().equals("")) {
            holder.mGuangchangUserMessageimagelist.setVisibility(View.VISIBLE);
            holder.mGuangchangUserMessageimagelist.setLayoutManager(GuangChangImageToClass.newView(activity,userCollections.get(position).getImages(),userCollections.get(position).getXuehao()));
            GuangChangImageAdapter guangChangImageAdapter = new GuangChangImageAdapter(activity, GuangChangImageToClass.imageToClass(userCollections.get(position).getImages(),userCollections.get(position).getXuehao()));
            holder.mGuangchangUserMessageimagelist.setAdapter(guangChangImageAdapter);
        }

        holder.mCollectionCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uxuehao = userCollections.get(position).getXuehao();
                uname = userCollections.get(position).getUser_name();
                udyanmic = userCollections.get(position).getDynamic_id();
                dynamicMessageDetailed = new DynamicMessageDetailed(activity,userCollections.get(position).getXuehao(),userCollections.get(position).getDynamic_id(),dynamicHandler);
                dynamicMessageDetailed.obtainDynamic();
                key = 1;
            }
        });

        holder.mCollectHeadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObtainUser.obtainUser(activity,userCollections.get(position).getXuehao(),handler);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userCollections.size();
    }
}
