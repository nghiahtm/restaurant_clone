package com.example.restaurantmanager.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantmanager.R;
import com.example.restaurantmanager.interfacez.ChooseFoodClickInterface;
import com.example.restaurantmanager.interfacez.ToppingClickInterface;
import com.example.restaurantmanager.realm_object.Topping;
import com.example.restaurantmanager.realm_object.order.SubOrder;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class ViewHolder_Suborder extends RecyclerView.ViewHolder{
    public RecyclerView recycler_foods;
    public View line;
    RealmList<SubOrder> mData;

    public ViewHolder_Suborder(@NonNull View itemView, ChooseFoodClickInterface onClickInterface, RealmList<SubOrder> mData) {
        super(itemView);
        recycler_foods = itemView.findViewById(R.id.recycler_foods);
        line = itemView.findViewById(R.id.line1);
        this.mData = mData;
        line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickInterface.chooseSuborder(mData.get(getBindingAdapterPosition()));
            }
        });

    }
}
