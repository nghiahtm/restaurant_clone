package com.example.restaurantmanager.view.main;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.dantsu.escposprinter.EscPosCharsetEncoding;
import com.dantsu.escposprinter.EscPosPrinter;
import com.dantsu.escposprinter.connection.tcp.TcpConnection;
import com.dantsu.escposprinter.exceptions.EscPosBarcodeException;
import com.dantsu.escposprinter.exceptions.EscPosConnectionException;
import com.dantsu.escposprinter.exceptions.EscPosEncodingException;
import com.dantsu.escposprinter.exceptions.EscPosParserException;
import com.example.restaurantmanager.R;
import com.example.restaurantmanager.adapter.ChooseOptionAdapter;
import com.example.restaurantmanager.adapter.CutBillSuborderAdapter;
import com.example.restaurantmanager.adapter.SuborderAdapter;
import com.example.restaurantmanager.adapter.UserToppingAdapter;
import com.example.restaurantmanager.adapter.ViewBill_SuborderAdapter;
import com.example.restaurantmanager.adapter.item.OptionItem;
import com.example.restaurantmanager.enums.UndoEnum;
import com.example.restaurantmanager.interfacez.ChooseFoodClickInterface;
import com.example.restaurantmanager.realm_object.Category;
import com.example.restaurantmanager.realm_object.Food;
import com.example.restaurantmanager.realm_object.FoodLog;
import com.example.restaurantmanager.realm_object.Option;
import com.example.restaurantmanager.realm_object.Setting;
import com.example.restaurantmanager.realm_object.SubCategory;
import com.example.restaurantmanager.realm_object.Table;
import com.example.restaurantmanager.realm_object.Topping;
import com.example.restaurantmanager.realm_object.order.FoodOrder;
import com.example.restaurantmanager.realm_object.order.OptionOrder;
import com.example.restaurantmanager.realm_object.order.Order;
import com.example.restaurantmanager.realm_object.order.SubOrder;
import com.example.restaurantmanager.realm_object.order.ToppingOrder;
import com.example.restaurantmanager.utils.DataUtil;
import com.example.restaurantmanager.utils.DateTimeUtil;
import com.example.restaurantmanager.utils.DoubleUtil;
import com.example.restaurantmanager.utils.StringUtil;
import com.example.restaurantmanager.utils.UndoItem;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;
import java.util.UUID;

import io.realm.Case;
import io.realm.RealmResults;
import io.realm.Sort;

public class ChooseFoodActivity extends BaseMainActivity implements ChooseFoodClickInterface {
    int page = 1;
    int pageFood = 1;
    int pageCount;
    int pageFoodCount = 0;
    RealmResults<Category> categories;
    TextView btn_page1;
    TextView btn_page2;
    Category page1;
    Category page2;
    Category choosePage;
    Food[][] foodArr;
    TableLayout table_layout ;
    ArrayList<Food> foods;
    AlertDialog chooseOptionDialog;
    GridView gridview_topping;
    AlertDialog  chooseToppingDialog;
    AlertDialog  payOrderDialog;
    AlertDialog  showOrderDialog;
    AlertDialog  showNoteDialog;
    AlertDialog cutOrderDialog;
    AlertDialog  payOrderDialog_cut;


    RecyclerView recyclerView_Option;
    TextView toolbar_tisch;
    Order order;
    Table table;
    SuborderAdapter mAdapter;
    RecyclerView mRecyclerview;
    NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    FoodOrder chooseFoodOrder;
    ToppingOrder chooseToppingOrder;
    Food chooseFood ;
    Order oldOrder;
    EditText txt_topping_name;
    EditText txt_topping_price;
    SearchView txt_search;
    AlertDialog addCustomFoodDialog;
    EditText txt_customFoodPrice;
    TextView tv_customFoodTitle;
    String customFoodName = "";
    int customFoodType = 0;
    SubOrder chooseSuborder;
    Stack<UndoItem> undoItemStack;

    FoodLog foodLog;

    void findViewById(){
        btn_page1 = findViewById(R.id.btn_page1);
        btn_page2 = findViewById(R.id.btn_page2);
        table_layout =  findViewById(R.id.table_layout);
        toolbar_tisch = findViewById(R.id.toolbar_tisch);
        mRecyclerview = findViewById(R.id.recyclerview_list_suborders);
        mNavigationView = findViewById(R.id.navigationView);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        txt_search  =(SearchView) findViewById(R.id.txt_searchFood);
        undoItemStack =  new Stack<>();

    }

    //////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_food);
        findViewById();
        mNavigationView.setItemIconTintList(null);
        categories =realm.where(Category.class)
                .equalTo("status",true)
                .sort("name", Sort.ASCENDING)
                .findAll();
        if(categories.size() > 0){
            choosePage = categories.get(0);
            getListFood();
        }
        pageCount = categories.size()/2;
        if(categories.size()>pageCount * 2){
            pageCount += 1;
        }
        getPage();

        UUID tableId = (UUID) getIntent().getSerializableExtra("tableId");
        realm.beginTransaction();
        table = realm.where(Table.class).equalTo("id", tableId).findFirst();
        oldOrder = realm.where(Order.class).equalTo("table.id",table.getId())
                .notEqualTo("id",DataUtil.unManageOrderId)
                .notEqualTo("id",DataUtil.unManageOrderId1)
                .notEqualTo("id",DataUtil.unManageOrderId2)
                .equalTo("isPay",false).findFirst();
        if(oldOrder == null){
            oldOrder = realm.createObject(Order.class, UUID.randomUUID());
            oldOrder.setTable(table);

        }
        Order temporaryOrder = realm.where(Order.class).equalTo("id", DataUtil.unManageOrderId).findFirst();
        if(temporaryOrder != null){
            temporaryOrder.deleteFromRealm();
        }
        if (oldOrder != null){
            Order unManagerOder = null;
            unManagerOder = realm.copyFromRealm(oldOrder);
            unManagerOder.setId(DataUtil.unManageOrderId);
            unManagerOder.setTable(null);
            temporaryOrder = realm.copyToRealm(unManagerOder);
        }
        else {
            temporaryOrder = realm.createObject(Order.class, DataUtil.unManageOrderId);
        }
        order = temporaryOrder ;
        setMyTitle();
        loadOrderDataView();

