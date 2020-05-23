package com.example.sif;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.example.sif.Lei.ShowActivityBar.FragmentActivityBar;

public class SchoolEducationalAdministration extends BaseActivity {

    private FragmentActivityBar fragmentActivityBar;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_educational_administration);

        ZTL();

        showActivitybar();

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.school_educational_administration_R);
        setPadding(this, relativeLayout);

        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        WebView webView = (WebView)findViewById(R.id.school_educational_administration);
                        WebSettings webSettings = webView.getSettings();
                        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
                        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
                        webSettings.setJavaScriptEnabled(true);
                        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
                        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
                        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
                        webView.setWebViewClient(new WebViewClient());
                        webView.loadUrl("http://www.szitu.cn:805");
                    }
                });
            }
        }).start();

    }

    @SuppressLint("NewApi")
    private void showActivitybar() {
        fragmentActivityBar = (FragmentActivityBar) getSupportFragmentManager().findFragmentById(R.id.school_educational_administration_bar);
        ShowActivityBars showActivityBars = new ShowActivityBars(this, fragmentActivityBar);
        showActivityBars.showKongJian(true, false, true, false, true, false, false);
        showActivityBars.showUI(R.drawable.zuo_black, (String) null, "校教务", null, null, 0, 0);
        showActivityBars.uiFunction(1, 0, 0, 0, 0, 0, 0);
        showActivityBars.barBackground1(R.color.beijing);
    }
}
