package com.example.restaurantmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.restaurantmanager.R;
import com.example.restaurantmanager.adapter.item.TableItem;
import com.example.restaurantmanager.interfacez.ChooseFoodClickInterface;
import com.example.restaurantmanager.interfacez.HomeTableClickInterface;
import com.example.restaurantmanager.realm_object.Topping;
import com.example.restaurantmanager.utils.DoubleUtil;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmResults;

public class UserToppingAdapter extends BaseAdapter {
    private RealmResults<Topping> listData;
    private LayoutInflater layoutInflater;
    ChooseFoodClickInterface chooseFoodClickInterface;
    public UserToppingAdapter(Context aContext, RealmResults<Topping> listData, ChooseFoodClickInterface chooseFoodClickInterface) {
        this.listData = listData;
        this.chooseFoodClickInterface = chooseFoodClickInterface;
        layoutInflater = LayoutInflater.from(aContext);

    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        convertView = layoutInflater.inflate(R.layout.adapter_topping_girdview, null);
        holder = new ViewHolder();
        holder.toppingName = convertView.findViewById(R.id.tv_topping_name);
        convertView.setTag(holder);
        Topping topping = this.listData.get(position);
        holder.toppingName.setText(topping.getName());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseFoodClickInterface.chooseToppingClick(topping);
            }
        });
        return convertView;
    }
    static class ViewHolder {
        TextView toppingName;
    }
}
