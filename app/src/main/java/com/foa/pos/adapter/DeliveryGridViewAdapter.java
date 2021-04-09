package com.foa.pos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.foa.pos.R;
import com.foa.pos.entity.Item;

import java.util.HashSet;
import java.util.List;

public class DeliveryGridViewAdapter extends ArrayAdapter<Item> {
    List<Item> items;
    private LayoutInflater layoutInflater;
    private Context context;

    public DeliveryGridViewAdapter(Context context, List<Item> objects) {
        super(context, 0, objects);
        this.items = objects;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
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
        convertView = layoutInflater.inflate(R.layout.card_item_delivery, null);
        return convertView;
    }

}
