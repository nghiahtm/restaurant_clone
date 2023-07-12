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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantmanager.R;
import com.example.restaurantmanager.adapter.SubcategoryAdapter;
import com.example.restaurantmanager.adapter.item.SubcategoryItem;
import com.example.restaurantmanager.interfacez.CategoryUpdateClickInterface;
import com.example.restaurantmanager.realm_object.Category;
import com.example.restaurantmanager.realm_object.Food;
import com.example.restaurantmanager.realm_object.SubCategory;
import com.example.restaurantmanager.utils.DataUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import io.realm.Realm;
import yuku.ambilwarna.AmbilWarnaDialog;

public class UpdateCategoryActivity extends BaseAdminActivity implements CategoryUpdateClickInterface {
    TextView txt_categoryName;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch switch_Enable;
    ImageView btn_addSubcategory;
    UUID categoryId;
    UUID subcategoryId;
    TextView txt_subcategoryName;
    Switch switch_subcategoryEnable;
    TextView txt_subcategoryColor;
    AlertDialog subCateDialog;
    TextView txt_title;
    int chooseColor;
    SubcategoryAdapter mAdapter;
    private RecyclerView mRecyclerview;
    List<SubcategoryItem> subcategoryItems;


    private void findViewById() {
        txt_categoryName = findViewById(R.id.category_name);
        switch_Enable = findViewById(R.id.enable_category);
        btn_addSubcategory = findViewById(R.id.btn_add_category);
        chooseColor = R.color.subcateColor;
        mRecyclerview = findViewById(R.id.recyclerView_detail);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_category);
        findViewById();
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        categoryId = (UUID) getIntent().getSerializableExtra("categoryId");;
        subcategoryItems = new ArrayList<SubcategoryItem>() ;
        mAdapter = new SubcategoryAdapter( subcategoryItems,UpdateCategoryActivity.this,categoryId,this);
        mRecyclerview.setAdapter(mAdapter);

        if(categoryId != null){
            realm.beginTransaction();
            final Category modal = realm.where(Category.class).equalTo("id", categoryId).findFirst();
            realm.commitTransaction();
            txt_categoryName.setText(modal.getName());
            switch_Enable.setChecked(modal.getStatus());
            setAdapter(modal);
            setTitle("Update category");
        }else {
            setTitle("Create category");
            switch_Enable.setChecked(true);
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
            updateCategory();
        }
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateCategory(){
        if(isValidate()){
            if(categoryId != null){
                dialog.show();
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        final Category modal = realm.where(Category.class).equalTo("id", categoryId).findFirst();
                        modal.setName(txt_categoryName.getText().toString());
                        modal.setStatus(switch_Enable.isChecked());
                        realm.copyToRealmOrUpdate(modal);
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        dialog.dismiss();
                        Toast.makeText(context, "Successfully updated", Toast.LENGTH_SHORT).show();
//                    finish();
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
            else {
                dialog.show();
                transaction = realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Category category = realm.createObject(Category.class,UUID.randomUUID());
                        String catName = txt_categoryName.getText().toString();
                        category.setName(catName);
                        Boolean bl = switch_Enable.isChecked();
                        category.setStatus(bl);
                        categoryId =category.getId();
                        setAdapter(category);
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        setTitle("Update category");
                        dialog.dismiss();
                        Toast.makeText(context, "Successfully created", Toast.LENGTH_SHORT).show();
                    }
                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {
                        dialog.dismiss();
                        categoryId = null;
                        Log.e("myapp",Log.getStackTraceString(error));
                        Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

    }
    public boolean isValidate(){
        return true;
    }
    @SuppressLint("ResourceAsColor")
    public void addsubcategory(View view){
        if(categoryId == null){
            Toast.makeText(context, "You have not added a category\n!", Toast.LENGTH_SHORT).show();
            return;
        }
        LayoutInflater factory = LayoutInflater.from(this);
        final View catDialogView = factory.inflate(R.layout.popup_update_subcatetory, null);
        subCateDialog = new AlertDialog.Builder(this).create();
        txt_subcategoryName = catDialogView.findViewById(R.id.name_subcategory_popup);
        switch_subcategoryEnable = catDialogView.findViewById(R.id.enable_subcategory);
        txt_subcategoryColor = catDialogView.findViewById(R.id.txt_subcategory_color);
        txt_subcategoryColor.setBackgroundColor(R.color.subcateColor);
        subCateDialog.setView(catDialogView);
        subCateDialog.setCancelable(false);
        txt_subcategoryColor.setOnClickListener(colorLick);
        switch_subcategoryEnable.setChecked(true);


        catDialogView.findViewById(R.id.cancel_btn_popup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subCateDialog.dismiss();
            }
        });
        catDialogView.findViewById(R.id.add_btn_popup).setOnClickListener(saveSubcategory);

        subCateDialog.show();

    }
    View.OnClickListener colorLick = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            AmbilWarnaDialog colorDialog = new AmbilWarnaDialog(getContext(), R.color.subcateColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                @Override
                public void onOk(AmbilWarnaDialog dialog, int color) {
                    chooseColor = color;
                    txt_subcategoryColor.setBackgroundColor(color);

                }
                @Override
                public void onCancel(AmbilWarnaDialog dialog) {
                }
            });

