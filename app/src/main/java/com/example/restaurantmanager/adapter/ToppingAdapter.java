package com.example.restaurantmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.restaurantmanager.R;
import com.example.restaurantmanager.interfacez.FoodClickInterface;
import com.example.restaurantmanager.interfacez.ToppingClickInterface;
import com.example.restaurantmanager.realm_object.Food;
import com.example.restaurantmanager.realm_object.Topping;
import com.example.restaurantmanager.view.admin.ToppingActivity;
import com.example.restaurantmanager.viewholder.ViewHolder_Food;
import com.example.restaurantmanager.viewholder.ViewHolder_Topping;

import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

public class ToppingAdapter extends RealmRecyclerViewAdapter<Topping, ViewHolder_Topping> {
    RealmResults<Topping> mData;
    Context mContext;
    Realm realm;
    ToppingClickInterface onClickInterface;


    public ToppingAdapter(@Nullable RealmResults<Topping> data, boolean autoUpdate, Context context, ToppingClickInterface onClickInterface) {
        super(data, autoUpdate);
        mData = data;
        mContext = context;
        this.onClickInterface = onClickInterface;

    }

    @NonNull
    @Override
    public ViewHolder_Topping onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_topping, parent, false);
        return new ViewHolder_Topping(itemView, mContext, mData, onClickInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder_Topping holder, int position) {
        final Topping temp = getItem(position);
        holder.name.setText(temp.getName()+"-"+(temp.getStatus() ? "enable":"disable"));
        holder.price.setText("€"+temp.getPrice().toString());
    }

}
