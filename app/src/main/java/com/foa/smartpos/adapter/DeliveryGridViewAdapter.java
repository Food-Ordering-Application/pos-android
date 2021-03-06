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
import androidx.constraintlayout.solver.state.State;
import androidx.recyclerview.widget.RecyclerView;

import com.foa.smartpos.R;
import com.foa.smartpos.fragment.DeliveryFragment;
import com.foa.smartpos.model.Order;
import com.foa.smartpos.model.OrderItem;
import com.foa.smartpos.model.enums.OrderStatus;
import com.foa.smartpos.sqlite.DatabaseManager;
import com.foa.smartpos.sqlite.ds.OrderDataSource;
import com.foa.smartpos.utils.EnumHelper;
import com.foa.smartpos.utils.Helper;
import com.foa.smartpos.dialog.EditOrderItemDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DeliveryGridViewAdapter extends RecyclerView.Adapter<DeliveryGridViewAdapter.ViewHolder> {
    List<Order> orders;
    private Context context;
    private OnSelectedItemListener onSelectedItemListener;
    String currentOrderId="";
    private TextView emptyListTextView;

    public DeliveryGridViewAdapter(Context context, List<Order> orderList,TextView emptyListTextView) {
        this.context = context;
        this.orders = orderList;
        this.emptyListTextView = emptyListTextView;
    }

    public void addOrder(Order order){
        orders.add(order);
        checkEmptyList();
        notifyItemInserted(orders.size()-1);
    }

    public void updateOrder(Order order){
        for (int i=0;i<orders.size();i++) {
            if (orders.get(i).getId().equals(order.getId())) {
                orders.set(i,order);
                notifyItemInserted(i);
                break;
            }
        }
    }

    public void setOrders(List<Order> orders){
        this.orders = orders;
        checkEmptyList();
        currentOrderId="";
        this.notifyDataSetChanged();
    }

    public void removeOrder(Order order){
        this.orders.remove(order);
        checkEmptyList();
        notifyDataSetChanged();
    }

    public void setCurrentOrderId(String orderId){
        this.currentOrderId = orderId;
        notifyDataSetChanged();
    }

    private void checkEmptyList(){
        if (orders.size()>0){
            emptyListTextView.setVisibility(View.GONE);
        }else{
            emptyListTextView.setVisibility(View.VISIBLE);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item_delivery, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order  = orders.get(position);
        if (currentOrderId.equals(order.getId())){
            holder.cardItem.setSelected(true);
        }else{
            holder.cardItem.setSelected(false);
        }

        holder.cardItem.setOnClickListener(v -> {
            if (order.getId().equals(currentOrderId)){
                currentOrderId="";
                if (onSelectedItemListener!=null){
                    onSelectedItemListener.OnSelected(null,position);
                }
            }else {
                currentOrderId=order.getId();
                if (onSelectedItemListener!=null) onSelectedItemListener.OnSelected(order,position);
            }
            notifyDataSetChanged();
        });

        //set data
        holder.orderId.setText(order.getDelivery().getCustomerName());
        holder.orderQty.setText(String.valueOf(order.getSumQuantity()));
        holder.orderAmount.setText(Helper.formatMoney(order.getGrandTotal()));
        holder.deliveryStatus.setText(EnumHelper.getOrderStatusString(order.getStatus()));
        holder.deliveryDistance.setText(Helper.formatDistance(order.getDelivery().getDistance()));
        holder.paymentStatus.setText("Ti???n m???t");
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


    public void setOnSelectedItemListener(OnSelectedItemListener listener){
        this.onSelectedItemListener =listener;
    }

    public interface OnSelectedItemListener{
        void OnSelected(Order order, int position);
    }
}
