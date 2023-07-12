package com.example.restaurantmanager.interfacez;

import com.example.restaurantmanager.adapter.item.OptionItem;

import java.util.UUID;

public interface OptionClickInterface {
    void onDeleteClicked(UUID id) ;
    void onUpdateOptionClicked(OptionItem optionItem) ;
    void onChangStatusOptionClicked(OptionItem optionItem) ;


}
