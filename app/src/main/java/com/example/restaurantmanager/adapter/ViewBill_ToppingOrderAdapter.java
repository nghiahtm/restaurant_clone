package com.example.restaurantmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantmanager.R;
import com.example.restaurantmanager.interfacez.ChooseFoodClickInterface;
import com.example.restaurantmanager.realm_object.order.FoodOrder;
import com.example.restaurantmanager.realm_object.order.ToppingOrder;

import io.realm.RealmList;

public class ViewBill_ToppingOrderAdapter extends RecyclerView.Adapter<ViewBill_ToppingOrderAdapter.ViewHolder> {
    private Context context;
    RealmList<ToppingOrder> toppings;
    public ViewBill_ToppingOrderAdapter(Context context, RealmList<ToppingOrder> toppings) {
        this.context = context;
        this.toppings = toppings;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View rootView = inflater.inflate(R.layout.adapter_viewbill_food_order_topping, null, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ToppingOrder toppingOrder = toppings.get(position);
        if(toppingOrder.getPrice() > 0){
            holder.lable.setText(toppingOrder.getName()+"-"+toppingOrder.getPrice().toString()+"€");
        }
        else {
            holder.lable.setText(toppingOrder.getName());
        }
    }

    @Override
    public int getItemCount() {
        return toppings.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView lable;
        public ViewHolder(View itemView) {
            super(itemView);
            lable = itemView.findViewById(R.id.tv_topping_name);
        }
    }
}
