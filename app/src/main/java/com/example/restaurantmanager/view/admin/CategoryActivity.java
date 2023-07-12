package com.example.restaurantmanager.view.admin;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.restaurantmanager.R;
import com.example.restaurantmanager.adapter.CategoryAdapter;
import com.example.restaurantmanager.interfacez.CategoryClickInterface;
import com.example.restaurantmanager.realm_object.Category;

import java.util.UUID;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class CategoryActivity extends BaseAdminActivity implements CategoryClickInterface {
    private RecyclerView mRecyclerview;
    private CategoryAdapter mAdapter;
    RealmResults<Category> categories ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        setActionbarTitle(getString(R.string.category_title));
        mRecyclerview = findViewById(R.id.recyclerView_detail);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));

        categories = realm.where(Category.class)
                .sort("name", Sort.ASCENDING)
                .findAll();
        mAdapter = new CategoryAdapter( categories,true, context, this);
        mRecyclerview.setAdapter(mAdapter);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_object, menu);
        final MenuItem barCodeItem = menu.findItem(R.id.menu_item_add);
        final MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search your category...");
        searchView.setMaxWidth(Integer.MAX_VALUE);
        barCodeItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(context, UpdateCategoryActivity.class);
                context.startActivity(intent);
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                categories = realm.where(Category.class)
                        .contains("name",s, Case.INSENSITIVE)
                        .sort("name", Sort.ASCENDING)
                        .findAll();
                mAdapter = new CategoryAdapter( categories,true, context, CategoryActivity.this);
                mRecyclerview.setAdapter(mAdapter);
                return false;
            }
        });
        
        return true;
    }
    @Override
    public void onDeleteClicked(UUID uuid){
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Confirm");
        b.setMessage("Are you sure delete?");
        b.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                transaction = realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        final Category modal = realm.where(Category.class).equalTo("id", uuid).findFirst();
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
        Intent intent = new Intent(context, UpdateCategoryActivity.class);
        intent.putExtra("categoryId", id);
        context.startActivity(intent);
    }

    @Override
    public void viewChangStatusClick(Category category) {
        realm.beginTransaction();
        category.setStatus(!category.getStatus());
        realm.commitTransaction();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}