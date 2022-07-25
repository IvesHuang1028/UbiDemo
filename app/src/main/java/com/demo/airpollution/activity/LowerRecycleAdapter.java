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

public class LowerRecycleAdapter extends RecyclerView.Adapter<LowerRecycleAdapter.ViewHolder>{
    private String TAG = "LowerRecycleAdapter";
    private Context context;
    private ArrayList<AirPollution> items;

    private IAdapter callback;

    public LowerRecycleAdapter(Context context, ArrayList<AirPollution> items , IAdapter callback) {
        this.items = items;
        this.context = context;
        this.callback = callback;
    }
    @NonNull
    @Override
    public LowerRecycleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyLog.i(TAG,  "onCreateViewHolder");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lower, parent, false);
        return new LowerRecycleAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final LowerRecycleAdapter.ViewHolder holder, final int position) {
        //處理每一筆資料
        holder.tv_item_siteId.setText(items.get(position).getSiteId());
        holder.tv_item_siteName.setText(items.get(position).getSiteName());
        holder.tv_item_pm25.setText(items.get(position).getPM25());
        holder.tv_item_status.setText(items.get(position).getStatus());
        holder.tv_item_country.setText(items.get(position).getCountry());

        if(holder.tv_item_status.getText().equals("良好")){
            holder.tv_item_status.setVisibility(View.GONE);
            holder.tv_item_status2.setVisibility(View.VISIBLE);
            holder.tv_item_status2.setTextSize(12);
            holder.tv_item_status2.setText("The status is good, we want to go out to have fun");
            holder.tv_item_status2.setTextColor(Color.parseColor("#ff0000"));
        }else{
            holder.tv_item_status.setVisibility(View.VISIBLE);
            holder.tv_item_status2.setVisibility(View.GONE );
            holder.tv_next.setVisibility(View.VISIBLE);
            holder.cl_lower_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context,"The status is not good",Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
    @Override
    public int getItemCount() {
        return items.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout cl_lower_item;
        TextView tv_item_siteId;
        TextView tv_item_siteName;
        TextView tv_item_pm25;
        TextView tv_item_status;
        TextView tv_item_status2;
        TextView tv_item_country;
        TextView tv_next;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cl_lower_item = itemView.findViewById(R.id.cl_upper);
            tv_item_siteId = itemView.findViewById(R.id.tv_siteId);
            tv_item_siteName = itemView.findViewById(R.id.tv_siteName);
            tv_item_pm25 = itemView.findViewById(R.id.tv_pm25);
            tv_item_status = itemView.findViewById(R.id.tv_status);
            tv_item_status2 = itemView.findViewById(R.id.tv_status2);
            tv_item_country = itemView.findViewById(R.id.tv_country);
            tv_next = itemView.findViewById(R.id.tv_next);
        }
    }
}
