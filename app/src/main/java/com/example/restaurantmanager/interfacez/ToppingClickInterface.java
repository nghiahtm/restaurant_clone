package com.example.restaurantmanager.interfacez;

import com.example.restaurantmanager.realm_object.Topping;

import java.util.UUID;

public interface ToppingClickInterface {
    void onDeleteClicked(UUID id) ;
    void onChangStatusClicked(Topping topping) ;
    void viewDetailClick(UUID id) ;


}
