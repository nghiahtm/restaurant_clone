package com.example.restaurantmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.restaurantmanager.R;
import com.example.restaurantmanager.interfacez.CategoryClickInterface;
import com.example.restaurantmanager.realm_object.Category;
import com.example.restaurantmanager.utils.DataUtil;
import com.example.restaurantmanager.viewholder.ViewHolder_Category;

import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

public class CategoryAdapter extends RealmRecyclerViewAdapter<Category,ViewHolder_Category> {
    RealmResults<Category> mData;
    Context mContext;
    CategoryClickInterface onClickInterface;


    public CategoryAdapter(@Nullable RealmResults<Category> data, boolean autoUpdate, Context context, CategoryClickInterface onClickInterface) {
        super(data, autoUpdate);
        mData = data;
        mContext = context;
        this.onClickInterface = onClickInterface;

    }

    @NonNull
    @Override
    public ViewHolder_Category onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_category, parent, false);
        return new ViewHolder_Category(itemView, mContext, mData, onClickInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder_Category holder, int position) {
        final Category temp = getItem(position);
        holder.name.setText( temp.getName()+"-"+(temp.getStatus() ? "enable":"disable"));
        holder.subcatCount.setText("Subcategories:"+temp.getSubCategories().size());


    }

}
