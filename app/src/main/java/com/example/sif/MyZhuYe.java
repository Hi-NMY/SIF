package com.example.sif;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.MediaStoreSignature;
import com.example.sif.Lei.MyBroadcastReceiver.BroadcastRec;
import com.example.sif.Lei.MyToolClass.DataEncryption;
import com.example.sif.Lei.MyToolClass.MyDateClass;
import com.example.sif.Lei.MyToolClass.ObtainUser;
import com.example.sif.Lei.MyToolClass.ShowDiaLog;
import com.example.sif.Lei.MyToolClass.ToastZong;
import com.example.sif.Lei.NewAppDownLoad.NewVersion;
import com.example.sif.Lei.NewAppDownLoad.ObtainVersion;
import com.example.sif.Lei.NiceImageView.CircleImageView;
import com.example.sif.Lei.NiceImageView.SolidImageView;
import com.example.sif.Lei.ShowActivityBar.FragmentActivityBar;
import com.example.sif.ui.dashboard.DashboardFragment;
import com.example.sif.ui.home.HomeFragment;
import com.example.sif.ui.notifications.NotificationsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyZhuYe extends BaseActivity implements View.OnClickListener {

//    BottomNavigationView navView;
//    NavController navController;

    private BottomNavigationView bottomNavigationView;
    private ImageView imageView;
    private RelativeLayout mZongR;
    private NavigationView navigationView;
    public DrawerLayout drawerLayout;
    private RelativeLayout mCelanShezhiZong;
    public FragmentActivityBar fragmentActivityBar;
    private TextView version_name;

    private RefreshMySpace refreshMySpace;
    private NewMessageNotice newMessageNotice;
    private boolean newNotice = false;
    private View mViewNoticemessage;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_zhu_ye);

        initView();

        refreshMySpace = new RefreshMySpace();
        newMessageNotice = new NewMessageNotice();
        BroadcastRec.obtainRecriver(MyApplication.getContext(), "refreshMySpace", refreshMySpace);
        BroadcastRec.obtainRecriver(MyApplication.getContext(), "newNotice", newMessageNotice);

        NewVersion.inspectVerSion(MyZhuYe.this);

        selectonFragment(1);

        //状态栏
        ZTL();
        //增加padding  避免状态栏遮挡
        setPadding(this, mZongR);
        //去除原有样式
        bottomNavigationView.setItemIconTintList(null);
        //下部导航栏
        daoHangLan(bottomNavigationView);

        //侧栏功能头部   //获取对象并传入
        navigationView = (NavigationView) findViewById(R.id.zhuye_celan);
        headerCeLan(navigationView);

        int nkey = getIntent().getIntExtra("key", 1);
        if (nkey == 0) {
            BroadcastRec.sendReceiver(this,"newNotice",0,"");
            Intent intent = new Intent(this, MessageNotice.class);
            startActivity(intent);
        }

    }


    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private HomeFragment homeFragment;
    private DashboardFragment dashboardFragment;
    private NotificationsFragment notificationsFragment;
    private int a;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void selectonFragment(int index) {
        fragmentActivityBar = (FragmentActivityBar) getSupportFragmentManager().findFragmentById(R.id.ZhuYe_Avtivitybar);
        if (fragmentManager == null) {
            fragmentManager = this.getSupportFragmentManager();
        }
        fragmentTransaction = fragmentManager.beginTransaction();
        hideFragments(fragmentTransaction);
        switch (index) {
            case 1:
                if (homeFragment == null) {
                    // 如果为空，则创建一个并添加到界面上
                    homeFragment = new HomeFragment();
                    fragmentTransaction.add(R.id.nav_host_fragment, homeFragment);
                    showActivityBarHome();
                    a = 1;
                    Log.d("aaa", "主页键");
                } else {
                    // 如果不为空，则直接将它显示出来
                    fragmentTransaction.show(homeFragment);
                    showActivityBarHome();
                    a = 1;
                    Log.d("aaa", "主页现");
                }
                break;
            case 2:
                if (dashboardFragment == null) {
                    // 如果为空，则创建一个并添加到界面上
                    dashboardFragment = new DashboardFragment();
                    fragmentTransaction.add(R.id.nav_host_fragment, dashboardFragment);
                    showActivityBarDash();
                    a = 2;
                    Log.d("aaa", "广场zong键");
                } else {
                    // 如果不为空，则直接将它显示出来
                    fragmentTransaction.show(dashboardFragment);
                    showActivityBarDash();
                    a = 2;
                    Log.d("aaa", "广场zong现");
                }
                break;
            case 3:
                if (notificationsFragment == null) {
                    // 如果为空，则创建一个并添加到界面上
                    notificationsFragment = new NotificationsFragment();
                    fragmentTransaction.add(R.id.nav_host_fragment, notificationsFragment);
                    showActivityBarNoti();
                    a = 3;
                    Log.d("aaa", "信息键");
                } else {
                    // 如果不为空，则直接将它显示出来
                    fragmentTransaction.show(notificationsFragment);
                    showActivityBarNoti();
                    Log.d("aaa", "信息现");
                    a = 3;
                }
                break;
        }
        fragmentTransaction.commit();
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (homeFragment != null) {
            transaction.hide(homeFragment);
            Log.d("aaa", "主页yin");
        }
        if (dashboardFragment != null) {
            transaction.hide(dashboardFragment);
            Log.d("aaa", "广场zongyin");
        }
        if (notificationsFragment != null) {
            transaction.hide(notificationsFragment);
            Log.d("aaa", "信息yin");
        }

    }

    private static int FONTSIZE = 19;
    private int fragmentKey = 0;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void showActivityBarHome() {
        biaoTi(fragmentActivityBar, this, "fonts/impact.ttf", FONTSIZE);
        fragmentActivityBar.showZuJian(this, true, false, true, false, false, true, false);
        fragmentActivityBar.showZuJianUI(R.drawable.wode_blue, null, "SIF", null, R.drawable.rili1, 0);
        fragmentActivityBar.showUiFunction(2, 0, 0, 0, 0, 1, 0);
        fragmentActivityBar.barBackground(R.color.beijing);
        fragmentKey = 1;
        fragmentActivityBar.messageNotice(false);
    }

    private static int FONTSIZE1 = 16;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void showActivityBarDash() {
        //更改字体
        biaoTi(fragmentActivityBar, this, "fonts/impact.ttf", FONTSIZE1);
        //页内切换
        fragmentActivityBar.showZuJian(this, true, true, false, true, false, true, false);
        fragmentActivityBar.showZuJianUI(R.drawable.wode_blue, "广场", null, "关注", R.drawable.tongzhiguanli, 0);
        fragmentActivityBar.showUiFunction(2, 1, 0, 1, 0, 2, 0);
        fragmentActivityBar.barBackground(R.color.beijing);
        fragmentKey = 2;
        if (newNotice){
            fragmentActivityBar.messageNotice(true);
        }else {
            fragmentActivityBar.messageNotice(false);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void showActivityBarNoti() {
        //更改字体
        biaoTi(fragmentActivityBar, this, "fonts/impact.ttf", FONTSIZE1);
        fragmentActivityBar.showZuJian(this, true, false, true, false, false, true, true);
        fragmentActivityBar.showZuJianUI(R.drawable.wode_blue, null, "消息", null, R.drawable.jiahao_black, R.drawable.saoma);
        fragmentActivityBar.showUiFunction(2, 0, 0, 0, 0, 0, 1);
        fragmentActivityBar.barBackground(R.color.beijing);
        fragmentKey = 3;
        fragmentActivityBar.messageNotice(false);
    }

    private View headerView;
    private ImageView headerGuanBi;
    private CircleImageView circleImageView;    //头像未添加！！！！！！！！！
    private TextView muserName;
    private TextView Name;
    private TextView mbanji;
    private TextView mxibu;
    private TextView mgeqian;
    private ImageView headerXiuGai;
    private String userName;
    private String userBirth;
    private String userXingBie;
    private String userGeQian;
    private String userZhuanYe;
    private int ENTER = 1;
    private boolean jiaru;
    private ExecutorService executorService;
    private RelativeLayout headerMySpace;
    private RelativeLayout headerCollection;
    private RelativeLayout headerFriendMa;
    private ShowDiaLog qrdialog;
    private Bitmap myQrImage;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void headerCeLan(NavigationView navigationView) {
        headerView = navigationView.getHeaderView(0);
        //初始化控件
        showDiaLog(this, R.drawable.loading2);
        handerinitView();
        gx();
        userName = muserName.getText().toString();
        userBirth = user_birth;
        userXingBie = xingbie;
        userGeQian = user_geqian;
        userZhuanYe = zhuanye;
        headerGuanBi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(Gravity.LEFT);
            }
        });

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyZhuYe.this, PersonalInformation.class);
                intent.putExtra("uName", userName);
                intent.putExtra("uBirth", userBirth);
                intent.putExtra("uXingBie", userXingBie);
                intent.putExtra("uGeQian", userGeQian);
                intent.putExtra("uZhuanYe", userZhuanYe);
                startActivityForResult(intent, ENTER);
            }
        });

        headerXiuGai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyZhuYe.this, PersonalInformation.class);
                intent.putExtra("uName", userName);
                intent.putExtra("uBirth", userBirth);
                intent.putExtra("uXingBie", userXingBie);
                intent.putExtra("uGeQian", userGeQian);
                intent.putExtra("uZhuanYe", userZhuanYe);
                startActivityForResult(intent, ENTER);
            }
        });
        headerMySpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyApplication.getContext(), MySpace.class);
                intent.putExtra("id", 1);
                intent.putExtra("uName", userName);
                intent.putExtra("uXingBie", userXingBie);
                intent.putExtra("uGeQian", userGeQian);
                intent.putExtra("uZhuanYe", userZhuanYe);
                intent.putExtra("xibu", xibu);
                intent.putExtra("nianji", nianji);
                intent.putExtra("uXueHao", getMyXueHao());
                startActivity(intent);
            }
        });

        headerCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyApplication.getContext(), MyCollection.class);
                startActivity(intent);
            }
        });

        headerFriendMa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = DataEncryption.encryptString("2" + getMyXueHao() + MyDateClass.showNowDate());
                myQrImage = CodeUtils.createImage(message, 400, 400, null);
                View qr = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.qrcode_dialog, null);
                qrdialog = new ShowDiaLog(MyZhuYe.this, R.style.AlertDialog_qr, qr);
                qrdialog.logWindow(new ColorDrawable(Color.TRANSPARENT));
                qrdialog.showMyDiaLog();
                ImageView close = (ImageView) qr.findViewById(R.id.my_qr_close);
                SolidImageView qrimage = (SolidImageView) qr.findViewById(R.id.my_qrCode);
                TextView qrtext = (TextView) qr.findViewById(R.id.my_qr_text);
                qrFunction(close, qrimage, qrtext);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.funOne:
                        Intent intent = new Intent(MyApplication.getContext(),SchoolTimeTable.class);
                        startActivity(intent);
                        break;
                    case R.id.funTwo:
                        Intent intent2 = new Intent(MyApplication.getContext(),MyDiary.class);
                        startActivity(intent2);
                        break;
                    case R.id.funThree:

                        break;
                }
                return true;
            }
        });
    }

    private void qrFunction(ImageView close, SolidImageView qrimage, TextView qrtext) {
        SpannableStringBuilder builder = new SpannableStringBuilder(qrtext.getText().toString());
        builder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.bilan)), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.bilan)), 6, 11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        qrtext.setText(builder);
        qrimage.setImageBitmap(myQrImage);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qrdialog.closeMyDiaLog();
            }
        });
    }

    private int number;
    private String updateTime;

    private void gx() {
        version_name.setText("V " + ObtainVersion.versionName(this));
        if (number != 6) {
            SharedPreferences sharedPreferences = getSharedPreferences("userImageHeadDate", MODE_PRIVATE);
            userImageHeadDate = sharedPreferences.getString("userImageHeadDate", String.valueOf(System.currentTimeMillis()));
            Glide.with(MyApplication.getContext())
                    .load("http://nmy1206.natapp1.cc/UserImageServer/" + getMyXueHao() + "/HeadImage/myHeadImage.png")
                    .signature(new MediaStoreSignature(userImageHeadDate, 1, 1))
                    .placeholder(R.drawable.nostartimage_three)
                    .fallback(R.drawable.defaultheadimage)
                    .error(R.drawable.defaultheadimage)
                    .into(circleImageView);
            executorService = Executors.newFixedThreadPool(2);
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    jiaru = geRenXinXiShare();
                }
            });
            executorService.shutdown();
            while (true) {
                if (executorService.isTerminated()) {
                    if (jiaru) {
                        muserName.setText(user_name);
                        Name.setText(name);
                        mbanji.setText(banji);
                        mxibu.setText(xibu);
                        mgeqian.setText(user_geqian);
                        muserName.postInvalidate();
                        Name.postInvalidate();
                        mbanji.postInvalidate();
                        mxibu.postInvalidate();
                        mgeqian.postInvalidate();
                        closeDiaLog();
                        break;
                    } else {
                        gx();
                        number += 1;
                        break;
                    }
                }
            }
        } else {
            ToastZong.ShowToast(this, "信息获取错误,请重新登陆！");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Glide.with(MyApplication.getContext())
                .load("http://nmy1206.natapp1.cc/UserImageServer/" + getMyXueHao() + "/HeadImage/myHeadImage.png")
                .signature(new MediaStoreSignature(userImageHeadDate, 1, 1))
                .placeholder(R.drawable.nostartimage_three)
                .fallback(R.drawable.defaultheadimage)
                .error(R.drawable.defaultheadimage)
                .into(circleImageView);
    }


    private Handler qrHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    ToastZong.ShowToast(MyApplication.getContext(), "用户信息错误");
                    break;
                case 2:
                    ToastZong.ShowToast(MyApplication.getContext(), "用户信息错误");
                    break;
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            headerCeLan(navigationView);
        }

        if (requestCode == 1000) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    try {
                        String message = bundle.getString(CodeUtils.RESULT_STRING);
                        String msg = DataEncryption.decryptString(message);
                        String fun = msg.substring(0, 1);
                        String x = msg.substring(1, 10);
                        ObtainUser.obtainUser(this, x, qrHander);
                    } catch (Exception e) {
                        ToastZong.ShowToast(this, "出错了,请重新扫描");
                    }
                } else {
                    ToastZong.ShowToast(this, "出错了,请重新扫描");
                }
            }
        }
    }

    private void handerinitView() {
        headerGuanBi = (ImageView) headerView.findViewById(R.id.celan_guanbi);
        headerXiuGai = (ImageView) headerView.findViewById(R.id.celan_xiugai);
        circleImageView = (CircleImageView) headerView.findViewById(R.id.celan_touxiang);
        muserName = (TextView) headerView.findViewById(R.id.celan_userName);
        Name = (TextView) headerView.findViewById(R.id.celan_Name);
        mbanji = (TextView) headerView.findViewById(R.id.celan_banji);
        mxibu = (TextView) headerView.findViewById(R.id.celan_xibu);
        mgeqian = (TextView) headerView.findViewById(R.id.celan_geqian);
        headerMySpace = (RelativeLayout) headerView.findViewById(R.id.header_myspace);
        headerCollection = (RelativeLayout) headerView.findViewById(R.id.header_shoucang);
        headerFriendMa = (RelativeLayout) headerView.findViewById(R.id.header_friendma);
    }


    //导航栏加号
    private void tianJia() {
        imageView.setVisibility(View.VISIBLE);
        imageView.setAlpha(1.0f);
        Animation animation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        );
        animation.setDuration(70);
        animation.setFillAfter(true);
        imageView.startAnimation(animation);
    }

    private void initView() {
        imageView = (ImageView) findViewById(R.id.GuangChang_TianJia);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view);
        mZongR = (RelativeLayout) findViewById(R.id.Zong_R);
        drawerLayout = (DrawerLayout) findViewById(R.id.celan_drawer);
        mCelanShezhiZong = (RelativeLayout) findViewById(R.id.celan_shezhi_Zong);

        mCelanShezhiZong.setOnClickListener(this);
        version_name = (TextView) findViewById(R.id.version_name);
        version_name.setOnClickListener(this);
        mViewNoticemessage = (View) findViewById(R.id.view_noticemessagezong);
    }

    //导航栏加号
    private void daoHangLan(BottomNavigationView bottomNavigationView) {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_dashboard:
                        new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tianJia();
                                        mViewNoticemessage.setVisibility(View.INVISIBLE);
                                    }
                                });
                            }
                        }.start();
                        selectonFragment(2);
                        break;
                    case R.id.navigation_home:
                        selectonFragment(1);
                        guangChangTianJia();
                        obtainNowNotice();
                        break;
                    case R.id.navigation_notifications:
                        selectonFragment(3);
                        guangChangTianJia();
                        obtainNowNotice();
                        break;
                }
                return true;
            }
        });
        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_dashboard:
                        Intent intent = new Intent(MyZhuYe.this, GuangChangMessage.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.activity_enter_bottom, R.anim.activity_main);
                        break;
                }
            }
        });
    }

    private void obtainNowNotice(){
        if (newNotice){
            mViewNoticemessage.setVisibility(View.VISIBLE);
        }else {
            mViewNoticemessage.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        guangChangTianJia();
    }

    private void guangChangTianJia() {
        imageView.setVisibility(View.INVISIBLE);
        imageView.setAlpha(0.0f);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.celan_shezhi_Zong:
                Intent intent1 = new Intent(this, SheZhi.class);
                startActivity(intent1);
                break;
        }
    }

    private long exitTime;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && a != 1) {
            selectonFragment(1);
            guangChangTianJia();
            obtainNowNotice();
            drawerLayout.closeDrawer(Gravity.LEFT);
            bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        }
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 && a == 1) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            ToastZong.ShowToast(this, "再按一次退出应用");
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments == null) {
            return;
        }
        for (Fragment fragment : fragments) {
            if (fragment != null) {
                fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    class RefreshMySpace extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String path = "http://nmy1206.natapp1.cc/userSpace.php";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            final String nowTime = simpleDateFormat.format(System.currentTimeMillis());
            sendUserSpace(1, path, nowTime, getMyXueHao(), null);
        }
    }

    public static boolean offKey = false;
    class NewMessageNotice extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            int a = intent.getIntExtra("textone",-1);
            if (a == 0 || a == 3){
                newNotice = true;
                obtainNowNotice();
                if (fragmentKey == 2){
                    fragmentActivityBar.messageNotice(true);
                }
                if (a == 3){
                    offKey = true;
                }
            }
            if (a == 1){
                newNotice = false;
                obtainNowNotice();
                offKey = false;
                if (fragmentKey == 2){
                    fragmentActivityBar.messageNotice(false);
                }
            }
        }
    }
}
