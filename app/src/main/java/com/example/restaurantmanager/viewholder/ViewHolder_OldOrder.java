package com.example.restaurantmanager.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantmanager.R;
import com.example.restaurantmanager.adapter.item.OldOrderItem;
import com.example.restaurantmanager.interfacez.HomeTableClickInterface;

import java.util.List;

public class ViewHolder_OldOrder extends RecyclerView.ViewHolder{
    List<OldOrderItem> mData;
    public ConstraintLayout background;
    public TextView tv_stt;
    public TextView tv_time;
    public TextView tv_price;
    public TextView tv_tableName;

    public ViewHolder_OldOrder(@NonNull View itemView, List<OldOrderItem> data, HomeTableClickInterface onClickInterface) {
        super(itemView);
        mData = data;
        background = itemView.findViewById(R.id.tv_background);
        tv_price = itemView.findViewById(R.id.tv_total);
        tv_stt = itemView.findViewById(R.id.tv_stt);
        tv_time = itemView.findViewById(R.id.tv_time);
        tv_tableName = itemView.findViewById(R.id.tv_table_name);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickInterface.chooseOrder(data.get( getBindingAdapterPosition()));
            }
        });

    }
}
