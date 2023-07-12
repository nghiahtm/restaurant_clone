package com.example.restaurantmanager.realm_object.order;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

@RealmClass(embedded=true)
public class SubOrder extends RealmObject {
    UUID id;
    RealmList<FoodOrder> foodOrders;
    Double totalBill = 0.0;

    public Double getTotalBill(){
        Double total = 0.0;
        for (FoodOrder foodOrder: foodOrders) {
            total+= foodOrder.getTotalPrice();
        }
        totalBill = total;
        return  total;
    }
    public List<FoodOrder> getListFoodByType(int type){
        List<FoodOrder> mfoodOrders = new ArrayList<>();
        for(FoodOrder mfoodOrder:this.foodOrders){
            if(mfoodOrder.getFoodType() == type){
                mfoodOrders.add(mfoodOrder);
            }
        }
        return mfoodOrders;
    }
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public RealmList<FoodOrder> getFoodOrders() {
        return foodOrders;
    }

    public void setFoodOrders(RealmList<FoodOrder> foodOrders) {
        this.foodOrders = foodOrders;
    }
}
