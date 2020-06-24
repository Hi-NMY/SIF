package com.example.sif.Lei.SearchFragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.sif.BaseFragment;
import com.example.sif.Lei.MyToolClass.ObtainSearchUser;
import com.example.sif.R;

public class UserSearchFragment extends BaseFragment {

    private View view;
    private EditText mSearchMessage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.search_fragment, container, false);
        initView();

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
                if (!mSearchMessage.getText().toString().trim().equals("")){
                    ObtainSearchUser.obtainUser(mSearchMessage.getText().toString().trim());
                }
            }
        });

        return view;
    }

    private void initView() {
        mSearchMessage = (EditText)view.findViewById(R.id.search_message);
        mSearchMessage.setHint("请输入昵称/账号查找新朋友");
    }
}
