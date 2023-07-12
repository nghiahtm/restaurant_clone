package com.example.restaurantmanager.utils;

import java.util.UUID;

public class DataUtil {
    public static UUID saveSubUID = null;
    public static int saveFoodType = 0;

    public static UUID unManageOrderId = new UUID(2004,2000);
    public static UUID unManageOrderId1 = new UUID(3004,1975);
    public static UUID unManageOrderId2 = new UUID(1905,1890);
    public static UUID settingId = new UUID(3004,1975);
    public static UUID unManageTable = new UUID(209,1945);

    public static UUID unManageFoodLog = new UUID(209,1945);

    public static String[] FoodTypes = {"Kueche", "Sushi", "Getränk","Vor - Kueche","Vor - Sushi"};
    public static int printerNbrCharactersPerLine = 42;

    public static int CashNumber = 0;

    public static int CardNumber = 1;

    public static int PaypalNumber = 2;
    public static String getPaymentMethod(int n){
        if(n == CashNumber){
            return  "Cash";
        }
        if(n == CardNumber){
            return  "Card";
        }
        if(n == PaypalNumber){
            return  "Paypal";
        }
        return "Money";
    }

}
