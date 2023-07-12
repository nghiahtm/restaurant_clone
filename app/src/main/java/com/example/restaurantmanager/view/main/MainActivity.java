package com.example.restaurantmanager.view.main;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.restaurantmanager.adapter.CutBillSuborderAdapter;
import com.example.restaurantmanager.adapter.OldOrderAdapter;
import com.example.restaurantmanager.adapter.OldSuborderAdapter;
import com.example.restaurantmanager.adapter.SuborderAdapter;
import com.example.restaurantmanager.adapter.UserTableAdapter;
import com.example.restaurantmanager.adapter.ViewBill_SuborderAdapter;
import com.example.restaurantmanager.adapter.item.OldOrderItem;
import com.example.restaurantmanager.adapter.item.TableItem;
import com.example.restaurantmanager.interfacez.HomeTableClickInterface;
import com.example.restaurantmanager.realm_object.Setting;
import com.example.restaurantmanager.realm_object.Table;
import com.example.restaurantmanager.realm_object.order.FoodOrder;
import com.example.restaurantmanager.realm_object.order.Order;
import com.example.restaurantmanager.realm_object.order.SubOrder;
import com.example.restaurantmanager.realm_object.order.ToppingOrder;
import com.example.restaurantmanager.utils.DataUtil;
import com.example.restaurantmanager.utils.DateTimeUtil;
import com.example.restaurantmanager.utils.DoubleUtil;
import com.example.restaurantmanager.utils.Session;
import com.example.restaurantmanager.view.admin.AdminActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import io.realm.RealmResults;
import io.realm.Sort;

//////////////////////////////////////////////////////////
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
////////////////////////////////////////////////////////////


public class MainActivity extends BaseMainActivity implements HomeTableClickInterface {
    GridView tableGridVivew;
    List<TableItem> lstTable;
    RealmResults<Table> tables;
    Boolean isHaveOrderFilter = null;
    UserTableAdapter mAdapter;

    //////////////////////////
    DevicePolicyManager deviceManger;
    ComponentName compName;
    View view;
    Boolean mIsKioskEnabled = false;
    ////////////////////////////

    public void findViewById() {
        tableGridVivew = findViewById(R.id.girdView_home);
        lstTable = new ArrayList<TableItem>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ///////////////////////////////////////////////////////
        deviceManger = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        DeviceAdmin deviceAdmin = new DeviceAdmin();
        compName = deviceAdmin.getComponentName(this);

        if (!deviceManger.isAdminActive(compName)) {
            Toast.makeText(this, getString(R.string.not_device_admin), Toast.LENGTH_SHORT).show();
        }

        if (deviceManger.isDeviceOwnerApp(getPackageName())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                String [] packageName = {getPackageName()};
                deviceManger.setLockTaskPackages(compName, packageName);
            }
        } else {
            Toast.makeText(this, getString(R.string.not_device_owner), Toast.LENGTH_SHORT).show();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            enableKioskMode(true);
        }

        view = getWindow().getDecorView();
        ///////////////////////////////////////////////////////


