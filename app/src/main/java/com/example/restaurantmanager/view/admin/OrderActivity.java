package com.example.restaurantmanager.view.admin;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;

import com.dantsu.escposprinter.EscPosCharsetEncoding;
import com.dantsu.escposprinter.EscPosPrinter;
import com.dantsu.escposprinter.connection.tcp.TcpConnection;
import com.dantsu.escposprinter.exceptions.EscPosBarcodeException;
import com.dantsu.escposprinter.exceptions.EscPosConnectionException;
import com.dantsu.escposprinter.exceptions.EscPosEncodingException;
import com.dantsu.escposprinter.exceptions.EscPosParserException;
import com.example.restaurantmanager.R;
import com.example.restaurantmanager.adapter.OldOrderAdapter;
import com.example.restaurantmanager.adapter.OldSuborderAdapter;
import com.example.restaurantmanager.adapter.item.OldOrderItem;
import com.example.restaurantmanager.adapter.item.TableItem;
import com.example.restaurantmanager.interfacez.HomeTableClickInterface;
import com.example.restaurantmanager.realm_object.Setting;
import com.example.restaurantmanager.realm_object.order.FoodOrder;
import com.example.restaurantmanager.realm_object.order.Order;
import com.example.restaurantmanager.utils.DataUtil;
import com.example.restaurantmanager.utils.DateTimeUtil;
import com.example.restaurantmanager.utils.DoubleUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

import io.realm.RealmResults;

public class OrderActivity extends BaseAdminActivity implements HomeTableClickInterface, DatePickerDialog.OnDateSetListener {

