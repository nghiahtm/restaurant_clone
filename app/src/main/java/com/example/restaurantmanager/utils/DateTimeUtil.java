package com.example.restaurantmanager.utils;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtil {
    public static String getCurrentLocalDateTimeStamp() {
        return LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }
    public static String getCurrentLocalDateStamp() {
        return LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }
    public static String formatDateTime(Date date){
        Format formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String s = formatter.format(date);
        return s;
    }
    public static String formatTime(Date date){
        Format formatter = new SimpleDateFormat("HH:mm:ss");
        String s = formatter.format(date);
        return s;
    }
    public static String formatDate(Date date){
        Format formatter = new SimpleDateFormat("dd-MM-yyyy");
        String s = formatter.format(date);
        return s;
    }
    public static Date getStartOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
    public static Date getEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }
}
