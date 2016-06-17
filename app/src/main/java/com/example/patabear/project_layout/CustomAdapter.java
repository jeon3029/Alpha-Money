package com.example.patabear.project_layout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Patabear on 2016-06-15.
 */
public class CustomAdapter extends BaseAdapter{
    private ArrayList<struct> itemDatas = null;
    private LayoutInflater layoutInflater = null;
    public CustomAdapter(ArrayList<struct> itemDatas, Context ctx){
        this.itemDatas = itemDatas;
        layoutInflater = (LayoutInflater)ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
    }
    public void setItemDatas(ArrayList<struct> itemDatas){
        this.itemDatas = itemDatas;
        this.notifyDataSetChanged();
    }
    @Override
    public int getCount(){
        return itemDatas !=null ? itemDatas.size() : 0;
    }

    @Override
    public Object getItem(int position) {

        return (itemDatas !=null && (position>=0 &&position<itemDatas.size())?itemDatas.get(position):null);
    }

    @Override
    public long getItemId(int position) {
        return (itemDatas!=null &&(position>=0 && position<itemDatas.size())?position:0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = layoutInflater.inflate(R.layout.list_item,parent,false);
            //convertView = layoutInflater.inflate(R.layout.list_item, null);
        }
        TextView history = (TextView)convertView.findViewById(R.id.history);
        TextView price = (TextView)convertView.findViewById(R.id.price);
        struct itemData_temp = itemDatas.get(position);
        history.setText(itemData_temp.storeName);
        price.setText(itemData_temp.price);
        return convertView;
    }
}
