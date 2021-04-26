package com.foa.pos.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.foa.pos.R;
import com.foa.pos.model.Cart;
import com.foa.pos.model.OrderItem;
import com.foa.pos.utils.Constants;
import com.foa.pos.utils.Helper;

import java.util.ArrayList;
import java.util.List;

public class CartListAdapter extends BaseAdapter {
 
    private List<OrderItem> dtList = new ArrayList<>();
    private Activity context;
    private LayoutInflater inflater;
    private CartListener listener;
    private  boolean isPayment = false;
    public CartListAdapter(Activity context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    public CartListAdapter(Activity context, List<OrderItem> data) {
     
        this.context = context;
        this.dtList = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    private class ViewHolder {
        TextView name;
        TextView price;
        TextView qty;
        ImageButton btnMinus;
        ImageButton btnPlus;
    }
 
    public int getCount() {
        return dtList.size();
    }

    public void setIsPayment(boolean isPayment){
        this.isPayment = isPayment;
    }
    
    public void set(List<OrderItem> list) {
    	dtList = list;
        notifyDataSetChanged();
    }
    
    public void remove(Cart user) {
    	dtList.remove(user);
        notifyDataSetChanged();
    }
    public void removeAll() {
    	dtList = new ArrayList<OrderItem>();
        notifyDataSetChanged();
    }
    
    public void removeByID(String code) {
    	for (int i = 0; i < dtList.size(); i++) {
			if(dtList.get(i).getId().equals(code))
				dtList.remove(i);
		};
        notifyDataSetChanged();
    }
    
    public void add(OrderItem user) {
    	dtList.add(user);
        notifyDataSetChanged();
    }
    
    public void insert(OrderItem user,int index) {
    	dtList.add(index, user);
        notifyDataSetChanged();
    }
 
    public Object getItem(int position) {
        return dtList.get(position);
    }
 
    public long getItemId(int position) {
        return 0;
    }
    
    @Override
    public void notifyDataSetChanged() {
    	// TODO Auto-generated method stub
    	super.notifyDataSetChanged();
		if(listener != null)
			listener.onChange(dtList);
    }
 
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;
        
        if (convertView == null) {
        	vi = inflater.inflate(R.layout.cart_list_item, null);
            holder = new ViewHolder();
 
            holder.name = (TextView) vi.findViewById(R.id.tvCartProductName);
            holder.price = (TextView) vi.findViewById(R.id.tvProductPrice);
            holder.qty = (TextView) vi.findViewById(R.id.tvTotal);
            holder.btnMinus = (ImageButton) vi.findViewById(R.id.btnSubQuantityCartItem);
            holder.btnPlus = (ImageButton)vi.findViewById(R.id.btnPlusQuantityCartItem);
            
            vi.setTag(holder);
        } else {
        	 holder=(ViewHolder)vi.getTag();
        }

        if(isPayment){
            holder.btnMinus.setVisibility(View.INVISIBLE);
            holder.btnPlus.setVisibility(View.INVISIBLE);
        }
        
        final OrderItem orderItem = (OrderItem) getItem(position);
        holder.name.setText(orderItem.getMenuItemName());
        holder.price.setText(Helper.decimalformat.format(orderItem.getQuantity()* orderItem.getPrice()) +" "+Helper.read(Constants.KEY_SETTING_CURRENCY_SYMBOL, Constants.VAL_DEFAULT_CURRENCY_SYMBOL));
        holder.qty.setText(String.valueOf(orderItem.getQuantity()));
        if (orderItem.getQuantity()==1){
            holder.btnMinus.setImageResource(R.drawable.ic_baseline_delete_24);
        }else {
            holder.btnMinus.setImageResource(R.drawable.ic_remove_circle);
        }

        if(orderItem.getQuantity() <= 0)
        {
        	holder.btnMinus.setEnabled(false);
        }
        
        holder.btnMinus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

                orderItem.setQuantity(orderItem.getQuantity() - 1);
				
				long discount =  (orderItem.getPrice()*orderItem.getQuantity()) * (orderItem.getDiscount()/100);
				long subtotal = (orderItem.getPrice()*orderItem.getQuantity()) - discount;
                orderItem.setSubTotal(subtotal);
				dtList.set(position, orderItem);
				
				if(orderItem.getQuantity() <= 0)
		        {
					dtList.remove(position);
					if(listener != null)
						listener.onRemove(orderItem.getId());
						
		        }
				notifyDataSetChanged();
			}
		});
        
        holder.btnPlus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                orderItem.setQuantity(orderItem.getQuantity() + 1);
			
				long discount = (orderItem.getPrice()*orderItem.getQuantity()) * (orderItem.getDiscount()/100);
				long subtotal = (orderItem.getPrice()*orderItem.getQuantity()) - discount;
                orderItem.setSubTotal(subtotal);
				
				dtList.set(position, orderItem);
				notifyDataSetChanged();
			}
		});
        
        return vi;
    }
    

    
    public void setCartListener(CartListener listener)
    {
    	this.listener = listener;
    }
    
    public interface CartListener {
        public void onRemove(String result);
        public void onChange(List<OrderItem> list);
    }
}