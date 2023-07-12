package com.example.restaurantmanager.view.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.example.restaurantmanager.R;
import com.example.restaurantmanager.adapter.TableAdapter;
import com.example.restaurantmanager.adapter.ToppingAdapter;
import com.example.restaurantmanager.interfacez.TableClickInterface;
import com.example.restaurantmanager.realm_object.Table;
import com.example.restaurantmanager.realm_object.Topping;
import com.example.restaurantmanager.realm_object.order.Order;

import java.util.UUID;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class TableActivity extends BaseAdminActivity implements TableClickInterface {
    private RecyclerView mRecyclerview;
    private TableAdapter mAdapter;
    RealmResults<Table> tables ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        setActionbarTitle("Tables");
        mRecyclerview = findViewById(R.id.recyclerView_detail);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));

        tables = realm.where(Table.class)
                .sort("name", Sort.ASCENDING)
                .findAll();
        mAdapter = new TableAdapter( tables,true, context, this);
        mRecyclerview.setAdapter(mAdapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_object, menu);
        final MenuItem addItem = menu.findItem(R.id.menu_item_add);
        final MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search your table...");
        searchView.setMaxWidth(Integer.MAX_VALUE);
        addItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(context, UpdateTableActivity.class);
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
                tables = realm.where(Table.class)
                        .contains("name",s, Case.INSENSITIVE)
                        .sort("name", Sort.ASCENDING)
                        .findAll();
                mAdapter = new TableAdapter( tables,true, context, TableActivity.this);
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
                realm.beginTransaction();
                final Table modal = realm.where(Table.class).equalTo("id", uuid).findFirst();
                final RealmResults<Order> orders = realm.where(Order.class).equalTo("table.id", uuid).findAll();
                if(orders.size() > 0){
                    new AlertDialog.Builder(context)
                            .setTitle("Error")
                            .setMessage("Can't delete a table that already has an order!")
                            .show();
                }
                else {
                    modal.deleteFromRealm();
                }
                realm.commitTransaction();
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
        Intent intent = new Intent(context, UpdateTableActivity.class);
        intent.putExtra("tableId", id);
        context.startActivity(intent);
    }

    @Override
    public void onChangStatusClicked(Table table) {
        realm.beginTransaction();
        table.setStatus(!table.getStatus());
        realm.commitTransaction();
    }
}