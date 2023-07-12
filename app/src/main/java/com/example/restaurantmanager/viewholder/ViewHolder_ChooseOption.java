package com.example.restaurantmanager.viewholder;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantmanager.R;
import com.example.restaurantmanager.adapter.item.OptionItem;
import com.example.restaurantmanager.interfacez.ChooseFoodClickInterface;
import com.example.restaurantmanager.interfacez.OptionClickInterface;

import java.util.List;
import java.util.UUID;

public class ViewHolder_ChooseOption extends RecyclerView.ViewHolder{
    public TextView name;
    public TextView price;
    public Activity mContext;
    List<OptionItem> mList;
    public ViewHolder_ChooseOption(@NonNull View itemView, Activity mContext, List<OptionItem> list, ChooseFoodClickInterface optionClickInterface) {
        super(itemView);
        this.mContext = mContext;
        mList = list;
        name = itemView.findViewById(R.id.adapter_option_name);
        price = itemView.findViewById(R.id.adapter_option_price);
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                optionClickInterface.chooseOptionClicked(mList.get(getBindingAdapterPosition()));
            }
        });

    }

}