    RecyclerView listbill_recyclerView;
    OldOrderAdapter oldOrderAdapter;
    List<OldOrderItem> oldOrderItemList;
    OldSuborderAdapter foodAdapter;
    RecyclerView listFood_recyclerView;
    Date myDate;
    RealmResults<Order> orders;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

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
        oldOrderItemList = new ArrayList<>();
        orders = realm.where(Order.class)
                .greaterThanOrEqualTo("payTime",startOfDay)
                .lessThanOrEqualTo("payTime",endOfDay)
                .notEqualTo("id", DataUtil.unManageOrderId)
                .notEqualTo("id",DataUtil.unManageOrderId1)
                .notEqualTo("id",DataUtil.unManageOrderId2)
                .notEqualTo("table.id",DataUtil.unManageTable)
                .findAll();
        for (Order oder:orders) {
            OldOrderItem oldOrderItem = new OldOrderItem();
            oldOrderItem.setId(oder.getId());
            oldOrderItem.setChoose(false);
            oldOrderItem.setTotal(oder.getTotalBill2());
            oldOrderItem.setPayTime(oder.getPayTime());
            oldOrderItem.setTableName((oder.getTable()!=null)?oder.getTable().getName():"");
            oldOrderItem.setStatus(oder.getStatus());
            oldOrderItemList.add(oldOrderItem);
        }
        oldOrderAdapter = new OldOrderAdapter(oldOrderItemList, OrderActivity.this,OrderActivity.this);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        listbill_recyclerView.setLayoutManager(linearLayoutManager);
        listbill_recyclerView.setAdapter(oldOrderAdapter);
    }
    Order chooseOrder;

    @Override
    public void chooseTable(TableItem tableItem) {

    }

    @Override
    public void showNote(TableItem tableItem) {

    }

    @Override
    public void foodOrderClick(FoodOrder foodOrder) {

    }

    @Override
    public void chooseOrder(OldOrderItem OldOrderItem) {
        for (OldOrderItem oldOrderItem : oldOrderItemList){
            oldOrderItem.setChoose(false);
        }
        OldOrderItem.setChoose(true);
        oldOrderAdapter.notifyDataSetChanged();
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        Order order = realm.where(Order.class).equalTo("id",OldOrderItem.getId()).findFirst();
        chooseOrder = order;
        foodAdapter = new OldSuborderAdapter( order.getSubOrders(),true,context);
        listFood_recyclerView.setLayoutManager(linearLayoutManager);
        listFood_recyclerView.setAdapter(foodAdapter);

    }


    EscPosPrinter printer;
    boolean rs = false;
    Setting setting;
    public boolean printBill(String ip,int port,Order order) {
        if(orders == null) return  false;
        if (printer != null){
            try {
                printer.disconnectPrinter();
            }
            catch (Exception e){

            }
        }
        rs = false;
        String date = DateTimeUtil.formatDate(myDate);
        Double total = 0d;
        String printStr =
                "[C]<b><font size='tall'>TAGESABSCHLUSS</font></b>\n" +
                        "[C] _______________________________\n"+
                        "[L]<font size='normal'>\n</font>\n"+
                        "[C]<b><font size='tall'>"+date+"</font><b>\n"+
                        "[L]<font size='normal'>\n</font>\n";


        int stt = 1;

        printStr += "[C] _______Cash_______\n";
        double moneyTotal = 0;
        for(Order order1 : orders.stream().filter(x -> x.getStatus() == DataUtil.CashNumber).collect(Collectors.toList())){
            printStr += "[L]<font size='normal'>"+String.format("%-10d",stt)+"</font>";
            printStr += "[L]<font size='normal'>"+DateTimeUtil.formatDateTime(order1.getPayTime())+"</font>\n";
            printStr += "[L]<font size='normal'>Tisch ."+((order1.getTable()!=null)?order1.getTable().getName():0)+"</font>";
            printStr += "[L]<font size='normal'>"+DoubleUtil.Round2String(order1.getTotalBill2())+"EUR</font>\n";
            printStr +="[L] <font size='normal'>\n</font>\n";
            stt++;
            total += order1.getTotalBill2();
            moneyTotal += order1.getTotalBill2();
        }
        printStr += "[R]<font size='normal'><b>------------</b></font>\n";
        printStr += "[R]<font size='tall'><b>"+DoubleUtil.Round2String(moneyTotal)+" EUR</b></font>\n";

        printStr += "[C] _______Card_______\n";
        double cardTotal = 0;
        for(Order order1 : orders.stream().filter(x -> x.getStatus() == DataUtil.CardNumber).collect(Collectors.toList())){
            printStr += "[L]<font size='normal'>"+String.format("%-10d",stt)+"</font>";
            printStr += "[L]<font size='normal'>"+DateTimeUtil.formatDateTime(order1.getPayTime())+"</font>\n";
            printStr += "[L]<font size='normal'>Tisch ."+((order1.getTable()!=null)?order1.getTable().getName():0)+"</font>";
            printStr += "[L]<font size='normal'>"+DoubleUtil.Round2String(order1.getTotalBill2())+"EUR</font>\n";
            printStr +="[L] <font size='normal'>\n</font>\n";
            stt++;
            total += order1.getTotalBill2();
            cardTotal += order1.getTotalBill2();
        }
        printStr += "[R]<font size='normal'><b>------------</b></font>\n";
        printStr += "[R]<font size='tall'><b>"+DoubleUtil.Round2String(cardTotal)+" EUR</b></font>\n";

        printStr += "[C] _______Paypal_______\n";
        double paypalTotal = 0;
        for(Order order1 : orders.stream().filter(x -> x.getStatus() == DataUtil.PaypalNumber).collect(Collectors.toList())){
            printStr += "[L]<font size='normal'>"+String.format("%-10d",stt)+"</font>";
            printStr += "[L]<font size='normal'>"+DateTimeUtil.formatDateTime(order1.getPayTime())+"</font>\n";
            printStr += "[L]<font size='normal'>Tisch ."+((order1.getTable()!=null)?order1.getTable().getName():0)+"</font>";
            printStr += "[L]<font size='normal'>"+DoubleUtil.Round2String(order1.getTotalBill2())+"EUR</font>\n";
            printStr +="[L] <font size='normal'>\n</font>\n";
            stt++;
            total += order1.getTotalBill2();
            paypalTotal += order1.getTotalBill2();
        }
        printStr += "[R]<font size='normal'><b>------------</b></font>\n";
        printStr += "[R]<font size='tall'><b>"+DoubleUtil.Round2String(paypalTotal)+" EUR</b></font>\n";

        printStr += "[R]<font size='normal'><b>------------</b></font>\n";
        printStr += "[L]<font size='tall'>Total:</font>";
        printStr += "[R]<font size='tall'><b>"+DoubleUtil.Round2String(total)+" EUR</b></font>\n";
        printStr +="[L] <font size='normal'>\n</font>\n";
        printStr += "[C]<b>*************</b>\n";
        printStr += "[C] ____________________\n";

        final String finalPrintStr = printStr;
        Thread thread = new Thread(new Runnable() {
            public void run() {
                try {
                    TcpConnection tcpConnection = new TcpConnection(ip, port);
                    printer = new EscPosPrinter(tcpConnection, 203, 80f, 42, new EscPosCharsetEncoding("CP850", 2));
                    printer.printFormattedTextAndCut(
                            finalPrintStr
                    );
                    rs = true;
                } catch (EscPosConnectionException e) {
                    e.printStackTrace();
                    showAlertDialogThread("Broken connection bill printer");
                    rs = false;

                } catch (EscPosParserException e) {
                    e.printStackTrace();
                    showAlertDialogThread("Invalid formatted text bill printer");
                    rs = false;

                } catch (EscPosEncodingException e) {
                    e.printStackTrace();
                    showAlertDialogThread("Bad selected encoding bill printer");
                    rs = false;
                } catch (EscPosBarcodeException e) {
                    showAlertDialogThread("Invalid barcode bill printer");
                    rs = false;
                }
                catch (Exception e){
                    e.printStackTrace();
                    showAlertDialogThread("Print Error bill printer");
                    rs = false;
                }
                finally {
                    if(printer != null)
                        printer.disconnectPrinter();

                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            showAlertDialogThread("Thread Error bill printer");
            rs = false;
        }
        return rs?true:false;
    }
    void showAlertDialogThread(String title){
        OrderActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                new AlertDialog.Builder(context)
                        .setTitle("Error")
                        .setMessage(title)
                        .show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_order, menu);
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
            b.setMessage("Are you sure you want to clean up your order?");
            b.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    realm.beginTransaction();
                    Date startOfDay = DateTimeUtil.getStartOfDay(new Date());

                    final RealmResults<Order> orders = realm.where(Order.class).lessThanOrEqualTo("payTime",startOfDay).findAll();
                    orders.deleteAllFromRealm();

                    realm.commitTransaction();
                    viewOldBill();
                    listFood_recyclerView.setAdapter(null);
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
        if (item.getItemId() == R.id.menu_item_print) {
            Setting setting = realm.where(Setting.class).equalTo("id", DataUtil.settingId).findFirst();
            if(setting != null && setting.getBillPrinter() != null ){
                    printBill(setting.getBillPrinter().getIP(),setting.getBillPrinter().getPort(),chooseOrder);

            }else{
                new AlertDialog.Builder(context)
                        .setTitle("Error")
                        .setMessage("Can't find bill printer setting!")
                        .show();
            }
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
}