package com.example.restaurantmanager.viewholder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantmanager.R;
import com.example.restaurantmanager.interfacez.ChooseFoodClickInterface;
import com.example.restaurantmanager.realm_object.order.SubOrder;

import io.realm.RealmList;

public class ViewHolder_ViewBill_Suborder extends RecyclerView.ViewHolder{
    public RecyclerView recycler_foods;
    public View line;

    public ViewHolder_ViewBill_Suborder(@NonNull View itemView) {
        super(itemView);
        recycler_foods = itemView.findViewById(R.id.recycler_foods);
        line = itemView.findViewById(R.id.line1);
    }
}
