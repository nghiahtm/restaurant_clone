package com.example.restaurantmanager.utils;

import java.util.Date;
import java.util.UUID;

import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

public class RealmMigrations implements RealmMigration {

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        final RealmSchema schema = realm.getSchema();

        if(oldVersion == 0){
            RealmObjectSchema fLog =
                    schema.get("FoodLog");

            if(fLog==null){
                fLog=schema.create("FoodLog");}
            else {
                schema.remove("FoodLog");
                fLog=schema.create("FoodLog");

            }
            RealmObjectSchema order =
                    schema.get("SubOrder");
            RealmObjectSchema table =
                    schema.get("Table");
            fLog.addField("id",UUID.class,FieldAttribute.PRIMARY_KEY);
            fLog.addRealmObjectField("order", order);
            fLog.addRealmObjectField("table", table);
            fLog.addField("removeTime", Date.class);
            oldVersion++;
        }
    }
}

