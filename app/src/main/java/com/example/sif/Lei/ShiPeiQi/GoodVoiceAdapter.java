package com.example.sif.Lei.ShiPeiQi;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
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
import com.example.sif.Lei.MyToolClass.CenterLayoutManager;
import com.example.sif.Lei.MyToolClass.ObtainUser;
import com.example.sif.Lei.MyToolClass.SendGeTuiMessage;
import com.example.sif.Lei.MyToolClass.ToastZong;
import com.example.sif.Lei.NiceImageView.CircleImageView;
import com.example.sif.Lei.NiceImageView.SolidImageView;
import com.example.sif.MyApplication;
import com.example.sif.NeiBuLei.NiceVoice;
import com.example.sif.R;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.io.IOException;
import java.util.List;

public class GoodVoiceAdapter extends RecyclerView.Adapter<GoodVoiceAdapter.ViewHolder> {

    public List<NiceVoice> niceVoices;
    private NiceVoice niceVoice;
    private Activity activity;
    private RecyclerView recyclerView;
    private CenterLayoutManager centerLayoutManager;
    public MediaPlayer player;
    private ViewHolder viewHolder;
    public int voiceId;

    private int[] drawables = new int[]{R.drawable.box1,R.drawable.box2,R.drawable.box3,R.drawable.box4,R.drawable.box5,R.drawable.box6};


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mGvUsername;
        RelativeLayout mGvRzong;
        CircleImageView mVoiceImage;
        CircleImageView usersmallImage;
        SolidImageView solidImageView;
        Button labelName;
        ImageView userSex;
        ImageView gvStartStop;
        ShineButton voiceThumb;
        RelativeLayout bottomWhite;
        ImageView voiceReport;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mGvUsername = (TextView) itemView.findViewById(R.id.gv_username);
            usersmallImage = (CircleImageView)itemView.findViewById(R.id.gv_userimage);
            mGvRzong = (RelativeLayout) itemView.findViewById(R.id.gv_Rzong);
            mVoiceImage = (CircleImageView)itemView.findViewById(R.id.voice_image);
            solidImageView = (SolidImageView)itemView.findViewById(R.id.ib_list_image);
            labelName = (Button)itemView.findViewById(R.id.label_name);
            userSex = (ImageView)itemView.findViewById(R.id.gv_usersex);
            gvStartStop = (ImageView)itemView.findViewById(R.id.gv_start_stop);
            voiceThumb = (ShineButton) itemView.findViewById(R.id.voice_thumb);
            bottomWhite = (RelativeLayout)itemView.findViewById(R.id.bottom_white);
            voiceReport = (ImageView)itemView.findViewById(R.id.voice_report);
        }
    }

    public GoodVoiceAdapter(List<NiceVoice> g, RecyclerView r, CenterLayoutManager c, Activity a) {
        this.niceVoices = g;
        this.recyclerView = r;
        this.centerLayoutManager = c;
        this.activity = a;
        player = new MediaPlayer();
        if (g.size() > 0){
            voiceId = g.get(g.size() - 1).getId();
        }
    }

    public void addMoreVoiceList(List<NiceVoice> g){
        if (g != null && g.size() > 0){
            g.remove(0);
        }
        if (g.size() == 0){
            ToastZong.ShowToast(MyApplication.getContext(),"没有更多了");
        }else {
            voiceId = g.get(g.size() - 1).getId();
            niceVoices.addAll(g);
            notifyItemRangeChanged(niceVoices.size() - g.size(), niceVoices.size() + 1);
        }
    }

    public void addNewVociceMy(NiceVoice niceVoice){
        this.niceVoices.add(0, niceVoice);
        notifyItemInserted(1);
        notifyItemRangeChanged(0, niceVoices.size() + 1);
    }

    private String myXueHao;
    public void sendXueHao(String x){
        this.myXueHao = x;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.goodvoice_list, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    public int select;
    private String updateTime;
    private Animation animation3;
    public boolean buiKey = true;
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        animation3 = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f );
        animation3.setRepeatCount(Animation.INFINITE);
        animation3.setInterpolator(new LinearInterpolator());
        animation3.setDuration(10000);
        updateTime = String.valueOf(System.currentTimeMillis());
        niceVoice = niceVoices.get(position);
        holder.mGvUsername.setText(niceVoice.getGv_username());
        holder.labelName.setText(niceVoice.getGv_label());
        switch (niceVoice.getGv_label()){
            case "随心说":
                holder.labelName.setBackgroundResource(R.drawable.blue_label);
                break;
            case "聆心声":
                holder.labelName.setBackgroundResource(R.drawable.yellow_label);
                break;
            case "趣   说":
                holder.labelName.setBackgroundResource(R.drawable.purple_label);
                break;
            case "美   音":
                holder.labelName.setBackgroundResource(R.drawable.red_label);
                break;
        }

        if (niceVoice.getGv_usersex().equals("男")){
            holder.userSex.setBackgroundResource(R.drawable.boy);
        }else {
            holder.userSex.setBackgroundResource(R.drawable.girl);
        }

        holder.voiceThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buiKey && !niceVoices.get(position).getGv_userxuehao().equals(myXueHao)){
                    SendGeTuiMessage.sendGeTuiMessage(1, niceVoices.get(position).getGv_userxuehao(),myXueHao,"1",String.valueOf(niceVoices.get(position).getId()),3);
                    buiKey = false;
                }
            }
        });


        if (!niceVoices.get(position).getGv_userxuehao().equals(holder.usersmallImage.getTag())) {
            updateTime = String.valueOf(System.currentTimeMillis());
            holder.usersmallImage.setTag(null);
            Glide.with(activity)
                    .load("http://nmy1206.natapp1.cc/UserImageServer/" + niceVoice.getGv_userxuehao() + "/HeadImage/myHeadImage.png")
                    .signature(new MediaStoreSignature(updateTime, 1, 1))
                    .placeholder(R.drawable.nostartimage_three)
                    .fallback(R.drawable.defaultheadimage)
                    .error(R.drawable.defaultheadimage)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .circleCrop()
                    .into(holder.usersmallImage);
            holder.usersmallImage.setTag(niceVoices.get(position).getGv_userxuehao());
        }

        if (!niceVoices.get(position).getGv_userxuehao().equals(holder.mVoiceImage.getTag())) {
            updateTime = String.valueOf(System.currentTimeMillis());
            holder.mVoiceImage.setTag(null);
            Glide.with(activity)
                    .load("http://nmy1206.natapp1.cc/UserImageServer/" + niceVoice.getGv_userxuehao() + "/HeadImage/myHeadImage.png")
                    .signature(new MediaStoreSignature(updateTime, 1, 1))
                    .placeholder(R.drawable.nostartimage_three)
                    .fallback(R.drawable.defaultheadimage)
                    .error(R.drawable.defaultheadimage)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .circleCrop()
                    .into(holder.mVoiceImage);
            holder.mVoiceImage.setTag(niceVoices.get(position).getGv_userxuehao());
        }

        if (!"1".equals(holder.solidImageView.getTag())) {
            holder.solidImageView.setTag(null);
            int draId = drawables[(int) (Math.random() * 6)];
            Glide.with(activity)
                    .load(activity.getDrawable(draId))
                    .signature(new MediaStoreSignature(updateTime, 1, 1))
                    .placeholder(R.drawable.nostartimage_two)
                    .fallback(R.drawable.nostartimage_two)
                    .error(R.drawable.nostartimage_two)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .centerCrop()
                    .into(holder.solidImageView);
            holder.solidImageView.setTag("1");
        }

        holder.usersmallImage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                ObtainUser.obtainUser(activity, niceVoices.get(position).getGv_userxuehao(), null);
            }
        });

        holder.gvStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (voiceKey){
                    case 0:
                        stopVoice();
                        voiceKey = 1;
                        one.clearAnimation();
                        holder.gvStartStop.setBackgroundResource(R.drawable.voicestart);
                        break;
                    case 1:
                        startVoice(position,one,holder.gvStartStop);
                        voiceKey = 0;
                        one.startAnimation(animation3);
                        holder.gvStartStop.setBackgroundResource(R.drawable.voicestop);
                        break;
                }
            }
        });

        holder.voiceReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastZong.ShowToast(MyApplication.getContext(),"举报成功");
            }
        });

    }

    private CircleImageView one;
    private ImageView two;
    private int voiceKey = 0;
    public void startVoice(int position, CircleImageView mVoiceImage,ImageView imageView){
        one = mVoiceImage;
        two = imageView;
        player.release();
        try {
            imageView.setBackgroundResource(R.drawable.voicestop);
            player = new MediaPlayer();
            player.setDataSource("http://nmy1206.natapp1.cc/"+niceVoices.get(position).getGv_voice());
            player.prepare();
            player.start();
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mVoiceImage.clearAnimation();
                    imageView.setBackgroundResource(R.drawable.voicestart);
                    voiceKey = 1;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pauseVoice(){
        try {
            if (player != null){
                player.pause();
                one.clearAnimation();
                two.setBackgroundResource(R.drawable.voicestop);
                voiceKey = 0;
            }
        }catch (Exception e){
            stopVoice();
        }
    }

    public void stopVoice(){
        player.release();
        if (one != null ){
            one.clearAnimation();
        }
        if (two != null){
            two.setBackgroundResource(R.drawable.voicestop);
        }
        voiceKey = 0;
    }

    public void startVoice(){
        try {
            if (player != null){
                player.start();
            }
        }catch (IllegalStateException e){
            stopVoice();
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return niceVoices.size();
    }
}
