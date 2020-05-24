package com.example.sif.Lei.ShiPeiQi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sif.DynamicDetailed;
import com.example.sif.IbDetailed;
import com.example.sif.Lei.LianJie.HttpUtil;
import com.example.sif.Lei.MyBroadcastReceiver.BroadcastRec;
import com.example.sif.Lei.MyToolClass.DynamicMessageDetailed;
import com.example.sif.Lei.MyToolClass.GuangChangImageToClass;
import com.example.sif.Lei.MyToolClass.SendGeTuiMessage;
import com.example.sif.Lei.MyToolClass.ShowDiaLog;
import com.example.sif.Lei.MyToolClass.ToastZong;
import com.example.sif.Lei.MyToolClass.UserDynamicThumb;
import com.example.sif.MyApplication;
import com.example.sif.NeiBuLei.UserSpace;
import com.example.sif.R;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.jetbrains.annotations.NotNull;
import org.litepal.LitePal;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MySpacePack extends RecyclerView.Adapter<MySpacePack.ViewHolder> {
    private List<UserSpace> userSpaces;
    private Activity activity;
    private int function;
    private static String xuehao;
    private String myXueHao;
    public int i;
    public String date;
    private int frush;
    private String uname;
    private String ip;

    private UserSpace userSpace;
    private DynamicMessageDetailed dynamicMessageDetailed;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView time;
        TextView message;
        TextView myThumb;
        TextView myComment;
        RelativeLayout Rzong;
        ImageButton myZan;
        ImageButton imageButton;
        ImageButton myC;
        TagFlowLayout mUserspaceIb;
        RecyclerView mUserspaceMessageimagelist;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time = (TextView) itemView.findViewById(R.id.MySpace_List_shijian);
            message = (TextView) itemView.findViewById(R.id.MySpace_List_Message);
            myThumb = (TextView) itemView.findViewById(R.id.MySpace_thumb);
            myZan = (ImageButton) itemView.findViewById(R.id.MySpace_List_dianzan);
            imageButton = (ImageButton) itemView.findViewById(R.id.MySpace_GengDuo_Button);
            myComment = (TextView) itemView.findViewById(R.id.MySpace_message);
            Rzong = (RelativeLayout) itemView.findViewById(R.id.myspace_RZong);
            myC = (ImageButton) itemView.findViewById(R.id.MySpace_List_pinglun);
            mUserspaceIb = (TagFlowLayout) itemView.findViewById(R.id.userspace_ib);
            mUserspaceMessageimagelist = (RecyclerView)itemView.findViewById(R.id.userspace_messageimagelist);
        }
    }

    public void moreFunction(int i, String xuehao1, String n) {
        this.function = i;
        this.xuehao = xuehao1;
        this.uname = n;
    }

    public MySpacePack(Activity activity1, List<UserSpace> userSpaces1) {
        frush = 1;
        this.activity = activity1;
        if (userSpaces != null) {
            userSpaces.clear();
        }
        this.userSpaces = userSpaces1;
        notifyDataSetChanged();
        if (userSpaces.size() > 0) {
            date = userSpaces.get(userSpaces.size() - 1).getUser_shijian();
        }
        SharedPreferences sharedPreferences = activity.getSharedPreferences("userSchool", Context.MODE_PRIVATE);
        this.myXueHao = sharedPreferences.getString("xuehao", "");
    }

    public void addGuangChangListbottom(List<UserSpace> userSpaces1) {
        frush = 2;
        i = getItemCount() - 3;
        userSpaces1.remove(0);
        if (userSpaces1.size() == 0) {
            ToastZong.ShowToast(activity, "没有动态了");
        } else {
            this.userSpaces.addAll(userSpaces1);
            notifyItemRangeChanged(userSpaces.size() - userSpaces1.size(), userSpaces.size() + 1);
        }
        date = userSpaces.get(userSpaces.size() - 1).getUser_shijian();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_space_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    public void myuserDynamicThumb(UserDynamicThumb u) {
        this.userDynamicThumb = u;
    }

    private String udynamicid;
    private String updateTime;
    private UserDynamicThumb userDynamicThumb;
    private int key;

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        userSpace = userSpaces.get(position);
        holder.message.setText(userSpace.getUser_xinxi());
        holder.time.setText(userSpace.getUser_shijian());

        if (userSpace.getUser_xinxi() != null) {
            holder.message.setText(userSpace.getUser_xinxi());
            holder.message.setVisibility(View.VISIBLE);
        }

        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        if (userSpaces.get(position).getBlock() != null && !userSpaces.get(position).getBlock().equals("")) {
            List<String> strings = new ArrayList<>();
            String[] s = userSpaces.get(position).getBlock().split(",");
            for (int a = 0; a < s.length; a++) {
                strings.add(s[a]);
            }
            holder.mUserspaceIb.setAdapter(new TagAdapter<String>(strings) {
                @Override
                public View getView(FlowLayout parent, int position, String o) {
                    View view = layoutInflater.inflate(R.layout.label_list1, holder.mUserspaceIb, false);
                    TextView selet = (TextView) view.findViewById(R.id.text);
                    selet.setText(o);
                    return view;
                }
            });
            holder.mUserspaceIb.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
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

        if (frush == 1) {
            if (!userSpace.getUser_image_url().equals("")) {
                holder.mUserspaceMessageimagelist.setVisibility(View.VISIBLE);
                holder.mUserspaceMessageimagelist.setLayoutManager(GuangChangImageToClass.newView(activity,userSpace.getUser_image_url(),userSpace.getUser_xuehao()));
                GuangChangImageAdapter guangChangImageAdapter = new GuangChangImageAdapter(activity, GuangChangImageToClass.imageToClass(userSpace.getUser_image_url(), userSpace.getUser_xuehao()));
                holder.mUserspaceMessageimagelist.setAdapter(guangChangImageAdapter);
            }
        } else {
            if (!userSpace.getUser_image_url().equals("")) {
                holder.mUserspaceMessageimagelist.setVisibility(View.VISIBLE);
                holder.mUserspaceMessageimagelist.setLayoutManager(GuangChangImageToClass.newView(activity,userSpace.getUser_image_url(),userSpace.getUser_xuehao()));
                GuangChangImageAdapter guangChangImageAdapter = new GuangChangImageAdapter(activity, GuangChangImageToClass.imageToClass(userSpace.getUser_image_url(), userSpace.getUser_xuehao()));
                holder.mUserspaceMessageimagelist.setAdapter(guangChangImageAdapter);
            }
        }


        if (userDynamicThumb.noYesThumb(userSpaces.get(position).getUser_dynamic_id())) {
            holder.myZan.setImageResource(R.drawable.yidianzan);
        } else {
            holder.myZan.setImageResource(R.drawable.weidianzan);
        }

        final Handler thumbHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        if (msg.obj.equals("0")) {
                            holder.myThumb.setText(String.valueOf(Integer.parseInt(holder.myThumb.getText().toString()) + 1));
                            holder.myZan.setImageResource(R.drawable.yidianzan);
                            if (!xuehao.equals(myXueHao)) {
                                SendGeTuiMessage.sendGeTuiMessage(1, xuehao, myXueHao, "1", userSpaces.get(position).getUser_dynamic_id(), 0);
                            }
                        }
                        if (msg.obj.equals("10")) {
                            if (Integer.parseInt(holder.myThumb.getText().toString()) != 0 && holder.myThumb.getText().toString() != null) {
                                holder.myThumb.setText(String.valueOf(Integer.parseInt(holder.myThumb.getText().toString()) - 1));
                                holder.myZan.setImageResource(R.drawable.weidianzan);
                            }
                        }
                        break;
                }
            }
        };

        if (userSpace.getUser_thumb() == 0) {
            holder.myThumb.setVisibility(View.INVISIBLE);
        } else {
            holder.myThumb.setText(String.valueOf(userSpaces.get(position).getUser_thumb()));
            holder.myThumb.setVisibility(View.VISIBLE);
        }
        if (userSpace.getUser_comment() == 0) {
            holder.myComment.setVisibility(View.INVISIBLE);
        } else {
            holder.myComment.setText(String.valueOf(userSpaces.get(position).getUser_comment()));
            holder.myComment.setVisibility(View.VISIBLE);
        }

        final String path = "http://nmy1206.natapp1.cc/gcThumb.php";
        final String path1 = "http://nmy1206.natapp1.cc/myThumb.php";
        holder.myZan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                udynamicid = userSpaces.get(position).getUser_dynamic_id();
                userDynamicThumb.userThumb(udynamicid, myXueHao, xuehao, thumbHandler);
            }
        });

        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                udynamicid = userSpaces.get(position).getUser_dynamic_id();
                showmoreFunction(function, udynamicid, position);
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
                            intent.putExtra("uxuehao", xuehao);
                            intent.putExtra("dynamic", (Serializable) dynamicMessageDetailed.userSpace);
                        }
                        activity.startActivity(intent);
                        break;
                    case 2:
                        ToastZong.ShowToast(MyApplication.getContext(), "超时.请重试");
                        break;
                }
            }
        };

        holder.Rzong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dynamicMessageDetailed = new DynamicMessageDetailed(activity, xuehao, userSpaces.get(position).getUser_dynamic_id(), dynamicHandler);
                dynamicMessageDetailed.obtainDynamic();
                key = 1;
            }
        });

        holder.myC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dynamicMessageDetailed = new DynamicMessageDetailed(activity, xuehao, userSpaces.get(position).getUser_dynamic_id(), dynamicHandler);
                dynamicMessageDetailed.obtainDynamic();
                key = 2;
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (msg.obj.equals("0")) {
                        LitePal.deleteAll(UserSpace.class, "user_dynamic_id = ?", udynamicid);
                        userSpaces.remove(msg.arg1);
                        notifyItemRemoved(msg.arg1);
                        if (userSpaces.size() == 0) {
                            notifyDataSetChanged();
                            activity.findViewById(R.id.my_space_list_message).setVisibility(View.INVISIBLE);
                            activity.findViewById(R.id.Mypace_null).setVisibility(View.VISIBLE);
                        }
                        notifyItemRangeChanged(msg.arg1, userSpaces.size() - msg.arg1);
                        BroadcastRec.sendReceiver(MyApplication.getContext(), "deleteDynamic", 0, udynamicid);
                        showDiaLog.closeMyDiaLog();
                    }
                    break;
            }
        }
    };


    private ShowDiaLog showDiaLog;
    private String path = "http://nmy1206.natapp1.cc/deleteDynamic.php";

    private void showmoreFunction(int function1, final String udynamicid, final int position1) {
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_two, null);
        view.setVerticalFadingEdgeEnabled(true);
        showDiaLog = new ShowDiaLog(activity, R.style.AlertDialog_Function, view);
        showDiaLog.logWindow(new ColorDrawable(Color.TRANSPARENT));
        showDiaLog.showMyDiaLog();
        Button deleteDynamic = (Button) view.findViewById(R.id.delete_GuangChang_Dynamic);
        Button deleteCancel = (Button) view.findViewById(R.id.delete_cancel);
        switch (function1) {
            case 1:
                deleteDynamic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HttpUtil.deleteDynamic(path, udynamicid, xuehao, new Callback() {
                            @Override
                            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                String a = response.body().string();
                                Message message = new Message();
                                message.what = 1;
                                message.obj = a;
                                message.arg1 = position1;
                                handler.sendMessage(message);
                            }

                            @Override
                            public void onFailure(@NotNull Call call, @NotNull IOException e) {

                            }
                        });
                    }
                });
                deleteCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDiaLog.closeMyDiaLog();
                    }
                });
                break;
            case 2:
                deleteDynamic.setText("举报此动态");
                deleteDynamic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastZong.ShowToast(activity, "举报成功");
                        showDiaLog.closeMyDiaLog();
                    }
                });

                deleteCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDiaLog.closeMyDiaLog();
                    }
                });
                break;
        }

    }

    @Override
    public int getItemCount() {
        return userSpaces.size();
    }

}
