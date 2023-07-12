package com.example.restaurantmanager.realm_object;

import com.example.restaurantmanager.realm_object.order.Order;
import com.example.restaurantmanager.realm_object.order.SubOrder;

import java.util.Date;
import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class FoodLog  extends RealmObject {
    @PrimaryKey
    UUID id;
    SubOrder order;
    Table table;
    Date removeTime;
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public SubOrder getOrder() {
        return order;
    }

    public void setOrder(SubOrder order) {
        this.order = order;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public Date getRemoveTime() {
        return removeTime;
    }

    public void setRemoveTime(Date removeTime) {
        this.removeTime = removeTime;
    }






}
