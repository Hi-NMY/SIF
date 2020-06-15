package com.example.sif.ui.sign;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.sif.BaseFragment;
import com.example.sif.GoodVoice;
import com.example.sif.Lei.MyBroadcastReceiver.BroadcastRec;
import com.example.sif.Lei.MyToolClass.ObtainUserSign;
import com.example.sif.Lei.MyToolClass.UpdateShareTask;
import com.example.sif.MyApplication;
import com.example.sif.R;
import com.tamsiree.rxui.view.dialog.RxDialog;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;

public class TaskFragment extends BaseFragment {

    private View view;
    private Button mTostart;
    private Button mTostart1;
    private Button mTostart2;
    private Button mTostart3;
    private Button mTostart4;
    private TextView mTaskText;
    private TextView mTaskText1;
    private TextView mTaskText2;
    private TextView mTaskText3;
    private TextView mTaskText4;
    private Map<Integer, Button> mapButton;
    private Map<Integer,TextView> mapTaskText;
    private Context context;
    private Activity activity;
    private ObtainTask obtainTask;
    private String myXueHao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sign_task_card_fragment, container, false);
        mapButton = new HashMap<>();
        mapTaskText = new HashMap<>();
        obtainTask = new ObtainTask();
        initView();
        BroadcastRec.obtainRecriver(MyApplication.getContext(), "obtainTask", obtainTask);
        SharedPreferences sharedPreferences = MyApplication.getContext().getSharedPreferences("userSchool", Context.MODE_PRIVATE);
        myXueHao = sharedPreferences.getString("xuehao", "");
        return view;
    }

    public void setActivity(Context c, Activity a){
        this.context = c;
        this.activity = a;
    }

    private void initView() {
        mTostart = view.findViewById(R.id.tostart);
        mapButton.put(1, mTostart);
        mTostart1 = view.findViewById(R.id.tostart1);
        mapButton.put(2, mTostart1);
        mTostart2 = view.findViewById(R.id.tostart2);
        mapButton.put(3, mTostart2);
        mTostart3 = view.findViewById(R.id.tostart3);
        mapButton.put(4, mTostart3);
        mTostart4 = view.findViewById(R.id.tostart4);
        mapButton.put(5, mTostart4);
        mTaskText = view.findViewById(R.id.task_text);
        mapTaskText.put(1,mTaskText);
        mTaskText1 = view.findViewById(R.id.task_text1);
        mapTaskText.put(2,mTaskText1);
        mTaskText2 = view.findViewById(R.id.task_text2);
        mapTaskText.put(3,mTaskText2);
        mTaskText3 = view.findViewById(R.id.task_text3);
        mapTaskText.put(4,mTaskText3);
        mTaskText4 = view.findViewById(R.id.task_text4);
        mapTaskText.put(5,mTaskText4);

        buttonClickListener();
    }

    private Handler rightStartHanlder = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dissmissLoading();
            showCoinDialog(10);
            obtainUserCoin();
            overTask(clickNum);
            UpdateShareTask.over(clickName);
        }
    };

    private void buttonClickListener() {
        for (int i = 1; i < mapButton.size() + 1; i++) {
            Button tostart = mapButton.get(i);
            tostart.setTag(i);
            tostart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch ((int) mapTaskText.get((int)tostart.getTag()).getTag()){
                        case 0:
                            startFun(0,(int)tostart.getTag());
                            break;
                        case 1:
                            startFun(1,(int)tostart.getTag());
                            break;
                        case 2:
                            startFun(2,(int)tostart.getTag());
                            break;
                    }
                }
            });
        }
    }

    private int clickNum;
    private String clickName;
    private void startFun(int fun,int num){
        switch (num){
            case 1:
                if (fun == 1){
                    clickName = "sign";
                    ObtainUserSign.updateTaskCoin(myXueHao,10,rightStartHanlder);
                    startLoading();
                }else if (fun == 2){
                    BroadcastRec.sendReceiver(MyApplication.getContext(),"GoToTop",0,"");
                }
                break;
            case 2:
                if (fun == 1){
                    clickName = "oneDynamic";
                    ObtainUserSign.updateTaskCoin(myXueHao,10,rightStartHanlder);
                    startLoading();
                }else if (fun == 2){
                    goToGuangChang();
                }
                break;
            case 3:
                if (fun == 1){
                    clickName = "thumbDynamic";
                    ObtainUserSign.updateTaskCoin(myXueHao,10,rightStartHanlder);
                    startLoading();
                }else if (fun == 2){
                    goToGuangChang();
                }
                break;
            case 4:
                if (fun == 1){
                    clickName = "goodVoice";
                    ObtainUserSign.updateTaskCoin(myXueHao,10,rightStartHanlder);
                    startLoading();
                }else if (fun == 2){
                    Intent intent = new Intent(MyApplication.getContext(), GoodVoice.class);
                    activity.startActivity(intent);
                    activity.finish();
                }
                break;
            case 5:
                if (fun == 1){
                    clickName = "goToSpace";
                    ObtainUserSign.updateTaskCoin(myXueHao,10,rightStartHanlder);
                    startLoading();
                }else if (fun == 2){
                    goToGuangChang();
                }
                break;
        }
        clickNum = num;
    }

    private void goToGuangChang(){
        activity.finish();
        BroadcastRec.sendReceiver(MyApplication.getContext(),"taskGoTo",0,"");
    }


    private void changeView() {
        switch (signNum){
            case 0:
                goToStar(1);
                break;
            case 1:
                alreadyTask(1);
                break;
            case -1:
                overTask(1);
                break;
        }
        switch (oneDynamic){
            case 0:
                goToStar(2);
                break;
            case 1:
                alreadyTask(2);
                break;
            case -1:
                overTask(2);
                break;
        }
        switch (thumbDynamic){
            case 0:
                goToStar(3);
                break;
            case 10:
                alreadyTask(3);
                break;
            case -1:
                overTask(3);
                break;
            default:
                goToStar(3);
                break;
        }
        switch (goodVoice){
            case 0:
                goToStar(4);
                break;
            case 1:
                alreadyTask(4);
                break;
            case -1:
                overTask(4);
                break;
        }

        switch (goToSpace){
            case 0:
                goToStar(5);
                break;
            case 10:
                alreadyTask(5);
                break;
            case -1:
                overTask(5);
                break;
            default:
                goToStar(5);
                break;
        }
    }

    private void goToStar(int a){
        mapTaskText.get(a).setTag(2);
        mapButton.get(a).setText("去完成");
        mapButton.get(a).setBackgroundResource(R.drawable.task_bg);
    }

    private void alreadyTask(int a){
        mapTaskText.get(a).setTag(1);
        mapButton.get(a).setText("领奖励");
        mapButton.get(a).setBackgroundResource(R.drawable.task_alreadybg);
    }

    private void overTask(int a){
        mapTaskText.get(a).setTag(0);
        mapButton.get(a).setText("已完成");
        mapButton.get(a).setBackgroundResource(R.drawable.daohang);
        mapButton.get(a).setTextColor(activity.getColor(R.color.ziti));
    }

    private RxDialog signLoaging;
    private void startLoading(){
        signLoaging = new RxDialog(context,R.style.tran_dialog);
        View loading = LayoutInflater.from(context).inflate(R.layout.sign_loading_lay,null);
        signLoaging.setContentView(loading);
        signLoaging.show();
    }

    private void dissmissLoading(){
        signLoaging.dismiss();
    }

    private void showCoinDialog(int i) {
        RxDialog rxDialog = new RxDialog(context,R.style.tran_dialog);
        View log = LayoutInflater.from(context).inflate(R.layout.coin_layout,null);
        TextView text = log.findViewById(R.id.coin_size);
        ImageView close = log.findViewById(R.id.close);
        text.setText("+" + i);
        rxDialog.setContentView(log);
        rxDialog.show();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rxDialog.dismiss();
            }
        });
    }

    private void obtainUserCoin(){
        RequestBody requestBody = new FormBody.Builder()
                .add("xuehao", myXueHao)
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url("http://nmy1206.natapp1.cc/ObtainUserCoin.php")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String a = response.body().string();
                BroadcastRec.sendReceiver(MyApplication.getContext(),"obtainCoin",Integer.valueOf(a),"");
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }
        });
    }

    private int signNum = 0;
    private int oneDynamic = 0;
    private int thumbDynamic = 0;
    private int goodVoice = 0;
    private int goToSpace = 0;

    class ObtainTask extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            SharedPreferences sharedPreferences = MyApplication.getContext().getSharedPreferences("taskNum",MODE_PRIVATE);
            signNum = sharedPreferences.getInt("sign",0);
            oneDynamic = sharedPreferences.getInt("oneDynamic",0);
            thumbDynamic = sharedPreferences.getInt("thumbDynamic",0);
            goodVoice = sharedPreferences.getInt("goodVoice",0);
            goToSpace = sharedPreferences.getInt("goToSpace",0);
            changeView();
        }
    }
}
