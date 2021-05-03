package com.foa.pos.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.foa.pos.R;
import com.foa.pos.model.MenuItemTopping;

import java.util.List;

public class ToppingListAdapter  extends BaseAdapter {

    private List<MenuItemTopping> dtList;
    private Activity context;
    private LayoutInflater inflater;

    public ToppingListAdapter(Activity context, List<MenuItemTopping> data) {

        this.context = context;
        this.dtList = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return dtList.size();
    }

    @Override
    public MenuItemTopping getItem(int position) {
        return  dtList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.card_item_topping,null);
        TextView toppingName = convertView.findViewById(R.id.toppingName);
        toppingName.setText(getItem(position).getName());
        return convertView;
    }
}
