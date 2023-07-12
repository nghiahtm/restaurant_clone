package com.example.restaurantmanager.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantmanager.R;
import com.example.restaurantmanager.interfacez.ChooseFoodClickInterface;
import com.example.restaurantmanager.realm_object.Food;

import io.realm.RealmResults;

public class ViewHolder_Food_ChooseFood extends RecyclerView.ViewHolder{
    RealmResults<Food> mData;
    public TextView name;
    public ViewHolder_Food_ChooseFood(@NonNull View itemView, RealmResults<Food> data, ChooseFoodClickInterface onClickInterface) {
        super(itemView);
        mData = data;
        name = itemView.findViewById(R.id.adapter_food_name);
        name.setOnClickListener (new View.OnClickListener () {
            @Override public void onClick (View v) {
                onClickInterface.chooseFood(data.get(getBindingAdapterPosition()));
            }
        });
    }
}
