package com.example.restaurantmanager.view.admin;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import com.example.restaurantmanager.R;
import com.example.restaurantmanager.adapter.CategoryAdapter;
import com.example.restaurantmanager.adapter.FoodAdapter;
import com.example.restaurantmanager.interfacez.FoodClickInterface;
import com.example.restaurantmanager.realm_object.Category;
import com.example.restaurantmanager.realm_object.Food;

import java.util.UUID;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class FoodActivity extends BaseAdminActivity implements FoodClickInterface {
    private RecyclerView mRecyclerview;
    private FoodAdapter mAdapter;
    RealmResults<Food> foods ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        setActionbarTitle("Foods");
        mRecyclerview = findViewById(R.id.recyclerView_detail);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));

        foods = realm.where(Food.class)
                .sort("name", Sort.ASCENDING)
                .findAll();
        mAdapter = new FoodAdapter( foods,true, context, this);
        mRecyclerview.setAdapter(mAdapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_object, menu);
        final MenuItem addItem = menu.findItem(R.id.menu_item_add);
        final MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search your food...");
        searchView.setMaxWidth(Integer.MAX_VALUE);
        addItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(context, UpdateFoodActivity.class);
                context.startActivity(intent);
                return true;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                foods = realm.where(Food.class)
                        .contains("name",s, Case.INSENSITIVE)
                        .sort("name", Sort.ASCENDING)
                        .findAll();
                mAdapter = new FoodAdapter( foods,true, context, FoodActivity.this);
                mRecyclerview.setAdapter(mAdapter);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDeleteClicked(UUID uuid) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Confirm");
        b.setMessage("Are you sure delete?");
        b.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                transaction = realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        final Food modal = realm.where(Food.class).equalTo("id", uuid).findFirst();
                        assert modal != null;
                        modal.deleteFromRealm();
                    }
                });
                dialog.cancel();
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
    public void viewDetailClick(UUID id) {
        Intent intent = new Intent(context, UpdateFoodActivity.class);
        intent.putExtra("foodId", id);
        context.startActivity(intent);
    }

    @Override
    public void viewChangStatusClick(Food food) {
        realm.beginTransaction();
        food.setStatus(!food.getStatus());
        realm.commitTransaction();
    }


}