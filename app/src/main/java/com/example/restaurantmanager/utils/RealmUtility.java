package com.example.restaurantmanager.utils;

import io.realm.RealmConfiguration;

public class RealmUtility {
    private static final int SCHEMA_V_PREV = 1;// previous schema version
    private static final int SCHEMA_V_NOW = 2;// change schema version if any change happened in schema


    public static int getSchemaVNow() {
        return SCHEMA_V_NOW;
    }


    public static RealmConfiguration getDefaultConfig() {
        return new RealmConfiguration.Builder()
                .schemaVersion(1)
//                .migration(new RealmMigrations())
                .build();
    }
}
