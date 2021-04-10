package com.foa.pos.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
    List<Order> items;
    private LayoutInflater layoutInflater;
    private Context context;
    LinearLayout ordersLayout;
    RelativeLayout detailLayout;
    GridView theGridView;
    ListView orderDetailsListView;

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
        holder.cardItem.setOnClickListener(v -> {
            v.setSelected(true);
            if (order.isSelected()){
                order.setSelected(false);
                disableSplitLayout();
            }else {
                Activity a = ((Activity) context);
                Log.e("a",""+a);
                order.setSelected(true);
                if(!checkHasSelectedItem(order)){//disable another item
                    enableSplitLayout();
                }
                loadOrderDetail(order);
            }
            notifyDataSetChanged();
        });

        //set data
        holder.orderId.setText(order.getOrderID().substring(0,6));
        holder.orderTime.setText(order.getStringCreatedOn());
        holder.orderQty.setText(String.valueOf(order.getSumQuantity()));
        holder.orderAmount.setText(String.valueOf(order.getAmount()));
        holder.orderStatus.setText(order.getStatus()?"Đã thanh toán": "Chưa thanh toán");

        (detailLayout.findViewById(R.id.btnCancelDetatil)).setOnClickListener(v -> {
            disableSplitLayout();
            clearSelectedItem();
        });
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
    private boolean checkHasSelectedItem(Order item){
        for (int i = 0; i < items.size(); i++) {
            if(item.getOrderID()!= items.get(i).getOrderID() && items.get(i).isSelected()){
                items.get(i).setSelected(false);
                return true; //has item selected
            }
        }
        return false;// no item selected;
    }

    private void enableSplitLayout() {
        final int width = Helper.getDisplayWidth()-140;
        ViewGroup.LayoutParams param = ordersLayout.getLayoutParams();
        param.width = (width / 3)*2;
        ordersLayout.setLayoutParams(param);
        ViewGroup.LayoutParams param2 = detailLayout.getLayoutParams();
        param2.width = (width / 3);
        detailLayout.setLayoutParams(param2);
        detailLayout.setVisibility(View.VISIBLE);
        theGridView.setNumColumns(3);
    }

    private void disableSplitLayout() {
        final int width = Helper.getDisplayWidth()-140;
        ViewGroup.LayoutParams param = ordersLayout.getLayoutParams();
        param.width = ViewGroup.LayoutParams.MATCH_PARENT;
        ordersLayout.setLayoutParams(param);
        detailLayout.setVisibility(View.GONE);
        theGridView.setNumColumns(5);
    }

    private void loadOrderDetail(Order order){
        ((TextView)detailLayout.findViewById(R.id.tvTotal)).setText(String.valueOf(order.getAmount()));
        ((TextView)detailLayout.findViewById(R.id.tvTotalPay)).setText(String.valueOf(order.getAmount()));
        ((TextView)detailLayout.findViewById(R.id.tvOderId)).setText(String.valueOf(order.getOrderID().substring(0,6)));
        ((TextView)detailLayout.findViewById(R.id.tvReceiveMoney)).setText(String.valueOf(order.getAmount()));
        ((TextView)detailLayout.findViewById(R.id.tvChange)).setText(String.valueOf(0));
        OrderDetailListAdapter adapter = new OrderDetailListAdapter((Activity) context);
        adapter.set(order.getOrderDetails());
        orderDetailsListView = detailLayout.findViewById(R.id.listOrderDetails);
        orderDetailsListView.setAdapter(adapter);
    }

    private void clearSelectedItem(){
        for (int i = 0; i < items.size(); i++) {
            if(items.get(i).isSelected()){
                items.get(i).setSelected(false);
                return;
            }
        }
    }

}
