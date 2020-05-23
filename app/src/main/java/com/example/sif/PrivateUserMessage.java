package com.example.sif;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.sif.Lei.MyToolClass.ObtainUser;
import com.example.sif.Lei.MyToolClass.ToastZong;
import com.example.sif.Lei.ShowActivityBar.FragmentActivityBar;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;

public class PrivateUserMessage extends BaseActivity {

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
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

    private String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_user_message);

//        //设置状态栏
//        ZTL();
//
//        //增加padding  避免状态栏遮挡
//        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.private_r);
//        setPadding(this, linearLayout);

        Intent intent = getIntent();
        userName = intent.getData().getQueryParameter("title");

//        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
//            @Override
//            public UserInfo getUserInfo(String s) {
//                UserInfo userInfo = new UserInfo(getMyXueHao(), getMyUserName(), Uri.parse("http://nmy1206.natapp1.cc/UserImageServer/"+getMyXueHao()+"/HeadImage/myHeadImage.png"));
//                RongIM.getInstance().refreshUserInfoCache(userInfo);
//                return userInfo;
//            }
//        },true);

        ConversationFragment conversationFragment=new ConversationFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.demo_privatemessage, conversationFragment);
        transaction.commit();
        RongIM.getInstance().setMessageAttachedUserInfo(true);

        RongIM.setConversationClickListener(new RongIM.ConversationClickListener() {
            @Override
            public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo, String s) {
                ObtainUser.obtainUser(PrivateUserMessage.this, userInfo.getUserId(), handler);
                return true;
            }

            @Override
            public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo, String s) {
                return false;
            }

            @Override
            public boolean onMessageClick(Context context, View view, Message message) {
                return false;
            }

            @Override
            public boolean onMessageLinkClick(Context context, String s, Message message) {
                return false;
            }

            @Override
            public boolean onMessageLongClick(Context context, View view, Message message) {
                return false;
            }
        });

        //ActivityBar
        showActivityBar();

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void showActivityBar() {
        FragmentActivityBar fragmentActivityBar = (FragmentActivityBar) getSupportFragmentManager().findFragmentById(R.id.private_message_bar);
        ShowActivityBars showActivityBars = new ShowActivityBars(this, fragmentActivityBar);
        showActivityBars.showKongJian(true, false, true, false, false, false, false);
        showActivityBars.showUI(R.drawable.zuo_black, null, userName, null, 0, 0);
        showActivityBars.uiFunction(1, 0, 0, 0, 0, 0, 0);
    }
}
