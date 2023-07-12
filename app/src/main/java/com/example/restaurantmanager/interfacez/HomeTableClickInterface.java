package com.example.restaurantmanager.interfacez;

import com.example.restaurantmanager.adapter.item.OldOrderItem;
import com.example.restaurantmanager.adapter.item.TableItem;
import com.example.restaurantmanager.realm_object.order.FoodOrder;
import com.example.restaurantmanager.realm_object.order.Order;

import java.util.UUID;

public interface HomeTableClickInterface {
    void chooseTable(TableItem tableItem) ;
    void showNote(TableItem tableItem) ;
    void foodOrderClick(FoodOrder foodOrder);
    void chooseOrder(OldOrderItem OldOrderItem);
}
