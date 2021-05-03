package com.foa.pos.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.foa.pos.R;
import com.foa.pos.model.Order;
import com.foa.pos.model.OrderItem;
import com.foa.pos.sqlite.DatabaseHelper;
import com.foa.pos.sqlite.DatabaseManager;
import com.foa.pos.sqlite.ds.OrderDataSource;
import com.foa.pos.utils.Helper;
import com.foa.pos.widget.EditOrderItemDialog;

import java.util.List;
import java.util.Random;

public class DeliveryGridViewAdapter extends ArrayAdapter<Order> {
    List<Order> orders;
    private LayoutInflater layoutInflater;
    private Context context;
    LinearLayout deliveriesLayout;
    RelativeLayout detailLayout;
    GridView theGridView;

    public DeliveryGridViewAdapter(Context context, List<Order> objects) {
        super(context, 0, objects);
        this.orders = objects;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
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
        deliveriesLayout = ((Activity) context).findViewById(R.id.bgDeliveries);
        detailLayout = ((Activity) context).findViewById(R.id.bgOrderDetail);
        theGridView = ((Activity) context).findViewById(R.id.deliveryGridView);

        if(convertView==null){
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.card_item_delivery, null);
            holder = new ViewHolder();
            holder.cardItem = convertView.findViewById(R.id.cardDelivery);
            holder.orderId = convertView.findViewById(R.id.orderId);
            holder.deliveryStatus = convertView.findViewById(R.id.deliveryStatus);
            holder.deliveryDistance = convertView.findViewById(R.id.deliveryDistance);
            holder.orderQty = convertView.findViewById(R.id.orderQty);
            holder.orderAmount = convertView.findViewById(R.id.orderAmount);
            holder.timeLeft = convertView.findViewById(R.id.timeLeft);
            holder.paymentStatus = convertView.findViewById(R.id.paymentStatus);
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
                Helper.disableSplitLayout(deliveriesLayout,detailLayout,theGridView);
            }else {
                Activity a = ((Activity) context);
                Log.e("a",""+a);
                order.setSelected(true);
                if(! Helper.checkHasSelectedItem(orders,order)){//disable another item
                    Helper.enableSplitLayout(deliveriesLayout,detailLayout,theGridView);
                }
                loadOrderDetail(order,detailLayout, context);
            }
            notifyDataSetChanged();
        });

        //set data
        holder.orderId.setText(order.getId().substring(0,6));
        //holder.timeLeft.setText(order.getOrderDelivery().getAcceptanceDeadline().toString());
        holder.orderQty.setText(String.valueOf(order.getSumQuantity()));
        holder.orderAmount.setText(String.valueOf(order.getGrandTotal()));
        holder.deliveryStatus.setText("Chờ xác nhận");
        holder.deliveryDistance.setText(String.valueOf( Math.abs(new Random().nextInt()%7+0.2)));
        holder.paymentStatus.setText("Tiền mặt");

//        (detailLayout.findViewById(R.id.btnCancelDetatil)).setOnClickListener(v -> {
//            Helper.disableSplitLayout(deliveriesLayout,detailLayout,theGridView);
//            Helper.clearSelectedItem(orders);
//        });

        return convertView;
    }

    class ViewHolder {
        LinearLayout cardItem;
        TextView orderId;
        TextView deliveryStatus;
        TextView deliveryDistance;
        TextView orderQty;
        TextView orderAmount;
        TextView paymentStatus;
        TextView timeLeft;
    }

    public static void loadOrderDetail(Order order, RelativeLayout detailLayout, Context context){
        ((TextView)detailLayout.findViewById(R.id.tvTotal)).setText(String.valueOf(order.getGrandTotal()));
        ((TextView)detailLayout.findViewById(R.id.tvTotalPay)).setText(String.valueOf(order.getGrandTotal()));
        ((TextView)detailLayout.findViewById(R.id.tvOderId)).setText(String.valueOf(order.getId().substring(0,6)));
        ((TextView)detailLayout.findViewById(R.id.tvReceiveMoney)).setText(String.valueOf(order.getGrandTotal()));
        if((TextView)detailLayout.findViewById(R.id.tvChange)!=null){
            ((TextView)detailLayout.findViewById(R.id.tvChange)).setText(String.valueOf(0));
        }
        OrderDetailListAdapter adapter = new OrderDetailListAdapter((Activity) context);
        adapter.set(order.getOrderItems());
        adapter.notifyDataSetChanged();
        ListView detailsListView = detailLayout.findViewById(R.id.listOrderDetails);
        detailsListView.setAdapter(adapter);
        detailsListView.setOnItemClickListener((parent, view, position, id) -> {
            Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
            OrderItem item =(OrderItem) detailsListView.getItemAtPosition(position);
            EditOrderItemDialog dialog = new EditOrderItemDialog(context,item);
            dialog.setOutOfProductListener(result -> {
                if (result){
                    DatabaseManager.initializeInstance(new DatabaseHelper(context));
                    SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
                    OrderDataSource DS = new OrderDataSource(db);
                    DS.updateOutSoldOrderItem(item.getId(),result);
                    ((OrderDetailListAdapter) detailsListView.getAdapter()).updateIsOutSold(item.getId(),item.getStockState());
                    ((OrderDetailListAdapter) detailsListView.getAdapter()).notifyDataSetChanged();
                }
            });
            dialog.show();
        });
    }
}
