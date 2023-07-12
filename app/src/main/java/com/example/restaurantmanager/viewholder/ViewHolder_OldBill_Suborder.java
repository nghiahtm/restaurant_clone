package com.example.restaurantmanager.viewholder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantmanager.R;

public class ViewHolder_OldBill_Suborder extends RecyclerView.ViewHolder{
    public RecyclerView recycler_foods;
    public View line;


    public ViewHolder_OldBill_Suborder(@NonNull View itemView) {
        super(itemView);
        recycler_foods = itemView.findViewById(R.id.recycler_foods);
        line = itemView.findViewById(R.id.line1);
    }
}