        findViewById();
        tableGridVivew.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                tableGridVivew.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int tableWith = tableGridVivew.getWidth()/4;
                int tableHeight =  tableGridVivew.getHeight()/(tableGridVivew.getHeight()/tableWith);
                getListData();
                mAdapter = new UserTableAdapter(getContext(), lstTable,tableWith,tableHeight,MainActivity.this);
                tableGridVivew.setAdapter(mAdapter);
            }
        });
    }
    private  void getListData() {
        Button btn1 = findViewById(R.id.button);
        Button btn2 = findViewById(R.id.button2);

        if(isHaveOrderFilter == null){
            tables = realm.where(Table.class)
                    .equalTo("status",true)
                    .sort("name", Sort.ASCENDING)
                    .findAll();
            btn1.setTextColor(Color.parseColor("#d16a19"));
            btn2.setTextColor(Color.parseColor("#000000"));

        }
        else {
            tables = realm.where(Table.class)
                    .equalTo("isHaveOrder",isHaveOrderFilter)
                    .equalTo("status",true)
                    .sort("name", Sort.ASCENDING)
                    .findAll();
            btn1.setTextColor(Color.parseColor("#000000"));
            btn2.setTextColor(Color.parseColor("#d16a19"));
        }
        lstTable.clear();

        for (Table table:tables) {
            TableItem tableItem = new TableItem();
            tableItem.setId(table.getId());
            tableItem.setName(table.getName());
            tableItem.setPrice(table.getTotalBill());
            tableItem.setHaveOrder(table.isHaveOrder());
            tableItem.setNote(table.getNote());
            lstTable.add(tableItem);
        }
    }
    public void  alleTischeClick(View view){
        isHaveOrderFilter = null;
        getListData();
        mAdapter.notifyDataSetChanged();
    }
    public void  offeneTisCheClick(View view){
        isHaveOrderFilter = true;
        getListData();
        mAdapter.notifyDataSetChanged();
    }

    public void clickAdmin(View view){
        if(Session.isLogin){
            Intent intent = new Intent(context, AdminActivity.class);
            context.startActivity(intent);
            return;
        }
        LayoutInflater factory = LayoutInflater.from(this);
        final View fDialogView = factory.inflate(R.layout.popup_show_login, null);
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        EditText txt_password = fDialogView.findViewById(R.id.popup_password);
        dialog.setView(fDialogView);
        dialog.setCancelable(false);
        fDialogView.findViewById(R.id.popup_btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        fDialogView.findViewById(R.id.popup_btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = txt_password.getText().toString();
                if(password.equals("230585")){
                    Intent intent = new Intent(context, AdminActivity.class);
                    context.startActivity(intent);
                    Session.isLogin = true;
                    dialog.dismiss();
                }
                
                //////////////////////////////////////////////////
                else if (password.equals("1234") && (mIsKioskEnabled = true)) {
                    stopLockTask(); // Disable lock task mode
                    mIsKioskEnabled = false;
                }
                //////////////////////////////////////////////////
                
                else {
                    Toast.makeText(context,"Wrong password",Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();

    }

    /////////////////////////////////////////////////
    @Override
    protected void onResume() {
        super.onResume();

        //////////////////////////////////////////////
        hideSystemUI();
        ////////////////////////////////////////////

        if(mAdapter != null){
            getListData();
            mAdapter.notifyDataSetChanged();
        }
    }


    /////////////////////////////////////////////////

    @Override
    public void onBackPressed() {
        // Check if lock task mode is active
        if (isInLockTaskMode()) {
            // Relaunch the activity to prevent exiting lock task mode
            startLockTask();
        } else {
            super.onBackPressed();
        }
    }

    private boolean isInLockTaskMode() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(1);
        if (runningTasks != null && !runningTasks.isEmpty()) {
            ComponentName componentName = runningTasks.get(0).topActivity;
            return componentName.getPackageName().equals(getPackageName());
        }
        return false;
    }


    //////////////////////////////////////////////////////////////
    private void enableKioskMode(boolean enable) {
        if (enable) {
            if (deviceManger.isLockTaskPermitted(this.getPackageName())) {
                startLockTask();
                mIsKioskEnabled = true;
                Toast.makeText(this, "Your Phone is Locked", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Kiosk Not Permitted", Toast.LENGTH_SHORT).show();
            }

            ///////////////////////////

            
            /////////////////////////////////
        }
    }


    private void hideSystemUI() {

        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
    //////////////////////////////////////////////////////////////



    @Override
    public void chooseTable(TableItem tableItem) {
        Intent intent = new Intent(context, ChooseFoodActivity.class);
        intent.putExtra("tableId", tableItem.getId());
        context.startActivity(intent);
    }

    @Override
    public void showNote(TableItem tableItem) {
        LayoutInflater factory = LayoutInflater.from(this);
        final View viewAddNote =  factory.inflate(R.layout.popup_show_note, null);
        final AlertDialog showNoteDialog =new AlertDialog.Builder(this).create();
        showNoteDialog.setCancelable(true);
        showNoteDialog.setView(viewAddNote);
        final TextView txt_note = viewAddNote.findViewById(R.id.content);
        txt_note.setText(tableItem.getNote()!=null?tableItem.getNote():"");
        final TextView title = viewAddNote.findViewById(R.id.title);
        title.setText("Note:"+tableItem.getName());
        showNoteDialog.show();
    }

    @Override
    public void foodOrderClick(FoodOrder foodOrder) {

    }


    AlertDialog listDialog;
    RecyclerView listbill_recyclerView;
    OldOrderAdapter oldOrderAdapter;
    List<OldOrderItem> oldOrderItemList;
    OldSuborderAdapter foodAdapter;
    RecyclerView listFood_recyclerView;

    boolean isPrint = false;

    public void viewOldBill(View view){
        LayoutInflater factory = LayoutInflater.from(this);
        final View viewBillDialogView =  factory.inflate(R.layout.popup_list_order, null);
        listDialog =new AlertDialog.Builder(this,android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen).create();
        listDialog.setCancelable(false);
        listDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        listDialog.setView(viewBillDialogView);
        listbill_recyclerView = viewBillDialogView.findViewById(R.id.recyclerview_1);
        listFood_recyclerView = viewBillDialogView.findViewById(R.id.recyclerview_2);
        Date myDate = new Date();
        Date startOfDay = DateTimeUtil.getStartOfDay(myDate);
        Date endOfDay = DateTimeUtil.getEndOfDay(myDate);

        oldOrderItemList = new ArrayList<>();
        RealmResults<Order> orders = realm.where(Order.class)
                                        .greaterThanOrEqualTo("payTime",startOfDay)
                                        .lessThanOrEqualTo("payTime",endOfDay)
                                        .notEqualTo("id",DataUtil.unManageOrderId)
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
        oldOrderAdapter = new OldOrderAdapter(oldOrderItemList,MainActivity.this,MainActivity.this);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        listbill_recyclerView.setLayoutManager(linearLayoutManager);
        listbill_recyclerView.setAdapter(oldOrderAdapter);

        viewBillDialogView.findViewById(R.id.popup_btn_cancel4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listDialog.cancel();
            }
        });
        viewBillDialogView.findViewById(R.id.btn_print_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPrint) return;
                isPrint = true;
                setting = realm.where(Setting.class).equalTo("id", DataUtil.settingId).findFirst();

                if(setting != null && setting.getBillPrinter() != null ){
                    if(chooseOrder!=null){
                        if(!printBill(setting.getBillPrinter().getIP(),setting.getBillPrinter().getPort(),chooseOrder)){
                            new AlertDialog.Builder(context)
                                    .setTitle("Error")
                                    .setMessage("Broken connection bill printer")
                                    .show();
                        }
                    }

                }else{
                    isPrint = false;
                    new AlertDialog.Builder(context)
                            .setTitle("Error")
                            .setMessage("Can't find bill printer setting!")
                            .show();
                }
                isPrint = false;




            }
        });

        viewBillDialogView.findViewById(R.id.btn_show_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chooseOrder!=null){
                    viewBill(chooseOrder);
                }
            }
        });

        listDialog.show();
    }
    Order chooseOrder;
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
    public boolean printLoop(int count ,String ip,int port,String finalPrintStr,String title){
        if (printer != null){
            try {
                if(tcpConnection != null){
                    tcpConnection.disconnect();
                }
                printer.disconnectPrinter();
            }
            catch (Exception e){

            }
        }
        try {
            tcpConnection = new TcpConnection(ip, port);
            printer = new EscPosPrinter(tcpConnection, 203, 80f, 42, new EscPosCharsetEncoding("CP850", 2));
            printer.printFormattedTextAndCut(
                    finalPrintStr
            );
            rs = true;
        } catch (Exception e) {
            if(count < 100){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                rs = printLoop(count + 1,ip,port,finalPrintStr,title);

            }
            else {
//                showAlertDialogThread("Broken connection "+title+ "printer");
                rs = false;
            }


        }
        finally {
            if(printer != null)
                printer.disconnectPrinter();

        }
        return  rs;
    }

    EscPosPrinter printer;
    TcpConnection tcpConnection;
    boolean rs = false;
    Setting setting;
    public boolean printBill(String ip,int port,Order order) {

//        if (printer != null){
//            try {
//                if(tcpConnection != null){
//                    tcpConnection.disconnect();
//                }
//                printer.disconnectPrinter();
//            }
//            catch (Exception e){
//
//            }
//        }
        rs = false;
        String tableName = (order.getTable()!=null)?order.getTable().getName():"";
        String date = DateTimeUtil.formatDateTime(order.getPayTime());
        Double tax =   DoubleUtil.Round2(order.getTotalBill2()*(100-setting.getTaxRate())/100);

        String printStr =
                "[C]<b><font size='tall'>TNT THE MODERN ASIA</font></b>\n" +
                        "[C]<b><font size='normal'>Evertsstrasse 14</font></b>\n" +
                        "[C]<b><font size='normal'>47798 Krefeld</font></b>\n" +
                        "[C]<b><font size='normal'>Tel.: 02151 4467688</font></b>\n" +
                        "[C]<b><font size='normal'>SteuerNr.: 117/5398/2923</font></b>\n" +
                        "[C] _______________________________\n"+
                        "[L]<font size='normal'>\n</font>\n"+
                        "[L]<b><font size='normal'>Tischnr ."+tableName+"</font></b>"+
                        "[R]<font size='normal'>"+date+"</font>\n";
        for(SubOrder subOrder:order.getSubOrders()){
            printStr += "[C]<font size='normal'><b>------------------</b></font>\n";
            for(FoodOrder foodOrder:subOrder.getFoodOrders()){
                String optionsName = foodOrder.getOption()!=null ? "-"+foodOrder.getOption().getName() : "";
                String foodDisplay = foodOrder.getCount()+" x "+foodOrder.getName()+optionsName;
                String foodPrice = DoubleUtil.Round2String(foodOrder.getTotalPrice2())+" EUR";
                int displayLeght = DataUtil.printerNbrCharactersPerLine- foodPrice.length()-1;
                if (foodDisplay.length() > displayLeght){
                    printStr +="[L]<font size='normal' >"+foodDisplay.substring(0,displayLeght)+" "+foodPrice+"</font>\n";
                    printStr +="[L]<font size='normal' >"+foodDisplay.substring(displayLeght,foodDisplay.length())+"</font>\n";
                }
                else {
                    printStr +="[L]<font size='normal' >"+foodDisplay+"</font>";
                    printStr +="[R]<font size='normal'>"+foodPrice+"</font>\n";
                }


                for(ToppingOrder topping : foodOrder.getToppings()){
                    printStr +="[L]\t<font size='normal'>>"+topping.getName()+"("+DoubleUtil.Round2String(topping.getPrice())+"EUR)"+"</font>\n";
                }
            }
        }
        printStr += "[R]<font size='normal'><b>------------</b></font>\n";
        printStr += "[R]<font size='tall'><b>"+DoubleUtil.Round2String(order.getTotalBill2())+" EUR</b></font>\n";
        printStr += "[R]<font size='normal'><b>------------</b></font>\n";
        printStr += "[L]<font size='normal'>MwSt . A "+setting.getTaxRate()+"%(Tax):</font>";
        printStr += "[R]<font size='normal'>"+ DoubleUtil.Round2String(order.getTotalBill2()-tax)+" EUR</font>\n";
        printStr += "[L]<font size='normal'>Netto</font>";
        printStr += "[R]<font size='normal'>"+DoubleUtil.Round2String(tax)+" EUR</font>\n";
        printStr += "[L]<font size='tall'>Total:</font>";
        printStr += "[R]<font size='tall'><b>"+(DoubleUtil.Round2String(order.getTotalBill2()))+" EUR</b></font>\n";
        printStr += "[R]<font size='normal'><b>============</b></font>\n";
        printStr += "[R]<font size='tall'><b>Bargeld</b></font>\n";
        printStr += "[C]<b>*************</b>\n";
        printStr += "[C] _________________________________________\n";
        printStr += "[L]<font size='normal'>\n</font>\n";
        printStr += "[C] <font size='normal'>Vielen Dank fuer Ihren Besuch!</font>\n";
        printStr += "[C] <font size='normal'>Auf Wiedersehen! Bis zum naechsten Ma1!</font>\n";
        printStr += "[C] <font size='normal'>IHR TNT THE MODERN ASIA TEAM</font>\n";
        
        printStr +="[L] <font size='normal'>\n\n</font>";
        final String finalPrintStr = printStr;
        Thread thread = new Thread(new Runnable() {
            public void run() {
                try {
//                    tcpConnection = new TcpConnection(ip, port);
//                    printer = new EscPosPrinter(tcpConnection, 203, 80f, 42, new EscPosCharsetEncoding("CP850", 2));
//                    printer.printFormattedTextAndCut(
//                            finalPrintStr
//                    );
                    rs = printLoop(0,ip,port,finalPrintStr,"Bill");
                } catch (Exception e) {

                    rs = false;

                }
//                finally {
//                    if(printer != null)
//                        printer.disconnectPrinter();
//
//                }
            }
//            public void run() {
//                try {
//                    tcpConnection = new TcpConnection(ip, port);
//                    printer = new EscPosPrinter(tcpConnection, 203, 80f,  DataUtil.printerNbrCharactersPerLine, new EscPosCharsetEncoding("CP850", 2));
//                    printer.printFormattedTextAndCut(
//                            finalPrintStr
//                    );
//                    rs = true;
//                } catch (EscPosConnectionException e) {
//                    e.printStackTrace();
//                    showAlertDialogThread("Broken connection bill printer");
//                    rs = false;
//
//                } catch (EscPosParserException e) {
//                    e.printStackTrace();
//                    showAlertDialogThread("Invalid formatted text bill printer");
//                    rs = false;
//
//                } catch (EscPosEncodingException e) {
//                    e.printStackTrace();
//                    showAlertDialogThread("Bad selected encoding bill printer");
//                    rs = false;
//                } catch (EscPosBarcodeException e) {
//                    showAlertDialogThread("Invalid barcode bill printer");
//                    rs = false;
//                }
//                catch (Exception e){
//                    e.printStackTrace();
//                    showAlertDialogThread("Print Error bill printer");
//                    rs = false;
//                }
//                finally {
//                    if(printer != null)
//                        printer.disconnectPrinter();
//
//                }
//            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
//            showAlertDialogThread("Thread Error bill printer");
            rs = false;
        }
        return rs;
    }
    void showAlertDialogThread(String title){
        MainActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                new AlertDialog.Builder(context)
                        .setTitle("Error")
                        .setMessage(title)
                        .show();
            }
        });
    }
    AlertDialog  showOrderDialog;

    public void viewBill(Order od){
        LayoutInflater factory = LayoutInflater.from(this);
        final View viewBillDialogView =  factory.inflate(R.layout.popup_show_order, null);
        showOrderDialog =new AlertDialog.Builder(this,android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen).create();
        showOrderDialog.setCancelable(false);
        showOrderDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        showOrderDialog.setView(viewBillDialogView);
        viewBillDialogView.findViewById(R.id.popup_screen).setRotation(0);
        String date = DateTimeUtil.getCurrentLocalDateTimeStamp();
        ((TextView)viewBillDialogView.findViewById(R.id.tv_tableName)).setText("Tischnr. "+((od.getTable()!=null) ? od.getTable().getName():""));
        ((TextView)viewBillDialogView.findViewById(R.id.tv_time)).setText(date);
        ((TextView)viewBillDialogView.findViewById(R.id.tv_total)).setText(DoubleUtil.Round2String(od.getTotalBill2())+" €");

        RecyclerView mRecyclerView = viewBillDialogView.findViewById(R.id.recyclerview_subFood);
        ViewBill_SuborderAdapter mAdapter = new ViewBill_SuborderAdapter( od.getSubOrders(),false,MainActivity.this);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        viewBillDialogView.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOrderDialog.cancel();
            }
        });

        showOrderDialog.show();
    }

}