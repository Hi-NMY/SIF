package com.example.sif;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.sif.Lei.LianJie.HttpUtil;
import com.example.sif.Lei.ShowActivityBar.FragmentActivityBar;
import com.example.sif.Lei.MyToolClass.ToastZong;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PersonalInformation extends BaseActivity implements View.OnClickListener {

    public TextView mChangeUserNameText;
    private RelativeLayout mChangeUserName;
    public TextView mChangeUserBirthText;
    private RelativeLayout mChangeUserBirth;
    public TextView mChangeUserXingBieText;
    private RelativeLayout mChangeUserXingBie;
    public TextView mChangeUserGeQianText;
    private RelativeLayout mChangeUserGeQian;
    public String uName;
    public String uBirth;
    public String uXingBie;
    public String uGeQian;
    public String uZhuanYe;
    private TextView mChangeUserZhuanYeText;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);
        initView();

        ZTL();

        showActivityBar();

        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.personalinformation_R);
        setPadding(this,relativeLayout);

        Intent intent = getIntent();
        uName = intent.getStringExtra("uName");
        uBirth = intent.getStringExtra("uBirth");
        uXingBie = intent.getStringExtra("uXingBie");
        uGeQian = intent.getStringExtra("uGeQian");
        uZhuanYe = intent.getStringExtra("uZhuanYe");
        //信息写入
        xieru();

    }

    private void xieru() {
        mChangeUserNameText.setText(uName);
        mChangeUserBirthText.setText(uBirth);
        mChangeUserXingBieText.setText(uXingBie);
        mChangeUserGeQianText.setText(uGeQian);
        mChangeUserZhuanYeText.setText(uZhuanYe);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void showActivityBar() {
        FragmentActivityBar fragmentActivityBar = (FragmentActivityBar) getSupportFragmentManager().findFragmentById(R.id.Change_personalinformation_frag);
        ShowActivityBars showActivityBars = new ShowActivityBars(this, fragmentActivityBar);
        showActivityBars.showKongJian(true, false, true, false, false, false, false);
        showActivityBars.showUI(R.drawable.zuo_black, null, "编辑个人信息", null, 0, 0);
        showActivityBars.uiFunction(3, 0, 0, 0, 0, 0, 0);
    }


    private void initView() {
        mChangeUserNameText = (TextView) findViewById(R.id.change_userName_text);
        mChangeUserName = (RelativeLayout) findViewById(R.id.change_userName);
        mChangeUserBirthText = (TextView) findViewById(R.id.change_userBirth_text);
        mChangeUserBirth = (RelativeLayout) findViewById(R.id.change_userBirth);
        mChangeUserXingBieText = (TextView) findViewById(R.id.change_userXingBie_text);
        mChangeUserXingBie = (RelativeLayout) findViewById(R.id.change_userXingBie);
        mChangeUserGeQianText = (TextView) findViewById(R.id.change_userGeQian_text);
        mChangeUserGeQian = (RelativeLayout) findViewById(R.id.change_userGeQian);

        mChangeUserName.setOnClickListener(this);
        mChangeUserGeQian.setOnClickListener(this);
        mChangeUserBirth.setOnClickListener(this);
        mChangeUserZhuanYeText = (TextView) findViewById(R.id.change_userZhuanYe_text);
        mChangeUserZhuanYeText.setOnClickListener(this);
    }

    private int ENTER = 1;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, ChangeInformation.class);
        switch (v.getId()) {
            case R.id.change_userName:
                intent.putExtra("uText", mChangeUserNameText.getText().toString());
                intent.putExtra("id", 1);
                startActivityForResult(intent, ENTER);
                break;
            case R.id.change_userGeQian:
                intent.putExtra("uText", mChangeUserGeQianText.getText().toString());
                intent.putExtra("id", 2);
                startActivityForResult(intent, ENTER);
                break;
            case R.id.change_userBirth:
                showDateDialog();
                break;
        }
    }

    private String path = "http://nmy1206.natapp1.cc/userXinXiXiuGai.php";

    private void showDateDialog() {
        Calendar startDate = Calendar.getInstance();
        startDate.set(1985, 0, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2004, 0, 1);
        Calendar nowDate = Calendar.getInstance();
        nowDate.set(2000, 4, 20);

        TimePickerView timePickerView = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onTimeSelect(Date date, View v) {
                showDiaLog(PersonalInformation.this, R.drawable.loading);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String datebirth = simpleDateFormat.format(date);
                cunshareUser("user", "user_birth", datebirth);
                changeInternetInformation(path, 2, datebirth, null, getMyXueHao());
                mChangeUserBirthText.setText(datebirth);
            }
        }).setType(new boolean[]{true, true, true, false, false, false})
                .setCancelText("取消")
                .setCancelColor(getResources().getColor(R.color.ziti))
                .setSubmitText("保存")
                .setTitleText("请选择出生日期")
                .setTitleSize(16)
                .setTitleBgColor(getResources().getColor(R.color.beijing))
                .setBgColor(getResources().getColor(R.color.beijing))
                .setCancelColor(getResources().getColor(R.color.gray))
                .setTitleColor(getResources().getColor(R.color.gray))
                .setSubmitColor(getResources().getColor(R.color.bilan))
                .setRangDate(startDate, endDate)
                .setDate(nowDate)
                .setLabel("-", "-", " ", null, null, null)
                .isCenterLabel(true)
                .isDialog(true).build();

        timePickerView.show();
    }

    private void cunshareUser(String dizhi, String text, String neirong) {
        SharedPreferences sharedPreferences2 = this.getSharedPreferences(dizhi, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences2.edit();
        editor.putString(text, neirong);
        editor.commit();
    }

    private Handler handlerper = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (msg.obj.equals("0")) {
                        closeDiaLog();
                        ToastZong.ShowToast(PersonalInformation.this, "保存成功");
                    } else {
                        closeDiaLog();
                        ToastZong.ShowToast(PersonalInformation.this, "保存失败,请重新设置");
                    }
                    break;
            }
        }
    };

    private void changeInternetInformation(String path, int id, final String message, String oldName, String xuehao1) {
        HttpUtil.changeInformation(path, id, message, oldName, xuehao1, new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String a = response.body().string();
                Message messageper = new Message();
                messageper.what = 1;
                messageper.obj = a;
                handlerper.sendMessage(messageper);
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                if (e instanceof SocketTimeoutException) {
                    ToastZong.ShowToast(PersonalInformation.this, "连接读取超时");
                }
                if (e instanceof ConnectException) {
                    ToastZong.ShowToast(PersonalInformation.this, "连接错误");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                switch (resultCode) {
                    case 1:
                        mChangeUserNameText.setText(data.getStringExtra("uText"));
                        break;
                    case 2:
                        mChangeUserGeQianText.setText(data.getStringExtra("uText"));
                        break;
                }
                break;
            default:

                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (mChangeUserNameText.getText().toString() != uName || mChangeUserBirthText.getText().toString() != uBirth ||
                mChangeUserGeQianText.getText().toString() != uGeQian) {
            Intent intent = new Intent();
            setResult(1, intent);
        }
        finish();
    }
}
