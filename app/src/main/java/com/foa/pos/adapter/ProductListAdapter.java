package com.foa.pos.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.foa.pos.R;
import com.foa.pos.model.MenuItem;
import com.foa.pos.fragment.ProductAddFragment;
import com.foa.pos.sqlite.DatabaseManager;
import com.foa.pos.sqlite.ds.MenuItemDataSource;
import com.foa.pos.utils.Constants;
import com.foa.pos.utils.Helper;

import java.util.List;

public class ProductListAdapter extends BaseAdapter {
 
    private List<MenuItem> dtList;
    private FragmentActivity context;
    private LayoutInflater inflater;
    private String itemID;
    public ProductListAdapter(FragmentActivity context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    public ProductListAdapter(FragmentActivity context, List<MenuItem> data) {
     
        this.context = context;
        this.dtList = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    public ProductListAdapter(FragmentActivity context, String itemid) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.itemID = itemid;
    }
    
 
    private class ViewHolder {
        TextView title;
        TextView body;
        ImageButton edit;
	    ImageButton delete;
    }
 
    public int getCount() {
        return dtList.size();
    }
    
    public void set(List<MenuItem> list) {
    	dtList = list;
        notifyDataSetChanged();
    }
    
    public void remove(MenuItem user) {
    	dtList.remove(user);
        notifyDataSetChanged();
    }
    
    public void add(MenuItem user) {
    	dtList.add(user);
        notifyDataSetChanged();
    }
    
    public void insert(MenuItem user, int index) {
    	dtList.add(index, user);
        notifyDataSetChanged();
    }
 
    public Object getItem(int position) {
        return dtList.get(position);
    }
 
    public long getItemId(int position) {
        return 0;
    }
 
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;
        
        if (convertView == null) {
        	vi = inflater.inflate(R.layout.product_list_item, null);
            holder = new ViewHolder();
 
            holder.title = (TextView) vi.findViewById(R.id.tvCartProductName);
            holder.body = (TextView) vi.findViewById(R.id.tvProductPrice);
            holder.edit = (ImageButton)vi.findViewById(R.id.imageView2);
            holder.delete = (ImageButton)vi.findViewById(R.id.imageView1);
            
            vi.setTag(holder);
        } else {
        	 holder=(ViewHolder)vi.getTag();
        }
        
        final MenuItem product = (MenuItem) getItem(position);
        holder.title.setText(product.getName());
        holder.body.setText(Helper.decimalformat.format(product.getPrice()));
        
        holder.edit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Fragment fragment  = new ProductAddFragment();
				Bundle arguments = new Bundle();
				arguments.putString(Constants.ARG_PRODUCT_ID, product.getId());
				
				if(itemID != null)
					arguments.putString(Constants.ARG_ITEM_ID, itemID);
				
				
				fragment.setArguments(arguments);
				context.getSupportFragmentManager().beginTransaction()
				.addToBackStack("add")
				.replace(R.id.master_detail_container, fragment).commit();
			}
		});
        
        holder.delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setMessage(R.string.delete_message).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
		               @Override
		               public void onClick(DialogInterface dialog, int id) {
		            		SQLiteDatabase db =  DatabaseManager.getInstance().openDatabase();
		        	        MenuItemDataSource ds = new MenuItemDataSource(db);
		        	        if(!ds.cekAvailable(product.getId()))
	            	    	{
		        	        	  
			        	        ds.delete(product.getGroupId());
			        	        dtList.remove(position);
			        	        notifyDataSetChanged();
			        	      
	            	    	}
	            	    	else
	            	    	{
	            	    		Toast.makeText(context, context.getString(R.string.data_being_used), Toast.LENGTH_SHORT).show();
	            	    	}
	            	    	 DatabaseManager.getInstance().closeDatabase();
		               }
		           }).setNegativeButton(R.string.cancel, null);
				
				builder.show();
				
			}
		});
        
        return vi;
    }
}