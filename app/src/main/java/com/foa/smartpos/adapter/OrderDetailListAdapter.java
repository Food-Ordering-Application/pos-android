package com.foa.smartpos.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.foa.smartpos.R;
import com.foa.smartpos.model.OrderItem;
import com.foa.smartpos.model.enums.StockState;
import com.foa.smartpos.utils.Constants;
import com.foa.smartpos.utils.Helper;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailListAdapter extends BaseAdapter {
 
    private List<OrderItem> dtList = new ArrayList<OrderItem>();
    private Activity context;
    private LayoutInflater inflater;
    //private CartListener listener;
    public OrderDetailListAdapter(Activity context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    public OrderDetailListAdapter(Activity context, List<OrderItem> data) {
     
        this.context = context;
        this.dtList = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    private class ViewHolder {
        RelativeLayout layoutOrderItem;
        TextView name;
        TextView toppingName;
        TextView price;
        TextView qty;
        ImageButton btnMinus;
        ImageButton btnPlus;
    }
 
    public int getCount() {
        return dtList.size();
    }
    
    public void set(List<OrderItem> list) {
    	dtList = list;
        notifyDataSetChanged();
    }

    public void updateStockStateOrderItem (String orderItemId, StockState stockState){
        for (OrderItem item: dtList) {
            if(item.getId().equals(orderItemId)){
                item.setStockState( stockState);
                notifyDataSetChanged();
            }
        }
    }
 
    public Object getItem(int position) {
        return dtList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
 
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;
        
        if (convertView == null) {
        	vi = inflater.inflate(R.layout.cart_list_item, null);
            holder = new ViewHolder();

            holder.layoutOrderItem = vi.findViewById(R.id.layoutOrderItem);
            holder.name = vi.findViewById(R.id.tvCartProductName);
            holder.toppingName = vi.findViewById(R.id.tvTopping);
            holder.price = vi.findViewById(R.id.tvProductPrice);
            holder.qty = vi.findViewById(R.id.tvTotal);
            holder.btnMinus = vi.findViewById(R.id.btnSubQuantityCartItem);
            holder.btnPlus = vi.findViewById(R.id.btnPlusQuantityCartItem);
            
            vi.setTag(holder);
        } else {
        	 holder=(ViewHolder)vi.getTag();
        }
        
        final OrderItem orderItem = (OrderItem) getItem(position);
        if (orderItem.getStockState()==StockState.OUT_OF_STOCK){
            holder.layoutOrderItem.setBackgroundResource(R.color.line_gray);
        }else{
            holder.layoutOrderItem.setBackgroundResource(R.color.white);
        }

        holder.name.setText(orderItem.getMenuItemName());
        holder.qty.setText(String.valueOf(orderItem.getQuantity()));
        holder.price.setText(Helper.formatMoney(orderItem.getSubTotal()));

        final StringBuilder toppingNamesBuilder = new StringBuilder();
        orderItem.getOrderItemToppings().forEach(item->
                toppingNamesBuilder.append(item.getName()).append("\n"));
        holder.toppingName.setText(toppingNamesBuilder.toString().trim());


        holder.btnMinus.setVisibility(View.INVISIBLE);
        holder.btnPlus.setVisibility(View.INVISIBLE);
        return vi;
    }
}