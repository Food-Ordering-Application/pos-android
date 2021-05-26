package com.foa.smartpos.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.foa.smartpos.R;
import com.foa.smartpos.model.ToppingItem;

import java.util.List;

public class ToppingListAdapter  extends BaseAdapter {

    private List<ToppingItem> dtList;
    private Activity context;
    private LayoutInflater inflater;

    public ToppingListAdapter(Activity context, List<ToppingItem> data) {

        this.context = context;
        this.dtList = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return dtList.size();
    }

    @Override
    public ToppingItem getItem(int position) {
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
