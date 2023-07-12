package com.example.restaurantmanager.utils;

import com.example.restaurantmanager.enums.UndoEnum;
import com.example.restaurantmanager.realm_object.Topping;
import com.example.restaurantmanager.realm_object.order.FoodOrder;
import com.example.restaurantmanager.realm_object.order.SubOrder;
import com.example.restaurantmanager.realm_object.order.ToppingOrder;

public class UndoItem {
    public UndoEnum undoEnum;
    public FoodOrder foodOrder;
    public ToppingOrder toppingOrder;
    public SubOrder subOrder;
}
