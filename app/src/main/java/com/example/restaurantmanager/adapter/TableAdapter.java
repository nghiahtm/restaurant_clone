package com.example.restaurantmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.restaurantmanager.R;
import com.example.restaurantmanager.interfacez.TableClickInterface;
import com.example.restaurantmanager.realm_object.Table;
import com.example.restaurantmanager.viewholder.ViewHolder_Table;

import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

public class TableAdapter extends RealmRecyclerViewAdapter<Table, ViewHolder_Table> {
    RealmResults<Table> mData;
    Context mContext;
    Realm realm;
    TableClickInterface onClickInterface;


    public TableAdapter(@Nullable RealmResults<Table> data, boolean autoUpdate, Context context, TableClickInterface onClickInterface) {
        super(data, autoUpdate);
        mData = data;
        mContext = context;
        this.onClickInterface = onClickInterface;
    }

    @NonNull
    @Override
    public ViewHolder_Table onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_table, parent, false);
        return new ViewHolder_Table(itemView, mContext, mData, onClickInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder_Table holder, int position) {
        final Table temp = getItem(position);
        holder.name.setText(temp.getName()+"-"+(temp.getStatus() ? "enable":"disable"));
    }

}
