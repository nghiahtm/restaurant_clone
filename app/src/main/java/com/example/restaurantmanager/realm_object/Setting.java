package com.example.restaurantmanager.realm_object;

import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Setting extends RealmObject {


    PrinterIPAdress billPrinter;
    PrinterIPAdress kuchePrinter;
    PrinterIPAdress sushiPrinter;
    PrinterIPAdress getrankPrinter;
    String adminPassword;

    public Double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Double taxRate) {
        this.taxRate = taxRate;
    }

    Double taxRate = 0.0;

    public Setting() {
    }


    @PrimaryKey
    UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    public void setFoodPrinters(RealmList<PrinterIPAdress> foodPrinters) {
    }

    public PrinterIPAdress getBillPrinter() {
        return billPrinter;
    }

    public void setBillPrinter(PrinterIPAdress billPrinter) {
        this.billPrinter = billPrinter;
    }

    public PrinterIPAdress getKuchePrinter() {
        return kuchePrinter;
    }

    public void setKuchePrinter(PrinterIPAdress kuchePrinter) {
        this.kuchePrinter = kuchePrinter;
    }

    public PrinterIPAdress getSushiPrinter() {
        return sushiPrinter;
    }

    public void setSushiPrinter(PrinterIPAdress sushiPrinter) {
        this.sushiPrinter = sushiPrinter;
    }

    public PrinterIPAdress getGetrankPrinter() {
        return getrankPrinter;
    }

    public void setGetrankPrinter(PrinterIPAdress getrankPrinter) {
        this.getrankPrinter = getrankPrinter;
    }


    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

}