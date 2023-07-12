package com.example.restaurantmanager.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantmanager.R;
import com.example.restaurantmanager.interfacez.CategoryClickInterface;
import com.example.restaurantmanager.interfacez.FoodClickInterface;
import com.example.restaurantmanager.realm_object.Category;
import com.example.restaurantmanager.realm_object.Food;

import io.realm.Realm;
import io.realm.RealmResults;

public class ViewHolder_Food extends RecyclerView.ViewHolder{
    RealmResults<Food> mData;
    Context mContext;
    public TextView name;
    public TextView optionCount;
    public TextView price;
    public ImageView btn_delete;
    public ViewHolder_Food(@NonNull View itemView, Context context, RealmResults<Food> data, FoodClickInterface onClickInterface) {
        super(itemView);
        mData = data;
        mContext = context;
        name = itemView.findViewById(R.id.adapter_food_name);
        optionCount = itemView.findViewById(R.id.adapter_optionCount);
        btn_delete = itemView.findViewById(R.id.adapter_btn_delete);
        price = itemView.findViewById(R.id.adapter_food_price);
        btn_delete.setOnClickListener (new View.OnClickListener () {
            @Override public void onClick (View v) {
                onClickInterface.onDeleteClicked(data.get(getBindingAdapterPosition()).getId());
            }
        });
        name.setOnClickListener (new View.OnClickListener () {
            @Override public void onClick (View v) {
                onClickInterface.viewDetailClick(data.get(getBindingAdapterPosition()).getId());
            }
        });
        itemView.findViewById(R.id.adapter_btn_public).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickInterface.viewChangStatusClick(data.get(getBindingAdapterPosition()));
            }
        });
    }
}
