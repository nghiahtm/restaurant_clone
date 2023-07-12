package com.example.restaurantmanager.realm_object;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

@RealmClass(embedded=true)
public class PrinterIPAdress extends RealmObject {
    String IP;
    String name;
    int port;

    public PrinterIPAdress() {
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
