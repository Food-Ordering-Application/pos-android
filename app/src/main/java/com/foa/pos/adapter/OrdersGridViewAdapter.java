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
import androidx.cardview.widget.CardView;

import com.foa.pos.R;
import com.foa.pos.entity.Item;
import com.foa.pos.entity.Order;
import com.foa.pos.utils.Helper;
import com.ramotion.foldingcell.FoldingCell;

import java.util.HashSet;
import java.util.List;

public class OrdersGridViewAdapter extends ArrayAdapter<Order> {
    List<Order> items;
    private LayoutInflater layoutInflater;
    private Context context;

    public OrdersGridViewAdapter(Context context, List<Order> objects) {
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
    public Order getItem(int position) {
        return items.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.card_item_bill, null);
            holder = new ViewHolder();
            holder.cardItem = convertView.findViewById(R.id.cardOrder);
            holder.orderId = convertView.findViewById(R.id.orderId);
            holder.orderTime = convertView.findViewById(R.id.orderTime);
            holder.orderQty = convertView.findViewById(R.id.orderQty);
            holder.orderAmount = convertView.findViewById(R.id.orderAmount);
            holder.orderStatus = convertView.findViewById(R.id.orderStatus);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        //Set item listener to change border
        Order order  = getItem(position);
        if(order.isSelected()){
            holder.cardItem.setSelected(true);
        }else{
            holder.cardItem.setSelected(false);
        }
        holder.cardItem.setOnClickListener(v -> {
            v.setSelected(true);
            if (order.isSelected()){
                order.setSelected(false);
            }else {
                order.setSelected(true);
                setUniqueSelected(order);//disable another item
            }
            notifyDataSetChanged();
        });

        //set data
        holder.orderId.setText(order.getOrderID().substring(0,6));
        holder.orderTime.setText(order.getStringCreatedOn());
        holder.orderQty.setText(String.valueOf(order.getSumQuantity()));
        holder.orderAmount.setText(String.valueOf(order.getAmount()));
        holder.orderStatus.setText(order.getStatus()?"Đã thanh toán": "Chưa thanh toán");

        return convertView;
    }

    static class ViewHolder {
        LinearLayout cardItem;
        TextView orderId;
        TextView orderTime;
        TextView orderQty;
        TextView orderAmount;
        TextView orderStatus;
    }
    void setUniqueSelected(Order item){
        for (int i = 0; i < items.size(); i++) {
            if(item.getOrderID()!= items.get(i).getOrderID() && items.get(i).isSelected()){
                items.get(i).setSelected(false);
                break;
            }
        }
    }


}
