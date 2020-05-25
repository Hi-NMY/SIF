package com.example.sif.Lei.ShiPeiQi;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.signature.MediaStoreSignature;
import com.example.sif.Lei.MyToolClass.GuangChangImageToClass;
import com.example.sif.Lei.NiceImageView.CircleImageView;
import com.example.sif.NeiBuLei.SchoolShopClass;
import com.example.sif.R;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

public class SchoolShopAdapter extends RecyclerView.Adapter<SchoolShopAdapter.ViewHolder> {

    private List<SchoolShopClass> schoolShopClasses;
    private Activity activity;
    private View view;
    private ViewHolder viewHolder;


    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView mShopUserimage;
        TextView mShopUsername;
        View mShopState;
        TextView mShopNotice;
        RecyclerView mShopImagelist;
        TagFlowLayout mShopLabellist;
        TextView mShopTime;
        Button mShopTomessage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mShopUserimage = (CircleImageView)itemView.findViewById(R.id.shop_userimage);
            mShopUsername = (TextView)itemView.findViewById(R.id.shop_username);
            mShopState = (View)itemView.findViewById(R.id.shop_state);
            mShopNotice = (TextView)itemView.findViewById(R.id.shop_notice);
            mShopImagelist = (RecyclerView)itemView.findViewById(R.id.shop_imagelist);
            mShopLabellist = (TagFlowLayout)itemView.findViewById(R.id.shop_labellist);
            mShopTime = (TextView)itemView.findViewById(R.id.shop_time);
            mShopTomessage = (Button)itemView.findViewById(R.id.shop_tomessage);
        }
    }

    public SchoolShopAdapter(Activity a,List<SchoolShopClass> s){
        this.activity = a;
        this.schoolShopClasses = s;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schoolshop_listitem, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    private SchoolShopClass schoolShopClass;
    private String updateTime;
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        schoolShopClass = schoolShopClasses.get(position);
       // holder.mShopUsername.setText();   获取 名字
        holder.mShopNotice.setText(schoolShopClass.getNotice());
        holder.mShopTime.setText(schoolShopClass.getSendtime());
        switch (schoolShopClass.getShopstate()) {
            case 0:
                holder.mShopState.setBackgroundResource(R.drawable.nowshop);
                break;
            case 1:
                holder.mShopState.setBackgroundResource(R.drawable.outshop);
                break;
            case 2:
                holder.mShopState.setBackgroundResource(R.drawable.stopshop);
                break;
        }

        if (!schoolShopClasses.get(position).getCommodityimage().equals("")) {
            holder.mShopImagelist.setVisibility(View.VISIBLE);
            holder.mShopImagelist.setLayoutManager(GuangChangImageToClass.shopNewView(activity,schoolShopClass.getCommodityimage(),schoolShopClass.getXuehao()));
            GuangChangImageAdapter guangChangImageAdapter = new GuangChangImageAdapter(activity, GuangChangImageToClass.shopimageToClass(schoolShopClass.getCommodityimage(),schoolShopClass.getXuehao()));
            holder.mShopImagelist.setAdapter(guangChangImageAdapter);
        }

        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        if (schoolShopClasses.get(position).getLabel() != null && !schoolShopClasses.get(position).getLabel().equals("")) {
            List<String> strings = new ArrayList<>();
            String[] s = schoolShopClasses.get(position).getLabel().split(",");
            for (int a = 0; a < s.length; a++) {
                strings.add(s[a]);
            }
            holder.mShopLabellist.setAdapter(new TagAdapter<String>(strings) {
                @Override
                public View getView(FlowLayout parent, int position, String o) {
                    View view = layoutInflater.inflate(R.layout.label_list1, holder.mShopLabellist, false);
                    TextView selet = (TextView) view.findViewById(R.id.text);
                    selet.setText(o);
                    return view;
                }
            });
//            holder.mShopLabellist.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
//                @Override
//                public boolean onTagClick(View view, int position, FlowLayout parent) {
//                    TextView t = (TextView) view.findViewById(R.id.text);
//                    Intent intent = new Intent(activity, IbDetailed.class);
//                    intent.putExtra("ibname", t.getText().toString());
//                    activity.startActivity(intent);
//                    return true;
//                }
//            });
        }

        updateTime = String.valueOf(System.currentTimeMillis());
        if (!schoolShopClasses.get(position).getXuehao().equals(holder.mShopUserimage.getTag())) {
            holder.mShopUserimage.setTag(null);
            Glide.with(activity)
                    .load("http://nmy1206.natapp1.cc/UserImageServer/" + schoolShopClass.getXuehao() + "/HeadImage/myHeadImage.png")
                    .signature(new MediaStoreSignature(updateTime, 1, 1))
                    .placeholder(R.drawable.nostartimage_three)
                    .fallback(R.drawable.defaultheadimage)
                    .error(R.drawable.defaultheadimage)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .circleCrop()
                    .into(holder.mShopUserimage);
            holder.mShopUserimage.setTag(schoolShopClasses.get(position).getXuehao());
        }

        holder.mShopTomessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return schoolShopClasses.size();
    }
}
