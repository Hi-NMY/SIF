package com.example.sif.Lei.SearchFragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

            }

            @Override
            public void afterTextChanged(Editable s) {
                    mSearchMessage.removeTextChangedListener(this);
                    mSearchMessage.setText(s.toString().replace("#",""));
                    mSearchMessage.setSelection(mSearchMessage.getText().length());
                    mSearchMessage.addTextChangedListener(this);

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
        });
        return view;
    }

    private void thisFun(){
        if (fragment instanceof FragmentIb){
            fun = 1;
        }
        if (fragment instanceof FragmentIbMe){
            fun = 2;
        }
    }

    private void initView(View view){
        mSearchMessage = (EditText)view.findViewById(R.id.search_message);
    }

}
