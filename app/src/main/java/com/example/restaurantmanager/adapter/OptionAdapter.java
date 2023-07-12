package com.example.restaurantmanager.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantmanager.R;
import com.example.restaurantmanager.adapter.item.OptionItem;
import com.example.restaurantmanager.adapter.item.SubcategoryItem;
import com.example.restaurantmanager.interfacez.CategoryUpdateClickInterface;
import com.example.restaurantmanager.interfacez.OptionClickInterface;
import com.example.restaurantmanager.viewholder.ViewHolder_Option;
import com.example.restaurantmanager.viewholder.ViewHolder_Subcategory;

import java.util.List;
import java.util.UUID;

public class OptionAdapter  extends  RecyclerView.Adapter<ViewHolder_Option>  {
    private final Activity mActivity;
    List<OptionItem> mList;
    UUID foodId;
    OptionClickInterface optionClickInterface;

    public OptionAdapter(List<OptionItem> list, Activity context, UUID foodId, OptionClickInterface optionClickInterface) {
        this.mActivity = context;
        mList = list;
        this.foodId = foodId;
        this.optionClickInterface = optionClickInterface;
    }
    @NonNull
    @Override
    public ViewHolder_Option onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View itemView = inflater.inflate(R.layout.adapter_options, parent, false);
        return new ViewHolder_Option(itemView, mActivity, mList, foodId, optionClickInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder_Option holder, int position) {
        OptionItem temp = mList.get(position);
        String name = temp.name + " - " + (temp.status ? "enable":"disable");
        holder.name.setText(name);
        holder.price.setText("€" + temp.price);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