            colorDialog.show();
        }
    };

    View.OnClickListener saveSubcategory = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            dialog.show();
            transaction = realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    final Category modal = realm.where(Category.class).equalTo("id", categoryId).findFirst();
                    SubCategory subCategory = realm.createEmbeddedObject(SubCategory.class,modal,"subCategories");
                    subCategory.setId(UUID.randomUUID());
                    subCategory.setName(txt_subcategoryName.getText().toString());
                    subCategory.setStatus(switch_subcategoryEnable.isChecked());
                    subCategory.setColor(chooseColor);
                    setAdapter(modal);
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    dialog.dismiss();
                    Toast.makeText(context, "Successfully created", Toast.LENGTH_SHORT).show();
                    subCateDialog.dismiss();
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
    void setAdapter(Category category){
        subcategoryItems.clear();
        for (SubCategory subCategory:category.getSubCategories()){
            subcategoryItems.add(new SubcategoryItem(subCategory.getId(),subCategory.getName()
                    ,subCategory.getColor(),subCategory.getStatus()));
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
                final Category modal = realm.where(Category.class).equalTo("id", categoryId).findFirst();
                for(int i = 0 ; i< modal.getSubCategories().size();i++ ){
                    SubCategory sub = modal.getSubCategories().get(i);
                    if(sub.getId().equals(uuid)){
                        modal.getSubCategories().remove(sub);
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

    @SuppressLint("ResourceAsColor")
    @Override
    public void onUpdateSubcategoryClicked(SubcategoryItem subCategory) {
        LayoutInflater factory = LayoutInflater.from(this);
        final View catDialogView = factory.inflate(R.layout.popup_update_subcatetory, null);
        subCateDialog = new AlertDialog.Builder(this).create();
        txt_subcategoryName = catDialogView.findViewById(R.id.name_subcategory_popup);
        switch_subcategoryEnable = catDialogView.findViewById(R.id.enable_subcategory);
        txt_subcategoryColor = catDialogView.findViewById(R.id.txt_subcategory_color);
        txt_title = catDialogView.findViewById(R.id.tv_title_popup);

        //set data
        txt_title.setText("Update subcategory");
        txt_subcategoryName.setText(subCategory.name);
        switch_subcategoryEnable.setChecked(subCategory.status);
        txt_subcategoryColor.setBackgroundColor(subCategory.color);
        subcategoryId = subCategory.id;
        chooseColor = subCategory.color;

        subCateDialog.setView(catDialogView);
        subCateDialog.setCancelable(false);
        txt_subcategoryColor.setOnClickListener(colorLick);

        catDialogView.findViewById(R.id.cancel_btn_popup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subCateDialog.dismiss();
            }
        });

        catDialogView.findViewById(R.id.add_btn_popup).setOnClickListener(updateSubcategory);

        subCateDialog.show();
    }

    @Override
    public void viewChangStatusClick(SubcategoryItem subCategory) {
        realm.beginTransaction();
        final Category modal = realm.where(Category.class).equalTo("id", categoryId).findFirst();
        for(int i = 0 ; i< modal.getSubCategories().size();i++ ){
            SubCategory sub = modal.getSubCategories().get(i);
            if(sub.getId().equals(subCategory.id)){
                sub.setStatus(!sub.getStatus());
            }
        }
        realm.commitTransaction();
        setAdapter(modal);
    }

    View.OnClickListener updateSubcategory = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            dialog.show();
            transaction = realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    final Category modal = realm.where(Category.class).equalTo("id", categoryId).findFirst();
                    for(int i = 0 ; i< modal.getSubCategories().size();i++ ){
                        SubCategory subCategory = modal.getSubCategories().get(i);
                        if(subCategory.getId().equals(subcategoryId)){
                            subCategory.setName(txt_subcategoryName.getText().toString());
                            subCategory.setStatus(switch_subcategoryEnable.isChecked());
                            subCategory.setColor(chooseColor);
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
                    subCateDialog.dismiss();
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
}
