package com.example.sif.ui.notifications;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.sif.BaseFragment;
import com.example.sif.Lei.MyToolClass.ObtainUser;
import com.example.sif.Lei.MyToolClass.ToastZong;
import com.example.sif.Lei.ShowActivityBar.FragmentActivityBar;
import com.example.sif.MyApplication;
import com.example.sif.MyZhuYe;
import com.example.sif.R;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imkit.model.UIConversation;
import io.rong.imlib.model.Conversation;

public class NotificationsFragment extends BaseFragment {

    private MyZhuYe myZhuYe;
    private FragmentActivityBar fragmentActivityBar;
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        myZhuYe = (MyZhuYe)getActivity();

        ConversationListFragment conversationListFragment = new ConversationListFragment();
        Uri uri = Uri.parse("rong://"+
                getActivity().getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(),"false")
                .build();

        conversationListFragment.setUri(uri);
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.demo,conversationListFragment);
        fragmentTransaction.commit();

        RongIM.setConversationListBehaviorListener(new RongIM.ConversationListBehaviorListener() {
            @Override
            public boolean onConversationPortraitClick(Context context, Conversation.ConversationType conversationType, String s) {
                ObtainUser.obtainUser(myZhuYe, s, handler);
                return true;
            }

            @Override
            public boolean onConversationPortraitLongClick(Context context, Conversation.ConversationType conversationType, String s) {
                return false;
            }

            @Override
            public boolean onConversationLongClick(Context context, View view, UIConversation uiConversation) {
                return false;
            }

            @Override
            public boolean onConversationClick(Context context, View view, UIConversation uiConversation) {
                return false;
            }
        });

        return root;
    }

}