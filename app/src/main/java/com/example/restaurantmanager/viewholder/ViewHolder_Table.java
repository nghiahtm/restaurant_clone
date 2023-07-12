package com.example.restaurantmanager.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantmanager.R;
import com.example.restaurantmanager.interfacez.TableClickInterface;
import com.example.restaurantmanager.interfacez.ToppingClickInterface;
import com.example.restaurantmanager.realm_object.Table;
import com.example.restaurantmanager.realm_object.Topping;

import io.realm.Realm;
import io.realm.RealmResults;

public class ViewHolder_Table extends RecyclerView.ViewHolder{
    RealmResults<Table> mData;
    Context mContext;
    public TextView name;
    public ImageView btn_delete;
    public ViewHolder_Table(@NonNull View itemView, Context context, RealmResults<Table> data, TableClickInterface onClickInterface) {
        super(itemView);
        mData = data;
        mContext = context;
        name = itemView.findViewById(R.id.adapter_table_name);
        btn_delete = itemView.findViewById(R.id.adapter_btn_delete);
        btn_delete.setOnClickListener (new View.OnClickListener () {
            @Override public void onClick (View v) {
                onClickInterface.onDeleteClicked(data.get(getBindingAdapterPosition()).getId());
            }
        });
        name.setOnClickListener (new View.OnClickListener () {
            @Override public void onClick (View v) {
                onClickInterface.viewDetailClick(data.get(getBindingAdapterPosition()).getId());
            }
        });
        itemView.findViewById(R.id.adapter_btn_public).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickInterface.onChangStatusClicked(data.get(getBindingAdapterPosition()));
            }
        });
    }
}
