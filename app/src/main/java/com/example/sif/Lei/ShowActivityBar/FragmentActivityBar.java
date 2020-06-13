package com.example.sif.Lei.ShowActivityBar;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.example.sif.*;
import com.example.sif.Lei.LianJie.HttpUtil;
import com.example.sif.Lei.MyToolClass.*;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;

public class FragmentActivityBar extends BaseFragment {
    private View view;
    private TextView mActivitybarLeft;
    private TextView mActivitybarRight;
    private TextView mActivitybarRight1;
    private TextView mActivitybarTitle;
    private TextView mActivitybarTitle1;
    private TextView mActivitybarTitle2;
    private RelativeLayout mActivityBar;
    private View mActivitybarTitleCircle;
    private View mActivitybarTitleCircle1;
    private RelativeLayout mBiaotiR;
    private RelativeLayout mBiaotiR1;

    private boolean Left = false;
    private boolean Right = false;
    private boolean Right1 = false;
    private boolean Title = false;
    private boolean Title1 = false;
    private boolean Title2 = false;
    private boolean mBiaoti_R = false;
    private boolean mBiaoti_R1 = false;
    private boolean textRight = false;
    private Activity activity;
    private TextView mActivitybarTextright;
    private View mViewNoticemessage;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_activitybar, container, false);
        mActivitybarLeft = (TextView) view.findViewById(R.id.activitybar_left);
        mActivitybarRight = (TextView) view.findViewById(R.id.activitybar_right);
        mActivitybarRight1 = (TextView) view.findViewById(R.id.activitybar_right1);
        mActivitybarTitle = (TextView) view.findViewById(R.id.activitybar_title);
        mActivitybarTitle1 = (TextView) view.findViewById(R.id.activitybar_title1);
        mActivitybarTitle2 = (TextView) view.findViewById(R.id.activitybar_title2);
        mBiaotiR = (RelativeLayout) view.findViewById(R.id.biaoti_R);
        mBiaotiR1 = (RelativeLayout) view.findViewById(R.id.biaoti_R1);
        mActivityBar = (RelativeLayout) view.findViewById(R.id.activity_bar);
        mActivitybarTitleCircle = (View) view.findViewById(R.id.activitybar_title_circle);
        mActivitybarTitleCircle1 = (View) view.findViewById(R.id.activitybar_title_circle1);
        mActivitybarTextright = (TextView) view.findViewById(R.id.activitybar_textright);
        mViewNoticemessage = (View)view.findViewById(R.id.view_noticemessage);

        return view;
    }

    //

    //控件功能
    public void showUiFunction(int leftFunction, int titleFunction, int title2Function, int title1Function, int textright, int rightFunction, int right1Function) {
        functionLeft(leftFunction);
        functionTitle(titleFunction);
        functionTitle1(title1Function);
        functionTitle2(title2Function);
        functionRight(rightFunction);
        functionRight1(right1Function);
        functionTextRight(textright);
    }

    private String path = "http://nmy1206.natapp1.cc/GuangChangMessage.php";
    private String path1 = "http://nmy1206.natapp1.cc/userXinXiXiuGai.php";
    private String path2 = "http://nmy1206.natapp1.cc/BlockChange.php";
    private LocalBroadcastManager localBroadcastManager;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (!msg.obj.equals("1")) {
                        UpdateShareTask.updateTask(2);
                        localBroadcastManager = LocalBroadcastManager.getInstance(activity);
                        Intent intent = new Intent("shuaG");
                        intent.putExtra("NEW", String.valueOf(msg.obj));
                        localBroadcastManager.sendBroadcast(intent);
                        ToastZong.ShowToast(activity, "发布成功");
                        baseActivity.closeDiaLog();
                        BaseActivity baseActivity = (BaseActivity) getActivity();
                        mActivitybarTextright.setClickable(true);
                        String path = "http://nmy1206.natapp1.cc/userSpace.php";
                        String path1 = "http://nmy1206.natapp1.cc/shuaXinGuangChang.php";
                        String nowDate = MyDateClass.showNowDate();
                        baseActivity.sendUserSpace(1, path, nowDate, xuehao, null);
                        baseActivity.shuaXinGuangChang(path1, null, nowDate, 1);
                        activity.finish();
                        activity.overridePendingTransition(R.anim.activity_out_bottom, R.anim.activity_main);
                    } else {
                        mActivitybarTextright.setClickable(true);
                        baseActivity.closeDiaLog();
                        ToastZong.ShowToast(activity, "发布错误");
                    }

                    break;
            }
        }
    };

    private Handler change = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (msg.obj.equals("0")) {
                        baseActivity.closeDiaLog();
                        ToastZong.ShowToast(activity, "保存成功");
                        switch (msg.arg1) {
                            case 1:
                                cunshareUser("user", "user_name", aa);
                                intentActivity(changeInformation, 1);
                                break;
                            case 2:
                                cunshareUser("user", "user_geqian", aa);
                                intentActivity(changeInformation, 2);
                                break;
                        }
                    } else {
                        baseActivity.closeDiaLog();
                        ToastZong.ShowToast(activity, "保存失败，请重新设置");
                    }

                    break;
            }
        }
    };

    private ChangeInformation changeInformation;
    private String aa;
    private BaseActivity baseActivity;
    private SharedPreferences sharedPreferences1;
    private String xuehao;
    private int i;
    private String mingzi;

    private void functionTextRight(final int textright) {
        if (textright == 2) {
            mActivitybarTextright.setTextColor(getResources().getColorStateList(R.color.bilan));
            mActivitybarTextright.setTextSize(17);
        }
        if (textright == 3) {
            mActivitybarTextright.setTextColor(getResources().getColorStateList(R.color.white));
            mActivitybarTextright.setTextSize(17);
            TextPaint textPaint = mActivitybarTextright.getPaint();
            textPaint.setFakeBoldText(true);
            //    mActivityBar.setAlpha(0.5f);
        }
        mActivitybarTextright.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                switch (textright) {
                    case 1:
                        GuangChangMessage message1 = (GuangChangMessage) getActivity();
                        baseActivity = (BaseActivity) getActivity();
                        baseActivity.showDiaLog(activity, R.drawable.loading);
                        mActivitybarTextright.setClickable(false);
                        SharedPreferences sharedPreferences = activity.getSharedPreferences("user", Context.MODE_PRIVATE);
                        mingzi = sharedPreferences.getString("user_name", null);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String shijian = simpleDateFormat.format(System.currentTimeMillis());
                        final TextView message = (TextView) activity.findViewById(R.id.guangchang_message_content);
                        String gcMessage = message.getText().toString();
                        String M = gcMessage.trim();
                        sharedPreferences1 = MyApplication.getContext().getSharedPreferences("userSchool", Context.MODE_PRIVATE);
                        xuehao = sharedPreferences1.getString("xuehao", null);
                        StringBuffer stringBuffer = message1.stringBuffer;
                        if (stringBuffer == null) {
                            stringBuffer.append("");
                        }
                        if (message1.ds != null && message1.ds.size() > 0) {
                            i = 1;
                        } else {
                            i = 0;
                        }
                        if (mingzi != null && shijian != null && (!TextUtils.isEmpty(M) || i != 0) && xuehao != null) {
                            HttpUtil.cunGuangChangMessage(i, path, message1.ds, mingzi, shijian, gcMessage, xuehao, 0, 0, stringBuffer,message1.placeString, null, new Callback() {
                                @Override
                                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                    String a = response.body().string();
                                    Message message1 = new Message();
                                    message1.what = 0;
                                    message1.obj = a;
                                    handler.sendMessage(message1);
                                }

                                @Override
                                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                                }
                            });

                            if (stringBuffer != null && !stringBuffer.equals("")) {
                                HttpUtil.blockChange(path2, stringBuffer, new Callback() {
                                    @Override
                                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                        String a = response.body().string();
                                    }

                                    @Override
                                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                                    }
                                });
                            }

                        } else {
                            mActivitybarTextright.setClickable(true);
                            baseActivity.closeDiaLog();
                            ToastZong.ShowToast(activity, "分享不能为空哦.....");
                        }
                        break;
                    case 2:
                        changeInformation = (ChangeInformation) getActivity();
                        aa = changeInformation.mChangeText.getText().toString();
                        baseActivity = (BaseActivity) getActivity();
                        if (changeInformation.i == 1 && !changeInformation.text.equals(aa)) {
                            baseActivity.showDiaLog(activity, R.drawable.loading);
                            changeInternetInformation(path1, 1, 1, aa, changeInformation.text, changeInformation.xuehao1);
                        } else {
                            if (changeInformation.i == 2 && !changeInformation.text.equals(aa)) {
                                baseActivity.showDiaLog(activity, R.drawable.loading);
                                changeInternetInformation(path1, 2, 3, aa, null, changeInformation.xuehao1);
                            } else {
                                activity.finish();
                            }
                        }
                        break;
                    case 3:
                        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        } else {
                            aaaa();
                        }
                        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.CAMERA}, 2);
                        } else {
                            aaaa();
                        }
                        break;
                }
            }
        });
    }

    private void aaaa() {
        SelectImage selectImage = new SelectImage(activity);
        selectImage.showSelectImage(3, null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    ToastZong.ShowToast(activity, "SD卡权限获取失败");
                }
                break;
            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    aaaa();
                } else {
                    ToastZong.ShowToast(activity, "相机权限获取失败");
                }
                break;
        }
    }

    private void changeInternetInformation(String path, final int number, int id, final String message, String oldName, String xuehao) {
        HttpUtil.changeInformation(path, id, message, oldName, xuehao, new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String a = response.body().string();
                Message message1 = new Message();
                message1.what = 0;
                message1.obj = a;
                message1.arg1 = number;
                change.sendMessage(message1);
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                if (e instanceof SocketTimeoutException) {
                    baseActivity.closeDiaLog();
                    ToastZong.ShowToast(changeInformation, "连接读取超时");
                }
                if (e instanceof ConnectException) {
                    baseActivity.closeDiaLog();
                    ToastZong.ShowToast(changeInformation, "连接错误");
                }
            }
        });
    }

    private void cunshareUser(String dizhi, String text, String neirong) {
        SharedPreferences sharedPreferences2 = activity.getSharedPreferences(dizhi, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences2.edit();
        editor.putString(text, neirong);
        editor.commit();
    }

    private void intentActivity(ChangeInformation changeInformation, int i) {
        Intent intent = new Intent();
        if (i == 1) {
            intent.putExtra("uText", changeInformation.mChangeText.getText().toString());
            activity.setResult(1, intent);
            activity.finish();
        }
        if (i == 2) {
            intent.putExtra("uText", changeInformation.mChangeText.getText().toString());
            activity.setResult(2, intent);
            activity.finish();
        }
    }

    private void functionLeft(final int leftFunction) {
        mActivitybarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (leftFunction) {
                    case 1:
                        activity.finish();
                        break;
                    case 2:
                        MyZhuYe myZhuYe = (MyZhuYe) getActivity();
                        myZhuYe.drawerLayout.openDrawer(Gravity.LEFT);
                        break;
                    case 3:
                        PersonalInformation personalInformation = (PersonalInformation) getActivity();
                        if (personalInformation.mChangeUserNameText.getText().toString() != personalInformation.uName || personalInformation.mChangeUserBirthText.getText().toString() != personalInformation.uBirth ||
                                personalInformation.mChangeUserGeQianText.getText().toString() != personalInformation.uGeQian) {
                            Intent intent = new Intent();
                            activity.setResult(1, intent);
                        }
                        activity.finish();
                    default:
                        break;
                }
            }
        });

    }

    private void functionTitle(final int titleFunction) {
        mActivitybarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (titleFunction) {
                    case 1:
                        tishiDianclick(0, mActivitybarTitleCircle, mActivitybarTitleCircle1);
                        tishiFond(0, mActivitybarTitle, mActivitybarTitle1);
                        break;
                    case 2:

                        break;
                    default:
                        break;
                }
            }
        });

    }

    private void functionTitle2(final int titleFunction) {
        mActivitybarTitle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (titleFunction) {
                    case 1:
                        mActivitybarTitle2.setTextColor(activity.getColor(R.color.beijing));
                        break;
                    case 2:

                        break;
                    default:
                        break;
                }
            }
        });

    }

    private void functionTitle1(final int title1Function) {
        mActivitybarTitle1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (title1Function) {
                    case 1:
                        tishiDianclick(1, mActivitybarTitleCircle, mActivitybarTitleCircle1);
                        tishiFond(1, mActivitybarTitle, mActivitybarTitle1);
                        break;
                    case 2:

                        break;
                    default:
                        break;
                }
            }
        });

    }

    private void functionRight(final int titleFunction) {
        mActivitybarRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (titleFunction) {
                    case 1:
                        Intent intent = new Intent(activity, MyCalendar.class);
                        activity.startActivity(intent);
                        break;
                    case 2:
                        MyZhuYe myZhuYe = (MyZhuYe)getActivity();
                        Intent intent1 = new Intent(activity, MessageNotice.class);
                        intent1.putExtra("ofkey", myZhuYe.offKey);
                        activity.startActivity(intent1);
                        break;
                    case 3:
                        baseActivity = (BaseActivity)getActivity();
                        SchoolShopPopupWindow schoolShopPopupWindow = new SchoolShopPopupWindow(MyApplication.getContext(),activity,baseActivity);
                        SchoolShop schoolShop = (SchoolShop) getActivity();
                        schoolShop.setPopupWindow(schoolShopPopupWindow);
                        schoolShopPopupWindow.showAtLocation(activity.findViewById(R.id.schoolshop_bar), Gravity.CENTER, 0, 0);
                        break;
                    case 4:
                        Intent intent2 = new Intent(activity,SignTrouble.class);
                        activity.startActivity(intent2);
                        break;
                    default:
                        break;
                }
            }
        });

    }

    private int REQUEST_CODE = 1000;

    private void functionRight1(final int title1Function) {
        mActivitybarRight1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (title1Function) {
                    case 1:
                        Intent intent = new Intent(activity, QRactivity.class);
                        activity.startActivityForResult(intent, REQUEST_CODE);
                        break;
                    case 2:
                        Intent intent1 = new Intent(activity, UserHomeShop.class);
                        activity.startActivity(intent1);
                        break;
                    default:
                        break;
                }
            }
        });

    }


    //控件UI
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void showZuJianUI(int leftUI, String titleUI, String title2UI, String title1UI, int rightUI, int right1UI) {
        mActivitybarLeft.setBackgroundResource(leftUI);
        mActivitybarTitle.setText(titleUI);
        mActivitybarTitle1.setText(title1UI);
        mActivitybarTitle2.setText(title2UI);
        mActivitybarRight.setBackgroundResource(rightUI);
        mActivitybarRight1.setBackgroundResource(right1UI);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void showZuJianUI(int leftUI, String titleUI, String title2UI, String title1UI, String textright, int rightUI, int right1UI) {
        mActivitybarLeft.setBackgroundResource(leftUI);
        mActivitybarTitle.setText(titleUI);
        mActivitybarTitle1.setText(title1UI);
        mActivitybarTitle2.setText(title2UI);
        mActivitybarTextright.setText(textright);
        mActivitybarRight1.setBackgroundResource(right1UI);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void showZuJianUI(int leftUI, int titleUI, int title2UI, int title1UI, int rightUI, int right1UI) {
        mActivitybarLeft.setBackgroundResource(leftUI);
        mActivitybarTitle.setBackgroundResource(titleUI);
        mActivitybarTitle1.setBackgroundResource(title1UI);
        mActivitybarTitle2.setBackgroundResource(title2UI);
        mActivitybarRight.setBackgroundResource(rightUI);
        mActivitybarRight1.setBackgroundResource(right1UI);
    }

    //获取控件使用
    public void showZuJian(Activity activity1, boolean left, boolean mBiaoti_r, boolean title2, boolean mBiaoti_r1, boolean textRight, boolean right, boolean right1) {
        this.Left = left;
        this.Right = right;
        this.textRight = textRight;
        this.Right1 = right1;
        this.Title2 = title2;
        this.mBiaoti_R = mBiaoti_r;
        this.mBiaoti_R1 = mBiaoti_r1;
        this.activity = activity1;
        xianShi();
    }

    //控件显隐
    private void xianShi() {
        if (Left) {
            mActivitybarLeft.setVisibility(View.VISIBLE);
        } else {
            mActivitybarLeft.setVisibility(View.INVISIBLE);
        }
        if (Right) {
            mActivitybarRight.setVisibility(View.VISIBLE);
        } else {
            mActivitybarRight.setVisibility(View.INVISIBLE);
        }
        if (Right1) {
            mActivitybarRight1.setVisibility(View.VISIBLE);
        } else {
            mActivitybarRight1.setVisibility(View.INVISIBLE);
        }
        if (mBiaoti_R) {
            mActivitybarTitle.setVisibility(View.VISIBLE);
        } else {
            mActivitybarTitle.setVisibility(View.INVISIBLE);
            mActivitybarTitleCircle.setVisibility(View.INVISIBLE);
        }
        if (mBiaoti_R1) {
            mActivitybarTitle1.setVisibility(View.VISIBLE);
        } else {
            mActivitybarTitle1.setVisibility(View.INVISIBLE);
            mActivitybarTitleCircle1.setVisibility(View.INVISIBLE);
        }
        if (Title2) {
            mActivitybarTitle2.setVisibility(View.VISIBLE);
        } else {
            mActivitybarTitle2.setVisibility(View.INVISIBLE);
        }
        if (textRight) {
            mActivitybarTextright.setVisibility(View.VISIBLE);
        } else {
            mActivitybarTextright.setVisibility(View.INVISIBLE);
        }
    }

    //背景色设置
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void barBackground(int color) {
        mActivityBar.setBackgroundResource(color);
    }

    //字体颜色设置
    public void fontColor(int color) {
        mActivitybarTitle2.setTextColor(color);
        mActivitybarTitle.setTextColor(color);
        mActivitybarTitle1.setTextColor(color);
    }

    public void messageNotice(boolean a){
        if (a){
            mViewNoticemessage.setVisibility(View.VISIBLE);
        }else {
            mViewNoticemessage.setVisibility(View.INVISIBLE);
        }
    }

}
