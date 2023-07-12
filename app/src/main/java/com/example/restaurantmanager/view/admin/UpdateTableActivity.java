package com.example.restaurantmanager.view.admin;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.restaurantmanager.R;
import com.example.restaurantmanager.realm_object.Table;
import com.example.restaurantmanager.realm_object.Topping;

import java.util.UUID;

import io.realm.Realm;

public class UpdateTableActivity extends BaseAdminActivity {
    UUID tableId;
    EditText txt_tableName;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch sw_tableEnable;

    void findViewById(){
        txt_tableName = findViewById(R.id.txt_table_name);
        sw_tableEnable = findViewById(R.id.sw_table_enable);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_table);
        findViewById();
        tableId = (UUID) getIntent().getSerializableExtra("tableId");;
        if(tableId !=null){
            realm.beginTransaction();
            final Table modal = realm.where(Table.class).equalTo("id", tableId).findFirst();
            realm.commitTransaction();
            txt_tableName.setText(modal.getName());
            sw_tableEnable.setChecked(modal.getStatus());
            setTitle("Update table");
        }
        else {
            sw_tableEnable.setChecked(true);
            setTitle("Create table");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_update, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_update) {
            updateTable();
        }
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean isValidate(){
        return true;
    }
    public void updateTable(){
        if(isValidate()){
            if (tableId != null){
                realm.beginTransaction();
                final Table table = realm.where(Table.class).equalTo("id", tableId).findFirst();
                table.setName( txt_tableName.getText().toString());
                table.setStatus(sw_tableEnable.isChecked());
                realm.copyToRealmOrUpdate(table);
                realm.commitTransaction();
                finish();
            }else{
                dialog.show();
                transaction = realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Table table = realm.createObject(Table.class,UUID.randomUUID());
                        table.setName( txt_tableName.getText().toString());
                        table.setStatus(sw_tableEnable.isChecked());
                        tableId = table.getId();

                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        dialog.dismiss();
                        finish();
                        Toast.makeText(context, "Successfully created", Toast.LENGTH_SHORT).show();
                    }
                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {
                        dialog.dismiss();
                        tableId = null;
                        Log.e("myapp",Log.getStackTraceString(error));
                        Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}