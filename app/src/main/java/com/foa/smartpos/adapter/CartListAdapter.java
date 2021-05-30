package com.foa.smartpos.adapter;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.foa.smartpos.R;
import com.foa.smartpos.model.Order;
import com.foa.smartpos.model.OrderItem;
import com.foa.smartpos.sqlite.DatabaseManager;
import com.foa.smartpos.sqlite.ds.OrderDataSource;
import com.foa.smartpos.utils.Debouncer;
import com.foa.smartpos.utils.Helper;
import com.foa.smartpos.utils.OrderHelper;
import com.foa.smartpos.session.OrderSession;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.ViewHolder> {

    private List<OrderItem> dataList = new ArrayList<>();
    private CartListener listener;
    private OrderDataSource OrderDS;

    private final Debouncer debouncer = new Debouncer();


    public CartListAdapter(Activity context, List<OrderItem> orderItems) {
        this.dataList= orderItems;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        OrderDS = new OrderDataSource(db);
    }
    public CartListAdapter(Activity context) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        OrderDS = new OrderDataSource(db);
    }
    public List<OrderItem> getDataList(){
        return dataList;
    }

    public static class ViewHolder  extends RecyclerView.ViewHolder{
        TextView name;
        TextView toppingNames;
        TextView price;
        TextView qty;
        ImageButton btnMinus;
        ImageButton btnPlus;
        public RelativeLayout layoutOrderItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvCartProductName);
            toppingNames = itemView.findViewById(R.id.tvTopping);
            price = itemView.findViewById(R.id.tvProductPrice);
            qty = itemView.findViewById(R.id.tvTotal);
            btnMinus = itemView.findViewById(R.id.btnSubQuantityCartItem);
            btnPlus = itemView.findViewById(R.id.btnPlusQuantityCartItem);
            layoutOrderItem = itemView.findViewById(R.id.layoutOrderItem);
        }
    }

    public int getCount() {
        return dataList.size();
    }

    public void set(List<OrderItem> list) {
        dataList = list;
        this.notifyItemRangeChanged(0,list.size());
    }

    public void removeAll() {
        if (dataList.size() > 0) {
            OrderDS.deleteAllOrderItem(dataList);
            dataList = new ArrayList<>();
            notifyDataSetChanged();
            OrderHelper.removeAllOrderItems();
            if (listener!=null) listener.onChange();
        }

    }

    public void removeItem(int pos) {
        dataList.remove(pos);
        notifyItemRemoved(pos);
        OrderHelper.removeOrderItemByPosition(pos);
        if(listener!=null) listener.onChange();
    }

    public void restoreItem(OrderItem item, int position) {
        dataList.add(position, item);
        notifyItemInserted(position);
        OrderHelper.restoreOrderItemByPosition(item,position);
        if(listener!=null) listener.onChange();
    }

    public void add(OrderItem orderItem) {
        dataList.add(orderItem);
        notifyItemInserted(dataList.size()-1);
        if(listener!=null) listener.onChange();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final OrderItem orderItem = dataList.get(position);
        holder.name.setText(orderItem.getMenuItemName());
        final StringBuilder toppingNamesBuilder = new StringBuilder();
        orderItem.getOrderItemToppings().forEach(item->
                toppingNamesBuilder.append(item.getName()).append("\n"));
        holder.toppingNames.setText(toppingNamesBuilder.toString().trim());
        holder.price.setText(Helper.formatMoney(orderItem.getSubTotal()));
        holder.qty.setText(String.valueOf(orderItem.getQuantity()));
        if (orderItem.getQuantity() == 1) {
            holder.btnMinus.setImageResource(R.drawable.ic_baseline_delete_24);
        } else {
            holder.btnMinus.setImageResource(R.drawable.ic_remove_circle);
        }

        if (orderItem.getQuantity() <= 0) {
            holder.btnMinus.setEnabled(false);
        }

        holder.btnMinus.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            Order order;
            orderItem.setQuantity(orderItem.getQuantity() - 1);
            if (orderItem.getQuantity()==0){
                removeOrderItem(orderItem,position);
                this.notifyItemRemoved(position);
            }else {
                order = updateOrderItemQuantity(orderItem, position);

                debouncer.debounce(CartListAdapter.class, () ->
                                OrderDS.updateOrderItemQuantity(orderItem.getId(), orderItem.getQuantity(), orderItem.getSubTotal(), order.getId(), order.getSubTotal())
                         , 1000, TimeUnit.MILLISECONDS);
                notifyItemChanged(position);
            }
            if (listener != null) listener.onChange();
        });

        holder.btnPlus.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            orderItem.setQuantity(orderItem.getQuantity() + 1);
            Order order = updateOrderItemQuantity(orderItem, position);
            notifyItemChanged(position);

            debouncer.debounce(CartListAdapter.class, () ->
                    OrderDS.updateOrderItemQuantity(orderItem.getId(), orderItem.getQuantity(), orderItem.getSubTotal(), order.getId(), order.getSubTotal())
                   , 1000, TimeUnit.MILLISECONDS);

        });
        holder.itemView.setTag(orderItem);
    }

    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void setCartListener(CartListener listener) {
        this.listener = listener;
    }

    public interface CartListener {
        void onChange();
    }

    private Order updateOrderItemQuantity(OrderItem orderItem, int pos) {
        Order order =  OrderSession.getInstance();
       for(int i=0;i<order.getOrderItems().size();i++){
            if(order.getOrderItems().get(i).getId().equals(orderItem.getId())){
                order.getOrderItems().get(i).setQuantity(orderItem.getQuantity());
                order.getOrderItems().get(i).setSubTotal(OrderHelper.updateOrderItemStatistic(order.getOrderItems().get(i)).getSubTotal());
                dataList.set(pos,order.getOrderItems().get(i));
            }
        };
        order = OrderHelper.updateOrderStatistic(order);
        OrderSession.setInstance(order);
        if (listener != null) listener.onChange();
        return order;
    }

    private Order removeOrderItem(OrderItem orderItem,int pos) {
        Order order =  OrderSession.getInstance();
        for (int i = 0; i < order.getOrderItems().size(); i++) {
            if (order.getOrderItems().get(i).getId().equals(orderItem.getId())) {
                order.getOrderItems().remove(i);
                order = OrderHelper.updateOrderStatistic(order);
                OrderDS.deleteOrderItem(orderItem.getId(), order.getId(),order.getSubTotal());
                dataList.remove(pos);
                break;
            }
        }
        OrderSession.setInstance(OrderHelper.updateOrderStatistic(order));
        return order;
    }
}