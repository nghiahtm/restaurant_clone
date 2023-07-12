package com.example.restaurantmanager.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantmanager.R;
import com.example.restaurantmanager.adapter.item.OptionItem;
import com.example.restaurantmanager.interfacez.ChooseFoodClickInterface;
import com.example.restaurantmanager.interfacez.OptionClickInterface;
import com.example.restaurantmanager.viewholder.ViewHolder_ChooseOption;
import com.example.restaurantmanager.viewholder.ViewHolder_Option;

import java.util.List;
import java.util.UUID;

public class ChooseOptionAdapter extends  RecyclerView.Adapter<ViewHolder_ChooseOption>  {
    private final Activity mActivity;
    List<OptionItem> mList;
    ChooseFoodClickInterface optionClickInterface;

    public ChooseOptionAdapter(List<OptionItem> list, Activity context, ChooseFoodClickInterface optionClickInterface) {
        this.mActivity = context;
        mList = list;
        this.optionClickInterface = optionClickInterface;
    }
    @NonNull
    @Override
    public ViewHolder_ChooseOption onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View itemView = inflater.inflate(R.layout.adapter_choose_option, parent, false);
        return new ViewHolder_ChooseOption(itemView, mActivity, mList, optionClickInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder_ChooseOption holder, int position) {
        OptionItem temp = mList.get(position);
        String name = temp.name;
        holder.name.setText(name);
        holder.price.setText(temp.price+"€");
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
