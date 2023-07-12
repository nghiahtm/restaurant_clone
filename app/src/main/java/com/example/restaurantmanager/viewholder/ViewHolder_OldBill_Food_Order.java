package com.example.restaurantmanager.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantmanager.R;
import com.example.restaurantmanager.interfacez.ChooseFoodClickInterface;
import com.example.restaurantmanager.realm_object.order.FoodOrder;

import io.realm.RealmList;

public class ViewHolder_OldBill_Food_Order extends RecyclerView.ViewHolder{
    public TextView txt_food_name;
    public TextView txt_food_price;
    public TextView txt_food_count;
    public RecyclerView recyclerView;
public ViewHolder_OldBill_Food_Order(@NonNull View itemView ) {
        super(itemView);
        txt_food_name = itemView.findViewById(R.id.txt_food_name);
        txt_food_price = itemView.findViewById(R.id.txt_food_price);
        txt_food_count = itemView.findViewById(R.id.txt_food_count);
        recyclerView = itemView.findViewById(R.id.recyclerview_topping);
    }
}
