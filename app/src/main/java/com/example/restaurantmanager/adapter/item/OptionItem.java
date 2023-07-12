package com.example.restaurantmanager.adapter.item;

import java.util.UUID;

public class OptionItem {
    public UUID id;
    public String name;
    public Double price;
    public boolean status;

    public OptionItem(UUID id, String name, Double price,boolean status) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.status = status;
    }
}
