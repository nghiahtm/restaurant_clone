package com.example.restaurantmanager.interfacez;

import com.example.restaurantmanager.realm_object.Food;

import java.util.UUID;

public interface FoodClickInterface {
    void onDeleteClicked(UUID id) ;
    void viewDetailClick(UUID id) ;
    void viewChangStatusClick(Food food) ;

}
