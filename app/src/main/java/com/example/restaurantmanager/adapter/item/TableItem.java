package com.example.restaurantmanager.adapter.item;

import java.util.UUID;

import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;

public class TableItem {
    String name;
    boolean status;
    boolean isHaveOrder;


    String note;
    Double price;
    UUID id;


    public TableItem() {
    }

    public TableItem(String name, boolean status, boolean isHaveOrder, Double price, UUID id) {
        this.name = name;
        this.status = status;
        this.isHaveOrder = isHaveOrder;
        this.price = price;
        this.id = id;
    }


    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isHaveOrder() {
        return isHaveOrder;
    }

    public void setHaveOrder(boolean haveOrder) {
        isHaveOrder = haveOrder;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}

