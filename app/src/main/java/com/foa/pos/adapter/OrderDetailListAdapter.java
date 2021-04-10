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
import com.foa.pos.entity.OrderDetails;
import com.foa.pos.utils.Constants;
import com.foa.pos.utils.Helper;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailListAdapter extends BaseAdapter {
 
    private List<OrderDetails> dtList = new ArrayList<OrderDetails>();
    private Activity context;
    private LayoutInflater inflater;
    //private CartListener listener;
    public OrderDetailListAdapter(Activity context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    public OrderDetailListAdapter(Activity context, List<OrderDetails> data) {
     
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
    
    public void set(List<OrderDetails> list) {
    	dtList = list;
        notifyDataSetChanged();
    }
    
    public void remove(OrderDetails user) {
    	dtList.remove(user);
        notifyDataSetChanged();
    }
    public void removeAll() {
    	dtList = new ArrayList<OrderDetails>();
        notifyDataSetChanged();
    }
    
    public void removeByID(String code) {
    	for (int i = 0; i < dtList.size(); i++) {
			if(dtList.get(i).getProductID().equals(code))
				dtList.remove(i);
		};
        notifyDataSetChanged();
    }
    
    public void add(OrderDetails user) {
    	dtList.add(user);
        notifyDataSetChanged();
    }
    
    public void insert(OrderDetails user,int index) {
    	dtList.add(index, user);
        notifyDataSetChanged();
    }
 
    public Object getItem(int position) {
        return dtList.get(position);
    }
 
    public long getItemId(int position) {
        return 0;
    }
    
//    @Override
//    public void notifyDataSetChanged() {
//    	// TODO Auto-generated method stub
//    	super.notifyDataSetChanged();
//		if(listener != null)
//			listener.onChange(dtList);
//    }
 
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
        
        final OrderDetails orderDetails = (OrderDetails) getItem(position);
        holder.name.setText(orderDetails.getProductName());
        holder.price.setText(Helper.decimalformat.format(orderDetails.getPrice()*orderDetails.getQty())
                +" "+Helper.read(Constants.KEY_SETTING_CURRENCY_SYMBOL, Constants.VAL_DEFAULT_CURRENCY_SYMBOL));
//        if (orderDetails.getQty()==1){
//            holder.btnMinus.setImageResource(R.drawable.ic_baseline_delete_24);
//        }else {
//            holder.btnMinus.setImageResource(R.drawable.ic_remove_circle);
//        }

        
        if(orderDetails.getQty() <= 0)
        {
        	holder.btnMinus.setEnabled(false);
        }
        holder.btnMinus.setVisibility(View.INVISIBLE);
        holder.btnPlus.setVisibility(View.INVISIBLE);

        
//        holder.btnMinus.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//
//				orderDetails.setQty(orderDetails.getQty() - 1);
//
//				double discount =  (orderDetails.getPrice()*orderDetails.getQty()) * (orderDetails.getDiscount()/100);
//				double subtotal = (orderDetails.getPrice()*orderDetails.getQty()) - discount;
//				orderDetails.setSubtotal(subtotal);
//				dtList.set(position, orderDetails);
//
//				if(orderDetails.getQty() <= 0)
//		        {
//					dtList.remove(position);
//					if(listener != null)
//						listener.onRemove(orderDetails.getProductID());
//
//		        }
//				notifyDataSetChanged();
//			}
//		});
        
//        holder.btnPlus.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				orderDetails.setQty(orderDetails.getQty() + 1);
//
//				double discount = (orderDetails.getPrice()*orderDetails.getQty()) * (orderDetails.getDiscount()/100);
//				double subtotal = (orderDetails.getPrice()*orderDetails.getQty()) - discount;
//				orderDetails.setSubtotal(subtotal);
//
//				dtList.set(position, orderDetails);
//				notifyDataSetChanged();
//			}
//		});
        
        return vi;
    }
    

    
//    public void setCartListener(CartListener listener)
//    {
//    	this.listener = listener;
//    }
    
//    public interface CartListener {
//        public void onRemove(String result);
//        public void onChange(List<OrderDetails> list);
//    }
}