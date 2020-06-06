package com.example.sif.Lei.ShiPeiQi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.signature.MediaStoreSignature;
import com.example.sif.DynamicDetailed;
import com.example.sif.IbDetailed;
import com.example.sif.Lei.LianJie.HttpUtil;
import com.example.sif.Lei.MyBroadcastReceiver.BroadcastRec;
import com.example.sif.Lei.MyToolClass.*;
import com.example.sif.Lei.NiceImageView.CircleImageView;
import com.example.sif.MyApplication;
import com.example.sif.NeiBuLei.GuangChangUserXinXi;
import com.example.sif.NeiBuLei.UserSpace;
import com.example.sif.R;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.litepal.LitePal;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GuangChang extends RecyclerView.Adapter<GuangChang.ViewHolder> {
    private List<GuangChangUserXinXi> guangChangUserXinXis;
    private Activity activity;
    private String myXueHao;
    private ViewHolder viewHolder;
    private GuangChangUserXinXi guangChangUserXinXi;
    public String date;
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
        TextView gcUserName;
        TextView gcUserShiJian;
        TextView gcUserXinXi;
        TextView gcUserThumb;
        TextView gcUserComment;
        ImageButton gcUserZan;
        ImageButton gcUserC;
        CircleImageView gcUserImage;
        RelativeLayout recyclerView;
        TagFlowLayout mGuangchangIb;
        ImageButton mGuangchangUsesGengduo;
        RecyclerView mGuangchangUserMessageimagelist;
        TextView mGuangchangPlace;
        LinearLayout mGuangchangPlaceLlt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            gcUserName = (TextView) itemView.findViewById(R.id.guangchang_user_name);
            gcUserShiJian = (TextView) itemView.findViewById(R.id.guangchang_user_shijian);
            gcUserXinXi = (TextView) itemView.findViewById(R.id.guangchang_user_xinxi);
            gcUserImage = (CircleImageView) itemView.findViewById(R.id.guangchang_user_tou_image);
            gcUserZan = (ImageButton) itemView.findViewById(R.id.guangchang_user_dianzan);
            gcUserThumb = (TextView) itemView.findViewById(R.id.guangchang_user_thumb);
            recyclerView = (RelativeLayout) itemView.findViewById(R.id.GuangChang_Message_detailed);
            gcUserComment = (TextView) itemView.findViewById(R.id.guangchang_user_message);
            gcUserC = (ImageButton) itemView.findViewById(R.id.guangchang_user_pinglun);
            mGuangchangIb = (TagFlowLayout) itemView.findViewById(R.id.guangchang_ib);
            mGuangchangUsesGengduo = (ImageButton) itemView.findViewById(R.id.guangchang_uses_gengduo);
            mGuangchangUserMessageimagelist = (RecyclerView) itemView.findViewById(R.id.guangchang_user_messageimagelist);
            mGuangchangPlace = (TextView)itemView.findViewById(R.id.guangchang_place);
            mGuangchangPlaceLlt = (LinearLayout)itemView.findViewById(R.id.guangchang_place_llt);
        }
    }

    public GuangChang(Activity activity1, List<GuangChangUserXinXi> guangChangUserXinXi) {
        frush = 1;
        this.activity = activity1;
        if (guangChangUserXinXis != null) {
            guangChangUserXinXis.clear();
        }
        this.guangChangUserXinXis = guangChangUserXinXi;
        notifyDataSetChanged();
        if (guangChangUserXinXis.size() > 0) {
            date = guangChangUserXinXis.get(guangChangUserXinXis.size() - 1).getGc_user_shijian();
        }
        SharedPreferences sharedPreferences = activity.getSharedPreferences("userSchool", Context.MODE_PRIVATE);
        this.myXueHao = sharedPreferences.getString("xuehao", "");
    }

    public void addGuangChangList(GuangChangUserXinXi guangChangUserXinXi1) {
        this.guangChangUserXinXis.add(0, guangChangUserXinXi1);
        notifyItemInserted(0);
        notifyItemRangeChanged(0, guangChangUserXinXis.size() + 1);
    }

    public void addGuangChangListbottom(List<GuangChangUserXinXi> guangChangUserXinXi1) {
        guangChangUserXinXi1.remove(0);
        if (guangChangUserXinXi1.size() == 0) {
            ToastZong.ShowToast(activity, "我也是有底线的");
        } else {
            this.guangChangUserXinXis.addAll(guangChangUserXinXi1);
            notifyItemRangeChanged(guangChangUserXinXis.size() - guangChangUserXinXi1.size(), guangChangUserXinXis.size() + 1);
        }
        date = guangChangUserXinXis.get(guangChangUserXinXis.size() - 1).getGc_user_shijian();
        frush = 2;
    }

    private UserFollow userFollow;

    public void sendUserFollow(UserFollow u) {
        this.userFollow = u;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.guangchang_listitem, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    public void myuserDynamicThumb(UserDynamicThumb u) {
        this.userDynamicThumb = u;
    }

    public void deleteDynamicMy(String id) {
        int a = 0;
        for (GuangChangUserXinXi g : guangChangUserXinXis) {
            if (g.getGc_user_dynamic_id().equals(id)) {
                guangChangUserXinXis.remove(g);
                notifyItemRangeChanged(a, guangChangUserXinXis.size() + 1);
                notifyItemInserted(a);
                return;
            }
            a += 1;
        }
    }

    private String updateTime;
    private String udynamicid;
    private String xuehao;
    private String a;
    private String uxuehao;
    private String uname;
    private DynamicMessageDetailed dynamicMessageDetailed;
    private UserDynamicThumb userDynamicThumb;
    private int frush;
    private int key;

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        guangChangUserXinXi = guangChangUserXinXis.get(position);
        holder.gcUserName.setText(guangChangUserXinXi.getGc_user_name());
        holder.gcUserShiJian.setText(MyDateClass.showDateClass(guangChangUserXinXi.getGc_user_shijian()));

        if (!guangChangUserXinXi.getGc_user_place().equals("")) {
            holder.mGuangchangPlaceLlt.setVisibility(View.VISIBLE);
            holder.mGuangchangPlace.setText(guangChangUserXinXi.getGc_user_place());
        }else {
            holder.mGuangchangPlaceLlt.setVisibility(View.GONE);
        }

        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        if (guangChangUserXinXis.get(position).getBlock() != null && !guangChangUserXinXis.get(position).getBlock().equals("")) {
            List<String> strings = new ArrayList<>();
            String[] s = guangChangUserXinXis.get(position).getBlock().split(",");
            for (int a = 0; a < s.length; a++) {
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

        if (guangChangUserXinXi.getGc_user_xinxi() != null) {
            holder.gcUserXinXi.setText(guangChangUserXinXi.getGc_user_xinxi());
            holder.gcUserXinXi.setVisibility(View.VISIBLE);
        }

        if (userDynamicThumb.noYesThumb(guangChangUserXinXis.get(position).getGc_user_dynamic_id())) {
            holder.gcUserZan.setImageResource(R.drawable.yidianzan);
        } else {
            holder.gcUserZan.setImageResource(R.drawable.weidianzan);
        }

        if (frush == 1) {
            updateTime = String.valueOf(System.currentTimeMillis());
            if (!guangChangUserXinXis.get(position).getGc_user_xuehao().equals(holder.gcUserImage.getTag())) {
                holder.gcUserImage.setTag(null);
                Glide.with(activity)
                        .load("http://nmy1206.natapp1.cc/UserImageServer/" + guangChangUserXinXi.getGc_user_xuehao() + "/HeadImage/myHeadImage.png")
                        .signature(new MediaStoreSignature(updateTime, 1, 1))
                        .placeholder(R.drawable.nostartimage_three)
                        .fallback(R.drawable.defaultheadimage)
                        .error(R.drawable.defaultheadimage)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .circleCrop()
                        .into(holder.gcUserImage);
                holder.gcUserImage.setTag(guangChangUserXinXis.get(position).getGc_user_xuehao());
            }

            if (!guangChangUserXinXis.get(position).getGc_user_image_url().equals("")) {
                holder.mGuangchangUserMessageimagelist.setVisibility(View.VISIBLE);
                holder.mGuangchangUserMessageimagelist.setLayoutManager(GuangChangImageToClass.newView(activity, guangChangUserXinXi.getGc_user_image_url(), guangChangUserXinXi.getGc_user_xuehao()));
                GuangChangImageAdapter guangChangImageAdapter = new GuangChangImageAdapter(activity, GuangChangImageToClass.imageToClass(guangChangUserXinXi.getGc_user_image_url(), guangChangUserXinXi.getGc_user_xuehao()));
                holder.mGuangchangUserMessageimagelist.setAdapter(guangChangImageAdapter);
            }

        } else {
            if (!guangChangUserXinXi.getGc_user_image_url().equals(holder.gcUserImage.getTag())) {
                holder.gcUserImage.setTag(null);
                Glide.with(activity)
                        .load("http://nmy1206.natapp1.cc/UserImageServer/" + guangChangUserXinXi.getGc_user_xuehao() + "/HeadImage/myHeadImage.png")
                        .signature(new MediaStoreSignature(updateTime, 1, 1))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .placeholder(R.drawable.nostartimage_three)
                        .fallback(R.drawable.defaultheadimage)
                        .error(R.drawable.defaultheadimage)
                        .circleCrop()
                        .into(holder.gcUserImage);
                holder.gcUserImage.setTag(guangChangUserXinXi.getGc_user_image_url());
            }

            if (!guangChangUserXinXis.get(position).getGc_user_image_url().equals("")) {
                holder.mGuangchangUserMessageimagelist.setVisibility(View.VISIBLE);
                holder.mGuangchangUserMessageimagelist.setLayoutManager(GuangChangImageToClass.newView(activity, guangChangUserXinXi.getGc_user_image_url(), guangChangUserXinXi.getGc_user_xuehao()));
                GuangChangImageAdapter guangChangImageAdapter = new GuangChangImageAdapter(activity, GuangChangImageToClass.imageToClass(guangChangUserXinXi.getGc_user_image_url(), guangChangUserXinXi.getGc_user_xuehao()));
                holder.mGuangchangUserMessageimagelist.setAdapter(guangChangImageAdapter);
            }
        }


        final Handler thumbHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        if (msg.obj.equals("0")) {
                            holder.gcUserThumb.setText(String.valueOf(Integer.parseInt(holder.gcUserThumb.getText().toString()) + 1));
                            holder.gcUserZan.setImageResource(R.drawable.yidianzan);
                            if (!guangChangUserXinXis.get(position).getGc_user_xuehao().equals(myXueHao)) {
                                SendGeTuiMessage.sendGeTuiMessage(1, guangChangUserXinXis.get(position).getGc_user_xuehao(), myXueHao, "1", guangChangUserXinXis.get(position).getGc_user_dynamic_id(), 0);
                            }
                        }
                        if (msg.obj.equals("10")) {
                            if (Integer.parseInt(holder.gcUserThumb.getText().toString()) != 0 && holder.gcUserThumb.getText().toString() != null) {
                                holder.gcUserThumb.setText(String.valueOf(Integer.parseInt(holder.gcUserThumb.getText().toString()) - 1));
                                holder.gcUserZan.setImageResource(R.drawable.weidianzan);
                            }
                        }
                        break;
                }
            }
        };

        if (guangChangUserXinXi.getGc_user_dynamic_thumb() == 0) {
            holder.gcUserThumb.setVisibility(View.INVISIBLE);
        } else {
            holder.gcUserThumb.setText(MyDateClass.sendMath(guangChangUserXinXi.getGc_user_dynamic_thumb()));
            holder.gcUserThumb.setVisibility(View.VISIBLE);
        }

        if (guangChangUserXinXi.getGc_user_dynamic_comment() == 0) {
            holder.gcUserComment.setVisibility(View.INVISIBLE);
        } else {
            holder.gcUserComment.setText(MyDateClass.sendMath(guangChangUserXinXi.getGc_user_dynamic_comment()));
            holder.gcUserComment.setVisibility(View.VISIBLE);
        }


        holder.gcUserZan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                udynamicid = guangChangUserXinXis.get(position).getGc_user_dynamic_id();
                xuehao = guangChangUserXinXis.get(position).getGc_user_xuehao();
                userDynamicThumb.userThumb(udynamicid, myXueHao, xuehao, thumbHandler);
            }
        });

        holder.gcUserImage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                ObtainUser.obtainUser(activity, guangChangUserXinXis.get(position).getGc_user_xuehao(), handler);
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
                            intent.putExtra("uname", uname);
                            intent.putExtra("uxuehao", uxuehao);
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

        holder.recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uxuehao = guangChangUserXinXis.get(position).getGc_user_xuehao();
                uname = guangChangUserXinXis.get(position).getGc_user_name();
                dynamicMessageDetailed = new DynamicMessageDetailed(activity, guangChangUserXinXis.get(position).getGc_user_xuehao(), guangChangUserXinXis.get(position).getGc_user_dynamic_id(), dynamicHandler);
                dynamicMessageDetailed.obtainDynamic();
                key = 1;
            }
        });

        holder.gcUserC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                key = 2;
                uxuehao = guangChangUserXinXis.get(position).getGc_user_xuehao();
                uname = guangChangUserXinXis.get(position).getGc_user_name();
                dynamicMessageDetailed = new DynamicMessageDetailed(activity, guangChangUserXinXis.get(position).getGc_user_xuehao(), guangChangUserXinXis.get(position).getGc_user_dynamic_id(), dynamicHandler);
                dynamicMessageDetailed.obtainDynamic();
            }
        });

        holder.mGuangchangUsesGengduo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                follColoXueHao = guangChangUserXinXis.get(position).getGc_user_xuehao();
                deleteId = guangChangUserXinXis.get(position).getGc_user_dynamic_id();
                if (follColoXueHao.equals(myXueHao)) {
                    View view = LayoutInflater.from(activity).inflate(R.layout.dialog_two, null);
                    view.setVerticalFadingEdgeEnabled(true);
                    showDiaLog = new ShowDiaLog(activity, R.style.AlertDialog_Function, view);
                    showDiaLog.logWindow(new ColorDrawable(Color.TRANSPARENT));
                    showDiaLog.showMyDiaLog();
                    Button followDynamic = (Button) view.findViewById(R.id.delete_GuangChang_Dynamic);
                    Button Cancel = (Button) view.findViewById(R.id.delete_cancel);
                    followDynamic.setText("删除");
                    startButton(1, followDynamic, Cancel, null);
                } else if (userFollow.noYesFollow(follColoXueHao) == true) {
                    View view = LayoutInflater.from(activity).inflate(R.layout.dialog_two, null);
                    view.setVerticalFadingEdgeEnabled(true);
                    showDiaLog = new ShowDiaLog(activity, R.style.AlertDialog_Function, view);
                    showDiaLog.logWindow(new ColorDrawable(Color.TRANSPARENT));
                    showDiaLog.showMyDiaLog();
                    Button followDynamic = (Button) view.findViewById(R.id.delete_GuangChang_Dynamic);
                    Button Cancel = (Button) view.findViewById(R.id.delete_cancel);
                    followDynamic.setText("举报");
                    startButton(3, followDynamic, Cancel, null);
                } else {
                    View view = LayoutInflater.from(activity).inflate(R.layout.dialog_three, null);
                    showDiaLog = new ShowDiaLog(activity, R.style.AlertDialog_Function, view);
                    showDiaLog.logWindow(new ColorDrawable(Color.TRANSPARENT));
                    showDiaLog.showMyDiaLog();
                    TextView title = (TextView) view.findViewById(R.id.dialog_text);
                    Button buttonOne = (Button) view.findViewById(R.id.button_one);
                    Button buttonTwo = (Button) view.findViewById(R.id.button_two);
                    Button buttonThree = (Button) view.findViewById(R.id.button_three);
                    title.setVisibility(View.GONE);
                    buttonOne.setText("举报");
                    buttonTwo.setText("关注");
                    startButton(2, buttonOne, buttonThree, buttonTwo);
                }
            }
        });


        //--------------------------------------------------------------------------------------------------------------------
    }

    private ShowDiaLog showDiaLog;
    private String follColoXueHao;

    private void startButton(int i, Button b1, Button b2, Button b3) {
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i != 1) {
                    reportDynamic();
                } else {
                    deleteMyDynamic();
                }
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDialog();
            }
        });

        if (i == 2) {
            b3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    followDynamic();
                }
            });
        }
    }

    private Handler followHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (msg.obj.equals("10")) {
                        showDiaLog.closeMyDiaLog();
                        SendGeTuiMessage.sendGeTuiMessage(1, follColoXueHao, myXueHao, "1", "-1", 2);
                        BroadcastRec.sendReceiver(MyApplication.getContext(), "noyesFollowUser", 0, null);
                        // userFollow.userFollowPeople.add();
                    }
                    break;
            }
        }
    };

    private void reportDynamic() {
        ToastZong.ShowToast(activity, "举报成功");
        showDiaLog.closeMyDiaLog();
    }

    private Handler deletehandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (msg.obj.equals("0")) {
                        LitePal.deleteAll(UserSpace.class, "user_dynamic_id = ?", udynamicid);
                        showDiaLog.closeMyDiaLog();
                    }
                    break;
            }
        }
    };

    private String deleteId;
    private String path = "http://nmy1206.natapp1.cc/deleteDynamic.php";

    private void deleteMyDynamic() {
        if (deleteId != null) {
            deleteDynamicMy(deleteId);
            BroadcastRec.sendReceiver(MyApplication.getContext(), "refreshMySpace", 0, null);
            HttpUtil.deleteDynamic(path, deleteId, myXueHao, new Callback() {
                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String a = response.body().string();
                    Message message = new Message();
                    message.what = 1;
                    message.obj = a;
                    deletehandler.sendMessage(message);
                }

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }
            });
        }
    }

    private void followDynamic() {
        userFollow.sendmyFollow(follColoXueHao, followHander);
    }

    private void closeDialog() {
        showDiaLog.closeMyDiaLog();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return guangChangUserXinXis.size();
    }

}