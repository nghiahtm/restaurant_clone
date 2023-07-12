package com.example.restaurantmanager.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.restaurantmanager.R;
import com.example.restaurantmanager.adapter.item.SubcategoryItem;
import com.example.restaurantmanager.interfacez.CategoryUpdateClickInterface;
import com.example.restaurantmanager.viewholder.ViewHolder_Subcategory;
import java.util.List;
import java.util.UUID;


public class SubcategoryAdapter extends RecyclerView.Adapter< ViewHolder_Subcategory> {
    private final Activity mActivity;
    List<SubcategoryItem> mList;
    UUID mCategoryId;
    CategoryUpdateClickInterface categoryUpdateClickInterface;

    public SubcategoryAdapter(List<SubcategoryItem> list, Activity context, UUID categoryId, CategoryUpdateClickInterface categoryUpdateClickInterface) {
        this.mActivity = context;
        mList = list;
        mCategoryId = categoryId;
        this.categoryUpdateClickInterface = categoryUpdateClickInterface;
    }

    @NonNull
    @Override
    public ViewHolder_Subcategory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View itemView = inflater.inflate(R.layout.adapter_subcategory, parent, false);
        return new ViewHolder_Subcategory(itemView, mActivity, mList, mCategoryId, categoryUpdateClickInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder_Subcategory holder, int position) {
        SubcategoryItem temp = mList.get(position);
        String name = temp.name + " - " + (temp.status ? "enable":"disable");
        holder.name.setText(name);
        holder.color.setBackgroundColor(temp.color);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
