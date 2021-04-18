package com.foa.pos.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.foa.pos.R;
import com.foa.pos.entity.Order;
import com.foa.pos.utils.Helper;

import java.util.List;

public class OrdersGridViewAdapter extends ArrayAdapter<Order> {
    List<Order> orders;
    private LayoutInflater layoutInflater;
    private Context context;
    LinearLayout ordersLayout;
    RelativeLayout detailLayout;
    ListView theGridView;
    ListView orderDetailsListView;

    public OrdersGridViewAdapter(Context context, List<Order> objects) {
        super(context, 0, objects);
        this.orders = objects;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return orders.size();
    }

    @Nullable
    @Override
    public Order getItem(int position) {
        return orders.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        ordersLayout = ((Activity) context).findViewById(R.id.bgOrders);
        detailLayout = ((Activity) context).findViewById(R.id.bgOrderDetail);
        theGridView = ((Activity) context).findViewById(R.id.orderGridView);

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
//        holder.cardItem.setOnClickListener(v -> {
//            v.setSelected(true);
//            if (order.isSelected()){
//                order.setSelected(false);
//                Helper.disableSplitLayout(ordersLayout,detailLayout,theGridView);
//            }else {
//                Activity a = ((Activity) context);
//                Log.e("a",""+a);
//                order.setSelected(true);
//                if(!Helper.checkHasSelectedItem(orders,order)){//disable another item
//                    //Helper.enableSplitLayout(ordersLayout,detailLayout,theGridView);
//                }
//                Helper.loadOrderDetail(order,detailLayout,context);
//            }
//            notifyDataSetChanged();
//        });

        //set data
        holder.orderId.setText(order.getOrderID().substring(0,6));
        holder.orderTime.setText(order.getStringCreatedOn());
        holder.orderQty.setText(String.valueOf(order.getSumQuantity()));
        holder.orderAmount.setText(String.valueOf(order.getAmount()));
        holder.orderStatus.setText(order.getStatus()?"Đã thanh toán": "Chưa thanh toán");

//        (detailLayout.findViewById(R.id.btnCancelDetatil)).setOnClickListener(v -> {
//            Helper.disableSplitLayout(ordersLayout,detailLayout,theGridView);
//            Helper.clearSelectedItem(orders);
//        });
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
}
