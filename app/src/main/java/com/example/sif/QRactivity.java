package com.example.sif;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.example.sif.Lei.MyToolClass.DataEncryption;
import com.example.sif.Lei.MyToolClass.ObtainUser;
import com.example.sif.Lei.MyToolClass.ToastZong;
import com.example.sif.Lei.ShowActivityBar.FragmentActivityBar;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.File;
import java.util.List;

public class QRactivity extends BaseActivity {

    private FragmentActivityBar fragmentActivityBar;
    private RelativeLayout relativeLayout;
    private View fragment;
    private FrameLayout mFlMyContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qractivity);

        ZTL();

        fragment = findViewById(R.id.qr_camera_bar);
        relativeLayout = (RelativeLayout)findViewById(R.id.qr_second);
        mFlMyContainer = (FrameLayout) findViewById(R.id.fl_my_container);

        showActivityBar();

        setPadding(this, relativeLayout);
      //  setPadding2(this, mFlMyContainer);

        CaptureFragment captureFragment = new CaptureFragment();
        CodeUtils.setFragmentArgs(captureFragment, R.layout.qr_camera);

        captureFragment.setAnalyzeCallback(analyzeCallback);

        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();

    }

    private void showActivityBar() {
        fragmentActivityBar = (FragmentActivityBar) getSupportFragmentManager().findFragmentById(R.id.qr_camera_bar);
        fragment.bringToFront();
        ShowActivityBars showActivityBars = new ShowActivityBars(this, fragmentActivityBar);
        showActivityBars.showKongJian(true, false, false, false, true, false, false);
        showActivityBars.showUI(R.drawable.close_white, (String) null, null, null, "相册", 0, 0);
        showActivityBars.uiFunction(1, 0, 0, 0, 3, 0, 0);
        showActivityBars.barBackground1(0);
    }

    private File file;
    private Handler qrHander = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    ToastZong.ShowToast(MyApplication.getContext(),"用户信息错误");
                    break;
                case 2:
                    ToastZong.ShowToast(MyApplication.getContext(),"用户信息错误");
                    break;
            }
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PictureConfig.CHOOSE_REQUEST && data != null) {
            List<LocalMedia> localMedia = PictureSelector.obtainMultipleResult(data);
            for (LocalMedia m : localMedia) {
                file = new File(m.getRealPath());
            }
            try {
                CodeUtils.analyzeBitmap(String.valueOf(file), new CodeUtils.AnalyzeCallback() {
                    @Override
                    public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                        String msg = DataEncryption.decryptString(result);
                        String fun = msg.substring(0, 1);
                        String x = msg.substring(1, 10);
                        ObtainUser.obtainUser(QRactivity.this,x,qrHander);
                    }

                    @Override
                    public void onAnalyzeFailed() {
                        ToastZong.ShowToast(QRactivity.this, "出错了,请重新扫描");
                    }
                });
            } catch (Exception e) {

            }
        }
    }

    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
            bundle.putString(CodeUtils.RESULT_STRING, result);
            resultIntent.putExtras(bundle);
            QRactivity.this.setResult(RESULT_OK, resultIntent);
            QRactivity.this.finish();
        }

        @Override
        public void onAnalyzeFailed() {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
            bundle.putString(CodeUtils.RESULT_STRING, "");
            resultIntent.putExtras(bundle);
            QRactivity.this.setResult(RESULT_OK, resultIntent);
            QRactivity.this.finish();
        }
    };

}
