package com.example.restaurantmanager.interfacez;

import com.example.restaurantmanager.realm_object.Table;

import java.util.UUID;

public interface TableClickInterface {
    void onDeleteClicked(UUID id) ;
    void viewDetailClick(UUID id) ;
    void onChangStatusClicked(Table table) ;


}
