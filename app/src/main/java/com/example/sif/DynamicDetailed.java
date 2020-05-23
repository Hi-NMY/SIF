package com.example.sif;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.sif.Lei.MyToolClass.GlideRoundTransform;
import com.example.sif.Lei.MyToolClass.MyDateClass;
import com.example.sif.Lei.MyToolClass.MyVeryDiaLog;
import com.example.sif.Lei.MyToolClass.ObtainUser;
import com.example.sif.Lei.MyToolClass.SendGeTuiMessage;
import com.example.sif.Lei.MyToolClass.ToastZong;
import com.example.sif.Lei.MyToolClass.UserDynamicCollection;
import com.example.sif.Lei.MyToolClass.UserDynamicComment;
import com.example.sif.Lei.MyToolClass.UserDynamicThumb;
import com.example.sif.Lei.NiceImageView.CircleImageView;
import com.example.sif.Lei.ShiPeiQi.CommentList;
import com.example.sif.Lei.ShowActivityBar.FragmentActivityBar;
import com.example.sif.NeiBuLei.CommentMessage;
import com.example.sif.NeiBuLei.UserSpace;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.tamsiree.rxui.view.dialog.RxDialog;
import com.tamsiree.rxui.view.dialog.RxDialogScaleView;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

public class DynamicDetailed extends BaseActivity implements View.OnClickListener, View.OnLayoutChangeListener {
    private CircleImageView mDynamicDetailedHeadimage;
    private TextView mDynamicDetailedUname;
    private TextView mDynamicDetailedTime;
    private TextView mDynamicDetailedMessage;
    private ImageView mDynamicDetailedImage;
    private TextView mDynamicDetailedShareText;
    private ImageButton mDynamicDetailedShare;
    private TextView mDynamicDetailedCommentText;
    private ImageButton mDynamicDetailedComment;
    private TextView mDynamicDetailedThumbText;
    private ImageButton mDynamicDetailedThumb;
    private ImageButton mDynamicDetailedCollection;
    private RecyclerView mDynamicDetailedRecyclerview;
    private TextView mTextTips;
    private View fragment;

    private View windowView;
    private int windowHeight;
    private int keyHeight;
    private UserSpace userSpace;
    private String uname;
    private String uxuehao;
    private UserDynamicThumb userDynamicThumb;
    private UserDynamicComment userDynamicComment;
    private EditText mCommentUsersendText;
    private Button mCommentUsersend;
    private RelativeLayout mDynamicR;
    private FragmentActivityBar fragmentActivityBar;
    private NestedScrollView mDynamicNestedScrollView;

    private int a;
    private View mDynamicYinying;
    private TextView mCommentSmartText;
    private RelativeLayout mCommentSmartR;
    private SmartRefreshLayout mCommentSmart;

    private IntentFilter intentFilter;
    private deleteUserComment deleteUserComment;
    private LocalBroadcastManager localBroadcastManager;
    private TagFlowLayout mDynamicDetailedIb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_detailed);
        initView();

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        intentFilter = new IntentFilter();
        intentFilter.addAction("deleteComment");
        deleteUserComment = new deleteUserComment();
        localBroadcastManager.registerReceiver(deleteUserComment, intentFilter);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //键盘
        windowView = findViewById(R.id.dynamic_R);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        windowHeight = metrics.heightPixels;
        keyHeight = windowHeight / 3;

        userDynamicComment = sendUserComment();
        userDynamicThumb = sendUserThumb(0);
        //内容接收
        Intent intent = getIntent();
        a = intent.getIntExtra("id", 1);
        userSpace = (UserSpace) intent.getSerializableExtra("dynamic");
        uname = intent.getStringExtra("uname");
        uxuehao = intent.getStringExtra("uxuehao");
        if (userSpace != null) {
            xieru();
            huoquComment();
        }
       // ZTL();
        //增加padding  避免状态栏遮挡
