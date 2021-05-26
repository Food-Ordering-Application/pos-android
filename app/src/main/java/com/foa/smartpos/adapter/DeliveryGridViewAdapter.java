package com.foa.smartpos.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.foa.smartpos.R;
import com.foa.smartpos.model.Order;
import com.foa.smartpos.model.OrderItem;
import com.foa.smartpos.sqlite.DatabaseManager;
import com.foa.smartpos.sqlite.ds.OrderDataSource;
import com.foa.smartpos.utils.Helper;
import com.foa.smartpos.dialog.EditOrderItemDialog;

import java.util.List;
import java.util.Random;

public class DeliveryGridViewAdapter extends RecyclerView.Adapter<DeliveryGridViewAdapter.ViewHolder> {
    List<Order> orders;
    private Context context;
    LinearLayout deliveriesLayout;
    RelativeLayout detailLayout;
    RecyclerView theGridView;

    public DeliveryGridViewAdapter(Context context) {
        this.context = context;
    }

    public DeliveryGridViewAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orders = orderList;
    }

    public void addOrder(Order order){
        orders.add(order);
        notifyItemInserted(orders.size()-1);
    }

    public void setOrders(List<Order> orders){
        this.orders = orders;
        this.notifyItemRangeChanged(0, orders.size());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item_delivery, parent, false);

        deliveriesLayout = ((Activity) context).findViewById(R.id.bgDeliveries);
        detailLayout = ((Activity) context).findViewById(R.id.bgOrderDetail);
        theGridView = ((Activity) context).findViewById(R.id.deliveryRecyclerView);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order  = orders.get(position);
        if(order.isSelected()){
            holder.cardItem.setSelected(true);
        }else{
            holder.cardItem.setSelected(false);
        }
        holder.cardItem.setOnClickListener(v -> {
            v.setSelected(true);
            if (order.isSelected()){
                order.setSelected(false);
                Helper.disableSplitLayout(context, deliveriesLayout,detailLayout,theGridView);
            }else {
                Activity a = ((Activity) context);
                Log.e("a",""+a);
                order.setSelected(true);
                if(! Helper.checkHasSelectedItem(orders,order)){//disable another item
                    Helper.enableSplitLayout(context,deliveriesLayout,detailLayout,theGridView);
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

        holder.itemView.setTag(order);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout cardItem;
        TextView orderId;
        TextView deliveryStatus;
        TextView deliveryDistance;
        TextView orderQty;
        TextView orderAmount;
        TextView paymentStatus;
        TextView timeLeft;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardItem = itemView.findViewById(R.id.cardDelivery);
            orderId = itemView.findViewById(R.id.orderId);
            deliveryStatus = itemView.findViewById(R.id.deliveryStatus);
            deliveryDistance = itemView.findViewById(R.id.deliveryDistance);
            orderQty = itemView.findViewById(R.id.orderQty);
            orderAmount = itemView.findViewById(R.id.orderAmount);
            timeLeft = itemView.findViewById(R.id.timeLeft);
            paymentStatus = itemView.findViewById(R.id.paymentStatus);
        }
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

                    SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
                    OrderDataSource DS = new OrderDataSource(db);
                    DS.updateOutSoldOrderItem(item.getId(),result);
                    ((OrderDetailListAdapter) detailsListView.getAdapter()).updateIsOutSold(item.getId(),item.getStockState());
                    ((OrderDetailListAdapter) detailsListView.getAdapter()).notifyDataSetChanged();

            });
            dialog.show();
        });
    }
}
