package com.example.sif.Lei.SearchFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.example.sif.BaseFragment;
import com.example.sif.InterestingBlock;
import com.example.sif.MyApplication;
import com.example.sif.R;
import com.example.sif.ui.ib_viewpager.FragmentIb;
import com.example.sif.ui.ib_viewpager.FragmentIbMe;

public class SearchFragment extends BaseFragment {

    private View view;
    private ImageView mSearchImage;
    private EditText mSearchMessage;
    private RelativeLayout mIbSearch;
    
    private Fragment fragment;
    private InterestingBlock ibActivity;
    private int fun = 0;

    private LocalBroadcastManager localBroadcastManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.search_fragment, container, false);
        ibActivity = (InterestingBlock)getActivity();
        thisFun();

        initView(view);

        mSearchMessage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    fragment = ibActivity.getFragment();
                    thisFun();
                }
            }
        });

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
                    mSearchMessage.removeTextChangedListener(this);
                    mSearchMessage.setText(s.toString().replace("#",""));
                    mSearchMessage.setSelection(mSearchMessage.getText().length());
                    mSearchMessage.addTextChangedListener(this);

                    if (mSearchMessage.getText().toString().trim().equals("")){
                        hideKeyboard(mSearchMessage,1);
                        sendMessage();
                    }
                }
        });

        mSearchMessage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hideKeyboard(mSearchMessage,1);
                    sendMessage();
                }
                return false;
            }
        });
        return view;
    }

    private void sendMessage(){
        if (fun == 1){
            localBroadcastManager = LocalBroadcastManager.getInstance(MyApplication.getContext());
            Intent intent = new Intent("searchIbZong");
            intent.putExtra("id",-2);
            intent.putExtra("blockname",mSearchMessage.getText().toString().trim());
            localBroadcastManager.sendBroadcast(intent);
        }

        if (fun == 2){
            localBroadcastManager = LocalBroadcastManager.getInstance(MyApplication.getContext());
            Intent intent = new Intent("searchIbMy");
            intent.putExtra("id",-2);
            intent.putExtra("blockname",mSearchMessage.getText().toString().trim());
            localBroadcastManager.sendBroadcast(intent);
        }
    }

    private void thisFun(){
        if (fragment instanceof FragmentIb){
            fun = 1;
        }
        if (fragment instanceof FragmentIbMe){
            fun = 2;
        }
    }

    private void hideKeyboard(View v, int i) {
        InputMethodManager inputMethodManager = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (i == 1) {
            inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        } else {
            inputMethodManager.showSoftInput(v, 0);
        }
    }

    private void initView(View view){
        mSearchMessage = (EditText)view.findViewById(R.id.search_message);
    }

}
