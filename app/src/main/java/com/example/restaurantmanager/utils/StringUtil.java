package com.example.restaurantmanager.utils;

public class StringUtil {
    public static String subName(String name,int size){
        if (name.length() > size){
            return name.substring(0,size-3)+"...";
        }
        return  name;
    }
}
