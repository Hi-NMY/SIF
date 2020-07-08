package com.example.sif.ui.sign;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.sif.BaseFragment;
import com.example.sif.Lei.MyBroadcastReceiver.BroadcastRec;
import com.example.sif.Lei.MyToolClass.*;
import com.example.sif.MyApplication;
import com.example.sif.NeiBuLei.UserSignClass;
import com.example.sif.R;
import com.tamsiree.rxkit.RxTextTool;
import com.tamsiree.rxui.view.dialog.RxDialog;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;

public class SignInFragment extends BaseFragment {

    private View view;
    private TextView mLongSginDay;
    private ImageView mImg;
    private View mNoSignBg;
    private TextView mSignText;
    private RelativeLayout mR;
    private ImageView mImg1;
    private View mNoSignBg1;
    private TextView mSignText1;
    private RelativeLayout mR1;
    private ImageView mImg2;
    private View mNoSignBg2;
    private TextView mSignText2;
    private RelativeLayout mR2;
    private ImageView mImg3;
    private View mNoSignBg3;
    private TextView mSignText3;
    private RelativeLayout mR3;
    private ImageView mImg4;
    private View mNoSignBg4;
    private TextView mSignText4;
    private RelativeLayout mR4;
    private ImageView mImg5;
    private View mNoSignBg5;
    private TextView mSignText5;
    private RelativeLayout mR5;
    private ImageView mImg6;
    private View mNoSignBg6;
    private TextView mSignText6;
    private RelativeLayout mR6;
    private ObtainWeek obtainWeek;
    private TextView mTxtTip;
    private TextView mTxtTip1;
    private TextView mTxtTip2;
    private TextView mTxtTip3;
    private TextView mTxtTip4;
    private TextView mTxtTip5;
    private TextView mTxtTip6;
    private Map<Integer, ImageView> mapImage;
    private Map<Integer, View> mapView;
    private Map<Integer, TextView> mapTextView;
    private Map<Integer, RelativeLayout> mapRelative;
    private Map<Integer,TextView> mapTipText;
    private String myXueHao;
    private Context context;
    private Activity activity;

    static {
        obtainIsMonday();
    }

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    private static void obtainIsMonday() {
        sharedPreferences = MyApplication.getContext().getSharedPreferences("isMonday",MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sign_signin_card_fragment, container, false);
        mapImage = new HashMap<>();
        mapView = new HashMap<>();
        mapTextView = new HashMap<>();
        mapRelative = new HashMap<>();
        mapTipText = new HashMap<>();
        obtainWeek = new ObtainWeek();
        initView();
        BroadcastRec.obtainRecriver(MyApplication.getContext(), "obtainWeek", obtainWeek);
        SharedPreferences sharedPreferences = MyApplication.getContext().getSharedPreferences("userSchool", Context.MODE_PRIVATE);
        myXueHao = sharedPreferences.getString("xuehao", "");
        return view;
    }

    public void setActivity(Context c, Activity a){
        this.context = c;
        this.activity = a;
    }

