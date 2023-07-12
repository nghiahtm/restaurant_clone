package com.example.restaurantmanager.interfacez;

import com.example.restaurantmanager.adapter.item.SubcategoryItem;
import com.example.restaurantmanager.realm_object.SubCategory;

import java.util.UUID;

public interface CategoryUpdateClickInterface {
    void onDeleteClicked(UUID id) ;
    void onUpdateSubcategoryClicked(SubcategoryItem subCategory) ;
    void viewChangStatusClick(SubcategoryItem subCategory) ;
}
