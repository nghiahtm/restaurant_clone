package com.example.restaurantmanager.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantmanager.R;
import com.example.restaurantmanager.interfacez.ChooseFoodClickInterface;
import com.example.restaurantmanager.interfacez.TableClickInterface;
import com.example.restaurantmanager.realm_object.Table;
import com.example.restaurantmanager.realm_object.order.FoodOrder;
import com.example.restaurantmanager.realm_object.order.ToppingOrder;
import com.example.restaurantmanager.utils.DoubleUtil;
import com.example.restaurantmanager.viewholder.ViewHolder_Food_Order;
import com.example.restaurantmanager.viewholder.ViewHolder_Table;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

public class FoodOrderAdapter extends RealmRecyclerViewAdapter<FoodOrder, ViewHolder_Food_Order> {
    RealmList<FoodOrder> mData;
    Context mContext;
    ChooseFoodClickInterface onClickInterface;


    public FoodOrderAdapter(@Nullable RealmList<FoodOrder> data, boolean autoUpdate, Context context, ChooseFoodClickInterface onClickInterface) {
        super(data, autoUpdate);
        mData = data;
        mContext = context;
        this.onClickInterface = onClickInterface;
    }

    @NonNull
    @Override
    public ViewHolder_Food_Order onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_food_sub_order, parent, false);
        return new ViewHolder_Food_Order(itemView, mData, onClickInterface);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder_Food_Order holder, int position) {
        final FoodOrder foodOrder = getItem(position);
        holder.txt_food_name.setText(foodOrder.getName()+((foodOrder.getOption()!=null)
                ?"-"+foodOrder.getOption().getName():""));
        holder.txt_food_count.setText(foodOrder.getCount()+"");
        holder.txt_food_price.setText(DoubleUtil.Round2String(foodOrder.getTotalPrice2())+"€");
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        ToppingOrderAdapter toppingOrderAdapter = new ToppingOrderAdapter(mContext,foodOrder.getToppings(),onClickInterface,foodOrder);
        holder.recyclerView.setAdapter(toppingOrderAdapter);
        if(foodOrder.getPrint()){
            holder.ll_background.setBackgroundColor(Color.parseColor("#F9F9C5"));
        }
    }

}
