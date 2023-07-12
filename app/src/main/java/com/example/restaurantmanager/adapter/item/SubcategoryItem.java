package com.example.restaurantmanager.adapter.item;

import java.util.UUID;

public class SubcategoryItem {
    public UUID id;
    public String name;
    public int color;
    public Boolean status;

    public SubcategoryItem(UUID id, String name, int color, Boolean status) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.status = status;
    }
}
