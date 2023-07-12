package com.example.restaurantmanager.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantmanager.R;
import com.example.restaurantmanager.databinding.MoreRvItemsBinding;
import com.example.restaurantmanager.adapter.item.MoreData;
import com.example.restaurantmanager.enums.MoreMenuEnum;
import com.example.restaurantmanager.realm_object.Topping;
import com.example.restaurantmanager.view.admin.CategoryActivity;
import com.example.restaurantmanager.view.admin.FoodActivity;
import com.example.restaurantmanager.view.admin.OrderActivity;
import com.example.restaurantmanager.view.admin.SettingActivity;
import com.example.restaurantmanager.view.admin.TableActivity;
import com.example.restaurantmanager.view.admin.ToppingActivity;
import com.example.restaurantmanager.view.admin.TrashActivity;
import com.example.restaurantmanager.view.admin.UpdateTableActivity;

import java.util.List;

public class MoreAdapter  extends RecyclerView.Adapter<MoreAdapter.ViewHolder> {
    private Context context;
    private List<MoreData> data;

    public MoreAdapter(Context context, List<MoreData> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View rootView = inflater.inflate(R.layout.more_rv_items, null, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.icon.setImageResource(data.get(position).getIcon());
        holder.lable.setText(data.get(position).getLabel());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView lable;
        public ViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.more_rv_image);
            lable =itemView.findViewById(R.id.more_rv_lable);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MoreMenuEnum id = data.get(getBindingAdapterPosition()).getId();
                    if(id == MoreMenuEnum.CATEGORIES){
                        Intent intent = new Intent(context, CategoryActivity.class);
                        context.startActivity(intent);
                    }
                    if(id == MoreMenuEnum.FOODS){
                        Intent intent = new Intent(context, FoodActivity.class);
                        context.startActivity(intent);
                    }if(id == MoreMenuEnum.TOPPINGS){
                        Intent intent = new Intent(context, ToppingActivity.class);
                        context.startActivity(intent);
                    }if(id == MoreMenuEnum.TABLES){
                        Intent intent = new Intent(context, TableActivity.class);
                        context.startActivity(intent);
                    }
                    if(id == MoreMenuEnum.SETTING){
                        Intent intent = new Intent(context, SettingActivity.class);
                        context.startActivity(intent);
                    }if(id == MoreMenuEnum.ORDERS){
                        Intent intent = new Intent(context, OrderActivity.class);
                        context.startActivity(intent);
                    }
                    if(id == MoreMenuEnum.TRASH){
                        Intent intent = new Intent(context, TrashActivity.class);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}
