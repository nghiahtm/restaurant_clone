package com.example.restaurantmanager.viewholder;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantmanager.R;
import com.example.restaurantmanager.adapter.item.OptionItem;
import com.example.restaurantmanager.adapter.item.SubcategoryItem;
import com.example.restaurantmanager.interfacez.CategoryUpdateClickInterface;
import com.example.restaurantmanager.interfacez.OptionClickInterface;

import java.util.List;
import java.util.UUID;

public class ViewHolder_Option extends RecyclerView.ViewHolder{
    public TextView name;
    public TextView price;
    public ImageView delete;
    public Activity mContext;
    List<OptionItem> mList;
    UUID mOptionId;
    public ViewHolder_Option(@NonNull View itemView, Activity mContext, List<OptionItem> list, final UUID mOptionId, OptionClickInterface optionClickInterface) {
        super(itemView);
        this.mContext = mContext;
        mList = list;
        this.mOptionId = mOptionId;
        name = itemView.findViewById(R.id.adapter_option_name);
        price = itemView.findViewById(R.id.adapter_option_price);
        delete = itemView.findViewById(R.id.adapter_btn_delete);
        delete.setOnClickListener (new View.OnClickListener () {
            @Override public void onClick (View v) {
                optionClickInterface.onDeleteClicked(mList.get(getBindingAdapterPosition()).id);

            }
        });
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                optionClickInterface.onUpdateOptionClicked(mList.get(getBindingAdapterPosition()));

            }
        });
        itemView.findViewById(R.id.adapter_btn_public).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                optionClickInterface.onChangStatusOptionClicked(mList.get(getBindingAdapterPosition()));
            }
        });
    }

}