        foodLog = realm.where(FoodLog.class).equalTo("id", DataUtil.unManageFoodLog).findFirst();
        if(foodLog != null){
            foodLog.deleteFromRealm();
        }
        foodLog = realm.createObject(FoodLog.class,DataUtil.unManageFoodLog);
        realm.createEmbeddedObject(SubOrder.class,foodLog,"order");
        realm.commitTransaction();

        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        if (item.getItemId()==R.id.nav_delele){
                            chooseToppingOrder = null;
                            chooseSuborder = null;
                            deleteFoodOrder(chooseFoodOrder);
                            chooseFoodOrder = null;

                        }
                        if(item.getItemId()==R.id.nav_add_sub_order){
                            chooseToppingOrder = null;
                            addSubOrder();
                            chooseSuborder = null;
                        }
                        if(item.getItemId()==R.id.nav_edit_count){
                            chooseToppingOrder = null;
                            chooseSuborder = null;
                            editCountFoodOrder();
                        }
                        if(item.getItemId()==R.id.nav_topping){
                            chooseSuborder = null;
                            chooseTopping();
                            chooseToppingOrder = null;
                        }
                        if(item.getItemId()==R.id.nav_trash){
                            chooseToppingOrder = null;
                            chooseSuborder = null;
                            trashFoodOrder(chooseFoodOrder);
                            chooseFoodOrder = null;
                        }
                        mDrawerLayout.closeDrawers();
                        return true;
                    }


                });
        txt_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(!s.equals("")){
                    pageFood = 1;
                    searchFood(s);
                }else {
                    pageFood = 1;
                    getListFood();
                }
                return false;
            }
        });

    }
    /////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////


    void addSubOrder(){
        //////////////////////////

        ///////////////////////////
        if(chooseSuborder == null && chooseFoodOrder!=null){
            realm.beginTransaction();
            dialog.show();
            SubOrder newSub = realm.createEmbeddedObject(SubOrder.class,order,"subOrders");
            cloneFoodOrder(chooseFoodOrder,newSub);
            chooseFoodOrder.deleteFromRealm();
            for(int i =0 ; i < order.getSubOrders().size(); i++){
                if(order.getSubOrders().get(i).getFoodOrders().size() == 0){
                    order.getSubOrders().get(i).deleteFromRealm();
                    i--;
                }
            }
            setMyTitle();
            mAdapter.notifyDataSetChanged();
            UndoItem undoItem = new UndoItem();
            undoItem.undoEnum = UndoEnum.AddSubOrder;
            undoItem.subOrder = newSub ;
            undoItemStack.push(undoItem);
            scrollToBottom();
            dialog.dismiss();
            realm.commitTransaction();
        }
        else if(chooseSuborder != null){
            deleteSubOrder(chooseSuborder);
        }

    }
    void deleteSubOrder(SubOrder chooseSuborder){
        realm.beginTransaction();
        try {
            dialog.show();
            int position = order.getSubOrders().indexOf(chooseSuborder);
            if(position == -1){
                return;
            }

            SubOrder toSubOrder = order.getSubOrders().get(position-1);
            for (FoodOrder foodOrder:chooseSuborder.getFoodOrders()) {
                cloneFoodOrder(foodOrder,toSubOrder);

            }
            chooseSuborder.deleteFromRealm();

            chooseSuborder = null;
            mAdapter.notifyDataSetChanged();
            scrollToBottom();
            mAdapter.notifyDataSetChanged();
        }
        catch (Exception e){
            e.printStackTrace();
        }finally {
            dialog.dismiss();
        }
        realm.commitTransaction();


    }
    void searchFood(String filter){

        RealmResults<Food> f = realm.where(Food.class)
                .equalTo("status",true)
                .contains("name",filter, Case.INSENSITIVE)
                .sort("subCategory.id", Sort.ASCENDING)
                .findAll();

        foods.clear();
        for (Food food:f) {
            foods.add(food);
        }
        pageFood = 1;
        pageFoodCount = foods.size()/16;
        if(pageFoodCount*16 < foods.size()) pageFoodCount +=1;
        getListFoodPage();
    }



    @SuppressLint("ResourceAsColor")
    public void getPage(){
        int toIndex = 2*page;
        int fromIndex = toIndex-2;
        if(toIndex > categories.size()) toIndex = categories.size();
        if(fromIndex<0) fromIndex = 0;
        List<Category> subList = categories.subList(fromIndex,toIndex);
        choosePage = subList.get(0);
        if(subList.size()>0){
            page1 = subList.get(0);
            btn_page1.setText(page1.getName());
            btn_page1.setEnabled(true);
            if (page1.equals(choosePage)){
                btn_page1.setBackgroundResource(R.color.choosePage);
            }
            else {
                btn_page1.setBackgroundResource(R.color.notChoosePage);
            }
        }
        else {
            btn_page1.setText("");
            btn_page1.setEnabled(false);
            btn_page1.setBackgroundResource(R.color.notChoosePage);

        }
        if(subList.size()>1){
            page2 = subList.get(1);
            btn_page2.setText(page2.getName());
            btn_page2.setEnabled(true);
            if (page2.equals(choosePage)){
                btn_page2.setBackgroundResource(R.color.choosePage);
            }
            else {
                btn_page2.setBackgroundResource(R.color.notChoosePage);
            }
        }
        else {
            btn_page2.setText("");
            btn_page2.setEnabled(false);
            btn_page2.setBackgroundResource(R.color.notChoosePage);
        }
        getListFood();


    }
    public void nextPage(View view){
        if(page >= pageCount){
            page = 1;
        }
        else {
            page +=1;
        }
        getPage();
    }
    public void prevPage(View view){
        if(page <= 1){
            page = pageCount;
        }
        else {
            page -=1;
        }
        getPage();
    }
    public  void btn_page1_click(View view){
        txt_search.setQuery("", false);
        txt_search.setIconified(true);
        choosePage = page1;
        btn_page1.setBackgroundResource(R.color.choosePage);
        btn_page2.setBackgroundResource(R.color.notChoosePage);
        pageFood = 1;
        getListFood();
    }
    public void btn_page2_click(View view){
        txt_search.setQuery("", false);
        txt_search.setIconified(true);
        choosePage = page2;
        btn_page2.setBackgroundResource(R.color.choosePage);
        btn_page1.setBackgroundResource(R.color.notChoosePage);
        pageFood = 1;
        getListFood();

    }
    public void getListFood( ){

        foods = new ArrayList<>();
        for (SubCategory subCate:choosePage.getSubCategories().sort("name")) {
            List<Food> listFood = realm.where(Food.class).equalTo("subCategory.id", subCate.getId()).findAll().sort("name");
            for (Food food:listFood) {
                foods.add(food);
            }
        }
        pageFoodCount = foods.size()/16;
        if(pageFoodCount*16 < foods.size()) pageFoodCount +=1;
        getListFoodPage();
    }

    private void getListFoodPage(){
        table_layout.removeAllViews();
        //code phân trang
        int toIndex = 16*pageFood;
        int fromIndex = toIndex-16;
        if(toIndex > foods.size()) toIndex = foods.size();
        if(fromIndex<0) fromIndex = 0;
        List<Food> subFoods = foods.subList(fromIndex,toIndex);

        int rowCount = subFoods.size()/4;
        if(rowCount*4 < subFoods.size())rowCount +=1;

        foodArr =new  Food[rowCount][4];
        int idx = 0;
        for (int i=0; i<4;i++){
            for (int j=0; j<rowCount;j++){
                if(idx<subFoods.size()){
                    foodArr[j][i]=subFoods.get(idx);
                    idx++;
                }
                else {
                    break;
                }
            }
        }
        for (int i=0; i<rowCount;i++){
            TableRow row =(TableRow) getLayoutInflater().inflate(R.layout.table_row_food, null);
            Food food1 = foodArr[i][0];
            Food food2 = foodArr[i][1];
            Food food3 = foodArr[i][2];
            Food food4 = foodArr[i][3];
            TextView tv1 = row.findViewById(R.id.tv1);
            TextView tv2 = row.findViewById(R.id.tv2);
            TextView tv3 = row.findViewById(R.id.tv3);
            TextView tv4 = row.findViewById(R.id.tv4);

            if(food1 != null){
                tv1.setBackgroundColor(food1.getSubCategory().getColor());
                tv1.setText(StringUtil.subName(food1.getName(),20));
                tv1.setEnabled(true);
                tv1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        chooseFood(food1);
                    }
                });
            }
            if(food2 != null){
                tv2.setBackgroundColor(food2.getSubCategory().getColor());
                tv2.setText(StringUtil.subName(food2.getName(),20));
                tv2.setEnabled(true);
                tv2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        chooseFood(food2);
                    }
                });
            }
            if(food3 != null){
                tv3.setBackgroundColor(food3.getSubCategory().getColor());
                tv3.setText(StringUtil.subName(food3.getName(),20));
                tv3.setEnabled(true);
                tv3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        chooseFood(food3);
                    }
                });
            }
            if(food4 != null){
                tv4.setBackgroundColor(food4.getSubCategory().getColor());
                tv4.setText(StringUtil.subName(food4.getName(),20));
                tv4.setEnabled(true);
                tv4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        chooseFood(food4);
                    }
                });
            }


            table_layout.addView(row);
        }
    }
    public void nextPageFood(View view){
        if(pageFood >= pageFoodCount){
            pageFood = 1;
        }
        else {
            pageFood +=1;
        }
        getListFoodPage();
    }
    Boolean isOptionPopup = false;
    void scrollToBottom(){
        if(order.getSubOrders().size() != 0){
            mRecyclerview.scrollToPosition(order.getSubOrders().size() - 1);
        }
    }
    public void chooseFood(Food food){
        txt_search.setQuery("", false);
        chooseFood = food;
        List<Option> options =  food.getOptions().where().equalTo("status",true).findAll();
        if (!isOptionPopup && options.size() > 0 ){
            LayoutInflater factory = LayoutInflater.from(this);
            final View optionDialogView = factory.inflate(R.layout.popup_food_option, null);
            chooseOptionDialog = new AlertDialog.Builder(this).create();
            recyclerView_Option = optionDialogView.findViewById(R.id.recycler_options);
            List<OptionItem> optionItems = new ArrayList<>();
            for (Option option:options){
                optionItems.add(new OptionItem(option.getId(),option.getName()
                        ,option.getPrice(),option.getStatus()));
            }
            ChooseOptionAdapter mAdapter = new ChooseOptionAdapter( optionItems, ChooseFoodActivity.this,this);
            recyclerView_Option.setAdapter( mAdapter);
            recyclerView_Option.setLayoutManager(new LinearLayoutManager(this));
            chooseOptionDialog.setView(optionDialogView);
            chooseOptionDialog.setCancelable(false);
            isOptionPopup = true;
            chooseOptionDialog.show();
            optionDialogView.findViewById(R.id.popup_btn_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    chooseOptionDialog.dismiss();
                    isOptionPopup = false;
                }
            });
        }else {
            addFood(null);
        }
    }

    @Override
    public void chooseSuborder(SubOrder subOrder) {
        revertFoodChooseBackground();
        chooseSuborder = subOrder;
        mDrawerLayout.openDrawer(GravityCompat.START);
    }



    @Override
    public void chooseOptionClicked(OptionItem optionItem) {
        addFood(optionItem);
        chooseOptionDialog.dismiss();
        isOptionPopup = false;

    }
    public void addFood(OptionItem optionItem){
        realm.beginTransaction();
        if(order.getSubOrders().size() == 0){
            realm.createEmbeddedObject(SubOrder.class,order,"subOrders");
            mAdapter.notifyDataSetChanged();
        }

        SubOrder subOrder = order.getSubOrders().get(order.getSubOrders().size()-1);
        FoodOrder foodOrder = realm.createEmbeddedObject(FoodOrder.class,subOrder,"foodOrders");
        foodOrder.setName(chooseFood.getName());
        foodOrder.setPrice(chooseFood.getPrice());
        foodOrder.setFoodType(chooseFood.getFoodType());
        foodOrder.setId(UUID.randomUUID());
        if(optionItem != null){
            OptionOrder optionOrder = realm.createEmbeddedObject(OptionOrder.class,foodOrder,"option");
            optionOrder.setName(optionItem.name);
            optionOrder.setPrice(optionItem.price);
        }
        foodOrder.getTotalPrice();
        Toast.makeText(context,"successfully added",Toast.LENGTH_SHORT).show();
        setMyTitle();
        UndoItem undoItem = new UndoItem();
        undoItem.undoEnum = UndoEnum.AddFood;
        undoItem.foodOrder = foodOrder;
        undoItemStack.push(undoItem);
        realm.commitTransaction();
        chooseFoodOrder = foodOrder;
        scrollToBottom();
    }
    public void clickOut(View view){
        finish();
    }
    public void loadOrderDataView(){
        mAdapter = new SuborderAdapter( order.getSubOrders(),false,ChooseFoodActivity.this,this);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerview.setLayoutManager(linearLayoutManager);
        mRecyclerview.setAdapter(mAdapter);
        scrollToBottom();
    }
    LinearLayout myll_food;
    int myColor;
    @Override
    public void foodOrderItemClick(FoodOrder foodOrder, LinearLayout ll_food) {
        revertFoodChooseBackground();
        chooseToppingOrder = null;
        chooseFoodOrder = foodOrder;
        mDrawerLayout.openDrawer(GravityCompat.START);
        myll_food = ll_food;
        ColorDrawable colorDrawable = (ColorDrawable)myll_food.getBackground();
        if(colorDrawable != null){
            myColor = colorDrawable.getColor();
        }
        else {
            myColor = Color.WHITE;
        }
        myll_food.setBackgroundColor(Color.GRAY);

    }

    public void revertFoodChooseBackground(){
        if(myll_food != null){
            myll_food.setBackgroundColor(myColor);
            myll_food = null;
        }
    }
    @Override
    public void chooseToppingClick(Topping topping) {
        txt_topping_name.setText(topping.getName());
        txt_topping_price.setText(topping.getPrice().toString());
    }

    @Override
    public void toppingClick(ToppingOrder toppingOrder, FoodOrder foodOrder) {
        revertFoodChooseBackground();
        chooseFoodOrder = foodOrder;
        chooseToppingOrder = toppingOrder;
        mDrawerLayout.openDrawer(GravityCompat.START);
    }
    public void deleteTopping(ToppingOrder chooseToppingOrder,FoodOrder chooseFoodOrder){

        if(chooseFoodOrder!=null ){
            if(chooseFoodOrder.getPrint()){
                return;
            }
            realm.beginTransaction();
            try {
                chooseToppingOrder.deleteFromRealm();
                chooseFoodOrder.getTotalPrice();
                setMyTitle();
            }catch (Exception e){

            }

            realm.commitTransaction();
            chooseToppingOrder = null;
        }

    }

    public void chooseTopping(){


        if(chooseToppingOrder!=null){
            deleteTopping(chooseToppingOrder,chooseFoodOrder);
            return;
        }
        if(chooseFoodOrder == null){
            return;
        }

        LayoutInflater factory = LayoutInflater.from(this);
        final View toppingDialogView = factory.inflate(R.layout.popup_add_topping, null);
        chooseToppingDialog = new AlertDialog.Builder(this,android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen).create();
        chooseToppingDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        gridview_topping = toppingDialogView.findViewById(R.id.gridview_topping);
        txt_topping_name = toppingDialogView.findViewById(R.id.txt_topping_name);
        txt_topping_price = toppingDialogView.findViewById(R.id.txt_topping_price);
        realm.beginTransaction();
        RealmResults<Topping> toppings = realm.where(Topping.class).equalTo("status",true).findAll();
        realm.commitTransaction();
        UserToppingAdapter mAdapter = new UserToppingAdapter(context, toppings, ChooseFoodActivity.this);
        gridview_topping.setAdapter( mAdapter);
        ViewGroup.LayoutParams params = gridview_topping.getLayoutParams();
        gridview_topping.setLayoutParams(params);
        chooseToppingDialog.setView(toppingDialogView);
        chooseToppingDialog.setCancelable(false);

        toppingDialogView.findViewById(R.id.popup_btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseToppingDialog.cancel();
            }
        });
        toppingDialogView.findViewById(R.id.popup_btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!txt_topping_name.getText().toString().trim().equals("")){
                    if(chooseFoodOrder.getPrint()){
                        return;
                    }
                    realm.beginTransaction();
                    ToppingOrder toppingOrder = realm.createEmbeddedObject(ToppingOrder.class,chooseFoodOrder,"toppings");
                    toppingOrder.setName(txt_topping_name.getText().toString());
                    String priceStr = !txt_topping_price.getText().toString().trim().equals("")?txt_topping_price.getText().toString():"0";
                    toppingOrder.setPrice(Double.parseDouble(priceStr));
                    chooseFoodOrder.getTotalPrice();
                    setMyTitle();
                    UndoItem undoItem = new UndoItem();
                    undoItem.undoEnum = UndoEnum.AddTopping;
                    undoItem.toppingOrder = toppingOrder;
                    undoItem.foodOrder = chooseFoodOrder;
                    undoItemStack.push(undoItem);
                    realm.commitTransaction();
                    chooseToppingDialog.cancel();
                }
            }
        });
        chooseToppingDialog.show();



    }


    public void deleteFoodOrder(FoodOrder chooseFoodOrder){

        if(chooseFoodOrder.getPrint()){
            return;
        }
        realm.beginTransaction();
        try {
           if(chooseFoodOrder == null){
               return;
           }
           chooseFoodOrder.deleteFromRealm();
           for(int i =0 ; i < order.getSubOrders().size(); i++){
               if(order.getSubOrders().get(i).getFoodOrders().size() == 0){
                   order.getSubOrders().get(i).deleteFromRealm();
//                order.getSubOrders().remove(order.getSubOrders().get(i).getFoodOrders().size() );
                   break;
               }
           }
           setMyTitle();
           mAdapter.notifyDataSetChanged();
       }catch (Exception e){
           e.printStackTrace();
       }
        realm.commitTransaction();

    }


    public void trashFoodOrder(FoodOrder chooseFoodOrder){

        if(!chooseFoodOrder.getPrint()){
            deleteFoodOrder(chooseFoodOrder);
            return;
        }
        realm.beginTransaction();
        try {
            if(chooseFoodOrder == null){
                return;
            }
            cloneFoodOrder(chooseFoodOrder,foodLog.getOrder());
            chooseFoodOrder.deleteFromRealm();
            for(int i =0 ; i < order.getSubOrders().size(); i++){
                if(order.getSubOrders().get(i).getFoodOrders().size() == 0){
                    order.getSubOrders().get(i).deleteFromRealm();
                    break;
                }
            }
            setMyTitle();
            mAdapter.notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
        }
        realm.commitTransaction();

    }

    public void cloneFoodAndSave(FoodOrder myFood){
//        Table myTable = realm.where(Table.class).equalTo("id", DataUtil.unManageTable).findFirst();
//        if(myTable == null ){
//            myTable = realm.createObject(Table.class, DataUtil.unManageTable);
//            myTable.setName("Log");
//            myTable.setHaveOrder(false);
//            myTable.setStatus(false);
//        }
//        Order myOrder = realm.createObject(Order.class, UUID.randomUUID());
//        myOrder.setTable(myTable);
//        myOrder.setPay(true);
//        myOrder.setOrderTime(new Date());
//        myOrder.setPayTime(new Date());
//
//        realm.createEmbeddedObject(SubOrder.class,myOrder,"subOrders");
//
//        SubOrder subOrder = myOrder.getSubOrders().get(order.getSubOrders().size()-1);
//
//        cloneFoodOrder(myFood,subOrder,table.getName());


    }
    public void editCountFoodOrder(){
        if(chooseFoodOrder == null){
            return;
        }
        if(chooseFoodOrder.getPrint()){
            return;
        }
        LayoutInflater factory = LayoutInflater.from(this);
        final View fDialogView = factory.inflate(R.layout.popup_update_food_order_count, null);
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        TextView tv_popup_title = fDialogView.findViewById(R.id.tv_title_popup);
        EditText txt_food_count = fDialogView.findViewById(R.id.popup_food_count);
        tv_popup_title.setText("Edit count:"+chooseFoodOrder.getName());
        txt_food_count.setText(""+chooseFoodOrder.getCount());
        dialog.setView(fDialogView);
        dialog.setCancelable(false);
        fDialogView.findViewById(R.id.popup_btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        fDialogView.findViewById(R.id.popup_btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(txt_food_count.getText().toString());
                if(count>=1){

                    realm.beginTransaction();
                    chooseFoodOrder.setCount(count);
                    chooseFoodOrder.getTotalPrice();
                    setMyTitle();
                    realm.commitTransaction();
                    chooseFoodOrder = null;
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public void saveOrder(){
        realm.beginTransaction();
        if(oldOrder != null){
            oldOrder.deleteFromRealm();
            oldOrder = null;
        }
        Order unManagerOder = realm.copyFromRealm(order);
        unManagerOder.setId((UUID.randomUUID()));
        unManagerOder.setTable(null);

        order.deleteFromRealm();
        order = realm.copyToRealm(unManagerOder);
        loadOrderDataView();
        if(order.isHaveFood()){
            order.setTable(table);
            table.setHaveOrder(true);
            table.setTotalBill(order.getTotalBill());
        }
        else {
            table.setHaveOrder(false);
            table.setTotalBill(0.0);
        }
        //save trash
        if(foodLog.getOrder().getFoodOrders().size() > 0){
            FoodLog myFoodLog = realm.copyFromRealm(foodLog);
            myFoodLog.setId((UUID.randomUUID()));
            myFoodLog.setTable(table);
            myFoodLog.setRemoveTime(new Date());
            realm.copyToRealm(myFoodLog);
        }
        realm.commitTransaction();
    }
    EscPosPrinter printer;
    TcpConnection tcpConnection;
    boolean rs = false;
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
                showAlertDialogThread("Broken connection "+title+ "printer");
                rs = false;
            }


        }
        finally {
            if(printer != null)
                printer.disconnectPrinter();

        }
        return  rs;
    }
    public boolean printFood(int foodType,String title,String ip,int port) {

        rs = false;
        String tableName = table.getName();
        String date = DateTimeUtil.getCurrentLocalDateTimeStamp();
        String printStr =
                "[L]<b><font size='tall'>Tischnr. "+tableName+"</font></b>" +
                        "[R]<font size='tall'>"+date+"</font>\n" +
                        "[C]<font size='tall'>"+title+"</font>\n" +
                        "[C] _____________________________\n";
        int i = 0;
        boolean isPrint = false;
        boolean isNoSize = true;
        try {
            isNoSize =  order.getSubOrders().size() ==0;

        }catch (Exception e){

        }
        if(order.getSubOrders()==null || isNoSize)
            return false;
        for(SubOrder subOrder:order.getSubOrders()){
            i++;
            printStr += "[C]<font size='tall'><b>-----------"+i+"-----------</b></font>\n";
            List<FoodOrder> foodOrders = subOrder.getListFoodByType(foodType);
            for(FoodOrder foodOrder:foodOrders){
                if(!foodOrder.getPrint()){
                    isPrint = true;
                    String optionsName = foodOrder.getOption()!=null ? "-"+foodOrder.getOption().getName() : "";
                    printStr +="[L]<font size='tall'>"+foodOrder.getCount()+" x "+foodOrder.getName()+optionsName+"</font>\n";
                    for(ToppingOrder topping : foodOrder.getToppings()){
                        printStr +="[L]\t<font size='tall' color='bg-black'><b>>"+topping.getName()+"</b></font>\n";
                    }
                }
            }
        }
        if(!isPrint){
            return true;
        }
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
                    rs = printLoop(0,ip,port,finalPrintStr,title);
                } catch (Exception e) {

                    rs = false;

                }
//                finally {
//                    if(printer != null)
//                        printer.disconnectPrinter();
//
//                }
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            showAlertDialogThread("Thread Error "+title+" printer");
            rs = false;
        }
        return rs?true:false;
    }
    public void updateFoodPrint(int foodType){
        realm.beginTransaction();
        for(SubOrder subOrder:order.getSubOrders()){
            List<FoodOrder> foodOrders = subOrder.getListFoodByType(foodType);
            for(FoodOrder foodOrder:foodOrders){
                foodOrder.setPrint(true);
            }
        }
        realm.commitTransaction();
    }
    Setting setting;
    Boolean isClickSave = false;
    public boolean printfFood(){
        boolean isFinish=false;

        setting = realm.where(Setting.class).equalTo("id", DataUtil.settingId).findFirst();

        if(setting != null ){
            if (setting.getKuchePrinter() == null){
                new AlertDialog.Builder(context)
                        .setTitle("Can't find Kueche printer setting!")
                        .show();
                this.isClickSave = false;
                dialog.dismiss();
                return false;
            }
            if (setting.getSushiPrinter() == null){
                new AlertDialog.Builder(context)
                        .setTitle("Can't find SuShi printer setting!")
                        .show();

                this.isClickSave = false;
                dialog.dismiss();
                return false;
            }
            if (setting.getGetrankPrinter() == null){
                new AlertDialog.Builder(context)
                        .setTitle("Can't find Getraenk printer setting!")
                        .show();
                this.isClickSave = false;
                dialog.dismiss();
                return false;
            }
            String kucheIp = setting.getKuchePrinter().getIP();
            int kuchePort = setting.getKuchePrinter().getPort();

            String sushiIp = setting.getSushiPrinter().getIP();
            int sushiPort = setting.getSushiPrinter().getPort();

            String getrankIp = setting.getGetrankPrinter().getIP();
            int getrankPort = setting.getGetrankPrinter().getPort();


            try {

                if(printFood(0,"Kueche",kucheIp,kuchePort)){
                    updateFoodPrint(0);
                    if(printFood(3,"Vor - Kueche",kucheIp,kuchePort)) {
                        updateFoodPrint(3);
                        if(printFood(1,"Sushi",sushiIp,sushiPort)){
                            updateFoodPrint(1);
                            if(printFood(4,"Vor - Sushi",sushiIp,sushiPort)){
                                updateFoodPrint(4);
                                boolean isPrintfGetrank = printFood(2,"Getraenk",getrankIp,getrankPort);
                                if(isPrintfGetrank){
                                    updateFoodPrint(2);
                                    isFinish = true;
                                    dialog.dismiss();

                                }
                            }
                        }
                    }

                }

            }catch (Exception e){
                e.printStackTrace();
                new AlertDialog.Builder(context)
                        .setTitle("Error")
                        .setMessage("Print Error")
                        .show();
                isFinish = false;

            }

        }else {
            new AlertDialog.Builder(context)
                    .setTitle("Error")
                    .setMessage("Can't find printer setting!")
                    .show();
            isFinish = false;
        }

        isClickSave = false;
        dialog.dismiss();
       return isFinish;
    }


    public void saveOrderPrintf(View view){

        if(order.getSubOrders().size()  == 0){
            finish();
        }
        if(isClickSave == true) return;
        isClickSave = true;
        dialog.show();
        saveOrder();
        if(order == null){
            finish();
        }
        if(printfFood()){
            finish();
        }

    }

    boolean isAddCustomFoodPopup = false;
    public void addCustomFood(View view){
        if (isAddCustomFoodPopup == true){
            return;
        }
        isAddCustomFoodPopup = true;
        int  id = view.getId();
        if(id == R.id.kuche_id){

            customFoodName = "Kueche";
            customFoodType = 0;
        }
        else if(id == R.id.sushi_id){
            customFoodName = "Sushi";
            customFoodType = 1;

        }
        else if(id == R.id.getrank_id){
            customFoodName = "Getraenk";
            customFoodType = 2;
        }

        addCustomFoodDialog = new AlertDialog.Builder(this).create();
        LayoutInflater factory = LayoutInflater.from(this);
        final View optionDialogView = factory.inflate(R.layout.popup_food_custom_price, null);
        txt_customFoodPrice = optionDialogView.findViewById(R.id.txt_price);
        tv_customFoodTitle = optionDialogView.findViewById(R.id.tv_title_popup);
        tv_customFoodTitle.setText(customFoodName);
        txt_customFoodPrice.setFocusableInTouchMode(true);
        txt_customFoodPrice.requestFocus();
        addCustomFoodDialog.setView(optionDialogView);
        addCustomFoodDialog.setCancelable(false);
        addCustomFoodDialog.show();
        addCustomFoodDialog.findViewById(R.id.popup_btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCustomFoodDialog.dismiss();
                isAddCustomFoodPopup = false;
            }
        });
        addCustomFoodDialog.findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                realm.beginTransaction();
                if(order.getSubOrders().size() == 0){
                    realm.createEmbeddedObject(SubOrder.class,order,"subOrders");
                    mAdapter.notifyDataSetChanged();
                }
                SubOrder subOrder = order.getSubOrders().get(order.getSubOrders().size()-1);
                FoodOrder foodOrder = realm.createEmbeddedObject(FoodOrder.class,subOrder,"foodOrders");
                foodOrder.setName(customFoodName);
                foodOrder.setId(UUID.randomUUID());
                foodOrder.setFoodType(customFoodType);
                String priceStr = txt_customFoodPrice.getText().toString().trim().equals("")?"0":txt_customFoodPrice.getText().toString();
                foodOrder.setPrice(Double.parseDouble(priceStr));
                setMyTitle();
                UndoItem undoItem = new UndoItem();
                undoItem.undoEnum = UndoEnum.AddFood;
                undoItem.foodOrder = foodOrder;
                undoItemStack.push(undoItem);
                realm.commitTransaction();
                addCustomFoodDialog.dismiss();
                isAddCustomFoodPopup = false;
                chooseFoodOrder = foodOrder;
            }
        });
    }
    void showAlertDialogThread(String title){
        ChooseFoodActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                new AlertDialog.Builder(context)
                        .setTitle("Error")
                        .setMessage(title)
                        .show();
            }
        });
    }
    void showToastThread(String title){
        ChooseFoodActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(context,title,Toast.LENGTH_SHORT).show();

            }
        });
    }
    boolean isAbschlussClick = false;
    public void abschlussClick(View view){
        saveOrder();
        if(isAbschlussClick) return;
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setMessage("Wollen Sie dieser Tisch abschiliessen?");
        b.setPositiveButton("JA", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                saveOrder();
                if(printfFood()){
                    showPayPopup();
                }
                isAbschlussClick = false;
                dialog.cancel();
            }
        });
        b.setNegativeButton("ABBRECHEN", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                isAbschlussClick = false;
                dialog.cancel();

            }
        });
        AlertDialog al = b.create();
        al.show();
    }

    CheckBox checkBox_isPrintBill;
    CheckBox checkBox_isPrintTax;
    TextView tv_totalBoll;
    ImageView btn_cutBill;
    ImageView btn_viewBill;
    ImageView btn_payBill;
    boolean isPaying = false;

    ImageView btn_money;
    ImageView btn_card;
    ImageView btn_paypal;
    private void showPayPopup(){
        if( order == null ||order.getTotalBill2() == null || order.getTotalBill2().equals(0d))
        {
            new AlertDialog.Builder(context)
                    .setTitle("Error")
                    .setMessage("Total is null!")
                    .show();
            return;
        }
        realm.beginTransaction();
        order.setStatus(DataUtil.CashNumber);
        realm.commitTransaction();
        LayoutInflater factory = LayoutInflater.from(this);
        final View payDialogView = factory.inflate(R.layout.popup_pay_order, null);
        payOrderDialog = new AlertDialog.Builder(this).create();
        payOrderDialog.setView(payDialogView);
        payOrderDialog.setCancelable(false);
        checkBox_isPrintBill = payDialogView.findViewById(R.id.checkBox_tax);
        checkBox_isPrintTax = payDialogView.findViewById(R.id.checkBox_printBill);
        tv_totalBoll = payDialogView.findViewById(R.id.tv_total);
        btn_cutBill = payDialogView.findViewById(R.id.btn_cut_order);
        btn_viewBill = payDialogView.findViewById(R.id.btn_view_bill);
        btn_payBill = payDialogView.findViewById(R.id.btn_pay_orderImage);
        btn_money = payDialogView.findViewById(R.id.imageView_money);
        btn_card = payDialogView.findViewById(R.id.imageView_card);
        btn_paypal = payDialogView.findViewById(R.id.imageView_paypal);

        payDialogView.findViewById(R.id.popup_btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payOrderDialog.dismiss();
            }
        });

        btn_payBill.setOnClickListener(payBillOnclick);
        btn_viewBill.setOnClickListener(viewBillOnclick);
        btn_cutBill.setOnClickListener(cutBillOnclick);
        tv_totalBoll.setText(DoubleUtil.Round2String(order.getTotalBill2()) + "€");



        btn_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_money.setBackgroundColor(Color.parseColor("#f9ca24"));
                btn_card.setBackgroundColor(Color.parseColor("#ffffff"));
                btn_paypal.setBackgroundColor(Color.parseColor("#ffffff"));
                realm.beginTransaction();
                order.setStatus(DataUtil.CashNumber);
                realm.commitTransaction();
            }
        });

        btn_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_card.setBackgroundColor(Color.parseColor("#f9ca24"));
                btn_money.setBackgroundColor(Color.parseColor("#ffffff"));
                btn_paypal.setBackgroundColor(Color.parseColor("#ffffff"));
                realm.beginTransaction();
                order.setStatus(DataUtil.CardNumber);
                realm.commitTransaction();

            }
        });

        btn_paypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_paypal.setBackgroundColor(Color.parseColor("#f9ca24"));
                btn_card.setBackgroundColor(Color.parseColor("#ffffff"));
                btn_money.setBackgroundColor(Color.parseColor("#ffffff"));
                realm.beginTransaction();
                order.setStatus(DataUtil.PaypalNumber);
                realm.commitTransaction();
            }
        });

        payOrderDialog.show();
    }
    View.OnClickListener payBillOnclick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(isPaying == true) return;
            isPaying = true;
            dialog.show();
            setting = realm.where(Setting.class).equalTo("id", DataUtil.settingId).findFirst();
            boolean isPrintBill = true;
            boolean isFinish = true;
            saveOrder();
            if (checkBox_isPrintBill.isChecked()){
                if(setting != null && setting.getBillPrinter() != null){
                    isPrintBill = printBill(setting.getBillPrinter().getIP(),setting.getBillPrinter().getPort(),order);

                }else{
                    new AlertDialog.Builder(context)
                            .setTitle("Error")
                            .setMessage("Can't find bill printer setting!")
                            .show();
                    isFinish = false;
                }

            }
            if (checkBox_isPrintTax.isChecked() && isPrintBill){
                if(setting != null && setting.getBillPrinter() != null){
                    isFinish = printTax(setting.getBillPrinter().getIP(),setting.getBillPrinter().getPort(),order);
                }else{
                    new AlertDialog.Builder(context)
                            .setTitle("Error")
                            .setMessage("Can't find bill printer setting!")
                            .show();
                    isFinish = false;
                }
            }
            isPaying = false;
            dialog.dismiss();
            realm.beginTransaction();
            Date date =  new Date();
            if(!order.isPay()){
                order.setPayTime(date);
            }
            order.setPay(true);
            table.setTotalBill(0.0);
            table.setHaveOrder(false);
            table.setNote("");
            realm.commitTransaction();
            if(isFinish==true){
                finish();
            }
        }
    };
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
        String tableName = table.getName();
        String date = DateTimeUtil.getCurrentLocalDateTimeStamp();
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
                    rs = printLoop(0,ip,port,finalPrintStr,"bill");
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
//                    printer = new EscPosPrinter(tcpConnection, 203, 80f, DataUtil.printerNbrCharactersPerLine, new EscPosCharsetEncoding("CP850", 2));
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
            showAlertDialogThread("Thread Error bill printer");
            rs = false;
        }
        return rs?true:false;
    }
    public boolean printTax(String ip,int port,Order od) {
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
        String printStr =
                "[C]<b><font size='normal'>TNT THE MODERN ASIA</font></b>\n" +
                        "[C]<b><font size='normal'>Evertsstrasse 14</font></b>\n" +
                        "[C]<b><font size='normal'>47798 Krefeld</font></b>\n" +
                        "[C]<b><font size='normal'>Tel.: 02151 4467688</font></b>\n" +
                        "[C]<b><font size='normal'>SteuerNr.: 117/5398/2923</font></b>\n" +
                        "[C] _______________________________\n"+
                        "[L]<font size='normal'>\n</font>\n"+
                        "[L]<font size='normal'>Angaben zum Nachweis der Höhe der</font>\n" +
                        "[L]<font size='normal'>betrieblichen Veranlassung von</font>\n" +
                        "[L]<font size='normal'>Betriebsaufwendungen</font>\n" +
                        "[L]<font size='normal'>\n\n</font>\n" +
                        "[L]<font size='normal'>Tag der Bewirtung</font>\n" +
                        "[L]<font size='normal'>\n</font>\n" +
                        "[L]<font size='normal'>Ort der Bewirtung (Stempel):</font>\n" +
                        "[L]<font size='normal'>\n\n\n</font>\n"+
                        "[L]<font size='normal'>Bewirtete Personen:</font>\n" +
                        "[L]<font size='normal'>\n\n\n</font>\n"+
                        "[L]<font size='normal'>Höhe der Aufwendungen:</font>\n"+
                        "[L]<font size='normal'>\n</font>\n" +
                        "[L]<font size='normal'>[X] bei Bewirting in Gaststaetten lt.</font>\n" +
                        "[L]<font size='normal'>beigefuegter Rechnung</font>\n" +
                        "[L]<font size='normal'>\n</font>\n" +
                        "[L]<font size='normal'>EUR "+DoubleUtil.Round2String(od.getTotalBill2())+"</font>\n"+
                        "[L]<font size='normal'>Ort und Datum:</font>\n"+
                        "[L]<font size='normal'>Unterschrift:</font>\n"+
                        "[L]<font size='normal'>\n\n\n</font>\n";

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
//                    printer = new EscPosPrinter(tcpConnection, 203, 80f, 42, new EscPosCharsetEncoding("CP850", 2));
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
            showAlertDialogThread("Thread Error bill printer");
            rs = false;
        }
        return rs?true:false;
    }
    public void viewBill(Order od){
        LayoutInflater factory = LayoutInflater.from(this);
        final View viewBillDialogView =  factory.inflate(R.layout.popup_show_order, null);
        showOrderDialog =new AlertDialog.Builder(this,android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen).create();
        showOrderDialog.setCancelable(false);
        showOrderDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        showOrderDialog.setView(viewBillDialogView);
        viewBillDialogView.findViewById(R.id.popup_screen).setRotation(0);
        String date = DateTimeUtil.getCurrentLocalDateTimeStamp();
        ((TextView)viewBillDialogView.findViewById(R.id.tv_tableName)).setText("Tischnr. "+table.getName());
        ((TextView)viewBillDialogView.findViewById(R.id.tv_time)).setText(date);
        ((TextView)viewBillDialogView.findViewById(R.id.tv_total)).setText(DoubleUtil.Round2String(od.getTotalBill2())+" €");

        RecyclerView mRecyclerView = viewBillDialogView.findViewById(R.id.recyclerview_subFood);
        ViewBill_SuborderAdapter mAdapter = new ViewBill_SuborderAdapter( od.getSubOrders(),false,ChooseFoodActivity.this);
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
    boolean isViewing = false;
    View.OnClickListener viewBillOnclick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(isViewing == true) return;
            isViewing = true;
            viewBill(order);
            isViewing = false;

        }
    };


    public void noteClick(View view){
        LayoutInflater factory = LayoutInflater.from(this);
        final View viewAddNote =  factory.inflate(R.layout.pop_up_add_note, null);
        showNoteDialog =new AlertDialog.Builder(this).create();
        showNoteDialog.setCancelable(false);
        showNoteDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        showNoteDialog.setView(viewAddNote);
        final EditText txt_note = viewAddNote.findViewById(R.id.txt_food_note);
        txt_note.setText(table.getNote()!=null?table.getNote():"");
        final ImageView clossNote = viewAddNote.findViewById(R.id.popup_btn_cancel2);

        clossNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                realm.beginTransaction();
                String txt = txt_note.getText().toString().trim();
                table.setNote(txt);
                setMyTitle();
                realm.commitTransaction();
                showNoteDialog.cancel();
            }
        });
        showNoteDialog.show();
    }
    public void setMyTitle(){
        toolbar_tisch.setText("Tisch "+table.getName()+(table.getNote().equals("")?"":"(*)")+"-"+DoubleUtil.Round2(order.getTotalBill())+"€");

    }

    ToppingOrder cloneToppingOrder(ToppingOrder topping, FoodOrder foodOrder){
        ToppingOrder toppingOrder = realm.createEmbeddedObject(ToppingOrder.class,foodOrder,"toppings");
        toppingOrder.setId(topping.getId());
        toppingOrder.setName(topping.getName());
        toppingOrder.setPrice(topping.getPrice());
        return toppingOrder;
    }
    OptionOrder cloneOptionOrder(OptionOrder option,FoodOrder foodOrder){
        OptionOrder optionOrder = realm.createEmbeddedObject(OptionOrder.class,foodOrder,"option");
        optionOrder.setPrice(option.getPrice());
        optionOrder.setName(option.getName());
        return  optionOrder;
    }
    FoodOrder cloneFoodOrder(FoodOrder food ,SubOrder subOrder){
        FoodOrder foodOrder = realm.createEmbeddedObject(FoodOrder.class,subOrder,"foodOrders");
        foodOrder.setId(food.getId());
        foodOrder.setPrice(food.getPrice());
        foodOrder.setTotalPrice(food.getTotalPrice2());
        foodOrder.setCount(food.getCount());
        foodOrder.setName(food.getName());
        foodOrder.setFoodType(food.getFoodType());
        foodOrder.setPrint(food.getPrint());
        for(ToppingOrder toppingOrder:food.getToppings()){
            cloneToppingOrder(toppingOrder,foodOrder);
        }
        if(food.getOption()!=null){
            cloneOptionOrder(food.getOption(),foodOrder);

        }

        return  foodOrder;
    }
    FoodOrder cloneFoodOrder(FoodOrder food ,SubOrder subOrder,String table){
        FoodOrder foodOrder = realm.createEmbeddedObject(FoodOrder.class,subOrder,"foodOrders");
        foodOrder.setId(food.getId());
        foodOrder.setPrice(food.getPrice());
        foodOrder.setTotalPrice(food.getTotalPrice2());
        foodOrder.setCount(food.getCount());
        foodOrder.setName(food.getName()+"-"+table);
        foodOrder.setFoodType(food.getFoodType());
        foodOrder.setPrint(food.getPrint());
        for(ToppingOrder toppingOrder:food.getToppings()){
            cloneToppingOrder(toppingOrder,foodOrder);
        }
        if(food.getOption()!=null){
            cloneOptionOrder(food.getOption(),foodOrder);

        }

        return  foodOrder;
    }

    public Order cloneOrder(Order oldOrder,UUID unManageOrderId){
        realm.beginTransaction();
        Order temporaryOrder = realm.where(Order.class).equalTo("id",unManageOrderId).findFirst();
        if(temporaryOrder != null){
            temporaryOrder.deleteFromRealm();
        }
        Order unManagerOder = null;
        unManagerOder = realm.copyFromRealm(oldOrder);
        unManagerOder.setId(unManageOrderId);
        unManagerOder.setTable(null);
        temporaryOrder = realm.copyToRealm(unManagerOder);
        realm.commitTransaction();
        return temporaryOrder;
    }
    public void createToOrder(){
        realm.beginTransaction();
        toOrder = realm.where(Order.class).equalTo("id",DataUtil.unManageOrderId2).findFirst();
        if(toOrder != null){
            toOrder.deleteFromRealm();
        }
        toOrder = realm.createObject(Order.class,DataUtil.unManageOrderId2);
        realm.createEmbeddedObject(SubOrder.class,toOrder,"subOrders");
        realm.commitTransaction();
    }
    boolean isCutting = false;
    View.OnClickListener cutBillOnclick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isCutting){
                return;
            }
            isCutting = true;
            cutBill();
            isCutting = false;
        }
    };
    Order fromOrder;
    Order toOrder;
    RecyclerView fromRecyclerView;
    CutBillSuborderAdapter fromAdapter;
    RecyclerView toRecyclerView;
    TextView tv_total_1;
    TextView tv_total_2;

    public void cutBill(){
        LayoutInflater factory = LayoutInflater.from(this);
        final View viewBillDialogView =  factory.inflate(R.layout.popup_cut_order, null);
        cutOrderDialog =new AlertDialog.Builder(this,android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen).create();
        cutOrderDialog.setCancelable(false);
        cutOrderDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        cutOrderDialog.setView(viewBillDialogView);
        fromRecyclerView = viewBillDialogView.findViewById(R.id.recyclerview_1);
        tv_total_1 =  viewBillDialogView.findViewById(R.id.tv_total_1);
        tv_total_2 =  viewBillDialogView.findViewById(R.id.tv_total_2);

        fromOrder = cloneOrder(order,DataUtil.unManageOrderId1);
        fromAdapter = new CutBillSuborderAdapter( fromOrder.getSubOrders(),false,context,ChooseFoodActivity.this,1);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        fromRecyclerView.setLayoutManager(linearLayoutManager);
        fromRecyclerView.setAdapter(fromAdapter);

        createToOrder();
        toRecyclerView = viewBillDialogView.findViewById(R.id.recyclerview_2);
        CutBillSuborderAdapter mAdapter2 = new CutBillSuborderAdapter( toOrder.getSubOrders(),false,context,ChooseFoodActivity.this,2);
        final LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        toRecyclerView.setLayoutManager(linearLayoutManager2);
        toRecyclerView.setAdapter(mAdapter2);

        viewBillDialogView.findViewById(R.id.popup_btn_cancel4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cutOrderDialog.cancel();
            }

        });
        viewBillDialogView.findViewById(R.id.btn_cut_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPayPopup_cut();
            }

        });
        realm.beginTransaction();
        cutBillChange();
        realm.commitTransaction();

        cutOrderDialog.show();
    }

    @Override
    public void cutFood1(FoodOrder foodOrder) {
        realm.beginTransaction();
            cloneFoodOrder(foodOrder,toOrder.getSubOrders().get(0));
            foodOrder.deleteFromRealm();
            for(int i =0 ; i < fromOrder.getSubOrders().size(); i++){
                if(fromOrder.getSubOrders().get(i).getFoodOrders().size() == 0 && fromOrder.getSubOrders().size()>1){
                    fromOrder.getSubOrders().get(i).deleteFromRealm();
                    break;
                }
            }
            fromAdapter.notifyDataSetChanged();
            cutBillChange();

        realm.commitTransaction();
    }

    @Override
    public void cutFood2(FoodOrder foodOrder) {
        realm.beginTransaction();
        cloneFoodOrder(foodOrder,fromOrder.getSubOrders().get(0));
        foodOrder.deleteFromRealm();
        cutBillChange();
        realm.commitTransaction();
    }
    public void cutBillChange(){
        fromOrder.getTotalBill();
        toOrder.getTotalBill();
        tv_total_1.setText("Tisch "+table.getName() +" - Summe:" +DoubleUtil.Round2String(fromOrder.getTotalBill2())+"€");
        tv_total_2.setText("Summe:" +DoubleUtil.Round2String(toOrder.getTotalBill2())+"€");

    }
    public void orderChange(){
        realm.beginTransaction();
        tv_totalBoll.setText(order.getTotalBill() + "€");
        realm.commitTransaction();


    }
    CheckBox checkBox_isPrintBill_cut;
    CheckBox checkBox_isPrintTax_cut;
    TextView tv_totalBoll_cut;
    ImageView btn_viewBill_cut;
    ImageView btn_payBill_cut;
    boolean isPaying_cut = false;
    ImageView btn_cut_money;
    ImageView btn_cut_card;
    ImageView btn_cut_paypal;
    private void showPayPopup_cut(){
        if( toOrder == null ||toOrder.getTotalBill2() == null || toOrder.getTotalBill2().equals(0d))
        {
            new AlertDialog.Builder(context)
                    .setTitle("Error")
                    .setMessage("Total is null!")
                    .show();
            return;
        }
        LayoutInflater factory = LayoutInflater.from(this);
        realm.beginTransaction();
        toOrder.setStatus(DataUtil.CashNumber);
        realm.commitTransaction();
        final View payDialogView = factory.inflate(R.layout.popup_pay_cut_order, null);
        payOrderDialog_cut = new AlertDialog.Builder(this).create();
        payOrderDialog_cut.setView(payDialogView);
        payOrderDialog_cut.setCancelable(false);
        checkBox_isPrintBill_cut = payDialogView.findViewById(R.id.checkBox_tax);
        checkBox_isPrintTax_cut = payDialogView.findViewById(R.id.checkBox_printBill);
        tv_totalBoll_cut = payDialogView.findViewById(R.id.tv_total);
        btn_viewBill_cut = payDialogView.findViewById(R.id.btn_view_bill);
        btn_payBill_cut = payDialogView.findViewById(R.id.btn_pay_orderImage);
        btn_payBill_cut.setOnClickListener(payBillCutOnclick);
        btn_viewBill_cut.setOnClickListener(viewBillOnclickCut);
        tv_totalBoll_cut.setText(DoubleUtil.Round2String(toOrder.getTotalBill2()) + "€");

        btn_cut_money = payDialogView.findViewById(R.id.imageView_money);
        btn_cut_card = payDialogView.findViewById(R.id.imageView_card);
        btn_cut_paypal = payDialogView.findViewById(R.id.imageView_paypal);

        payDialogView.findViewById(R.id.popup_btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payOrderDialog_cut.dismiss();
            }
        });


        btn_cut_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_cut_money.setBackgroundColor(Color.parseColor("#f9ca24"));
                btn_cut_card.setBackgroundColor(Color.parseColor("#ffffff"));
                btn_cut_paypal.setBackgroundColor(Color.parseColor("#ffffff"));
                realm.beginTransaction();
                toOrder.setStatus(DataUtil.CashNumber);
                realm.commitTransaction();
            }
        });

        btn_cut_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_cut_card.setBackgroundColor(Color.parseColor("#f9ca24"));
                btn_cut_money.setBackgroundColor(Color.parseColor("#ffffff"));
                btn_cut_paypal.setBackgroundColor(Color.parseColor("#ffffff"));
                realm.beginTransaction();
                toOrder.setStatus(DataUtil.CardNumber);
                realm.commitTransaction();

            }
        });

        btn_cut_paypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_cut_paypal.setBackgroundColor(Color.parseColor("#f9ca24"));
                btn_cut_card.setBackgroundColor(Color.parseColor("#ffffff"));
                btn_cut_money.setBackgroundColor(Color.parseColor("#ffffff"));
                realm.beginTransaction();
                toOrder.setStatus(DataUtil.PaypalNumber);
                realm.commitTransaction();
            }
        });
        payOrderDialog_cut.show();
    }
    View.OnClickListener payBillCutOnclick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(isPaying_cut == true) return;
            isPaying_cut = true;
            dialog.show();
            setting = realm.where(Setting.class).equalTo("id", DataUtil.settingId).findFirst();
            boolean isPrintBill = true;
            boolean isFinish = false;
            if (checkBox_isPrintBill_cut.isChecked()){
                if(setting != null && setting.getBillPrinter() != null){
                    isPrintBill = printBill(setting.getBillPrinter().getIP(),setting.getBillPrinter().getPort(),toOrder);

                }else{
                    new AlertDialog.Builder(context)
                            .setTitle("Error")
                            .setMessage("Can't find bill printer setting!")
                            .show();
                }

            }
            if (checkBox_isPrintTax_cut.isChecked() && isPrintBill){
                if(setting != null && setting.getBillPrinter() != null){
                    isFinish = printTax(setting.getBillPrinter().getIP(),setting.getBillPrinter().getPort(),toOrder);
                }else{
                    new AlertDialog.Builder(context)
                            .setTitle("Error")
                            .setMessage("Can't find bill printer setting!")
                            .show();
                }
            }
            isPaying_cut = false;
            Date date =  new Date();
            Order subOrder = cloneOrder(toOrder,UUID.randomUUID());
            realm.beginTransaction();
            subOrder.setPayTime(date);
            subOrder.setPay(true);
            subOrder.setTable(table);
            for (int i = 0; i<toOrder.getSubOrders().get(0).getFoodOrders().size();i++) {
                FoodOrder mFoodOrder = toOrder.getSubOrders().get(0).getFoodOrders().get(i);
                deleteFoodById(mFoodOrder.getId(),order);
                deleteFoodById(mFoodOrder.getId(),toOrder);
                i -- ;
            }
            table.setTotalBill(order.getTotalBill());
            if(!order.isHaveFood()){
                isFinish = true;
                order.deleteFromRealm();
                table.setTotalBill(0.0);
                table.setHaveOrder(false);
                table.setNote("");
            }
            dialog.dismiss();
            realm.commitTransaction();
            if(isFinish){
                payOrderDialog_cut.cancel();
                finish();
            }else {
                orderChange();
                payOrderDialog_cut.cancel();
            }

        }
    };

    public void deleteFoodById(UUID id,Order order){
        for (SubOrder subOrder:order.getSubOrders()) {
            for(int i=0;i<subOrder.getFoodOrders().size();i++){
                if (subOrder.getFoodOrders().get(i).getId().equals(id)){
                    subOrder.getFoodOrders().get(i).deleteFromRealm();
                    return;
                }
            }
        }
    }
    boolean isViewingCut = false;
    View.OnClickListener viewBillOnclickCut = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(isViewingCut == true) return;
            isViewingCut = true;
            viewBill(toOrder);
            isViewingCut = false;

        }
    };
    public void undo(View view){
        if(undoItemStack.size() >0){
            UndoItem undoItem = undoItemStack.pop();
            if(undoItem.undoEnum == UndoEnum.AddFood){
               deleteFoodOrder(undoItem.foodOrder);
            }
            if(undoItem.undoEnum == UndoEnum.AddTopping){
                deleteTopping(undoItem.toppingOrder,undoItem.foodOrder);
            }
            if(undoItem.undoEnum == UndoEnum.AddSubOrder){
                deleteSubOrder(undoItem.subOrder);
            }
            chooseSuborder = null;
            chooseFoodOrder = null;
            chooseToppingOrder = null;
        }
    }
    public void addSubCate(View view){
        realm.beginTransaction();
        SubOrder newSub =  realm.createEmbeddedObject(SubOrder.class,order,"subOrders");
        mAdapter.notifyDataSetChanged();
        UndoItem undoItem = new UndoItem();
        undoItem.undoEnum = UndoEnum.AddSubOrder;
        undoItem.subOrder = newSub ;
        undoItemStack.push(undoItem);
        realm.commitTransaction();
    }
    public void addToppingChooseFood(View view){
        view.setClickable(false);
       if(chooseFoodOrder !=  null){
           chooseTopping();
       }
       view.setClickable(true);
    }

}