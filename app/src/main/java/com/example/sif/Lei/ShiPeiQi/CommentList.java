package com.example.sif.Lei.ShiPeiQi;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.sif.Lei.MyBroadcastReceiver.BroadcastRec;
import com.example.sif.Lei.MyToolClass.*;
import com.example.sif.Lei.NiceImageView.CircleImageView;
import com.example.sif.MyApplication;
import com.example.sif.NeiBuLei.CommentMessage;
import com.example.sif.R;
import com.tamsiree.rxkit.RxTextTool;

import java.util.List;

public class CommentList extends RecyclerView.Adapter<CommentList.ViewHolder> {

    public List<CommentMessage> commentMessages;
    private View view;
    private Activity activity;
    private String xuehao;
    private String myXueHao;
    public String date;

    private CommentMessage commentMessage;

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView mDynamicCommentHeadimage;
        TextView mDynamicCommentName;
        TextView mDynamicCommentTime;
        TextView mDynamicCommentMessage;
        TextView mDynamicCommentMessageFloor;
        RelativeLayout mCommentR;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mDynamicCommentHeadimage = (CircleImageView)itemView.findViewById(R.id.dynamic_comment_headimage);
            mDynamicCommentName = (TextView)itemView.findViewById(R.id.dynamic_comment_name);
            mDynamicCommentTime = (TextView)itemView.findViewById(R.id.dynamic_comment_time);
            mDynamicCommentMessage = (TextView)itemView.findViewById(R.id.dynamic_comment_message);
            mDynamicCommentMessageFloor = (TextView)itemView.findViewById(R.id.dynamic_comment_message_floor);
            mCommentR = (RelativeLayout)itemView.findViewById(R.id.comment_R);
        }
    }

    public CommentList(Activity a){
        this.activity = a;
    }

    public CommentList(Activity a, List<CommentMessage> cs) {
        this.activity = a;
        this.commentMessages = cs;
        notifyDataSetChanged();
        date = commentMessages.get(commentMessages.size()-1).getComment_time();
    }

    public void sendActivity(Activity a){
        this.activity = a;
    }

    public void sendXueHao(String x,String mx){
        this.xuehao = x;
        this.myXueHao = mx;
    }

    private boolean myComment;
    private int newMyComment = 0;
    public void addComment(List<CommentMessage> c){
       // floor = 1;
        if (commentMessages == null||commentMessages.size() == 0){
            this.commentMessages = c;
            notifyDataSetChanged();
            date = commentMessages.get(commentMessages.size()-1).getComment_time();
        }else {
            this.commentMessages.addAll(0,c);
            notifyItemInserted(0);
            notifyItemRangeChanged(0,commentMessages.size()+1);
            newMyComment += 1;
            myComment = true;
        }
    }

    public void addOldComment(List<CommentMessage> c){
        // floor = 1;
        c.remove(0);
        if (c.size() == 0){
            ToastZong.ShowToast(activity,"没有更多评论了");
        }else {
            if (myComment){
                for (int i = 0;i < newMyComment;i++){
                    commentMessages.remove(0);
                }
                this.commentMessages.addAll(c);
                notifyDataSetChanged();
                myComment = false;
            }else {
                this.commentMessages.addAll(c);
                notifyItemRangeChanged(commentMessages.size()-c.size(),commentMessages.size()+1);
            }

        }
        date = commentMessages.get(commentMessages.size()-1).getComment_time();
    }

    public void deleteComment(){
        commentMessages.remove(removePosition);
        notifyItemRangeChanged(removePosition,commentMessages.size()+1);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_comment_list, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

  //  private int floor = 1;
    private static int removePosition;
    public int deleteId;
    public String commentTime;
    public String commentXueHao;
    public String nowXueHao;
    public String nowUserName;
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        commentMessage = commentMessages.get(position);
        holder.mDynamicCommentName.setText(UserNameJudge.judgeName(holder.mDynamicCommentName,commentMessage.getComment_username(),commentMessage.getComment_xuehao(),xuehao));
        holder.mDynamicCommentTime.setText(MyDateClass.showDateClass(commentMessage.getComment_time()));
        if (!commentMessage.getTo_username().equals("")){
            RxTextTool.getBuilder("")
                    .append("回复")
                    .append(UserNameJudge.judgeName(commentMessage.getTo_username(),xuehao))
                    .setForegroundColor(activity.getColor(R.color.bilan))
                    .append(":" + commentMessage.getDynamic_comment())
                    .into(holder.mDynamicCommentMessage);
        }else {
            holder.mDynamicCommentMessage.setText(commentMessage.getDynamic_comment());
        }
      //  holder.mDynamicCommentMessageFloor.setText(sendfloor()+"楼");

        Glide.with(activity)
                .load(InValues.send(R.string.httpHeader) +"/UserImageServer/" + commentMessage.getComment_xuehao() + "/HeadImage/myHeadImage.png")
                .placeholder(R.drawable.nostartimage_three)
                .fallback(R.drawable.defaultheadimage)
                .error(R.drawable.defaultheadimage)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)//跳过内存缓存
                .transition(DrawableTransitionOptions.withCrossFade())
                .circleCrop()
                .into(holder.mDynamicCommentHeadimage);

        Handler handlerspace = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        ToastZong.ShowToast(MyApplication.getContext(),"用户信息错误");
                        break;
                    case 2:
                        ToastZong.ShowToast(MyApplication.getContext(),"用户信息错误");
                        break;
                }
            }
        };

        holder.mDynamicCommentHeadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObtainUser.obtainUser(activity,commentMessages.get(position).getComment_xuehao(),handlerspace);
            }
        });


        holder.mCommentR.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                obtainNowUser(position);
                MyPopuoWindow myPopuoWindow = new MyPopuoWindow(MyApplication.getContext());
                myPopuoWindow.showAsDropDown(holder.mCommentR,-400,-150,Gravity.RIGHT);
                removePosition = position;
                deleteId = commentMessages.get(position).getId();
                commentTime = commentMessages.get(position).getComment_time();
                commentXueHao = commentMessages.get(position).getComment_xuehao();
                if (xuehao.equals(myXueHao)){
                    myPopuoWindow.textFunction(2);
                }else {
                    if (commentMessages.get(position).getComment_xuehao().equals(myXueHao)){
                        myPopuoWindow.textFunction(2);
                    }else {
                        myPopuoWindow.textFunction(1);
                    }
                }
                return true;
            }
        });

        holder.mCommentR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!commentMessages.get(position).getComment_xuehao().equals(myXueHao)){
                    obtainNowUser(position);
                    BroadcastRec.sendReceiver(MyApplication.getContext(),"replyComment",0,"");
                }
            }
        });
    }

    private void obtainNowUser(int position){
        nowUserName = commentMessages.get(position).getComment_username();
        nowXueHao = commentMessages.get(position).getComment_xuehao();
    }

    @Override
    public int getItemCount() {
        return commentMessages.size();
    }

}
