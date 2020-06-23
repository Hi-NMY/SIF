package com.example.sif.Lei.MyToolClass;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import com.example.sif.MyApplication;
import com.example.sif.R;
import com.example.sif.SearchNewUser;

public class MessageAddDialog extends Dialog implements View.OnClickListener {

    private View rootView;
    private Context context;
    private int vId;


    public MessageAddDialog(@NonNull Context context, int themeResId) {
        super(context, R.style.adddialog);
        this.context = context;
        this.vId = themeResId;
    }

    public void init() {
        rootView = LayoutInflater.from(context).inflate(vId, null);
        setContentView(rootView);
        initView();
    }

    private RelativeLayout mAddFunOne;
    private void initView() {
        mAddFunOne = (RelativeLayout)rootView.findViewById(R.id.add_fun_one);
        mAddFunOne.setOnClickListener(this);
    }

    public void showDialog() {
        this.show();
        init();
    }

    public View getView() {
        return rootView;
    }

    public void dismissDialog(){
        this.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_fun_one:
                Intent intent = new Intent(MyApplication.getContext(), SearchNewUser.class);
                context.startActivity(intent);
                dismissDialog();
                break;
        }
    }
}
