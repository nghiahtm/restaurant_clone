package com.example.restaurantmanager.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantmanager.R;
import com.example.restaurantmanager.adapter.item.OldOrderItem;
import com.example.restaurantmanager.interfacez.HomeTableClickInterface;
import com.example.restaurantmanager.utils.DataUtil;
import com.example.restaurantmanager.utils.DateTimeUtil;
import com.example.restaurantmanager.utils.DoubleUtil;
import com.example.restaurantmanager.viewholder.ViewHolder_OldOrder;

import java.util.List;

public class OldOrderAdapter extends  RecyclerView.Adapter<ViewHolder_OldOrder>  {
    private final Activity mActivity;
    List<OldOrderItem> mList;
    HomeTableClickInterface homeTableClickInterface;

    public OldOrderAdapter(List<OldOrderItem> list, Activity activity,HomeTableClickInterface homeTableClickInterface){
        this.mActivity = activity;
        mList = list;
        this.homeTableClickInterface = homeTableClickInterface;
    }
    @NonNull
    @Override
    public ViewHolder_OldOrder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View itemView = inflater.inflate(R.layout.adapter_list_order, parent, false);
        return new ViewHolder_OldOrder(itemView, mList, homeTableClickInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder_OldOrder holder, int position) {
        OldOrderItem temp = mList.get(position);
        holder.tv_stt.setText(position+1+"");
        holder.tv_time.setText(DateTimeUtil.formatTime(temp.getPayTime()));
        holder.tv_price.setText(DataUtil.getPaymentMethod(temp.getStatus())+":"+DoubleUtil.Round2String(temp.getTotal())+" €");
        holder.tv_tableName.setText("Tisch "+temp.getTableName());
        if(temp.isChoose()){
            holder.background.setBackgroundColor(Color.parseColor("#00D7FF"));
        }
        else {
            holder.background.setBackgroundColor(Color.parseColor("#ffffff"));

        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
