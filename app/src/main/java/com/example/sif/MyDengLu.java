package com.example.sif;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.sif.Lei.LianJie.ShuJuLianJie;
import com.example.sif.Lei.MyToolClass.InValues;
import com.example.sif.Lei.MyToolClass.MyDateClass;
import com.example.sif.Lei.MyToolClass.ToastZong;
import com.example.sif.Lei.ShowActivityBar.FragmentActivityBar;

public class MyDengLu extends BaseActivity implements View.OnClickListener {

    private EditText mDengluXueHao;
    private TextView mDengluZhuCe;
    private Button mXiaYiBu;

    private static int MAXLENGTH = 9;

    protected static final int ERROR = 2;
    protected static final int SUCCESS = 1;

    private String path = "";


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SUCCESS:
                    String I = (String)msg.obj;
                    switch (I){
                        case "0":
                            huoQuGeRenXinXi(MyDengLu.this,InValues.send(R.string.UserSchool),InValues.send(R.string.User),userZhangHao,handler);
                            closeDiaLog();
                            String nowDate = MyDateClass.showNowDate();
                            String path3="";
                            shuaXinGuangChang(InValues.send(R.string.shuaXinGuangChang),null,nowDate,1);
                            String path4 = "";
                            sendUserSpace(1,InValues.send(R.string.userSpace),nowDate,userZhangHao,null);
                            String path5 = "";
                            huoquThumb(InValues.send(R.string.Thumb),userZhangHao);
                            Intent intent = new Intent(MyDengLu.this,MyDengLu2.class);
                            intent.putExtra("zhanghao",mDengluXueHao.getText().toString().trim());
                            setMyXueHao(mDengluXueHao.getText().toString().trim());
                            startActivity(intent);
                            break;
                        case "3":
                            closeDiaLog();
                            ToastZong.ShowToast(MyDengLu.this,"服务器错误！");
                            break;
                        case "5":
                            closeDiaLog();
                            ToastZong.ShowToast(MyDengLu.this,"请输入正确的学号！");
                            break;
                    }
                    break;
                case ERROR:
                    closeDiaLog();
                    ToastZong.ShowToast(MyDengLu.this,"连接错误");
                    break;
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_deng_lu);
        initView();
        //设置状态栏
        ZTL();
        //ActivityBar
        showActivityBar();

        checkPermission();

        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.DengLu1_R);
        setPadding(this,relativeLayout);
        //下一步按钮
        noXiaYiBu(mDengluXueHao,mXiaYiBu,MAXLENGTH);

        //获取账号
        Intent intent = getIntent();
        String tian = intent.getStringExtra("zhanghao");
        mDengluXueHao.setText(tian);
    }

    private void initView() {
        mDengluXueHao = (EditText) findViewById(R.id.denglu_XueHao);
        mDengluZhuCe = (TextView) findViewById(R.id.denglu_ZhuCe);
        mXiaYiBu = (Button) findViewById(R.id.xiayibu);

        mXiaYiBu.setOnClickListener(this);
        mDengluZhuCe.setOnClickListener(this);

    }

    private String path1 = "";
    private String path2 = "";
    private String userZhangHao;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.xiayibu:
                userZhangHao = mDengluXueHao.getText().toString().trim();
                while (true) {
                    if (TextUtils.isEmpty(userZhangHao)) {
                        break;
                    }
                    if (userZhangHao != null) {
                        showDiaLog(this,R.drawable.loading);
                        new Thread() {
                            @Override
                            public void run() {
                                try {
                                    sleep(100);
                                    ShuJuLianJie shuJuLianJie = new ShuJuLianJie(MyDengLu.this, InValues.send(R.string.XueSheng_YanZheng), "POST", handler);
                                    shuJuLianJie.chuanShu(userZhangHao);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();

                        break;
                    }
                }

                break;
            case R.id.denglu_ZhuCe:
                Intent intent = new Intent(this,ZhuCe.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void showActivityBar(){
        FragmentActivityBar fragmentActivityBar = (FragmentActivityBar) getSupportFragmentManager().findFragmentById(R.id.Denglu_Frag_ActivityBar);
        ShowActivityBars showActivityBars = new ShowActivityBars(this,fragmentActivityBar);
        showActivityBars.showKongJian(true, false, false,false, false,false, false);
        showActivityBars.showUI(R.drawable.zuo_white, (String) null, null,null, 0, 0);
        showActivityBars.uiFunction(1,0,0,0,0,0,0);
        showActivityBars.barBackground1(0);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,  final String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        checkPermission();
    }

}
