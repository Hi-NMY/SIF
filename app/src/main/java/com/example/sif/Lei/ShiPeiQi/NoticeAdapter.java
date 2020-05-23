package com.example.sif.Lei.ShiPeiQi;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.signature.MediaStoreSignature;
import com.example.sif.DynamicDetailed;
import com.example.sif.Lei.MyBroadcastReceiver.BroadcastRec;
import com.example.sif.Lei.MyToolClass.DynamicMessageDetailed;
import com.example.sif.Lei.MyToolClass.ObtainUser;
import com.example.sif.Lei.MyToolClass.ToastZong;
import com.example.sif.Lei.NiceImageView.CircleImageView;
import com.example.sif.MyApplication;
import com.example.sif.NeiBuLei.NoticeClass;
import com.example.sif.R;
import com.tamsiree.rxkit.RxTextTool;

import org.litepal.LitePal;

import java.io.Serializable;
import java.util.List;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> {

    public List<NoticeClass> noticeClasses;
    private Activity activity;
    private View view;
    private ViewHolder viewHolder;
    private NoticeClass noticeClass;
    private DynamicMessageDetailed dynamicMessageDetailed;
    public int noticeId;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    ToastZong.ShowToast(activity, "用户信息错误");
                    break;
                case 2:
                    ToastZong.ShowToast(activity, "用户信息错误");
                    break;
            }
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView mNoticeUserimage;
        RelativeLayout mNotiveRzong;
        TextView mNoticeMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mNoticeUserimage = (CircleImageView) itemView.findViewById(R.id.notice_userimage);
            mNotiveRzong = (RelativeLayout) itemView.findViewById(R.id.notive_rzong);
            mNoticeMessage = (TextView)itemView.findViewById(R.id.notice_message);
        }
    }

    public NoticeAdapter(Activity a, List<NoticeClass> n) {
        this.activity = a;
        if (n != null && n.size() > 0){
            this.noticeClasses = n;
            noticeId = noticeClasses.get(noticeClasses.size() - 1).getId();
        }
    }

    public void addNoticebottom(List<NoticeClass> n) {
        n.remove(0);
        if (n.size() == 0) {

        } else {
            this.noticeClasses.addAll(n);
            notifyItemRangeChanged(noticeClasses.size() - n.size(), noticeClasses.size() + 1);
        }
        noticeId = noticeClasses.get(noticeClasses.size() - 1).getId();
    }

    private String myXuehao;
    private String myName;
    public void myXuehao(String h,String n) {
        this.myXuehao = h;
        this.myName = n;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_list_item, null);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    private String updateTime;
    private int key;
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        noticeClass = noticeClasses.get(position);
        updateTime = String.valueOf(System.currentTimeMillis());

        switch (noticeClasses.get(position).getSee()){
            case 1:
                holder.mNotiveRzong.setAlpha(1f);
                break;
            case 2:
                holder.mNotiveRzong.setAlpha(0.4f);
                break;
        }

        switch (noticeClasses.get(position).getFun()){
            case 1:
                RxTextTool.getBuilder("")
                        .append("这位Sifer ")
                        .setResourceId(R.drawable.small_redheart)
                        .append(" 赞 ")
                        .setBold()
                        .append("了你的动态")
                        .into(holder.mNoticeMessage);
                break;
            case 2:
                RxTextTool.getBuilder("")
                        .append("有位Sifer ")
                        .append(" 评论 ")
                        .setBold()
                        .append("了你的动态")
                        .into(holder.mNoticeMessage);
                break;
            case 3:
                RxTextTool.getBuilder("")
                        .append("这位Sifer ")
                        .append(" 关注 ")
                        .setBold()
                        .append("了你")
                        .into(holder.mNoticeMessage);
                break;
            case 4:
                RxTextTool.getBuilder("")
                        .append("这位Sifer ")
                        .append(" Bui~ ")
                        .setBold()
                        .append("了一下你的语音")
                        .into(holder.mNoticeMessage);
                break;
        }

        if (!noticeClasses.get(position).getSendXueHao().equals(holder.mNoticeUserimage.getTag())) {
            holder.mNoticeUserimage.setTag(null);
            Glide.with(activity)
                    .load("http://nmy1206.natapp1.cc/UserImageServer/" + noticeClass.getSendXueHao() + "/HeadImage/myHeadImage.png")
                    .signature(new MediaStoreSignature(updateTime, 1, 1))
                    .placeholder(R.drawable.nostartimage_three)
                    .fallback(R.drawable.defaultheadimage)
                    .error(R.drawable.defaultheadimage)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .circleCrop()
                    .into(holder.mNoticeUserimage);
            holder.mNoticeUserimage.setTag(noticeClasses.get(position).getSendXueHao());
        }

        holder.mNoticeUserimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObtainUser.obtainUser(activity,noticeClasses.get(position).getSendXueHao(), handler);
            }
        });

        Handler dynamicHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        Intent intent = new Intent(activity, DynamicDetailed.class);
                        if (msg.obj.equals("1")) {
                            ToastZong.ShowToast(MyApplication.getContext(), "em...动态被删除了");
                        } else {
                            intent.putExtra("id", key);
                            intent.putExtra("uname",myName);
                            intent.putExtra("uxuehao", myXuehao);
                            intent.putExtra("dynamic", (Serializable) dynamicMessageDetailed.userSpace);
                            activity.startActivity(intent);
                        }
                        break;
                    case 2:
                        ToastZong.ShowToast(MyApplication.getContext(), "超时.请重试");
                        break;
                }
            }
        };

        holder.mNotiveRzong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (noticeClasses.get(position).getFun()){
                    case 1:
                        dynamicMessageDetailed = new DynamicMessageDetailed(activity, myXuehao, noticeClasses.get(position).getDynamicId(), dynamicHandler);
                        dynamicMessageDetailed.obtainDynamic();
                        key = 1;
                        holder.mNotiveRzong.setAlpha(0.4f);
                        notifyItemChanged(position);
                        updateSee(noticeClasses.get(position).getId());
                        break;
                    case 2:
                        dynamicMessageDetailed = new DynamicMessageDetailed(activity, myXuehao, noticeClasses.get(position).getDynamicId(), dynamicHandler);
                        dynamicMessageDetailed.obtainDynamic();
                        key = 1;
                        holder.mNotiveRzong.setAlpha(0.4f);
                        notifyItemChanged(position);
                        updateSee(noticeClasses.get(position).getId());
                        break;
                    case 3:
                        holder.mNotiveRzong.setAlpha(0.4f);
                        notifyItemChanged(position);
                        updateSee(noticeClasses.get(position).getId());
                        break;
                    case 4:
                        holder.mNotiveRzong.setAlpha(0.4f);
                        notifyItemChanged(position);
                        updateSee(noticeClasses.get(position).getId());
                        break;
                }
            }
        });
    }

    public void inspectNotice(boolean k){
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean a = false;
                List<NoticeClass> nc = LitePal.order("id desc").limit(noticeClasses.size()).find(NoticeClass.class);
                if (nc != null && nc.size() > 0){
                    for (NoticeClass n:nc){
                        if (n.getSee() == 1){
                            a = true;
                            break;
                        }
                    }
                    if (!a && !k){
                        BroadcastRec.sendReceiver(activity,"newNotice",1,"");
                    }
                }
            }
        }).start();
    }

    private void updateSee(int id){
        NoticeClass noticeClass = new NoticeClass();
        noticeClass.setSee(2);
        noticeClass.updateAll("id = ?", String.valueOf(id));
    }

    @Override
    public int getItemCount() {
        return noticeClasses.size();
    }
}