    private void initView() {
        mLongSginDay = view.findViewById(R.id.long_sgin_day);
        mImg = view.findViewById(R.id.img);
        mapImage.put(1, mImg);
        mNoSignBg = view.findViewById(R.id.no_sign_bg);
        mapView.put(1, mNoSignBg);
        mSignText = view.findViewById(R.id.sign_text);
        mapTextView.put(1, mSignText);
        mR = view.findViewById(R.id.r);
        mapRelative.put(1, mR);
        mImg1 = view.findViewById(R.id.img1);
        mapImage.put(2, mImg1);
        mNoSignBg1 = view.findViewById(R.id.no_sign_bg1);
        mapView.put(2, mNoSignBg1);
        mSignText1 = view.findViewById(R.id.sign_text1);
        mapTextView.put(2, mSignText1);
        mR1 = view.findViewById(R.id.r1);
        mapRelative.put(2, mR1);
        mImg2 = view.findViewById(R.id.img2);
        mapImage.put(3, mImg2);
        mNoSignBg2 = view.findViewById(R.id.no_sign_bg2);
        mapView.put(3, mNoSignBg2);
        mSignText2 = view.findViewById(R.id.sign_text2);
        mapTextView.put(3, mSignText2);
        mR2 = view.findViewById(R.id.r2);
        mapRelative.put(3, mR2);
        mImg3 = view.findViewById(R.id.img3);
        mapImage.put(4, mImg3);
        mNoSignBg3 = view.findViewById(R.id.no_sign_bg3);
        mapView.put(4, mNoSignBg3);
        mSignText3 = view.findViewById(R.id.sign_text3);
        mapTextView.put(4, mSignText3);
        mR3 = view.findViewById(R.id.r3);
        mapRelative.put(4, mR3);
        mImg4 = view.findViewById(R.id.img4);
        mapImage.put(5, mImg4);
        mNoSignBg4 = view.findViewById(R.id.no_sign_bg4);
        mapView.put(5, mNoSignBg4);
        mSignText4 = view.findViewById(R.id.sign_text4);
        mapTextView.put(5, mSignText4);
        mR4 = view.findViewById(R.id.r4);
        mapRelative.put(5, mR4);
        mImg5 = view.findViewById(R.id.img5);
        mapImage.put(6, mImg5);
        mNoSignBg5 = view.findViewById(R.id.no_sign_bg5);
        mapView.put(6, mNoSignBg5);
        mSignText5 = view.findViewById(R.id.sign_text5);
        mapTextView.put(6, mSignText5);
        mR5 = view.findViewById(R.id.r5);
        mapRelative.put(6, mR5);
        mImg6 = view.findViewById(R.id.img6);
        mapImage.put(7, mImg6);
        mNoSignBg6 = view.findViewById(R.id.no_sign_bg6);
        mapView.put(7, mNoSignBg6);
        mSignText6 = view.findViewById(R.id.sign_text6);
        mapTextView.put(7, mSignText6);
        mR6 = view.findViewById(R.id.r6);
        mapRelative.put(7, mR6);
        mTxtTip = view.findViewById(R.id.txt_tip);
        mapTipText.put(1,mTxtTip);
        mTxtTip1 = view.findViewById(R.id.txt_tip1);
        mapTipText.put(2,mTxtTip1);
        mTxtTip2 = view.findViewById(R.id.txt_tip2);
        mapTipText.put(3,mTxtTip2);
        mTxtTip3 = view.findViewById(R.id.txt_tip3);
        mapTipText.put(4,mTxtTip3);
        mTxtTip4 = view.findViewById(R.id.txt_tip4);
        mapTipText.put(5,mTxtTip4);
        mTxtTip5 = view.findViewById(R.id.txt_tip5);
        mapTipText.put(6,mTxtTip5);
        mTxtTip6 = view.findViewById(R.id.txt_tip6);
        mapTipText.put(7,mTxtTip6);

        clickSign();
    }


