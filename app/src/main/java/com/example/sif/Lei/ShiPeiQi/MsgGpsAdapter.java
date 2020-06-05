package com.example.sif.Lei.ShiPeiQi;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.sif.Lei.MyBroadcastReceiver.BroadcastRec;
import com.example.sif.NeiBuLei.MsgGpsClass;
import com.example.sif.R;

import java.util.List;

public class MsgGpsAdapter extends RecyclerView.Adapter<MsgGpsAdapter.ViewHolder> {

    public List<MsgGpsClass> msgGpsClasses;
    private Activity activity;
    private View view;
    private ViewHolder viewHolder;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mPlaceName;
        TextView mPlaceDetailed;
        RelativeLayout mMsggpsR;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mPlaceName = (TextView)itemView.findViewById(R.id.place_name);
            mPlaceDetailed = (TextView)itemView.findViewById(R.id.place_detailed);
            mMsggpsR = (RelativeLayout)itemView.findViewById(R.id.msggps_r);
        }
    }

    public MsgGpsAdapter(Activity a,List<MsgGpsClass> m){
        this.activity = a;
        this.msgGpsClasses = m;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gcmsg_lgps_listitem, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    private MsgGpsClass msgGpsClass;
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        msgGpsClass = msgGpsClasses.get(position);
        holder.mPlaceName.setText(msgGpsClass.getPlaceName());
        if (!msgGpsClass.getPlaceDetailed().equals("")){
            holder.mPlaceDetailed.setVisibility(View.VISIBLE);
            holder.mPlaceDetailed.setText(msgGpsClass.getPlaceDetailed());
        }else {
            holder.mPlaceDetailed.setVisibility(View.GONE);
        }

        holder.mMsggpsR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BroadcastRec.sendReceiver(activity,"selectPlace",0,msgGpsClasses.get(position).getPlaceName());
            }
        });

    }

    @Override
    public int getItemCount() {
        return msgGpsClasses.size();
    }
}