//        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.dynamic_R);
//        setPadding(this, relativeLayout);

        showActivityBar();

        //输入监控
        mCommentUsersendText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mCommentUsersendText.getText().length() > 0) {
                    mCommentUsersend.setVisibility(View.VISIBLE);
                } else {
                    mCommentUsersend.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //刷新监听
        mCommentSmart.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                userDynamicComment.detailedComment(oldCommentHander, 1, commentList.date);
            }
        });
    }

    private CommentList commentList;
    private LinearLayoutManager linearLayoutManager;
    private Handler commentHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (msg.obj.equals("0")) {
                        ToastZong.ShowToast(DynamicDetailed.this, "评论成功");
                        if (commentList == null) {
                            mTextTips.setVisibility(View.VISIBLE);
                            mCommentSmart.setVisibility(View.VISIBLE);
                            runDynamicComment(c);
                        } else {
                            commentList.addComment(c);
                        }
                        commentList.notifyItemInserted(0);
                        commentList.notifyItemRangeChanged(0, commentList.commentMessages.size() + 1);
                        mDynamicNestedScrollView.smoothScrollTo(0, 0);
                        mDynamicDetailedRecyclerview.smoothScrollToPosition(0);
                        mDynamicDetailedRecyclerview.setAdapter(commentList);
                        if (mDynamicDetailedCommentText.getText().toString().equals("评论")) {
                            mDynamicDetailedCommentText.setText("1");
                        } else {
                            mDynamicDetailedCommentText.setText(String.valueOf(Integer.valueOf(mDynamicDetailedCommentText.getText().toString()) + 1));
                        }
                        mCommentUsersendText.setText("");
                        hideKeyboard(mDynamicR, 1);
                        if (!uxuehao.equals(getMyXueHao())) {
                            SendGeTuiMessage.sendGeTuiMessage(1, uxuehao,getMyXueHao(),"1",userSpace.getUser_dynamic_id(),1);
                        }
                    }
                    break;
                case 1:
                    if (msg.obj.equals("1")) {
                        ToastZong.ShowToast(DynamicDetailed.this, "出错啦");
                    } else {
                        runDynamicComment(userDynamicComment.commentMessages);
                    }
                    break;
            }
        }
    };

    private boolean collection;
    private Handler collectionHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    mDynamicDetailedCollection.setImageResource(R.drawable.yishoucang);
                    mDynamicDetailedCollection.postInvalidate();
                    collection = true;
                    break;
                case 1:
                    mDynamicDetailedCollection.setImageResource(R.drawable.weishoucang);
                    mDynamicDetailedCollection.postInvalidate();
                    collection = false;
                    break;

            }
        }
    };

    private Handler myColetionHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    mDynamicDetailedCollection.setImageResource(R.drawable.weishoucang);
                    collection = false;
                    break;
                case 1:
                    ToastZong.ShowToast(MyApplication.getContext(), "错误,请重试");
                    break;
                case 10:
                    mDynamicDetailedCollection.setImageResource(R.drawable.yishoucang);
                    collection = true;
                    ToastZong.ShowToast(MyApplication.getContext(), "收藏成功");
                    break;
                default:
                    ToastZong.ShowToast(MyApplication.getContext(), "错误,请重试");
                    break;
            }
        }
    };


    private void runDynamicComment(List<CommentMessage> c) {
        linearLayoutManager = new LinearLayoutManager(this);
        mDynamicDetailedRecyclerview.setLayoutManager(linearLayoutManager);
        if (commentList == null) {
            commentList = new CommentList(this, c);
            commentList.sendXueHao(uxuehao, getMyXueHao());
        }
        commentList.notifyDataSetChanged();
        mDynamicDetailedRecyclerview.setAdapter(commentList);
    }

    private void huoquComment() {
        if (userSpace.getUser_comment() != 0) {
            String nowTime = MyDateClass.showNowDate();
            mTextTips.setVisibility(View.VISIBLE);
            mCommentSmart.setVisibility(View.VISIBLE);
            if (userSpace.getUser_comment() >= 8) {
                mCommentSmart.setEnableLoadMore(true);
            }
            userDynamicComment.dynamicXinxi(userSpace.getUser_dynamic_id(), userSpace.getUser_comment());
            userDynamicComment.detailedComment(commentHandler, 0, nowTime);
        }
        UserDynamicCollection.noYesCollection(0, userSpace.getUser_dynamic_id(), getMyXueHao(), collectionHander);
    }

    private void hideKeyboard(View v, int i) {
        InputMethodManager inputMethodManager = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (i == 1) {
            inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        } else {
            inputMethodManager.showSoftInput(v, 0);
        }
    }

    private void showActivityBar() {
        fragmentActivityBar = (FragmentActivityBar) getSupportFragmentManager().findFragmentById(R.id.dynamic_detailed_bar);
        fragment.bringToFront();
        ShowActivityBars showActivityBars = new ShowActivityBars(this, fragmentActivityBar);
        showActivityBars.showKongJian(true, false, true, false, true, false, false);
        showActivityBars.showUI(R.drawable.zuo_black, (String) null, "动态", null, null, 0, 0);
        showActivityBars.uiFunction(1, 0, 0, 0, 0, 0, 0);
        showActivityBars.barBackground1(R.color.beijing);

        Rect rect = new Rect();
        mDynamicNestedScrollView.getHitRect(rect);
        mDynamicNestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (!mDynamicDetailedHeadimage.getLocalVisibleRect(rect)) {
                    showActivityBars.showUI(R.drawable.zuo_black, (String) null, uname, null, null, 0, 0);
                } else {
                    showActivityBars.showUI(R.drawable.zuo_black, (String) null, "动态", null, null, 0, 0);
                }
            }
        });
    }

    private String uheadImage;

    private void xieru() {
        boolean uT = userDynamicThumb.noYesThumb(userSpace.getUser_dynamic_id());
        if (uT) {
            mDynamicDetailedThumb.setImageResource(R.drawable.yidianzan);
        } else {
            mDynamicDetailedThumb.setImageResource(R.drawable.weidianzan);
        }
        uheadImage = "http://nmy1206.natapp1.cc/UserImageServer/" + uxuehao + "/HeadImage/myHeadImage.png";
        Glide.with(DynamicDetailed.this)
                .load("http://nmy1206.natapp1.cc/UserImageServer/" + uxuehao + "/HeadImage/myHeadImage.png")
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(R.drawable.nostartimage_three)
                .fallback(R.drawable.defaultheadimage)
                .error(R.drawable.defaultheadimage)
                .circleCrop()
                .into(mDynamicDetailedHeadimage);
        mDynamicDetailedTime.setText(MyDateClass.showDateClass(userSpace.getUser_shijian()));
        mDynamicDetailedMessage.setText(userSpace.getUser_xinxi());
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        if (userSpace.getBlock() != null && !userSpace.getBlock().equals("")){
            List<String> strings = new ArrayList<>();
            String[] s = userSpace.getBlock().split(",");
            for (int a = 0;a < s.length;a++){
                strings.add(s[a]);
            }
            mDynamicDetailedIb.setAdapter(new TagAdapter<String>(strings) {
                @Override
                public View getView(FlowLayout parent, int position, String o) {
                    View view = layoutInflater.inflate(R.layout.label_list1, mDynamicDetailedIb, false);
                    TextView selet = (TextView) view.findViewById(R.id.text);
                    selet.setText(o);
                    return view;
                }
            });
            mDynamicDetailedIb.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                @Override
                public boolean onTagClick(View view, int position, FlowLayout parent) {
                    TextView t = (TextView)view.findViewById(R.id.text);
                    Intent intent = new Intent(MyApplication.getContext(), IbDetailed.class);
                    intent.putExtra("ibname",t.getText().toString());
                    startActivity(intent);
                    return true;
                }
            });
        }
        if (userSpace.getUser_thumb() == 0) {
            mDynamicDetailedThumbText.setText("点赞");
        } else {
            mDynamicDetailedThumbText.setText(MyDateClass.sendMath(userSpace.getUser_thumb()));
        }
        if (userSpace.getUser_comment() == 0) {
            mDynamicDetailedCommentText.setText("评论");
            mTextTips.setVisibility(View.INVISIBLE);
        } else {
            mDynamicDetailedCommentText.setText(MyDateClass.sendMath(userSpace.getUser_comment()));
            mTextTips.setVisibility(View.VISIBLE);
        }

        if (!userSpace.getUser_image_url().equals("")) {
            mDynamicDetailedImage.setVisibility(View.VISIBLE);
            Glide.with(DynamicDetailed.this)
                    .load("http://nmy1206.natapp1.cc/" + userSpace.getUser_image_url())
                    .placeholder(R.drawable.nostartimage_two)
                    .fallback(R.drawable.nostartimage_two)
                    .error(R.drawable.nostartimage_two)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .override(400, 400)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .transform(new GlideRoundTransform(6))
                    .into(mDynamicDetailedImage);
        }


        if (a == 2) {
            mCommentUsersendText.setFocusable(true);
            mCommentUsersendText.setFocusableInTouchMode(true);
            mCommentUsersendText.requestFocus();
            hideKeyboard(mCommentUsersendText, 0);
        }

        mDynamicYinying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(mCommentUsersendText, 1);
                mDynamicYinying.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        windowView.addOnLayoutChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(deleteUserComment);
    }

    private void initView() {
        mDynamicDetailedHeadimage = (CircleImageView) findViewById(R.id.dynamic_detailed_headimage);
        mDynamicDetailedUname = (TextView) findViewById(R.id.dynamic_detailed_uname);
        mDynamicDetailedTime = (TextView) findViewById(R.id.dynamic_detailed_time);
        mDynamicDetailedMessage = (TextView) findViewById(R.id.dynamic_detailed_message);
        mDynamicDetailedImage = (ImageView) findViewById(R.id.dynamic_detailed_image);
        mDynamicDetailedShareText = (TextView) findViewById(R.id.dynamic_detailed_share_text);
        mDynamicDetailedShare = (ImageButton) findViewById(R.id.dynamic_detailed_share);
        mDynamicDetailedCommentText = (TextView) findViewById(R.id.dynamic_detailed_comment_text);
        mDynamicDetailedComment = (ImageButton) findViewById(R.id.dynamic_detailed_comment);
        mDynamicDetailedThumbText = (TextView) findViewById(R.id.dynamic_detailed_thumb_text);
        mDynamicDetailedThumb = (ImageButton) findViewById(R.id.dynamic_detailed_thumb);
        mDynamicDetailedCollection = (ImageButton) findViewById(R.id.dynamic_detailed_collection);
        mDynamicDetailedRecyclerview = (RecyclerView) findViewById(R.id.dynamic_detailed_recyclerview);
        mTextTips = (TextView) findViewById(R.id.text_tips);
        fragment = findViewById(R.id.dynamic_detailed_bar);

        mDynamicDetailedHeadimage.setOnClickListener(this);
        mDynamicDetailedImage.setOnClickListener(this);
        mDynamicDetailedCollection.setOnClickListener(this);
        mDynamicDetailedThumb.setOnClickListener(this);
        mDynamicDetailedComment.setOnClickListener(this);
        mDynamicDetailedShare.setOnClickListener(this);


        mCommentUsersendText = (EditText) findViewById(R.id.comment_usersend_text);
        mCommentUsersendText.setOnClickListener(this);
        mCommentUsersend = (Button) findViewById(R.id.comment_usersend);
        mCommentUsersend.setOnClickListener(this);
        mDynamicR = (RelativeLayout) findViewById(R.id.dynamic_R);
        mDynamicR.setOnClickListener(this);
        mDynamicNestedScrollView = (NestedScrollView) findViewById(R.id.dynamic_NestedScrollView);
        mDynamicNestedScrollView.setOnClickListener(this);
        mDynamicYinying = (View) findViewById(R.id.dynamic_yinying);
        mDynamicYinying.setOnClickListener(this);
        mCommentSmartText = (TextView) findViewById(R.id.comment_smart_text);
        mCommentSmartText.setOnClickListener(this);
        mCommentSmartR = (RelativeLayout) findViewById(R.id.comment_smart_R);
        mCommentSmartR.setOnClickListener(this);
        mCommentSmart = (SmartRefreshLayout) findViewById(R.id.comment_smart);

        mDynamicDetailedIb = (TagFlowLayout) findViewById(R.id.dynamic_detailed_ib);

    }

    private Handler thumbHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (msg.obj.equals("0")) {
                        if (mDynamicDetailedThumbText.getText().toString().equals("点赞")) {
                            mDynamicDetailedThumbText.setText(String.valueOf(1));
                        } else {
                            mDynamicDetailedThumbText.setText(String.valueOf(Integer.parseInt(mDynamicDetailedThumbText.getText().toString()) + 1));
                        }
                        mDynamicDetailedThumb.setImageResource(R.drawable.yidianzan);
                        if (!uxuehao.equals(getMyXueHao())){
                            SendGeTuiMessage.sendGeTuiMessage(1, uxuehao,getMyXueHao(),"1", userSpace.getUser_dynamic_id(),0);
                        }
                    }
                    if (msg.obj.equals("10")) {
                        if (mDynamicDetailedThumbText.getText().toString().equals("评论")) {
                            mDynamicDetailedThumbText.setText(String.valueOf(1));
                        } else {
                            if (Integer.parseInt(mDynamicDetailedThumbText.getText().toString()) != 0 && !mDynamicDetailedThumbText.getText().toString().equals("点赞")) {
                                mDynamicDetailedThumbText.setText(String.valueOf(Integer.parseInt(mDynamicDetailedThumbText.getText().toString()) - 1));
                            }
                        }
                        mDynamicDetailedThumb.setImageResource(R.drawable.weidianzan);
                    }
                    break;
            }
        }
    };

    private List<CommentMessage> c = new ArrayList<CommentMessage>();
    private Handler handlerspace = new Handler() {
        @Override
        public void handleMessage(Message msg) {
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

    private Handler oldCommentHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (!msg.obj.equals("1")) {
                        commentList.addOldComment(userDynamicComment.commentMessages);
                        mCommentSmart.finishLoadMore();
                    }
                    break;
            }
        }
    };

    private Handler deleteCommentHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (msg.obj.equals("0")) {
                        mDynamicDetailedCommentText.setText(String.valueOf(Integer.valueOf(mDynamicDetailedCommentText.getText().toString()) - 1));
                    } else {
                        ToastZong.ShowToast(DynamicDetailed.this, "出错了,请重试");
                    }
                    break;
            }
        }
    };

    private RxDialogScaleView rxDialogScaleView;
    private RxDialog rxDialog;
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dynamic_detailed_headimage:
                if (uxuehao != null) {
                    ObtainUser.obtainUser(this, uxuehao, handlerspace);
                }
                break;
            case R.id.dynamic_detailed_image:
                rxDialogScaleView = new RxDialogScaleView(this);
                rxDialog = new RxDialog(this, R.style.tran_dialog);
                rxDialog.setCanceledOnTouchOutside(false);
                String name = String.valueOf(userSpace.getUser_image_url()).substring(39);
                String NewName = "http://nmy1206.natapp1.cc/UserImageServer/" + uxuehao + "/ADynamicImage/" + name;
                String path1 = "http://nmy1206.natapp1.cc/" + userSpace.getUser_image_url();
                MyVeryDiaLog.veryImageDiaLog(rxDialogScaleView, NewName, path1, bitMapHandler);
                MyVeryDiaLog.transparentDiaLog(this, rxDialog);
                break;
            case R.id.dynamic_detailed_collection:
                if (userSpace != null && userSpace.getUser_dynamic_id() != null) {
                    if (collection) {
                        UserDynamicCollection.detailedCollection(2, userSpace.getUser_dynamic_id(), getMyXueHao(), myColetionHander);
                    } else {
                        UserDynamicCollection.detailedCollection(1, userSpace.getUser_dynamic_id(), uheadImage, uxuehao, uname, userSpace.getUser_xinxi(), userSpace.getUser_image_url(), getMyXueHao(), myColetionHander);
                    }
                }
                break;
            case R.id.dynamic_detailed_thumb:
                if (userSpace != null && userSpace.getUser_dynamic_id() != null) {
                    userDynamicThumb.userThumb(userSpace.getUser_dynamic_id(), getMyXueHao(), uxuehao, thumbHandler);
                }
                break;
            case R.id.dynamic_detailed_comment:
                mCommentUsersendText.setFocusable(true);
                mCommentUsersendText.setFocusableInTouchMode(true);
                mCommentUsersendText.requestFocus();
                hideKeyboard(mCommentUsersendText, 0);
                break;
            case R.id.dynamic_detailed_share:

                break;
            case R.id.comment_usersend:
                if (TextUtils.isEmpty(mCommentUsersendText.getText().toString())) {
                    ToastZong.ShowToast(this, "评论内容不能为空");
                } else {
                    if (userSpace != null && userSpace.getUser_dynamic_id() != null) {
                        c = new ArrayList<>();
                        String nowTime = MyDateClass.showNowDate();
                        CommentMessage message = new CommentMessage();
                        message.setId(0);
                        message.setComment_time(nowTime);
                        message.setComment_username(user_name);
                        message.setComment_xuehao(getMyXueHao());
                        message.setDynamic_comment(mCommentUsersendText.getText().toString());
                        message.setDynamic_id(userSpace.getUser_dynamic_id());
                        message.setTo_username(null);
                        message.setUser_headimage_url(user_head_portrait);
                        message.setUser_ip(null);
                        c.add(message);
                        userDynamicComment.sendComment(0, userSpace.getUser_dynamic_id(), mCommentUsersendText.getText().toString(), nowTime, user_name, null, user_head_portrait,
                                getMyXueHao(), uxuehao, 0, commentHandler);
                    }
                }
                break;
        }
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
            mDynamicYinying.setVisibility(View.VISIBLE);
        } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
            mDynamicYinying.setVisibility(View.INVISIBLE);
        }
    }

    private void closeFresh() {
        if (commentList.commentMessages.size() <= 8) {
            mCommentSmartR.setVisibility(View.INVISIBLE);
        }
    }

    class deleteUserComment extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            commentList.deleteComment();
            closeFresh();
            if (commentList.deleteId == 0) {
                userDynamicComment.sendComment(2, userSpace.getUser_dynamic_id(), "", commentList.commentTime, "", "",
                        "", commentList.commentXueHao, uxuehao, commentList.deleteId, deleteCommentHander);
            } else {
                userDynamicComment.sendComment(1, userSpace.getUser_dynamic_id(), "", "", "", "",
                        "", "", uxuehao, commentList.deleteId, deleteCommentHander);
            }
        }
    }
}
