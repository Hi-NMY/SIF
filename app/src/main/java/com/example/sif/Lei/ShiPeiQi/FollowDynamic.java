package com.example.sif.Lei.ShiPeiQi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.signature.MediaStoreSignature;
import com.example.sif.DynamicDetailed;
import com.example.sif.IbDetailed;
import com.example.sif.Lei.MyBroadcastReceiver.BroadcastRec;
import com.example.sif.Lei.MyToolClass.DynamicMessageDetailed;
import com.example.sif.Lei.MyToolClass.GlideRoundTransform;
import com.example.sif.Lei.MyToolClass.MyDateClass;
import com.example.sif.Lei.MyToolClass.MyVeryDiaLog;
import com.example.sif.Lei.MyToolClass.ObtainUser;
import com.example.sif.Lei.MyToolClass.SendGeTuiMessage;
import com.example.sif.Lei.MyToolClass.ShowDiaLog;
import com.example.sif.Lei.MyToolClass.ToastZong;
import com.example.sif.Lei.MyToolClass.UserDynamicThumb;
import com.example.sif.Lei.MyToolClass.UserFollow;
import com.example.sif.Lei.NiceImageView.CircleImageView;
import com.example.sif.MyApplication;
import com.example.sif.NeiBuLei.UserSpace;
import com.example.sif.R;
import com.tamsiree.rxui.view.dialog.RxDialog;
import com.tamsiree.rxui.view.dialog.RxDialogScaleView;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FollowDynamic extends RecyclerView.Adapter<FollowDynamic.ViewHolder> {

    private List<UserSpace> userSpaces;
    private Activity activity;
    private View view;
    private ViewHolder viewHolder;
    private int frush;
    public String date;
    private UserDynamicThumb userDynamicThumb;
    private UserSpace userSpace;
    private String myXueHao;

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
        ImageButton mGuangchangUsesGengduo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            gcUserName = (TextView)itemView.findViewById(R.id.guangchang_user_name);
            gcUserShiJian = (TextView)itemView.findViewById(R.id.guangchang_user_shijian);
            gcUserXinXi = (TextView)itemView.findViewById(R.id.guangchang_user_xinxi);
            gcUserImage = (CircleImageView) itemView.findViewById(R.id.guangchang_user_tou_image);
            gcUserZan = (ImageButton)itemView.findViewById(R.id.guangchang_user_dianzan);
            gcUserThumb = (TextView)itemView.findViewById(R.id.guangchang_user_thumb);
            imageView = (ImageView)itemView.findViewById(R.id.guangchang_user_messageimage);
            recyclerView = (RelativeLayout)itemView.findViewById(R.id.GuangChang_Message_detailed);
            gcUserComment = (TextView)itemView.findViewById(R.id.guangchang_user_message);
            gcUserC = (ImageButton)itemView.findViewById(R.id.guangchang_user_pinglun);
            mGuangchangIb = (TagFlowLayout)itemView.findViewById(R.id.guangchang_ib);
            mGuangchangUsesGengduo = (ImageButton)itemView.findViewById(R.id.guangchang_uses_gengduo);
        }
    }

    public FollowDynamic(Activity a, List<UserSpace> g){
        this.activity = a;
        frush = 1;
        if (userSpaces!=null){
            userSpaces.clear();
        }
        this.userSpaces = g;
        notifyDataSetChanged();
        if (userSpaces.size()>0){
            date = userSpaces.get(userSpaces.size()-1).getUser_shijian();
        }
        SharedPreferences sharedPreferences = activity.getSharedPreferences("userSchool", Context.MODE_PRIVATE);
        this.myXueHao = sharedPreferences.getString("xuehao","");
    }

    public void addFollowList(UserSpace u){
        this.userSpaces.add(0,u);
        notifyItemInserted(0);
        notifyItemRangeChanged(0,userSpaces.size()+1);
    }

    public void addFollowListbottom(List<UserSpace> u){
        u.remove(0);
        if (u.size() == 0){
            ToastZong.ShowToast(activity,"我也是有底线的");
        }else {
            this.userSpaces.addAll(u);
            notifyItemRangeChanged(userSpaces.size()-u.size(),userSpaces.size()+1);
        }
        date = userSpaces.get(userSpaces.size()-1).getUser_shijian();
        frush = 2;
    }

    public void myuserDynamicThumb(UserDynamicThumb u){
        this.userDynamicThumb = u;
    }

    private UserFollow userFollow;
    public void sendUserFollow(UserFollow u){
        this.userFollow = u;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.guangchang_listitem,parent,false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    private String updateTime;
    private String udynamicid;
    private String xuehao;
    private String a;
    private String uxuehao;
    private String uname;
    private DynamicMessageDetailed dynamicMessageDetailed;
    private int key;
    private RxDialogScaleView rxDialogScaleView;
    private RxDialog rxDialog;
    private Handler bitMapHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            rxDialog.dismiss();
            if (msg.obj != null){
                if (msg.what == 1){
                    ToastZong.ShowToast(activity,"图片加载错误");
                    rxDialogScaleView.setImage((Bitmap) msg.obj);
                }else {
                    rxDialogScaleView.setImage((Bitmap) msg.obj);
                }
            }else {
                ToastZong.ShowToast(activity,"错误");
            }
        }
    };
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        userSpace = userSpaces.get(position);
        holder.gcUserName.setText(userSpace.getUser_name());
        holder.gcUserShiJian.setText(MyDateClass.showDateClass(userSpace.getUser_shijian()));

        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        if (userSpaces.get(position).getBlock() != null && !userSpaces.get(position).getBlock().equals("")){
            List<String> strings = new ArrayList<>();
            String[] s = userSpaces.get(position).getBlock().split(",");
            for (int a = 0;a < s.length;a++){
                strings.add(s[a]);
            }
            holder.mGuangchangIb.setAdapter(new TagAdapter<String>(strings) {
                @Override
                public View getView(FlowLayout parent, int position, String o) {
                    View view = layoutInflater.inflate(R.layout.label_list1, holder.mGuangchangIb, false);
                    TextView selet = (TextView) view.findViewById(R.id.text);
                    selet.setText(o);
                    return view;
                }
            });
            holder.mGuangchangIb.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                @Override
                public boolean onTagClick(View view, int position, FlowLayout parent) {
                    TextView t = (TextView) view.findViewById(R.id.text);
                    Intent intent = new Intent(activity, IbDetailed.class);
                    intent.putExtra("ibname", t.getText().toString());
                    activity.startActivity(intent);
                    return true;
                }
            });
        }

        if (userSpace.getUser_xinxi()!=null){
            holder.gcUserXinXi.setText(userSpace.getUser_xinxi());
            holder.gcUserXinXi.setVisibility(View.VISIBLE);
        }

        if (userDynamicThumb.noYesThumb(userSpaces.get(position).getUser_dynamic_id())){
            holder.gcUserZan.setImageResource(R.drawable.yidianzan);
        }else {
            holder.gcUserZan.setImageResource(R.drawable.weidianzan);
        }

        if (frush == 1){
            updateTime = String.valueOf(System.currentTimeMillis());
            if (!userSpaces.get(position).getUser_xuehao().equals(holder.gcUserImage.getTag())){
                holder.gcUserImage.setTag(null);
                Glide.with(activity)
                        .load("http://nmy1206.natapp1.cc/UserImageServer/"+userSpace.getUser_xuehao()+"/HeadImage/myHeadImage.png")
                        .signature(new MediaStoreSignature(updateTime,1,1))
                        .placeholder(R.drawable.nostartimage_three)
                        .fallback(R.drawable.defaultheadimage)
                        .error(R.drawable.defaultheadimage)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .circleCrop()
                        .into(holder.gcUserImage);
                holder.gcUserImage.setTag(userSpaces.get(position).getUser_xuehao());
            }

            if (!userSpace.getUser_image_url().equals(holder.imageView.getTag())){
                holder.imageView.setTag(null);
                if (!userSpaces.get(position).getUser_image_url().equals("")){
                    holder.imageView.setVisibility(View.VISIBLE);
                    Glide.with(activity)
                            .load("http://nmy1206.natapp1.cc/"+userSpace.getUser_image_url())
                            .signature(new MediaStoreSignature(updateTime,1,1))
                            .placeholder(R.drawable.nostartimage_two)
                            .fallback(R.drawable.nostartimage_two)
                            .error(R.drawable.nostartimage_two)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .override(300,300)
                            .centerCrop()
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .transform(new GlideRoundTransform(6))
                            .into(holder.imageView);
                    holder.imageView.setTag(userSpace.getUser_image_url());
                }
            }

        }else {
            if (!userSpace.getUser_image_url().equals(holder.gcUserImage.getTag())){
                holder.gcUserImage.setTag(null);
                Glide.with(activity)
                        .load("http://nmy1206.natapp1.cc/UserImageServer/"+userSpace.getUser_xuehao()+"/HeadImage/myHeadImage.png")
                        .signature(new MediaStoreSignature(updateTime,1,1))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .placeholder(R.drawable.nostartimage_three)
                        .fallback(R.drawable.defaultheadimage)
                        .error(R.drawable.defaultheadimage)
                        .circleCrop()
                        .into(holder.gcUserImage);
                holder.gcUserImage.setTag(userSpace.getUser_image_url());
            }

            if (!userSpace.getUser_image_url().equals(holder.imageView.getTag())){
                holder.imageView.setTag(null);
                if (!userSpaces.get(position).getUser_image_url().equals("")){
                    holder.imageView.setVisibility(View.VISIBLE);
                    Glide.with(activity)
                            .load("http://nmy1206.natapp1.cc/"+userSpace.getUser_image_url())
                            .placeholder(R.drawable.nostartimage_two)
                            .fallback(R.drawable.nostartimage_two)
                            .error(R.drawable.nostartimage_two)
                            .override(300,300)
                            .centerCrop()
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .transform(new GlideRoundTransform(6))
                            .into(holder.imageView);
                    holder.imageView.setTag(userSpace.getUser_image_url());
                }
            }
        }


        final Handler thumbHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        if (msg.obj.equals("0")){
                            holder.gcUserThumb.setText(String.valueOf(Integer.parseInt(holder.gcUserThumb.getText().toString())+1));
                            holder.gcUserZan.setImageResource(R.drawable.yidianzan);
                            if (!userSpaces.get(position).getUser_xuehao().equals(myXueHao)){
                                SendGeTuiMessage.sendGeTuiMessage(1,userSpaces.get(position).getUser_xuehao(),myXueHao,"1",userSpaces.get(position).getUser_dynamic_id(),0);
                            }
                        }
                        if (msg.obj.equals("10")){
                            holder.gcUserThumb.setText(String.valueOf(Integer.parseInt(holder.gcUserThumb.getText().toString())-1));
                            holder.gcUserZan.setImageResource(R.drawable.weidianzan);
                        }
                        break;
                }
            }
        };

        if (userSpace.getUser_thumb() == 0){
            holder.gcUserThumb.setVisibility(View.INVISIBLE);
        }else {
            holder.gcUserThumb.setText(MyDateClass.sendMath(userSpace.getUser_thumb()));
            holder.gcUserThumb.setVisibility(View.VISIBLE);
        }

        if (userSpace.getUser_comment() == 0){
            holder.gcUserComment.setVisibility(View.INVISIBLE);
        }else {
            holder.gcUserComment.setText(MyDateClass.sendMath(userSpace.getUser_comment()));
            holder.gcUserComment.setVisibility(View.VISIBLE);
        }


        holder.gcUserZan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                udynamicid = userSpaces.get(position).getUser_dynamic_id();
                xuehao = userSpaces.get(position).getUser_xuehao();
                userDynamicThumb.userThumb(udynamicid,myXueHao,xuehao,thumbHandler);
            }
        });

        holder.gcUserImage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                ObtainUser.obtainUser(activity,userSpaces.get(position).getUser_xuehao(),handler);
            }
        });

        Handler dynamicHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        Intent intent = new Intent(activity, DynamicDetailed.class);
                        if (msg.obj.equals("1")){
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

        holder.recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uxuehao = userSpaces.get(position).getUser_xuehao();
                uname = userSpaces.get(position).getUser_name();
                dynamicMessageDetailed = new DynamicMessageDetailed(activity,userSpaces.get(position).getUser_xuehao(),userSpaces.get(position).getUser_dynamic_id(),dynamicHandler);
                dynamicMessageDetailed.obtainDynamic();
                key = 1;
            }
        });

        holder.gcUserC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                key = 2;
                uxuehao = userSpaces.get(position).getUser_xuehao();
                uname = userSpaces.get(position).getUser_name();
                dynamicMessageDetailed = new DynamicMessageDetailed(activity,userSpaces.get(position).getUser_xuehao(),userSpaces.get(position).getUser_dynamic_id(),dynamicHandler);
                dynamicMessageDetailed.obtainDynamic();
            }
        });

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rxDialogScaleView = new RxDialogScaleView(activity);
                rxDialog = new RxDialog(activity,R.style.tran_dialog);
                rxDialog.setCanceledOnTouchOutside(false);
                String name = String.valueOf(userSpaces.get(position).getUser_image_url()).substring(39);
                String NewName = "http://nmy1206.natapp1.cc/UserImageServer/"+userSpaces.get(position).getUser_xuehao()+"/ADynamicImage/"+name;
                String path1 = "http://nmy1206.natapp1.cc/"+userSpaces.get(position).getUser_image_url();
                MyVeryDiaLog.veryImageDiaLog(rxDialogScaleView,NewName,path1,bitMapHandler);
                MyVeryDiaLog.transparentDiaLog(activity,rxDialog);
            }
        });

        holder.mGuangchangUsesGengduo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                follColoXueHao = userSpaces.get(position).getUser_xuehao();
                if (follColoXueHao.equals(myXueHao)||userFollow.noYesFollow(follColoXueHao) == true){
                    View view = LayoutInflater.from(activity).inflate(R.layout.dialog_two, null);
                    view.setVerticalFadingEdgeEnabled(true);
                    showDiaLog = new ShowDiaLog(activity, R.style.AlertDialog_Function, view);
                    showDiaLog.logWindow(new ColorDrawable(Color.TRANSPARENT));
                    showDiaLog.showMyDiaLog();
                    Button followDynamic = (Button) view.findViewById(R.id.delete_GuangChang_Dynamic);
                    Button Cancel = (Button) view.findViewById(R.id.delete_cancel);
                    followDynamic.setText("举报");
                    startButton(1,followDynamic,Cancel,null);
                }else {
                    View view = LayoutInflater.from(activity).inflate(R.layout.dialog_three,null);
                    showDiaLog = new ShowDiaLog(activity,R.style.AlertDialog_Function,view);
                    showDiaLog.logWindow(new ColorDrawable(Color.TRANSPARENT));
                    showDiaLog.showMyDiaLog();
                    TextView title = (TextView)view.findViewById(R.id.dialog_text);
                    Button buttonOne = (Button)view.findViewById(R.id.button_one);
                    Button buttonTwo = (Button)view.findViewById(R.id.button_two);
                    Button buttonThree = (Button)view.findViewById(R.id.button_three);
                    title.setVisibility(View.GONE);
                    buttonOne.setText("举报");
                    buttonTwo.setText("关注");
                    startButton(2,buttonOne,buttonThree,buttonTwo);
                }
            }
        });
        //--------------------------------------------------------------------------------------------------------------------
    }

    private ShowDiaLog showDiaLog;
    private String follColoXueHao;
    private void startButton(int i,Button b1,Button b2,Button b3){
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportDynamic();
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDialog();
            }
        });

        if (i == 2){
            b3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    followDynamic();
                    BroadcastRec.sendReceiver(MyApplication.getContext(),"noyesFollowUser",0,null);
                }
            });
        }
    }

    private Handler followHander = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    if (msg.obj.equals("10")){
                        showDiaLog.closeMyDiaLog();
                        SendGeTuiMessage.sendGeTuiMessage(1,follColoXueHao,myXueHao,"1","-1",2);
                        // userFollow.userFollowPeople.add();
                    }
                    if (msg.obj.equals("11")){
                        ToastZong.ShowToast(activity,"取消关注");
                    }
                    break;
            }
        }
    };

    private void reportDynamic() {

    }

    private void followDynamic(){
        userFollow.sendmyFollow(follColoXueHao,followHander);
    }

    private void closeDialog(){
        showDiaLog.closeMyDiaLog();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        return userSpaces.size();
    }
}
