package com.example.restaurantmanager.view.admin;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import com.example.restaurantmanager.R;
import com.example.restaurantmanager.adapter.item.MoreData;
import com.example.restaurantmanager.enums.MoreMenuEnum;
import com.example.restaurantmanager.adapter.MoreAdapter;

import java.util.ArrayList;
import java.util.List;


public class AdminActivity extends BaseAdminActivity {
    String[] label;
    int[] icons;
    MoreMenuEnum[] menus;
    private RecyclerView mRecyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        mRecyclerview = findViewById(R.id.more_quickly_manage_rv);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        MoreAdapter moreAdpater = new MoreAdapter(context, createDataForQuickManage());
        mRecyclerview.setAdapter(moreAdpater);

    }
    List<MoreData> createDataForQuickManage() {
        label = new String[]{getString(R.string.categories)
                , getString(R.string.foods)
                , getString(R.string.toppings)
                ,"Tables"
                , getString(R.string.orders)
                ,"Trash"
                ,"Setting"
        };
        icons = new int[]{
                R.drawable.icon_category
                , R.drawable.icon_food
                , R.drawable.icon_topping
                , R.drawable.ic_vector_table
                , R.drawable.icon_order
                , R.drawable.icon_order
                ,R.drawable.ic_baseline_settings_24
        };
        menus = new MoreMenuEnum[]{
                MoreMenuEnum.CATEGORIES,
                MoreMenuEnum.FOODS,
                MoreMenuEnum.TOPPINGS,
                MoreMenuEnum.TABLES,
                MoreMenuEnum.ORDERS,
                MoreMenuEnum.TRASH,
                MoreMenuEnum.SETTING
        };

        boolean enabled[] = {true, true, true, true,true,true,true};

        List<MoreData> moreData = new ArrayList<>();
        for (int i = 0; i < label.length; i++) {
            moreData.add(new MoreData(label[i], icons[i], menus[i], enabled[i]));
        }
        return moreData;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}