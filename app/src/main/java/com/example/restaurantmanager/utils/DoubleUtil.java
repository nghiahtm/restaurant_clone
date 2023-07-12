package com.example.restaurantmanager.utils;

import android.annotation.SuppressLint;

import java.text.DecimalFormat;

public class DoubleUtil {
    public static Double Round2(Double d){
        DecimalFormat df = new DecimalFormat("#0.##");
        return Double.parseDouble( df.format(d));
    }
    @SuppressLint("DefaultLocale")
    public static String Round2String(Double d){
        DecimalFormat df = new DecimalFormat("#0.##");
        Double dub =  Double.parseDouble( df.format(d));
        return  String.format( "%.2f", dub );
    }
}
