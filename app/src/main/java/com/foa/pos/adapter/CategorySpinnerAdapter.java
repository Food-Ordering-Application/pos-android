package com.foa.pos.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.foa.pos.R;
import com.foa.pos.model.MenuGroup;

import java.util.ArrayList;
import java.util.List;

public class CategorySpinnerAdapter extends BaseAdapter {
 
    private List<MenuGroup> dtList = new ArrayList<MenuGroup>();
    private Activity context;
    private LayoutInflater inflater;
    public CategorySpinnerAdapter(Activity context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    public CategorySpinnerAdapter(Activity context, List<MenuGroup> data) {
     
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
    
    public void set(List<MenuGroup> list) {
    	dtList = list;
        notifyDataSetChanged();
    }
    
    public int indexOf(String code) {
    	int index = -1;
    	for(int i = 0;i < dtList.size();i++) {
			if(code.equals(dtList.get(i).getId()))
				index = i;
		}
    	return index;
    }
    
    public void remove(MenuGroup user) {
    	dtList.remove(user);
        notifyDataSetChanged();
    }
    
    public void add(MenuGroup user) {
    	dtList.add(user);
        notifyDataSetChanged();
    }
    
    public void insert(MenuGroup user, int index) {
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
        
        final MenuGroup category = (MenuGroup) getItem(position);

        holder.title.setText( category.getName());

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
        
        final MenuGroup category = (MenuGroup) getItem(position);
        holder.title.setText(category.getName());

        return vi;
	}
    
}