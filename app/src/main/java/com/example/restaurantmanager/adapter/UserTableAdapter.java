package com.example.restaurantmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.restaurantmanager.R;
import com.example.restaurantmanager.adapter.item.TableItem;
import com.example.restaurantmanager.interfacez.HomeTableClickInterface;
import com.example.restaurantmanager.utils.DoubleUtil;

import java.util.List;

public class UserTableAdapter extends BaseAdapter {
    private List<TableItem> listData;
    private LayoutInflater layoutInflater;
    private Context context;
    int tableWith;
    int tableHeight;
    HomeTableClickInterface homeTableClickInterface;
    public UserTableAdapter(Context aContext, List<TableItem> listData, int tableWith, int tableHeight, HomeTableClickInterface homeTableClickInterface) {
        this.context = aContext;
        this.listData = listData;
        this.tableWith = tableWith;
        this.tableHeight = tableHeight;
        this.homeTableClickInterface = homeTableClickInterface;
        layoutInflater = LayoutInflater.from(aContext);

    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.adapter_table_girdview, null);
            holder = new ViewHolder();
            AbsListView.LayoutParams param = new AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    tableHeight);
            convertView.setLayoutParams(param);

            holder.flagView = (ImageView) convertView.findViewById(R.id.imageView_Table);
            holder.NumberNameView = (TextView) convertView.findViewById(R.id.textView_NumberTable);
            holder.pireView = (TextView) convertView.findViewById(R.id.textView_price);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        TableItem table = this.listData.get(position);
        String tableName = table.getName()+(table.getNote().equals("")?"":"(*)");
        holder.NumberNameView.setText(tableName);

        if(table.isHaveOrder()){
            holder.flagView.setImageResource(R.drawable.table_people);
            holder.pireView.setText(DoubleUtil.Round2(table.getPrice())+"€");
        }
        else {
            holder.flagView.setImageResource(R.drawable.table);
            holder.pireView.setText("");
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeTableClickInterface.chooseTable(table);
            }
        });
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                homeTableClickInterface.showNote(table);
                return true;
            }
        });
        return convertView;
    }
    static class ViewHolder {
        ImageView flagView;
        TextView NumberNameView;
        TextView pireView;
    }
}
