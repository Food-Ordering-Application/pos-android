package com.foa.smartpos.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.foa.smartpos.R;
import com.foa.smartpos.dialog.OrderDetailDialog;
import com.foa.smartpos.model.Order;
import com.foa.smartpos.utils.Helper;

import java.util.List;

public class OrdersListViewAdapter extends ArrayAdapter<Order> {
    List<Order> orders;
    private LayoutInflater layoutInflater;
    private Context context;
    LinearLayout ordersLayout;
    RelativeLayout detailLayout;
    ListView theGridView;
    ListView orderDetailsListView;

    public OrdersListViewAdapter(Context context, List<Order> objects) {
        super(context, 0, objects);
        this.orders = objects;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setData(List<Order> orders){
        this.orders = orders;
        notifyDataSetChanged();
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
            holder.orderAmount = convertView.findViewById(R.id.orderAmount);
            holder.orderStatus = convertView.findViewById(R.id.orderStatus);
            holder.syncStatusIcon = convertView.findViewById(R.id.syncStatusIcon);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(position%2==0){
            holder.cardItem.setBackgroundResource(R.color.white);
        }else{
            holder.cardItem.setBackgroundResource(R.color.item_transparan);
        }

        Order order  = getItem(position);
        if(order.isSelected()){
            holder.cardItem.setSelected(true);
        }else{
            holder.cardItem.setSelected(false);
        }
        holder.cardItem.setOnClickListener(v -> {
            OrderDetailDialog dialog  = new OrderDetailDialog(context,order);
            dialog.show();
        });


        holder.orderId.setText(order.getId());
        holder.orderTime.setText(Helper.dateTimeformat.format(order.getCreatedAt()));
        holder.orderAmount.setText(String.valueOf(order.getGrandTotal()));
        holder.orderStatus.setText("Tiền mặt");
        if (order.getSyncedAt()==null||order.getDelivery()==null){
            holder.syncStatusIcon.setImageResource(R.drawable.ic_sync_cloud_none);
        }else{
            holder.syncStatusIcon.setImageResource(R.drawable.ic_sync_cloud_done);
        }



        return convertView;
    }

    static class ViewHolder {
        LinearLayout cardItem;
        TextView orderId;
        TextView orderTime;
        TextView orderAmount;
        TextView orderStatus;
        ImageView syncStatusIcon;
    }
}
