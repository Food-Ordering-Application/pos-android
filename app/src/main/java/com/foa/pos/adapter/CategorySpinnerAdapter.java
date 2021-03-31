package com.foa.pos.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.foa.pos.R;
import com.foa.pos.entity.ProductCategory;
import com.foa.pos.utils.Helper;

import java.util.ArrayList;
import java.util.List;

public class CategorySpinnerAdapter extends BaseAdapter {
 
    private List<ProductCategory> dtList = new ArrayList<ProductCategory>();
    private Activity context;
    private LayoutInflater inflater;
    public CategorySpinnerAdapter(Activity context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    public CategorySpinnerAdapter(Activity context, List<ProductCategory> data) {
     
        this.context = context;
        this.dtList = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
 
    private class ViewHolder {
        TextView title;
    }
 
    public int getCount() {
        return dtList.size();
    }
    
    public void set(List<ProductCategory> list) {
    	dtList = list;
        notifyDataSetChanged();
    }
    
    public int indexOf(String code) {
    	int index = -1;
    	for(int i = 0;i < dtList.size();i++) {
			if(code.equals(dtList.get(i).getCategoryID()))
				index = i;
		}
    	return index;
    }
    
    public void remove(ProductCategory user) {
    	dtList.remove(user);
        notifyDataSetChanged();
    }
    
    public void add(ProductCategory user) {
    	dtList.add(user);
        notifyDataSetChanged();
    }
    
    public void insert(ProductCategory user, int index) {
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
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
    	// TODO Auto-generated method stub
    	View vi = convertView;
        ViewHolder holder;
        
        if (convertView == null) {
        	vi = inflater.inflate(R.layout.category_spinner_item, null);
            holder = new ViewHolder();
 
            holder.title = (TextView) vi.findViewById(R.id.tvCartProductName);
            
            vi.setTag(holder);
        } else {
        	 holder=(ViewHolder)vi.getTag();
        }
        
        final ProductCategory category = (ProductCategory) getItem(position);
        holder.title.setText(category.getCategoryName());
        holder.title.setTypeface(Helper.OpenSansRegular);
        
        return vi;
    }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View vi = convertView;
        ViewHolder holder;
        
        if (convertView == null) {
        	vi = inflater.inflate(R.layout.category_spinner_item, null);
            holder = new ViewHolder();
 
            holder.title = (TextView) vi.findViewById(R.id.tvCartProductName);
            
            vi.setTag(holder);
        } else {
        	 holder=(ViewHolder)vi.getTag();
        }
        
        final ProductCategory category = (ProductCategory) getItem(position);
        holder.title.setText(category.getCategoryName());
        holder.title.setTypeface(Helper.OpenSansRegular);
        
        return vi;
	}
    
}