package com.example.sif.Lei.MyToolClass;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.example.sif.Lei.LianJie.HttpUtil;
import com.example.sif.NeiBuLei.BlockLabel;
import com.example.sif.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class VeryPopupWindow extends PopupWindow implements View.OnClickListener {
    private Context context;
    private View view;
    private View mLabelTopR;
    private TextView mLabelClose;
    private TextView mLabelSend;
    private EditText mSearchMessage;
    private TagFlowLayout mMySelectorIb;
    private RelativeLayout mMySelectorR;
    private TextView mNullIbList;
    private TagFlowLayout mHisIbList;
    private TagFlowLayout mVeryibList;
    private RelativeLayout mVeryRzong;
    private RelativeLayout mSearchibRzong;
    private TagFlowLayout mSearchibList;

    private static Map<Integer,String> maphis;
    private static Map<Integer,String> mapvery;
    private static Map<Integer,String> maphisZong;
    private static Map<Integer,String> mapveryZong;
    private List<String> s;

    public VeryPopupWindow(Context c,List<String> strings) {
        maphis = new HashMap<>();
        mapvery = new HashMap<>();
        maphisZong = new HashMap<>();
        mapveryZong = new HashMap<>();
        this.context = c;
        this.s = strings;
        this.view = LayoutInflater.from(c).inflate(R.layout.very_popupview, null);
        initview();
        this.setContentView(view);
        this.setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        ColorDrawable colorDrawable = new ColorDrawable(0xb0000000);
        this.setBackgroundDrawable(colorDrawable);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.verypopupview);

    }

    private void initview() {
        mLabelTopR = (View) view.findViewById(R.id.label_top_r);
        mLabelClose = (TextView) view.findViewById(R.id.label_close);
        mLabelSend = (TextView) view.findViewById(R.id.label_send);
        mSearchMessage = (EditText) view.findViewById(R.id.search_message);
        mMySelectorIb = (TagFlowLayout) view.findViewById(R.id.my_selector_ib);
        mMySelectorR = (RelativeLayout) view.findViewById(R.id.my_selector_r);
        mNullIbList = (TextView) view.findViewById(R.id.null_ib_list);
        mHisIbList = (TagFlowLayout) view.findViewById(R.id.his_ib_list);
        mVeryibList = (TagFlowLayout) view.findViewById(R.id.veryib_list);
        mVeryRzong = (RelativeLayout) view.findViewById(R.id.very_Rzong);
        mSearchibRzong = (RelativeLayout) view.findViewById(R.id.searchib_Rzong);
        mSearchibList = (TagFlowLayout) view.findViewById(R.id.searchib_list);
        httpSendBlock(1, "");
        readShareHis();
        searchListener();

        mLabelClose.setOnClickListener(this);
        mLabelSend.setOnClickListener(this);
    }

    private LocalBroadcastManager localBroadcastManager;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.label_close:
                this.dismiss();
                break;
            case R.id.label_send:
                localBroadcastManager = LocalBroadcastManager.getInstance(context);
                Intent intent = new Intent("BlockLabel");
                intent.putExtra("B", (Serializable)strings);
                localBroadcastManager.sendBroadcast(intent);
                if (strings != null && strings.size() > 0){
                    sendShareHis();
                }
                this.dismiss();
                break;
        }
    }

    private void sendShareHis(){
        StringBuffer stringBuffer = new StringBuffer();
        SharedPreferences sharedPreferences = context.getSharedPreferences("block",Context.MODE_PRIVATE);
        String s = sharedPreferences.getString("b",null);
        if (s != null) {
            hisblock = Arrays.asList(s.split(","));
            for (String S:strings){
                if (!hisblock.contains(S)){
                    stringBuffer.append(S + ",");
                }
            }
        }else {
            for (String S:strings) {
                stringBuffer.append(S + ",");
            }
        }
        SharedPreferences sharedPreferences1 = context.getSharedPreferences("block",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences1.edit();
        if (s == null){
            editor.putString("b", String.valueOf(stringBuffer));
        }else {
            editor.putString("b", String.valueOf(stringBuffer)+s);
        }
        editor.commit();
    }

    private List<String> hisblock;
    private void readShareHis(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("block",Context.MODE_PRIVATE);
        String s = sharedPreferences.getString("b",null);
        if (s != null){
            hisblock = Arrays.asList(s.split(","));
            List arrList = new ArrayList(hisblock);
            myHisBlock(arrList);
            mNullIbList.setVisibility(View.INVISIBLE);
            mHisIbList.setVisibility(View.VISIBLE);
        }
    }

    private void changeview(int i) {
        switch (i) {
            case 1:
                mSearchibRzong.setVisibility(View.VISIBLE);
                mVeryRzong.setVisibility(View.GONE);
                break;
            case 2:
                mVeryRzong.setVisibility(View.VISIBLE);
                mSearchibRzong.setVisibility(View.GONE);
                break;
        }
    }

    private String name;
    private void searchListener() {
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

                name = mSearchMessage.getText().toString().trim();
                if (name != null && !name.equals("")) {
                    httpSendBlock(2, name);
                    changeview(1);
                } else {
                    changeview(2);
                }
            }
        });
    }

    private void myHisBlock(List<String> hisblock) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        if (hisblock.size() > 14){
            for (int a = 14;a < hisblock.size();a++){
                hisblock.remove(a);
            }
        }
        int num = 0;
        for (String b:hisblock){
            maphisZong.put(num,b);
            num += 1;
        }
        mHisIbList.setAdapter(new TagAdapter<String>(hisblock) {
            @Override
            public View getView(FlowLayout parent, int position, String o) {
                View view = layoutInflater.inflate(R.layout.label_list1, mHisIbList, false);
                selet = (TextView) view.findViewById(R.id.text);
                selet.setText(o);
                return view;
            }

            @Override
            public void onSelected(int position, View view) {
                super.onSelected(position, view);
                t = (TextView) view.findViewById(R.id.text);
                t.setTextColor(ContextCompat.getColor(context, R.color.ziti));
                if (hisKey == 0){
                    maphis.put(position,t.getText().toString());
                    checkVeryBlock(1,t.getText().toString());
                    mySelectorBlock(t.getText().toString(), 1);
                }
                hisKey = 0;
            }

            @Override
            public void unSelected(int position, View view) {
                super.unSelected(position, view);
                t = (TextView) view.findViewById(R.id.text);
                t.setTextColor(ContextCompat.getColor(context, R.color.gray));
                if (hisKey == 0 && key == 0){
                    checkVeryBlock(2,t.getText().toString());
                    maphis.remove(position);
                    mySelectorBlock(t.getText().toString(), 2);
                }
                hisKey = 0;
                key = 0;
            }
        });
    }

    private TextView textView;
    private TextView t;
    private View veryView;

    public void veryBlockLabel() {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        mVeryibList.setMaxSelectCount(5);
        int num = 0;
        for (BlockLabel b:blockLabels){
            mapveryZong.put(num,b.getIbname());
            num += 1;
        }
        mVeryibList.setAdapter(new TagAdapter<BlockLabel>(blockLabels) {
            @Override
            public View getView(FlowLayout parent, int position, BlockLabel s) {
                veryView = layoutInflater.inflate(R.layout.label_list1, mVeryibList, false);
                textView = (TextView) veryView.findViewById(R.id.text);
                textView.setText(s.getIbname());
                return veryView;
            }

            @SuppressLint("ResourceAsColor")
            @Override
            public void onSelected(int position, View view) {
                super.onSelected(position, view);
                t = (TextView) view.findViewById(R.id.text);
                t.setTextColor(ContextCompat.getColor(context, R.color.ziti));
                if (veryKey == 0) {
                    checkHisBlock(1,t.getText().toString());
                    mapvery.put(position, blockLabels.get(position).getIbname());
                    mySelectorBlock(blockLabels.get(position).getIbname(), 1);
                }
                veryKey = 0;
            }

            @Override
            public void unSelected(int position, View view) {
                super.unSelected(position, view);
                t = (TextView) view.findViewById(R.id.text);
                t.setTextColor(ContextCompat.getColor(context, R.color.gray));
                if (veryKey == 0 && key == 0){
                    checkHisBlock(2,t.getText().toString());
                    mapvery.remove(position);
                    mySelectorBlock(blockLabels.get(position).getIbname(), 2);
                }
                veryKey = 0;
                key = 0;
            }
        });
    }

    private void searchBlockSele() {
        if (blockLabels1.size() > 14){
            for (int a = 14;a < blockLabels1.size();a++){
                blockLabels1.remove(a);
            }
        }
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        mSearchibList.setAdapter(new TagAdapter<BlockLabel>(blockLabels1) {
            @Override
            public View getView(FlowLayout parent, int position, BlockLabel o) {
                View view = layoutInflater.inflate(R.layout.label_list1, mSearchibList, false);
                selet = (TextView) view.findViewById(R.id.text);
                selet.setText(o.getIbname());
                return view;
            }

            @Override
            public void onSelected(int position, View view) {
                super.onSelected(position, view);
                t = (TextView) view.findViewById(R.id.text);
                String nt = t.getText().toString();
                checkHisBlock(1,nt);
                checkVeryBlock(1,nt);
                if (position == 0){
                    mySelectorBlock(blockLabels1.get(position).getIbname().substring(2), 1);
                }else {
                    mySelectorBlock(blockLabels1.get(position).getIbname(), 1);
                }

                changeview(2);
                mSearchMessage.getText().clear();
                blockLabels1.clear();
            }
        });
    }

    private int veryKey = 0;
    private void checkVeryBlock(int i,String a){
        int num = (int) ValueToMap.getKey(mapveryZong,a);
        if (num != -1){
            switch (i){
                case 1:
                    if (mapveryZong.containsValue(a)){
                        veryKey = 1;
                        mVeryibList.getChildAt(num).performClick();
                        mapvery.put(num,a);
                    }
                    break;
                case 2:
                    if (mapvery.containsValue(a)){
                        veryKey = 1;
                        mVeryibList.getChildAt(num).performClick();
                        mapvery.remove(num);
                    }
                    break;
            }
        }
    }

    private int hisKey = 0;
    private void checkHisBlock(int i,String a){
        int num = (int) ValueToMap.getKey(maphisZong,a);
        if (num != -1){
            switch (i){
                case 1:
                    if (maphisZong.containsValue(a)){
                        hisKey = 1;
                        mHisIbList.getChildAt(num).performClick();
                        maphis.put(num,a);
                    }
                    break;
                case 2:
                    if (maphis.containsValue(a)){
                        hisKey = 1;
                        mHisIbList.getChildAt(num).performClick();
                        maphis.remove(num);
                    }
                    break;
            }
        }
    }

    private TextView selet;
    private ImageView seleB;
    private List<String> strings;
    private int key = 0;

    public void mySelectorBlock(String t, int i) {
        if (mMySelectorR.getVisibility() == View.GONE) {
            mMySelectorR.setVisibility(View.VISIBLE);
        }
        if (i == 1) {
            if (strings == null) {
                strings = new ArrayList<>(20);
                strings.add(0, t);
            } else {
                strings.add(0, t);
            }
            if (strings.size() > 5){
                checkHisBlock(2,strings.get(5));
                checkVeryBlock(2,strings.get(5));
                strings.remove(5);
            }
        } else {
            strings.remove(t);
            if (strings.size() == 0){
                mMySelectorR.setVisibility(View.GONE);
            }
        }

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        mMySelectorIb.setAdapter(new TagAdapter<String>(strings) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                View view = layoutInflater.inflate(R.layout.label_list2, mMySelectorIb, false);
                selet = (TextView) view.findViewById(R.id.text);
                seleB = (ImageView) view.findViewById(R.id.block_close);
                selet.setText(s);

                seleB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        key = 1;
                        checkHisBlock(2,strings.get(position));
                        checkVeryBlock(2,strings.get(position));
                        mySelectorBlock(strings.get(position), 2);
                        if (strings == null || strings.size() == 0) {
                            mMySelectorR.setVisibility(View.GONE);
                        }
                    }
                });
                return view;
            }
        });
    }

    private Handler veryBlockHadler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    veryBlockLabel();
                    if (s != null && s.size() > 0){
                        for (int a = 0; a < s.size();a++){
                            checkVeryBlock(1,s.get(a));
                            checkHisBlock(1,s.get(a));
                            mySelectorBlock(s.get(a),1);
                        }
                    }
                    break;
                case 2:
                    if (strings != null){
                      for (BlockLabel b:blockLabels1){
                        if (strings.contains(b.getIbname())){
                            blockLabels1.remove(b);
                            }
                        }
                    }

                    int num = 0;
                    for (BlockLabel b:blockLabels1){
                        if (!b.getIbname().equals("#"+name)){
                            num += 1;
                        }
                    }
                    if (blockLabels1.size() == num + 1){
                        searchBlockSele();
                    }else {
                        BlockLabel blockLabel = new BlockLabel();
                        blockLabel.setIbname("新建#"+name);
                        blockLabels1.add(0,blockLabel);
                        searchBlockSele();
                    }
                    break;
            }
        }
    };

    private String path = "http://nmy1206.natapp1.cc/LabelBlock.php";
    public List<BlockLabel> blockLabels;
    public List<BlockLabel> blockLabels1;

    private void httpSendBlock(int i, String name) {
        HttpUtil.blockLabel(InValues.send(R.string.LabelBlock), i, name, new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try {
                    String a = response.body().string();
                    if (i == 1){
                        blockLabels = new Gson().fromJson(a, new TypeToken<List<BlockLabel>>() {
                        }.getType());
                    }else {
                        blockLabels1 = new Gson().fromJson(a, new TypeToken<List<BlockLabel>>() {
                        }.getType());
                    }
                    Message message = new Message();
                    message.obj = blockLabels;
                    message.what = i;
                    veryBlockHadler.sendMessage(message);
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }
        });
    }
}
