package com.example.restaurantmanager.viewholder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantmanager.R;
import com.example.restaurantmanager.interfacez.ChooseFoodClickInterface;
import com.example.restaurantmanager.realm_object.order.FoodOrder;

import io.realm.RealmList;

public class ViewHolder_CutBill_Food_Order extends RecyclerView.ViewHolder{
    RealmList<FoodOrder> mData;
    public TextView txt_food_name;
    public TextView txt_food_price;
    public TextView txt_food_count;
    public RecyclerView recyclerView;
    public ViewHolder_CutBill_Food_Order(@NonNull View itemView, RealmList<FoodOrder> data, ChooseFoodClickInterface onClickInterface,int inList) {
        super(itemView);
        mData = data;
        txt_food_name = itemView.findViewById(R.id.txt_food_name);
        txt_food_price = itemView.findViewById(R.id.txt_food_price);
        txt_food_count = itemView.findViewById(R.id.txt_food_count);
        recyclerView = itemView.findViewById(R.id.recyclerview_topping);
        itemView.findViewById(R.id.click_Food).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(inList == 1){
                   onClickInterface.cutFood1(mData.get(getBindingAdapterPosition()));
               }
                if(inList == 2){
                    onClickInterface.cutFood2(mData.get(getBindingAdapterPosition()));
                }
            }
        });
    }
}