    private Handler newSignHanlder = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dissmissLoading();
            obtainUserCoin();
            UpdateShareTask.updateTask(1);
            BroadcastRec.sendReceiver(MyApplication.getContext(),"obtainTask",0,"");
            mLongSginDay = view.findViewById(R.id.long_sgin_day);
            RxTextTool.getBuilder("")
                    .append("已累计在线")
                    .append(String.valueOf(nowLongDay + 1))
                    .setForegroundColor(MyApplication.getContext().getColor(R.color.yanghong))
                    .append("天")
                    .into(mLongSginDay);
            mLongSginDay.postInvalidate();
            nowLongDay += 1;
        }
    };

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
                .url(InValues.send(R.string.ObtainUserCoin))
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

    private ShowDiaLog patchDialog;
    private RxDialog signLoaging;
    private void clickSign() {
        for (int i = 1; i < mapRelative.size() + 1; i++) {
            RelativeLayout rel = mapRelative.get(i);
            rel.setTag(i);
            rel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((Integer) mapImage.get((Integer) rel.getTag()).getTag() == 1) {
                        startLoading();
                        ObtainUserSign.updateSign(myXueHao, setSign((Integer) rel.getTag()),(Integer) rel.getTag(), newSignHanlder);
                        startSign((Integer) rel.getTag());
                    } else if ((Integer) mapImage.get((Integer) rel.getTag()).getTag() == 3) {
                        showPatchDia((Integer) rel.getTag());
                    }
                }
            });
        }
    }

    private void startLoading(){
        signLoaging = new RxDialog(context,R.style.tran_dialog);
        View loading = LayoutInflater.from(context).inflate(R.layout.sign_loading_lay,null);
        signLoaging.setContentView(loading);
        signLoaging.show();
    }

    private void dissmissLoading(){
        signLoaging.dismiss();
    }

    private Handler patchHanlder = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dissmissLoading();
            patchDialog.closeMyDiaLog();
            startSign(a);
            obtainUserCoin();
            mLongSginDay = view.findViewById(R.id.long_sgin_day);
            RxTextTool.getBuilder("")
                    .append("已累计在线")
                    .append(String.valueOf(nowLongDay + 1))
                    .setForegroundColor(MyApplication.getContext().getColor(R.color.yanghong))
                    .append("天")
                    .into(mLongSginDay);
            mLongSginDay.postInvalidate();
            nowLongDay += 1;
        }
    };

    private int a = -1;
    private void showPatchDia(int num){
        a = num;
        View qr = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.patchsign_dialog, null);
        patchDialog = new ShowDiaLog(activity, R.style.AlertDialog_qr, qr);
        patchDialog.logWindow(new ColorDrawable(Color.TRANSPARENT));
        Button cancel = qr.findViewById(R.id.cancel);
        Button determine = qr.findViewById(R.id.determine);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                patchDialog.closeMyDiaLog();
            }
        });
        determine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLoading();
                if (userSignClass.getCoin() >= 50){
                    ObtainUserSign.PatchSign(myXueHao,setSign(num),num,patchHanlder);
                }else {
                    dissmissLoading();
                    ToastZong.ShowToast(MyApplication.getContext(),"金币不足,快去做任务获取金币吧");
                }
            }
        });
        patchDialog.showMyDiaLog();
    }

    private int setSign(int a) {
        if (!sharedPreferences.getBoolean("Monday",true)){
            String keySign = String.valueOf(ObtainUserSign.keySign);
            keySign = keySign + a;
            return Integer.valueOf(keySign);
        }else {
            return a;
        }
    }

    public void changeSignView() {
        for (int i = 1; i < mapImage.size() + 1; i++) {
            if (i < nowWeek) {
                patchSign(i);
            } else if (i == nowWeek) {
                signIn(i);
            } else if (i > nowWeek) {
                noSign(i);
            }
        }
    }

    public UserSignClass userSignClass;
    private int nowLongDay = -1;

    public void twiceChangeSignView() {
        mLongSginDay = view.findViewById(R.id.long_sgin_day);
        nowLongDay = userSignClass.getLongday();
        RxTextTool.getBuilder("")
                .append("已累计在线")
                .append(String.valueOf(userSignClass.getLongday()))
                .setForegroundColor(MyApplication.getContext().getColor(R.color.yanghong))
                .append("天")
                .into(mLongSginDay);
        mLongSginDay.postInvalidate();

        if (!sharedPreferences.getBoolean("Monday",true)){
            alreadySign(String.valueOf(userSignClass.getSignday()));
        }
    }

    private void alreadySign(String s) {
        if (!s.equals("") && !s.equals("0")){
            for (int i = 1; i < s.length() + 1; i++) {
                int a = Integer.valueOf(s.substring(i - 1, i));
                mapImage.get(a).setVisibility(View.VISIBLE);
                mapImage.get(a).setBackgroundResource(R.drawable.sign_right);
                mapImage.get(a).setTag(0);
                mapView.get(a).setVisibility(View.INVISIBLE);
                mapTextView.get(a).setVisibility(View.INVISIBLE);
                mapTipText.get(a).setTextColor(MyApplication.getContext().getColor(R.color.darkgray));
            }
        }
    }

    private void signIn(int i) {
        mapImage.get(i).setVisibility(View.INVISIBLE);
        mapImage.get(i).setTag(1);
        mapView.get(i).setVisibility(View.VISIBLE);
        mapTextView.get(i).setVisibility(View.VISIBLE);
    }

    private void noSign(int i) {
        mapImage.get(i).setVisibility(View.VISIBLE);
        mapImage.get(i).setTag(2);
        mapImage.get(i).setBackgroundResource(R.drawable.glod_coin);
        mapView.get(i).setVisibility(View.INVISIBLE);
        mapTextView.get(i).setVisibility(View.INVISIBLE);
    }

    private void patchSign(int i) {
        mapImage.get(i).setVisibility(View.VISIBLE);
        mapImage.get(i).setTag(3);
        mapImage.get(i).setBackgroundResource(R.drawable.no_sign);
        mapView.get(i).setVisibility(View.INVISIBLE);
        mapTextView.get(i).setVisibility(View.INVISIBLE);
        mapTipText.get(i).setTextColor(MyApplication.getContext().getColor(R.color.darkgray));
    }

    private void startSign(int i) {
        showCoinDialog(i);
        mapImage.get(i).setVisibility(View.VISIBLE);
        mapImage.get(i).setTag(0);
        mapImage.get(i).setBackgroundResource(R.drawable.sign_right);
        mapView.get(i).setVisibility(View.INVISIBLE);
        mapTextView.get(i).setVisibility(View.INVISIBLE);
        mapTipText.get(i).setTextColor(MyApplication.getContext().getColor(R.color.darkgray));
    }

    private void showCoinDialog(int i) {
        editor.putBoolean("firstKey",true);
        editor.commit();
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

    private int nowWeek = -1;
    private int nowDay = 0;

    class ObtainWeek extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            nowWeek = intent.getIntExtra("textone", -1);
            if (nowWeek != -1) {
                if (nowWeek == 1) {
                    nowWeek = 7;
                    editor.putBoolean("firstKey",true);
                    editor.commit();
                } else if (nowWeek > 1) {
                    nowWeek -= 1;
                }
                changeSignView();
            }

            nowDay += 1;

            if (nowDay == 2) {
                userSignClass = ObtainUserSign.userSignClass;
                twiceChangeSignView();
                BroadcastRec.sendReceiver(MyApplication.getContext(), "startAnimation", 0, "");
            }
        }
    }
}
