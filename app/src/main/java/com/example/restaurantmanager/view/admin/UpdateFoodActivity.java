package com.example.restaurantmanager.view.admin;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantmanager.R;
import com.example.restaurantmanager.adapter.FoodAdapter;
import com.example.restaurantmanager.adapter.OptionAdapter;
import com.example.restaurantmanager.adapter.item.OptionItem;
import com.example.restaurantmanager.interfacez.OptionClickInterface;
import com.example.restaurantmanager.realm_object.Category;
import com.example.restaurantmanager.realm_object.Food;
import com.example.restaurantmanager.realm_object.Option;
import com.example.restaurantmanager.realm_object.SubCategory;
import com.example.restaurantmanager.utils.DataUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class UpdateFoodActivity extends BaseAdminActivity implements OptionClickInterface {
    UUID foodId;
    UUID optionId;
    EditText txt_foodName;
    EditText txt_foodPrice;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch sw_foodEnable;
    AlertDialog foodDialog;
    EditText txt_optionName;
    EditText txt_optionPrice;
    RecyclerView mRecyclerview;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch switch_optionEnable;
    ArrayList<OptionItem> optionItems;
    OptionAdapter mAdapter;
    TextView txt_title;
    private List<String> list;
    Spinner catSpinner;
    List<SubCategory> subCategories;
    Spinner spinnerFoodType;


    void findViewById(){
        txt_foodName = findViewById(R.id.txt_food_name);
        txt_foodPrice = findViewById(R.id.txt_food_price);
        sw_foodEnable = findViewById(R.id.sw_food_enable);
        optionItems = new ArrayList<>();
        mRecyclerview = findViewById(R.id.recyclerView_detail);
        catSpinner =   findViewById(R.id.cat_spinner);
        subCategories = new ArrayList<>();
        spinnerFoodType = findViewById(R.id.foodType_spinner);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_food);
        setTitle("Create Food");
        findViewById();
        foodDialog = new AlertDialog.Builder(this).create();
        mAdapter = new OptionAdapter( optionItems,UpdateFoodActivity.this,foodId,this);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerview.setAdapter(mAdapter);
        foodId = (UUID) getIntent().getSerializableExtra("foodId");;
        setListSubCate();
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, DataUtil.FoodTypes);
        spinnerFoodType.setAdapter(arrayAdapter);

        if(foodId !=null){
            realm.beginTransaction();
            final Food modal = realm.where(Food.class).equalTo("id", foodId).findFirst();
            realm.commitTransaction();
            txt_foodName.setText(modal.getName());
            sw_foodEnable.setChecked(modal.getStatus());
            txt_foodPrice.setText(modal.getPrice().toString());
            spinnerFoodType.setSelection(modal.getFoodType());
            if(modal.getSubCategory() !=null)
                chooseCate(modal.getSubCategory().getId());
            setAdapter(modal);
            setTitle("Update food");
        }
        else {
            if(DataUtil.saveSubUID != null){
                chooseCate(DataUtil.saveSubUID);
            }
            if(DataUtil.saveFoodType != 0){
                spinnerFoodType.setSelection(DataUtil.saveFoodType);
            }
            sw_foodEnable.setChecked(true);
            setTitle("Create food");
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
            updateFood();
        }
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean isValidate(){
        return true;
    }
    public void updateFood(){

        if(isValidate()){
            int index = spinnerFoodType.getSelectedItemPosition();
            if (foodId != null){
                dialog.show();
                realm.beginTransaction();
                final Food food = realm.where(Food.class).equalTo("id", foodId).findFirst();
                food.setName( txt_foodName.getText().toString());
                food.setStatus(sw_foodEnable.isChecked());
                String prStr = txt_foodPrice.getText().toString().trim();
                prStr = (prStr.equals(""))?"0":prStr;
                double price = Double.parseDouble(prStr);
                food.setPrice(price);
                SubCategory subCategory = getSubCate();
                DataUtil.saveSubUID =subCategory.getId();
                DataUtil.saveFoodType = food.getFoodType();
                food.setSubCategory(subCategory);
                food.setFoodType(index);
                realm.copyToRealmOrUpdate(food);
                realm.commitTransaction();
                dialog.dismiss();
                Toast.makeText(context, "Successfully created", Toast.LENGTH_SHORT).show();

            }else{
                dialog.show();
                realm.beginTransaction();
                Food food = realm.createObject(Food.class,UUID.randomUUID());
                food.setName( txt_foodName.getText().toString());
                food.setStatus(sw_foodEnable.isChecked());
                String prStr = txt_foodPrice.getText().toString().trim();
                prStr = (prStr.equals(""))?"0":prStr;
                double price = Double.parseDouble(prStr);
                food.setPrice(price);
                SubCategory subCategory = getSubCate();
                DataUtil.saveSubUID =subCategory.getId();
                DataUtil.saveFoodType = food.getFoodType();
                food.setSubCategory(subCategory);
                foodId = food.getId();
                food.setFoodType(index);
                realm.commitTransaction();
                setTitle("Update food");
                dialog.dismiss();
                Toast.makeText(context, "Successfully updated", Toast.LENGTH_SHORT).show();

            }
        }
    }
    public void addOptionClick(View view) {
        if(foodId == null){
            Toast.makeText(context, "You have not added a food\n!", Toast.LENGTH_SHORT).show();
            return;
        }
        LayoutInflater factory = LayoutInflater.from(this);
        final View optionDialogView = factory.inflate(R.layout.popup_update_option, null);
        foodDialog = new AlertDialog.Builder(this).create();
        txt_optionName = optionDialogView.findViewById(R.id.popup_option_name);
        txt_optionPrice = optionDialogView.findViewById(R.id.popup_option_price);
        switch_optionEnable = optionDialogView.findViewById(R.id.popup_option_enable);
        foodDialog.setView(optionDialogView);
        foodDialog.setCancelable(false);
        switch_optionEnable.setChecked(true);

        optionDialogView.findViewById(R.id.popup_btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                foodDialog.dismiss();
            }
        });
        optionDialogView.findViewById(R.id.popup_btn_add).setOnClickListener(saveOption);
        foodDialog.show();
    }
    View.OnClickListener saveOption = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dialog.show();
            transaction = realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    final Food modal = realm.where(Food.class).equalTo("id", foodId).findFirst();
                    Option option = realm.createEmbeddedObject(Option.class,modal,"options");
                    option.setId(UUID.randomUUID());
                    option.setName(txt_optionName.getText().toString());
                    option.setStatus(switch_optionEnable.isChecked());
                    String prStr = txt_optionPrice.getText().toString().trim();
                    prStr = (prStr.equals(""))?"0":prStr;
                    double price = Double.parseDouble(prStr);
                    option.setPrice(price);
                    setAdapter(modal);
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    dialog.dismiss();
                    Toast.makeText(context, "Successfully created", Toast.LENGTH_SHORT).show();
                    foodDialog.dismiss();
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {
                    dialog.dismiss();
                    Log.e("myapp",Log.getStackTraceString(error));
                    Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show();
                }
            });

        }
    };
    void setAdapter(Food food){
        optionItems.clear();
        for (Option option:food.getOptions()){
            optionItems.add(new OptionItem(option.getId(),option.getName()
                    ,option.getPrice(),option.getStatus()));
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onDeleteClicked(UUID uuid) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Confirm");
        b.setMessage("Are you sure delete?");
        b.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                realm.beginTransaction();
                final Food modal = realm.where(Food.class).equalTo("id", foodId).findFirst();
                for(int i = 0 ; i< modal.getOptions().size();i++ ){
                    Option option = modal.getOptions().get(i);
                    if(option.getId().equals(uuid)){
                        modal.getOptions().remove(option);
                        i--;
                    }
                }
                realm.commitTransaction();
                setAdapter(modal);
            }
        });
        b.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog al = b.create();
        al.show();
    }

    @Override
    public void onUpdateOptionClicked(OptionItem optionItem) {
        LayoutInflater factory = LayoutInflater.from(this);
        final View optionDialogView = factory.inflate(R.layout.popup_update_option, null);
        foodDialog = new AlertDialog.Builder(this).create();
        txt_optionName = optionDialogView.findViewById(R.id.popup_option_name);
        txt_optionPrice = optionDialogView.findViewById(R.id.popup_option_price);
        switch_optionEnable = optionDialogView.findViewById(R.id.popup_option_enable);
        txt_title = optionDialogView.findViewById(R.id.tv_title_popup);

        //set data
        txt_title.setText("Update option");
        txt_optionName.setText(optionItem.name);
        switch_optionEnable.setChecked(optionItem.status);
        txt_optionPrice.setText(optionItem.price.toString());
        optionId = optionItem.id;

        foodDialog.setView(optionDialogView);
        foodDialog.setCancelable(false);

        optionDialogView.findViewById(R.id.popup_btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                foodDialog.dismiss();
            }
        });

        optionDialogView.findViewById(R.id.popup_btn_add).setOnClickListener(updateOption);

        foodDialog.show();
    }

    @Override
    public void onChangStatusOptionClicked(OptionItem optionItem) {
        realm.beginTransaction();
        final Food modal = realm.where(Food.class).equalTo("id", foodId).findFirst();
        for(int i = 0 ; i< modal.getOptions().size();i++ ){
            Option option = modal.getOptions().get(i);
            if(option.getId().equals(optionItem.id)){
                option.setStatus(!option.getStatus());
            }
        }
        realm.commitTransaction();
        setAdapter(modal);
    }

    View.OnClickListener updateOption = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            dialog.show();
            transaction = realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    final Food modal = realm.where(Food.class).equalTo("id", foodId).findFirst();
                    for(int i = 0 ; i< modal.getOptions().size();i++ ){
                        Option option = modal.getOptions().get(i);
                        if(option.getId().equals(optionId)){
                            option.setName(txt_optionName.getText().toString());
                            option.setStatus(switch_optionEnable.isChecked());
                            option.setPrice(Double.parseDouble(txt_optionPrice.getText().toString()));

                            break;
                        }
                    }
                    setAdapter(modal);
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    dialog.dismiss();
                    Toast.makeText(context, "Successfully created", Toast.LENGTH_SHORT).show();
                    foodDialog.dismiss();
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {
                    dialog.dismiss();
                    Log.e("myapp",Log.getStackTraceString(error));
                    Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show();
                }
            });

        }
    };

    private void setListSubCate(){
        list = new ArrayList<>();
        RealmResults<Category> categorys = realm.where(Category.class)
                .sort("name", Sort.ASCENDING)
                .equalTo("status",true)
                .findAll();
        for (Category category:categorys) {
            for (SubCategory subCategory: category.getSubCategories()) {
               if (subCategory.getStatus() == true){
                   subCategories.add(subCategory);
               }
            }
        }
        int i = 0;
        for (SubCategory subCategory:subCategories) {
            list.add(i++ +"-"+ subCategory.getName());
        }

        ArrayAdapter spinnerAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, list);

        catSpinner.setAdapter(spinnerAdapter);

    }
    private SubCategory getSubCate(){
        int index = Integer.parseInt(catSpinner.getSelectedItem().toString().substring(0,catSpinner.getSelectedItem().toString().indexOf('-')));
        return subCategories.get(index);
    }
    private void chooseCate(UUID uuid){

        for (SubCategory subCategory:subCategories) {
            if(subCategory.getId().equals(uuid)){
                int index = subCategories.indexOf(subCategory);
                catSpinner.setSelection(index);
                break;
            }
        }
    }


}