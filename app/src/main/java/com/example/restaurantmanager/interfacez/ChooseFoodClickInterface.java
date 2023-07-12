package com.example.restaurantmanager.interfacez;

import android.widget.LinearLayout;

import com.example.restaurantmanager.adapter.item.OptionItem;
import com.example.restaurantmanager.realm_object.Food;
import com.example.restaurantmanager.realm_object.Topping;
import com.example.restaurantmanager.realm_object.order.FoodOrder;
import com.example.restaurantmanager.realm_object.order.SubOrder;
import com.example.restaurantmanager.realm_object.order.ToppingOrder;

import java.util.UUID;

public interface ChooseFoodClickInterface {
    void chooseOptionClicked(OptionItem optionItem) ;
    void foodOrderItemClick(FoodOrder foodOrder, LinearLayout ll_chooseFood);
    void chooseToppingClick(Topping topping);
    void toppingClick(ToppingOrder toppingOrder, FoodOrder foodOrder);
    void chooseFood(Food food);
    void chooseSuborder(SubOrder subOrder);
    void cutFood1(FoodOrder foodOrder);
    void cutFood2(FoodOrder foodOrder);

}
