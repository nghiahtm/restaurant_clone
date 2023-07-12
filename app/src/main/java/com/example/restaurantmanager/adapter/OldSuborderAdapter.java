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
import com.example.restaurantmanager.viewholder.ViewHolder_CutBill_Suborder;
import com.example.restaurantmanager.viewholder.ViewHolder_OldBill_Suborder;

import io.realm.RealmList;
import io.realm.RealmRecyclerViewAdapter;

public class OldSuborderAdapter extends RealmRecyclerViewAdapter<SubOrder, ViewHolder_OldBill_Suborder> {
    RealmList<SubOrder> mData;
    Context mContext;
    ChooseFoodClickInterface onClickInterface;
    int inList;


    public OldSuborderAdapter(@Nullable RealmList<SubOrder> data, boolean autoUpdate, Context context) {
        super(data, autoUpdate);
        mData = data;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder_OldBill_Suborder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_oldbill_sub_order, parent, false);
        return new ViewHolder_OldBill_Suborder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder_OldBill_Suborder holder, int position) {
        final SubOrder subOrder = getItem(position);
        OldFoodOrderAdapter mAdapter;
        mAdapter = new OldFoodOrderAdapter( subOrder.getFoodOrders(),true, mContext);
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
