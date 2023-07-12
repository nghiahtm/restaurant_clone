package com.example.restaurantmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.restaurantmanager.R;
import com.example.restaurantmanager.interfacez.ChooseFoodClickInterface;
import com.example.restaurantmanager.realm_object.order.SubOrder;
import com.example.restaurantmanager.viewholder.ViewHolder_Suborder;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmRecyclerViewAdapter;

public class SuborderAdapter extends RealmRecyclerViewAdapter<SubOrder, ViewHolder_Suborder> {
    RealmList<SubOrder> mData;
    Context mContext;
    ChooseFoodClickInterface onClickInterface;


    public SuborderAdapter(@Nullable RealmList<SubOrder> data, boolean autoUpdate, Context context, ChooseFoodClickInterface onClickInterface) {
        super(data, autoUpdate);
        mData = data;
        mContext = context;
        this.onClickInterface = onClickInterface;
    }

    @NonNull
    @Override
    public ViewHolder_Suborder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_sub_order, parent, false);
        return new ViewHolder_Suborder(itemView,onClickInterface,mData);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder_Suborder holder, int position) {
        final SubOrder subOrder = getItem(position);
        FoodOrderAdapter mAdapter;
        mAdapter = new FoodOrderAdapter( subOrder.getFoodOrders(),true, mContext,onClickInterface);
        holder.recycler_foods.setLayoutManager(new LinearLayoutManager(mContext));
        holder.recycler_foods.setAdapter(mAdapter);
        if (position == 0){
            holder.line.setVisibility(View.GONE);
        }
        else {
            holder.line.setVisibility(View.VISIBLE);

        }


    }

}
