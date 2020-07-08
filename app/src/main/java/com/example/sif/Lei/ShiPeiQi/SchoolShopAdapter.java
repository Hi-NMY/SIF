package com.example.sif.Lei.ShiPeiQi;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.signature.MediaStoreSignature;
import com.example.sif.Lei.LianJie.HttpUtil;
import com.example.sif.Lei.MyToolClass.GuangChangImageToClass;
import com.example.sif.Lei.MyToolClass.InValues;
import com.example.sif.Lei.MyToolClass.ObtainUser;
import com.example.sif.Lei.MyToolClass.ToastZong;
import com.example.sif.Lei.NiceImageView.CircleImageView;
import com.example.sif.NeiBuLei.SchoolShopClass;
import com.example.sif.NeiBuLei.UserNameClass;
import com.example.sif.R;
import com.example.sif.SchoolShop;
import com.google.gson.Gson;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import okhttp3.Call;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SchoolShopAdapter extends RecyclerView.Adapter<SchoolShopAdapter.ViewHolder> {

    private List<SchoolShopClass> schoolShopClasses;
    private Activity activity;
    private SchoolShop schoolShop;
    private View view;
    private ViewHolder viewHolder;
    public String date;
    private String myXueHao;
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
        CircleImageView mShopUserimage;
        TextView mShopUsername;
        RelativeLayout mShopState;
        TextView mShopStateText;
        TextView mShopNotice;
        RecyclerView mShopImagelist;
        TagFlowLayout mShopLabellist;
        TextView mShopTime;
        Button mShopTomessage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mShopUserimage = (CircleImageView)itemView.findViewById(R.id.shop_userimage);
            mShopUsername = (TextView)itemView.findViewById(R.id.shop_username);
            mShopState = (RelativeLayout)itemView.findViewById(R.id.shop_state);
            mShopStateText = (TextView) itemView.findViewById(R.id.shopstate_text);
            mShopNotice = (TextView)itemView.findViewById(R.id.shop_notice);
            mShopImagelist = (RecyclerView)itemView.findViewById(R.id.shop_imagelist);
            mShopLabellist = (TagFlowLayout)itemView.findViewById(R.id.shop_labellist);
            mShopTime = (TextView)itemView.findViewById(R.id.shop_time);
            mShopTomessage = (Button)itemView.findViewById(R.id.shop_tomessage);
        }
    }

    public SchoolShopAdapter(Activity a,SchoolShop sp,List<SchoolShopClass> s){
        this.activity = a;
        this.schoolShopClasses = s;
        this.schoolShop = sp;
        if (schoolShopClasses.size() > 0) {
            date = schoolShopClasses.get(schoolShopClasses.size() - 1).getSendtime();
        }
        SharedPreferences sharedPreferences = activity.getSharedPreferences("userSchool", Context.MODE_PRIVATE);
        myXueHao = sharedPreferences.getString("xuehao", "");
    }

    public void addNewShop(SchoolShopClass s) {
        this.schoolShopClasses.add(0, s);
        notifyItemInserted(0);
        notifyItemRangeChanged(0, schoolShopClasses.size() + 1);
    }

    public void addSchoolShopAdapter(List<SchoolShopClass> s) {
        s.remove(0);
        if (s.size() == 0) {
            ToastZong.ShowToast(activity, "阿欧，没有了");
        } else {
            this.schoolShopClasses.addAll(s);
            notifyItemRangeChanged(schoolShopClasses.size() - s.size(), schoolShopClasses.size() + 1);
        }
        date = schoolShopClasses.get(schoolShopClasses.size() - 1).getSendtime();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schoolshop_listitem, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    private SchoolShopClass schoolShopClass;
    private String updateTime;
    private UserNameClass userNameClass;
    private String obtainnamePath = "http://nmy1206.natapp1.cc/ObtainUserName.php";
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        schoolShopClass = schoolShopClasses.get(position);

        if (schoolShopClass.getXuehao().equals(myXueHao)){
            holder.mShopTomessage.setVisibility(View.INVISIBLE);
        }else {
            holder.mShopTomessage.setVisibility(View.VISIBLE);
        }

        final Handler userNameHanlder = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 0:
                        holder.mShopUsername.setText(userNameClass.getUser_name());
                        break;
                    case 1:
                        holder.mShopUsername.setText("");
                        break;
                }
            }
        };

        HttpUtil.obtainUserName(InValues.send(R.string.ObtainUserName),schoolShopClass.getXuehao(), new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String a = response.body().string();
                try {
                    userNameClass = new Gson().fromJson(a,UserNameClass.class);
                    Message message = new Message();
                    message.what = 0;
                    userNameHanlder.sendMessage(message);
                }catch (Exception e){
                    Message message = new Message();
                    message.what = 1;
                    userNameHanlder.sendMessage(message);
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Message message = new Message();
                message.what = 1;
                userNameHanlder.sendMessage(message);
            }
        });
        holder.mShopNotice.setText(schoolShopClass.getNotice());
        holder.mShopTime.setText(schoolShopClass.getSendtime());
        switch (schoolShopClass.getShopstate()) {
            case 0:
                holder.mShopState.setBackgroundResource(R.drawable.shop_state_bg);
                holder.mShopStateText.setText("正  售");
                break;
            case 1:
                holder.mShopState.setBackgroundResource(R.drawable.shop_state_bg2);
                holder.mShopStateText.setText("已售出");
                break;
            case 2:
                holder.mShopState.setBackgroundResource(R.drawable.shop_state_bg1);
                holder.mShopStateText.setText("暂  挂");
                break;
        }

        if (!schoolShopClasses.get(position).getCommodityimage().equals("")) {
            holder.mShopImagelist.setVisibility(View.VISIBLE);
            holder.mShopImagelist.setLayoutManager(GuangChangImageToClass.newView(activity,schoolShopClass.getCommodityimage(),schoolShopClass.getXuehao()));
            GuangChangImageAdapter guangChangImageAdapter = new GuangChangImageAdapter(activity, GuangChangImageToClass.imageToClass(schoolShopClass.getCommodityimage(),schoolShopClass.getXuehao()));
            holder.mShopImagelist.setAdapter(guangChangImageAdapter);
        }

        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        if (schoolShopClasses.get(position).getLabel() != null && !schoolShopClasses.get(position).getLabel().equals("")) {
            List<String> strings = new ArrayList<>();
            String[] s = schoolShopClasses.get(position).getLabel().split(",");
            for (int a = 0; a < s.length; a++) {
                strings.add(s[a]);
            }
            holder.mShopLabellist.setAdapter(new TagAdapter<String>(strings) {
                @Override
                public View getView(FlowLayout parent, int position, String o) {
                    View view = layoutInflater.inflate(R.layout.shop_label_item, holder.mShopLabellist, false);
                    TextView selet = (TextView) view.findViewById(R.id.text);
                    selet.setText(o);
                    return view;
                }

                @Override
                public void onSelected(int position, View view) {
                    super.onSelected(position, view);
                    TextView t = (TextView) view.findViewById(R.id.text);
                    schoolShop.searchStart(t.getText().toString());
                }
            });
        }

        updateTime = String.valueOf(System.currentTimeMillis());
        if (!schoolShopClasses.get(position).getXuehao().equals(holder.mShopUserimage.getTag())) {
            holder.mShopUserimage.setTag(null);
            Glide.with(activity)
                    .load(InValues.send(R.string.httpHeader) +"/UserImageServer/" + schoolShopClass.getXuehao() + "/HeadImage/myHeadImage.png")
                    .signature(new MediaStoreSignature(updateTime, 1, 1))
                    .placeholder(R.drawable.nostartimage_three)
                    .fallback(R.drawable.defaultheadimage)
                    .error(R.drawable.defaultheadimage)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .circleCrop()
                    .into(holder.mShopUserimage);
            holder.mShopUserimage.setTag(schoolShopClasses.get(position).getXuehao());
        }

        holder.mShopTomessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Conversation.ConversationType conversationType  = Conversation.ConversationType.PRIVATE;
                RongIM.getInstance().startConversation(activity , conversationType, schoolShopClasses.get(position).getXuehao(), holder.mShopUsername.getText().toString());
            }
        });

        holder.mShopUserimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObtainUser.obtainUser(activity, schoolShopClasses.get(position).getXuehao(), handler);
            }
        });
    }

    @Override
    public int getItemCount() {
        return schoolShopClasses.size();
    }
}
