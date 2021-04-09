package com.foa.pos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.foa.pos.R;
import com.foa.pos.entity.Item;
import com.ramotion.foldingcell.FoldingCell;

import java.util.HashSet;
import java.util.List;

public class OrdersGridViewAdapter extends ArrayAdapter<Item> {
    List<Item> items;
    private LayoutInflater layoutInflater;
    private Context context;

    public OrdersGridViewAdapter(Context context, List<Item> objects) {
        super(context, 0, objects);
        this.items = objects;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Nullable
    @Override
    public Item getItem(int position) {
        return items.get(position);
    }


    @Override
    public long getItemId(int position) {
        return getItem(position).getID();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.card_item_bill, null);
        return convertView;
    }

}
