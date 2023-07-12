package com.example.restaurantmanager.interfacez;

import com.example.restaurantmanager.realm_object.Category;
import com.example.restaurantmanager.realm_object.SubCategory;

import java.util.UUID;

public interface CategoryClickInterface {
    void onDeleteClicked(UUID id) ;
    void viewDetailClick(UUID id) ;
    void viewChangStatusClick(Category category) ;


}
