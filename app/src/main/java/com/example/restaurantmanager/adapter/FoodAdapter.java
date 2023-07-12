package com.example.restaurantmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.restaurantmanager.R;
import com.example.restaurantmanager.interfacez.CategoryClickInterface;
import com.example.restaurantmanager.interfacez.FoodClickInterface;
import com.example.restaurantmanager.realm_object.Category;
import com.example.restaurantmanager.realm_object.Food;
import com.example.restaurantmanager.utils.DataUtil;
import com.example.restaurantmanager.viewholder.ViewHolder_Category;
import com.example.restaurantmanager.viewholder.ViewHolder_Food;

import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

public class FoodAdapter extends RealmRecyclerViewAdapter<Food, ViewHolder_Food> {
    RealmResults<Food> mData;
    Context mContext;
    Realm realm;
    FoodClickInterface onClickInterface;


    public FoodAdapter(@Nullable RealmResults<Food> data, boolean autoUpdate, Context context, FoodClickInterface onClickInterface) {
        super(data, autoUpdate);
        mData = data;
        mContext = context;
        this.onClickInterface = onClickInterface;

    }

    @NonNull
    @Override
    public ViewHolder_Food onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_food, parent, false);
        return new ViewHolder_Food(itemView, mContext, mData, onClickInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder_Food holder, int position) {
        final Food temp = getItem(position);
        holder.name.setText(DataUtil.FoodTypes[temp.getFoodType()]+"-"+temp.getSubCategory().getName()+ ":"+temp.getName()+"-"+(temp.getStatus() ? "enable":"disable"));
        holder.optionCount.setText("Options:"+temp.getOptionsCount());
        holder.price.setText("€"+temp.getPrice().toString());

    }

}
