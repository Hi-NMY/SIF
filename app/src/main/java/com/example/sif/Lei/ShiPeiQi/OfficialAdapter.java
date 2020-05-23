package com.example.sif.Lei.ShiPeiQi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sif.NeiBuLei.OfficialNoticeList;
import com.example.sif.R;

import java.util.List;

public class OfficialAdapter extends RecyclerView.Adapter<OfficialAdapter.ViewHolder> {

    private List<OfficialNoticeList> officialNoticeLists;
    private OfficialNoticeList officialNoticeList;


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mOfficialTime;
        TextView mOfficialNotice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mOfficialNotice = (TextView)itemView.findViewById(R.id.official_notice);
            mOfficialTime = (TextView)itemView.findViewById(R.id.official_time);
        }
    }

    public OfficialAdapter(List<OfficialNoticeList> o) {
        this.officialNoticeLists = o;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.official_notice_list_item, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        officialNoticeList = officialNoticeLists.get(position);
        holder.mOfficialTime.setText(officialNoticeList.getTime());
        holder.mOfficialNotice.setText(officialNoticeList.getMessage());
    }

    @Override
    public int getItemCount() {
        return officialNoticeLists.size();
    }
}
