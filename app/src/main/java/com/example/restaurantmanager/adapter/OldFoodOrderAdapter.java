package com.example.restaurantmanager.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.restaurantmanager.R;
import com.example.restaurantmanager.interfacez.ChooseFoodClickInterface;
import com.example.restaurantmanager.realm_object.order.FoodOrder;
import com.example.restaurantmanager.utils.DoubleUtil;
import com.example.restaurantmanager.viewholder.ViewHolder_CutBill_Food_Order;
import com.example.restaurantmanager.viewholder.ViewHolder_OldBill_Food_Order;

import io.realm.RealmList;
import io.realm.RealmRecyclerViewAdapter;

public class OldFoodOrderAdapter extends RealmRecyclerViewAdapter<FoodOrder, ViewHolder_OldBill_Food_Order> {
    RealmList<FoodOrder> mData;
    Context mContext;
    ChooseFoodClickInterface onClickInterface;

    public OldFoodOrderAdapter(@Nullable RealmList<FoodOrder> data, boolean autoUpdate, Context context) {
        super(data, autoUpdate);
        mData = data;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder_OldBill_Food_Order onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_oldbill_food_sub_order, parent, false);
        return new ViewHolder_OldBill_Food_Order(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder_OldBill_Food_Order holder, int position) {
        final FoodOrder foodOrder = getItem(position);
        holder.txt_food_name.setText(foodOrder.getName()+((foodOrder.getOption()!=null)
                ?"-"+foodOrder.getOption().getName():""));
        holder.txt_food_count.setText(foodOrder.getCount()+"");
        holder.txt_food_price.setText(DoubleUtil.Round2String(foodOrder.getTotalPrice2())+"€");
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        OldToppingOrderAdapter toppingOrderAdapter = new OldToppingOrderAdapter(mContext,foodOrder.getToppings());
        holder.recyclerView.setAdapter(toppingOrderAdapter);
    }

}
