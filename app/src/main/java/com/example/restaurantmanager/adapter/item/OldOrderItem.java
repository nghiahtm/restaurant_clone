package com.example.restaurantmanager.adapter.item;

import com.example.restaurantmanager.enums.MoreMenuEnum;
import com.example.restaurantmanager.realm_object.Table;
import com.example.restaurantmanager.realm_object.order.SubOrder;

import java.util.Date;
import java.util.UUID;

import io.realm.RealmList;

public class OldOrderItem {
    UUID id;
    String tableName;
    Date payTime;
    boolean isChoose;
    int status;

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    Double total;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
