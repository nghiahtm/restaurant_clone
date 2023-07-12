package com.example.restaurantmanager.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantmanager.R;
import com.example.restaurantmanager.interfacez.CategoryClickInterface;
import com.example.restaurantmanager.realm_object.Category;

import io.realm.Realm;
import io.realm.RealmResults;

public class ViewHolder_Category extends RecyclerView.ViewHolder{
    RealmResults<Category> mData;
    Context mContext;
    public TextView name;
    public TextView subcatCount;
    public ImageView btn_delete;
    public ViewHolder_Category(@NonNull View itemView, Context context, RealmResults<Category> data, CategoryClickInterface onClickInterface) {
        super(itemView);
        mData = data;
        mContext = context;
        name = itemView.findViewById(R.id.adapter_category_name);
        subcatCount = itemView.findViewById(R.id.adapter_subcategoryCount);
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
                onClickInterface.viewChangStatusClick(data.get(getBindingAdapterPosition()));
            }
        });
    }
}
