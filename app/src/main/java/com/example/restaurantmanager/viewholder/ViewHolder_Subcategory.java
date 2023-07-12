package com.example.restaurantmanager.viewholder;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantmanager.R;
import com.example.restaurantmanager.adapter.item.SubcategoryItem;
import com.example.restaurantmanager.interfacez.CategoryUpdateClickInterface;
import com.example.restaurantmanager.realm_object.Category;
import com.example.restaurantmanager.realm_object.SubCategory;

import org.w3c.dom.Text;

import java.util.List;
import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmResults;

public class ViewHolder_Subcategory  extends RecyclerView.ViewHolder{
    public TextView name;
    public TextView color;
    public ImageView delete;
    public Activity mContext;
    List<SubcategoryItem> mList;
    UUID mCategoryId;


    public ViewHolder_Subcategory(@NonNull View itemView, Activity mContext, List<SubcategoryItem> list, final UUID categoryId,CategoryUpdateClickInterface categoryUpdateClickInterface) {
        super(itemView);
        this.mContext = mContext;
        mList = list;
        mCategoryId = categoryId;
        name = itemView.findViewById(R.id.adapter_subcategory_name);
        color = itemView.findViewById(R.id.adapter_color);
        delete = itemView.findViewById(R.id.adapter_btn_delete);
        delete.setOnClickListener (new View.OnClickListener () {

            @Override public void onClick (View v) {
                categoryUpdateClickInterface.onDeleteClicked(mList.get(getBindingAdapterPosition()).id);
            }
        });
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryUpdateClickInterface.onUpdateSubcategoryClicked(mList.get(getBindingAdapterPosition()));

            }
        });
        itemView.findViewById(R.id.adapter_btn_public).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryUpdateClickInterface.viewChangStatusClick(mList.get(getBindingAdapterPosition()));
            }
        });
    }
}
