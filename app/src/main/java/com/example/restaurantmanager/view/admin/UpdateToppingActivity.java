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
import com.example.restaurantmanager.realm_object.Food;
import com.example.restaurantmanager.realm_object.Topping;

import java.util.UUID;

import io.realm.Realm;

public class UpdateToppingActivity extends BaseAdminActivity {
    UUID toppingId;
    EditText txt_toppingName;
    EditText txt_toppingPrice;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch sw_toppingEnable;

    void findViewById(){
        txt_toppingName = findViewById(R.id.txt_topping_name);
        txt_toppingPrice = findViewById(R.id.txt_topping_price);
        sw_toppingEnable = findViewById(R.id.sw_topping_enable);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_topping);

        findViewById();
        toppingId = (UUID) getIntent().getSerializableExtra("toppingId");;

        if(toppingId !=null){
            realm.beginTransaction();
            final Topping modal = realm.where(Topping.class).equalTo("id", toppingId).findFirst();
            realm.commitTransaction();
            txt_toppingName.setText(modal.getName());
            sw_toppingEnable.setChecked(modal.getStatus());
            txt_toppingPrice.setText(modal.getPrice().toString());
            setTitle("Update topping");
        }
        else {
            sw_toppingEnable.setChecked(true);
            setTitle("Create topping");
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
            updateFoodTopping();
        }
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean isValidate(){
        return true;
    }
    public void updateFoodTopping(){
        if(isValidate()){
            if (toppingId != null){
                realm.beginTransaction();
                final Topping topping = realm.where(Topping.class).equalTo("id", toppingId).findFirst();
                topping.setName( txt_toppingName.getText().toString());
                topping.setStatus(sw_toppingEnable.isChecked());
                String prStr = txt_toppingPrice.getText().toString().trim();
                prStr = (prStr.equals(""))?"0":prStr;
                double price = Double.parseDouble(prStr);
                topping.setPrice(price);
                realm.copyToRealmOrUpdate(topping);
                realm.commitTransaction();
                finish();
            }else{
                dialog.show();
                transaction = realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Topping topping = realm.createObject(Topping.class,UUID.randomUUID());
                        topping.setName( txt_toppingName.getText().toString());
                        topping.setStatus(sw_toppingEnable.isChecked());
                        String prStr = txt_toppingPrice.getText().toString().trim();
                        prStr = (prStr.equals(""))?"0":prStr;
                        double price = Double.parseDouble(prStr);
                        topping.setPrice(price);
                        toppingId = topping.getId();

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
                        toppingId = null;
                        Log.e("myapp",Log.getStackTraceString(error));
                        Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}