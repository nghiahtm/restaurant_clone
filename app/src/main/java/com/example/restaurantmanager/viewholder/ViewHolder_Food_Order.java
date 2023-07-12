package com.example.restaurantmanager.viewholder;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantmanager.R;
import com.example.restaurantmanager.interfacez.ChooseFoodClickInterface;
import com.example.restaurantmanager.interfacez.FoodClickInterface;
import com.example.restaurantmanager.interfacez.HomeTableClickInterface;
import com.example.restaurantmanager.realm_object.Food;
import com.example.restaurantmanager.realm_object.order.FoodOrder;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class ViewHolder_Food_Order extends RecyclerView.ViewHolder{
    RealmList<FoodOrder> mData;
    public TextView txt_food_name;
    public TextView txt_food_price;
    public TextView txt_food_count;
    public RecyclerView recyclerView;
    public LinearLayout ll_background;
    public ViewHolder_Food_Order(@NonNull View itemView, RealmList<FoodOrder> data, ChooseFoodClickInterface onClickInterface) {
        super(itemView);
        mData = data;
        txt_food_name = itemView.findViewById(R.id.txt_food_name);
        txt_food_price = itemView.findViewById(R.id.txt_food_price);
        txt_food_count = itemView.findViewById(R.id.txt_food_count);
        recyclerView = itemView.findViewById(R.id.recyclerview_topping);
        ll_background = itemView.findViewById(R.id.ll_background);

        itemView.findViewById(R.id.click_Food).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickInterface.foodOrderItemClick(mData.get(getBindingAdapterPosition()),ll_background);
            }
        });
    }
}
