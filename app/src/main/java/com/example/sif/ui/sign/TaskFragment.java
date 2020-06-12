package com.example.sif.ui.sign;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.sif.BaseFragment;
import com.example.sif.GoodVoice;
import com.example.sif.Lei.MyBroadcastReceiver.BroadcastRec;
import com.example.sif.MyApplication;
import com.example.sif.R;

import java.util.HashMap;
import java.util.Map;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sign_task_card_fragment, container, false);
        mapButton = new HashMap<>();
        mapTaskText = new HashMap<>();
        initView();
        //模拟任务状态
        for (int i = 1; i < mapTaskText.size() + 1; i++){
            mapTaskText.get(i).setTag(2);
        }
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

    private void buttonClickListener() {
        for (int i = 1; i < mapButton.size() + 1; i++) {
            Button tostart = mapButton.get(i);
            tostart.setTag(i);
            tostart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch ((int) mapTaskText.get((int)tostart.getTag()).getTag()){
                        case 0:

                            break;
                        case 1:

                            break;
                        case 2:
                            startFun(2,(int)tostart.getTag());
                            break;
                    }
                }
            });
        }
    }

    private void startFun(int fun,int num){
        switch (num){
            case 1:
                if (fun == 0){

                }else if (fun == 1){

                }else if (fun == 2){
                    BroadcastRec.sendReceiver(MyApplication.getContext(),"GoToTop",0,"");
                }
                break;
            case 2:
                if (fun == 0){

                }else if (fun == 1){

                }else if (fun == 2){
                    goToGuangChang();
                }
                break;
            case 3:
                if (fun == 0){

                }else if (fun == 1){

                }else if (fun == 2){
                    goToGuangChang();
                }
                break;
            case 4:
                if (fun == 0){

                }else if (fun == 1){

                }else if (fun == 2){
                    Intent intent = new Intent(MyApplication.getContext(), GoodVoice.class);
                    activity.startActivity(intent);
                    activity.finish();
                }
                break;
            case 5:
                if (fun == 0){

                }else if (fun == 1){

                }else if (fun == 2){
                    goToGuangChang();
                }
                break;
        }
    }

    private void goToGuangChang(){
        activity.finish();
        BroadcastRec.sendReceiver(MyApplication.getContext(),"taskGoTo",0,"");
    }


}
