package com.example.restaurantmanager.view.admin;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dantsu.escposprinter.EscPosCharsetEncoding;
import com.dantsu.escposprinter.EscPosPrinter;
import com.dantsu.escposprinter.connection.tcp.TcpConnection;
import com.dantsu.escposprinter.exceptions.EscPosBarcodeException;
import com.dantsu.escposprinter.exceptions.EscPosConnectionException;
import com.dantsu.escposprinter.exceptions.EscPosEncodingException;
import com.dantsu.escposprinter.exceptions.EscPosParserException;
import com.example.restaurantmanager.R;
import com.example.restaurantmanager.adapter.FoodAdapter;
import com.example.restaurantmanager.adapter.FoodOrderAdapter;
import com.example.restaurantmanager.adapter.OldFoodOrderAdapter;
import com.example.restaurantmanager.adapter.OldOrderAdapter;
import com.example.restaurantmanager.adapter.OldSuborderAdapter;
import com.example.restaurantmanager.adapter.TrashAdapter;
import com.example.restaurantmanager.adapter.item.OldOrderItem;
import com.example.restaurantmanager.adapter.item.TableItem;
import com.example.restaurantmanager.adapter.item.TrashItem;
import com.example.restaurantmanager.interfacez.HomeTableClickInterface;
import com.example.restaurantmanager.interfacez.ITrashTableClick;
import com.example.restaurantmanager.realm_object.Food;
import com.example.restaurantmanager.realm_object.FoodLog;
import com.example.restaurantmanager.realm_object.Setting;
import com.example.restaurantmanager.realm_object.order.FoodOrder;
import com.example.restaurantmanager.realm_object.order.Order;
import com.example.restaurantmanager.realm_object.order.SubOrder;
import com.example.restaurantmanager.utils.DataUtil;
import com.example.restaurantmanager.utils.DateTimeUtil;
import com.example.restaurantmanager.utils.DoubleUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import io.realm.Case;
import io.realm.RealmResults;
import io.realm.Sort;

public class TrashActivity extends BaseAdminActivity implements ITrashTableClick, DatePickerDialog.OnDateSetListener {

    RecyclerView listbill_recyclerView;
    TrashAdapter trashAdapter;
    List<TrashItem> trashItems;
    FoodAdapter foodAdapter;
    RecyclerView listFood_recyclerView;
    Date myDate;
    RealmResults<FoodLog> foodLogs;
    RealmResults<Food> foods ;
    FoodLog chooseFoodLog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash);

        String title = DateTimeUtil.getCurrentLocalDateStamp();
        setActionbarTitle(title);
        myDate = new Date();
        viewOldBill();
    }

    public void viewOldBill(){
        listbill_recyclerView = findViewById(R.id.recyclerview_1);
        listFood_recyclerView = findViewById(R.id.recyclerview_2);
        Date startOfDay = DateTimeUtil.getStartOfDay(myDate);
        Date endOfDay = DateTimeUtil.getEndOfDay(myDate);
        setActionbarTitle(DateTimeUtil.formatDate(myDate));
        trashItems = new ArrayList<>();
        foodLogs = realm.where(FoodLog.class)
                .greaterThanOrEqualTo("removeTime",startOfDay)
                .lessThanOrEqualTo("removeTime",endOfDay)
                .notEqualTo("id", DataUtil.unManageFoodLog)
                .notEqualTo("table.id",DataUtil.unManageTable)
                .findAll();
        realm.beginTransaction();

        for (FoodLog foodLog:foodLogs) {
            TrashItem trashItem = new TrashItem();
            trashItem.setId(foodLog.getId());
            trashItem.setChoose(false);
            trashItem.setTotal(foodLog.getOrder().getTotalBill());
            trashItem.setPayTime(foodLog.getRemoveTime());
            trashItem.setTableName((foodLog.getTable()!=null)?foodLog.getTable().getName():"");
            trashItems.add(trashItem);
        }
        realm.commitTransaction();

        trashAdapter = new TrashAdapter(trashItems, TrashActivity.this, TrashActivity.this);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        listbill_recyclerView.setLayoutManager(linearLayoutManager);
        listbill_recyclerView.setAdapter(trashAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_trash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        if (item.getItemId() == R.id.menu_item_search) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(myDate);
            DatePickerDialog dialog = new DatePickerDialog(this, this, calendar.get(Calendar.YEAR),  calendar.get(Calendar.MONTH),  calendar.get(Calendar.DAY_OF_MONTH));
            dialog.show();
        }
        if (item.getItemId() ==R.id.menu_item_delete) {
            AlertDialog.Builder b = new AlertDialog.Builder(this);
            b.setTitle("Confirm");
            b.setMessage("Are you sure you want to clean up your trash?");
            b.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    realm.beginTransaction();
                    Date startOfDay = DateTimeUtil.getStartOfDay(new Date());
                    Date endOfDay = DateTimeUtil.getEndOfDay(myDate);
                    final RealmResults<FoodLog> foodLogs1 =
                            realm.where(FoodLog.class).greaterThanOrEqualTo("removeTime",startOfDay)
                                    .lessThanOrEqualTo("removeTime",endOfDay)
                                    .notEqualTo("id", DataUtil.unManageFoodLog)
                                    .notEqualTo("table.id",DataUtil.unManageTable).findAll();
                    foodLogs1.deleteAllFromRealm();

                    realm.commitTransaction();
                    viewOldBill();
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Date date = new GregorianCalendar(i, i1 , i2).getTime();
        myDate = date;
        viewOldBill();
        listFood_recyclerView.setAdapter(null);
    }
    OldFoodOrderAdapter mAdapter;
    @Override
    public void chooseOrder(TrashItem trashItem) {
        for (TrashItem item : trashItems){
            item.setChoose(false);
        }
        trashItem.setChoose(true);
        trashAdapter.notifyDataSetChanged();

        chooseFoodLog = realm.where(FoodLog.class).equalTo("id",trashItem.getId()).findFirst();
        mAdapter = new OldFoodOrderAdapter( chooseFoodLog.getOrder().getFoodOrders(),true, getContext());

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listFood_recyclerView.setLayoutManager(llm);
        listFood_recyclerView.setAdapter( mAdapter );



    }
}