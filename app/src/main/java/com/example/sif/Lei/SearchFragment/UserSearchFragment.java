package com.example.sif.Lei.SearchFragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.sif.BaseFragment;
import com.example.sif.Lei.MyToolClass.EasyLoading;
import com.example.sif.Lei.MyToolClass.ObtainSearchUser;
import com.example.sif.Lei.MyToolClass.ToastZong;
import com.example.sif.MyApplication;
import com.example.sif.R;
import com.example.sif.SearchNewUser;

public class UserSearchFragment extends BaseFragment {

    private View view;
    private EditText mSearchMessage;
    private SearchNewUser searchNewUser;

    private Handler newUserHanlder = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ToastZong.ShowToast(MyApplication.getContext(),"未搜索到该用户");
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.search_fragment, container, false);
        initView();
        searchNewUser = (SearchNewUser)getActivity();
        mSearchMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(" ")) {
                    String[] str = s.toString().split(" ");
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < str.length; i++) {
                        sb.append(str[i]);
                    }
                    mSearchMessage.setText(sb.toString());
                    mSearchMessage.setSelection(start);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mSearchMessage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!mSearchMessage.getText().toString().trim().equals("")){
                        searchNewUser.hideKeyboard(mSearchMessage,1);
                        EasyLoading.startLoading(searchNewUser);
                        ObtainSearchUser.obtainUser(mSearchMessage.getText().toString().trim(),newUserHanlder);
                    }
                }
                return false;
            }
        });

        return view;
    }

    private void initView() {
        mSearchMessage = (EditText)view.findViewById(R.id.search_message);
        mSearchMessage.setHint("请输入昵称/账号查找新朋友");
    }
}
