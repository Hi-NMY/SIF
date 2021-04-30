package com.example.sif.Lei.MyToolClass;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.sif.BaseActivity;
import com.example.sif.Lei.LianJie.HttpUtil;
import com.example.sif.Lei.MyBroadcastReceiver.BroadcastRec;
import com.example.sif.Lei.NiceImageView.SolidImageView;
import com.example.sif.Lei.ShiPeiQi.GuangChangMessageImageList;
import com.example.sif.MyApplication;
import com.example.sif.NeiBuLei.DouBleImagePath;
import com.example.sif.R;
import com.tamsiree.rxui.view.dialog.RxDialog;
import com.tamsiree.rxui.view.dialog.RxDialogScaleView;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SchoolShopPopupWindow extends PopupWindow implements View.OnClickListener{

    private Context context;
    private Activity activity;
    private BaseActivity baseActivity;
    private View view;

    public SchoolShopPopupWindow(Context c, Activity a, BaseActivity b) {
        this.context = c;
        this.activity = a;
        this.baseActivity = b;
        checkBoxes = new ArrayList<>();
        labels = new ArrayList<>();
        this.view = LayoutInflater.from(c).inflate(R.layout.shoppopupview, null);
        this.setContentView(view);
        initView(view);
        this.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        ColorDrawable colorDrawable = new ColorDrawable(0xb0000000);
        this.setBackgroundDrawable(colorDrawable);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.verypopupview);

    }

    private EditText mToshopNotice;
    private RecyclerView mToshopImageList;
    private SolidImageView mToshopAddimage;
    private TagFlowLayout mToshopLabel;
    private TextView mToshopAddlabel;
    private TextView mNowshop;
    private TextView mStopshop;
    private Button mCloseButton;
    private Button mRightButton;

    private void initView(View view) {
        mToshopNotice = (EditText) view.findViewById(R.id.toshop_notice);
        mToshopImageList = (RecyclerView) view.findViewById(R.id.toshop_image_list);
        mToshopAddimage = (SolidImageView) view.findViewById(R.id.toshop_addimage);
        mToshopLabel = (TagFlowLayout) view.findViewById(R.id.toshop_label);
        mToshopAddlabel = (TextView) view.findViewById(R.id.toshop_addlabel);
        mNowshop = (TextView) view.findViewById(R.id.nowshop);
        mStopshop = (TextView) view.findViewById(R.id.stopshop);
        mCloseButton = (Button) view.findViewById(R.id.close_button);
        mRightButton = (Button) view.findViewById(R.id.right_button);
        mCloseButton.setOnClickListener(this);
        mRightButton.setOnClickListener(this);
        mToshopNotice.setOnClickListener(this);
        mToshopAddimage.setOnClickListener(this);
        mToshopAddlabel.setOnClickListener(this);
        mNowshop.setOnClickListener(this);
        mStopshop.setOnClickListener(this);
    }


    private int shopstate = -1;
    private String imageName = "";
    private ShowDiaLog showDiaLog1;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close_button:
                dissMissPopupwindow();
                break;
            case R.id.right_button:
                baseActivity.showDiaLog(activity, R.drawable.loading2);
                if (mToshopNotice.getText().toString().equals("")) {
                    ToastZong.ShowToast(MyApplication.getContext(), "描述不能为空哦");
                    baseActivity.closeDiaLog();
                } else if (ds == null || ds.size() <= 0) {
                    ToastZong.ShowToast(MyApplication.getContext(), "未添加图片");
                    baseActivity.closeDiaLog();
                } else if (stringLabels == null) {
                    ToastZong.ShowToast(MyApplication.getContext(), "请选择标签");
                    baseActivity.closeDiaLog();
                } else if (shopstate == -1) {
                    ToastZong.ShowToast(MyApplication.getContext(), "请选择状态");
                    baseActivity.closeDiaLog();
                } else {
                    sendNewShop();
                }
                break;
            case R.id.toshop_notice:

                break;
            case R.id.toshop_addimage:
                imageName = System.currentTimeMillis() +
                        (int) (Math.random() * 1000) + ".png";
                SelectImage selectImage = new SelectImage(activity);
                selectImage.showSelectImage(1, imageName);
                break;
            case R.id.toshop_addlabel:
                View view1 = LayoutInflater.from(activity).inflate(R.layout.shop_label_select, null);
                initShopLabelView(view1);
                showDiaLog1 = new ShowDiaLog(activity, R.style.AlertDialog_qr, view1);
                showDiaLog1.logWindow(new ColorDrawable(Color.TRANSPARENT));
                showDiaLog1.showMyDiaLog();
                break;
            case R.id.nowshop:
                shopstate = 0;
                mNowshop.setTextColor(activity.getColor(R.color.ziti));
                mStopshop.setTextColor(activity.getColor(R.color.lightgray));
                break;
            case R.id.stopshop:
                shopstate = 2;
                mNowshop.setTextColor(activity.getColor(R.color.lightgray));
                mStopshop.setTextColor(activity.getColor(R.color.ziti));
                break;
        }
    }

    private CheckBox mLift;
    private CheckBox mStudy;
    private CheckBox mGame;
    private CheckBox mElectric;
    private CheckBox mElectronics;
    private CheckBox mChair;
    private CheckBox mLamp;
    private CheckBox mBook;
    private CheckBox mShoes;
    private CheckBox mBag;
    private CheckBox mClothes;
    private CheckBox mCosmetics;
    private CheckBox mPhone;
    private CheckBox mWatch;
    private CheckBox mHair;
    private CheckBox mOther;
    private Button mLabelButton;
    private List<CheckBox> checkBoxes;
    private List<String> labels;
    private StringBuffer stringLabels;
    private void initShopLabelView(View view1) {
        checkBoxes = new ArrayList<>();
        mLift = (CheckBox)view1.findViewById(R.id.lift);
        checkBoxes.add(mLift);
        mStudy = (CheckBox)view1.findViewById(R.id.study);
        checkBoxes.add(mStudy);
        mGame = (CheckBox)view1.findViewById(R.id.game);
        checkBoxes.add(mGame);
        mElectric = (CheckBox)view1.findViewById(R.id.electric);
        checkBoxes.add(mElectric);
        mElectronics = (CheckBox)view1.findViewById(R.id.electronics);
        checkBoxes.add(mElectronics);
        mChair = (CheckBox)view1.findViewById(R.id.chair);
        checkBoxes.add(mChair);
        mLamp = (CheckBox)view1.findViewById(R.id.lamp);
        checkBoxes.add(mLamp);
        mBook = (CheckBox)view1.findViewById(R.id.book);
        checkBoxes.add(mBook);
        mShoes = (CheckBox)view1.findViewById(R.id.shoes);
        checkBoxes.add(mShoes);
        mBag = (CheckBox)view1.findViewById(R.id.bag);
        checkBoxes.add(mBag);
        mClothes = (CheckBox)view1.findViewById(R.id.clothes);
        checkBoxes.add(mClothes);
        mCosmetics = (CheckBox)view1.findViewById(R.id.cosmetics);
        checkBoxes.add(mCosmetics);
        mPhone = (CheckBox)view1.findViewById(R.id.phone);
        checkBoxes.add(mPhone);
        mWatch = (CheckBox)view1.findViewById(R.id.watch);
        checkBoxes.add(mWatch);
        mHair = (CheckBox)view1.findViewById(R.id.hair);
        checkBoxes.add(mHair);
        mOther = (CheckBox)view1.findViewById(R.id.other);
        checkBoxes.add(mOther);
        mLabelButton = (Button)view1.findViewById(R.id.label_button);

        setOldLabel();

        mLabelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                labels.clear();
                stringLabels = new StringBuffer();
                for (CheckBox c:checkBoxes){
                    if (c.isChecked()){
                        labels.add(c.getText().toString());
                    }
                }
                if (labels.size() != 0) {
                    for (String f : labels) {
                        stringLabels.append(f + ",");
                    }
                } else {
                    stringLabels.append("");
                }
                showDiaLog1.closeMyDiaLog();
                numLabel = 0;
                setLabelList();
            }
        });
    }

    private int numLabel = 0;
    private void setOldLabel() {
        numLabel = labels.size();
        for (CheckBox c:checkBoxes){
            c.setOnCheckedChangeListener(null);
            if (labels.contains(c.getText().toString())){
                c.setChecked(true);
            }
            c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        if (numLabel >= 3){
                            c.setChecked(false);
                            ToastZong.ShowToast(MyApplication.getContext(),"只能选择三个标签");
                        }else {
                            numLabel++;
                        }
                    }else {
                        numLabel--;
                    }
                }
            });
        }
    }


    private TextView myShopLabel;
    private void setLabelList() {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        mToshopLabel.setAdapter(new TagAdapter<String>(labels) {
            @Override
            public View getView(FlowLayout parent, int position, String o) {
                View view = layoutInflater.inflate(R.layout.shop_label_item, mToshopLabel, false);
                myShopLabel = (TextView) view.findViewById(R.id.text);
                myShopLabel.setText(o);
                return view;
            }
        });
    }

    private void dissMissPopupwindow() {
        this.dismiss();
    }

    private String path = "";
    private Handler newShopHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            baseActivity.closeDiaLog();
            dissMissPopupwindow();
            BroadcastRec.sendReceiver(context,"newSendShop",0,newShop);
        }
    };

    private String newShop;
    private void sendNewShop() {
        HttpUtil.sendAddNewShop(InValues.send(R.string.NewSchoolShop), ds, baseActivity.getMyXueHao(), MyDateClass.showNowDate(), stringLabels, mToshopNotice.getText().toString(), shopstate, new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                newShop = response.body().string();
                newShopHandler.sendMessage(new Message());
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }
        });
    }

    public List<DouBleImagePath> ds;
    private GuangChangMessageImageList guangChangMessageImageList;
    private LinearLayoutManager linearLayoutManager;
    private DouBleImagePath douBleImagePath;
    public String imagepath;

    public void sendSmallImage(String path, String imageApath) {
        if (ds == null) {
            ds = new ArrayList<>();
            linearLayoutManager = new LinearLayoutManager(activity);
            linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            mToshopImageList.setLayoutManager(linearLayoutManager);
        }
        if (path != null) {
            imagepath = path;
            if (ds.size() < 4) {
                mToshopImageList.setVisibility(View.VISIBLE);
                douBleImagePath = new DouBleImagePath();
                douBleImagePath.setMinPath(imagepath);
                douBleImagePath.setMaxPath(imageApath);
                ds.add(0, douBleImagePath);
                guangChangMessageImageList = new GuangChangMessageImageList(activity, this, ds);
                mToshopImageList.setAdapter(guangChangMessageImageList);
                if (ds.size() == 3) {
                    mToshopAddimage.setVisibility(View.GONE);
                }
            }
        } else {
            ToastZong.ShowToast(MyApplication.getContext(), "图片加载失败,请重新选择");
        }
    }

    public void removeList(int position) {
        ds.remove(position);
        if (ds == null || ds.size() < 3) {
            mToshopAddimage.setVisibility(View.VISIBLE);
            guangChangMessageImageList.removeImage(position);
        }
    }

    private RxDialog rxDialog;
    private RxDialogScaleView rxDialogScaleView;
    private Handler bitMapHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            rxDialog.dismiss();
            if (msg.obj != null) {
                if (msg.what == 1) {
                    ToastZong.ShowToast(MyApplication.getContext(), "图片加载错误");
                    rxDialogScaleView.setImage((Bitmap) msg.obj);
                } else {
                    rxDialogScaleView.setImage((Bitmap) msg.obj);
                }
            } else {
                ToastZong.ShowToast(MyApplication.getContext(), "错误");
            }
        }
    };

    public void lookPicture(int position) {
        rxDialogScaleView = new RxDialogScaleView(activity);
        rxDialog = new RxDialog(activity, R.style.tran_dialog);
        rxDialog.setCanceledOnTouchOutside(false);
        MyVeryDiaLog.veryImageDiaLog(rxDialogScaleView, ds.get(position).getMaxPath(), bitMapHandler);
        MyVeryDiaLog.transparentDiaLog(activity, rxDialog);
    }

}
