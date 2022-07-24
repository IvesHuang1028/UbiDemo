package com.demo.airpollution.activity;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.airpollution.Interface.IAdapter;
import com.demo.airpollution.MyLog;
import com.demo.airpollution.R;

import java.util.ArrayList;

public class UpperRecycleAdapter extends RecyclerView.Adapter<UpperRecycleAdapter.ViewHolder>{
    private String TAG = "RackRecycleAdapter";
    private Context context;
    private ArrayList<AirPollution> items;

    private IAdapter callback;

    public UpperRecycleAdapter(Context context, ArrayList<AirPollution> items , IAdapter callback) {
        this.items = items;
        this.context = context;
        this.callback = callback;
    }
    @NonNull
    @Override
    public UpperRecycleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyLog.i(TAG,  "onCreateViewHolder");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_upper, parent, false);
        return new UpperRecycleAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final UpperRecycleAdapter.ViewHolder holder, final int position) {
        //處理每一筆資料
        holder.tv_item_siteId.setText(items.get(position).getSiteId());
        holder.tv_item_siteName.setText(items.get(position).getSiteName());
        holder.tv_item_pm25.setText(items.get(position).getPM25());
        holder.tv_item_status.setText(items.get(position).getStatus());
        holder.tv_item_country.setText(items.get(position).getCountry());
    }
    @Override
    public int getItemCount() {
        return items.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout cl_upper_item;
        TextView tv_item_siteId;
        TextView tv_item_siteName;
        TextView tv_item_pm25;
        TextView tv_item_status;
        TextView tv_item_country;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cl_upper_item = itemView.findViewById(R.id.cl_upper);
            tv_item_siteId = itemView.findViewById(R.id.tv_siteId);
            tv_item_siteName = itemView.findViewById(R.id.tv_siteName);
            tv_item_pm25 = itemView.findViewById(R.id.tv_pm25);
            tv_item_status = itemView.findViewById(R.id.tv_status);
            tv_item_country = itemView.findViewById(R.id.tv_country);
        }
    }
}
