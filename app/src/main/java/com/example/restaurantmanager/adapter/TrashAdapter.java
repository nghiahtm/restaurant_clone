package com.example.restaurantmanager.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantmanager.R;
import com.example.restaurantmanager.adapter.item.OldOrderItem;
import com.example.restaurantmanager.adapter.item.TrashItem;
import com.example.restaurantmanager.interfacez.HomeTableClickInterface;
import com.example.restaurantmanager.interfacez.ITrashTableClick;
import com.example.restaurantmanager.utils.DateTimeUtil;
import com.example.restaurantmanager.utils.DoubleUtil;
import com.example.restaurantmanager.viewholder.ViewHolder_OldOrder;
import com.example.restaurantmanager.viewholder.ViewHolder_Trash;

import java.util.List;

public class TrashAdapter extends  RecyclerView.Adapter<ViewHolder_Trash>  {
    private final Activity mActivity;
    List<TrashItem> mList;
    ITrashTableClick homeTableClickInterface;

    public TrashAdapter(List<TrashItem> list, Activity activity, ITrashTableClick homeTableClickInterface){
        this.mActivity = activity;
        mList = list;
        this.homeTableClickInterface = homeTableClickInterface;
    }
    @NonNull
    @Override
    public ViewHolder_Trash onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View itemView = inflater.inflate(R.layout.adapter_list_order, parent, false);
        return new ViewHolder_Trash(itemView, mList, homeTableClickInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder_Trash holder, int position) {
        TrashItem temp = mList.get(position);
        holder.tv_stt.setText(position+1+"");
        holder.tv_time.setText(DateTimeUtil.formatTime(temp.getPayTime()));
        holder.tv_price.setText(DoubleUtil.Round2String(temp.getTotal())+" €");
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
