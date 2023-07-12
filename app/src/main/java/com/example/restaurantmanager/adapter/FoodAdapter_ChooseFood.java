package com.example.restaurantmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.restaurantmanager.R;
import com.example.restaurantmanager.interfacez.ChooseFoodClickInterface;
import com.example.restaurantmanager.interfacez.FoodClickInterface;
import com.example.restaurantmanager.realm_object.Food;
import com.example.restaurantmanager.viewholder.ViewHolder_Food;
import com.example.restaurantmanager.viewholder.ViewHolder_Food_ChooseFood;

import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

public class FoodAdapter_ChooseFood extends RealmRecyclerViewAdapter<Food, ViewHolder_Food_ChooseFood> {
    RealmResults<Food> mData;
    Context mContext;
    ChooseFoodClickInterface onClickInterface;


    public FoodAdapter_ChooseFood(@Nullable RealmResults<Food> data, boolean autoUpdate, Context context, ChooseFoodClickInterface onClickInterface) {
        super(data, autoUpdate);
        mData = data;
        mContext = context;
        this.onClickInterface = onClickInterface;

    }

    @NonNull
    @Override
    public ViewHolder_Food_ChooseFood onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_food_choose_food, parent, false);
        return new ViewHolder_Food_ChooseFood(itemView, mData, onClickInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder_Food_ChooseFood holder, int position) {
        final Food temp = getItem(position);
        holder.name.setText(temp.getName());
    }

}
