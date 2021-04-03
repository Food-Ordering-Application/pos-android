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
import com.foa.pos.entity.User;
import com.foa.pos.fragment.UserAddFragment;
import com.foa.pos.sqlite.DatabaseManager;
import com.foa.pos.sqlite.ds.UserDataSource;
import com.foa.pos.utils.Constants;
import com.foa.pos.utils.Helper;

import java.util.List;

public class UserListAdapter extends BaseAdapter {
 
    private List<User> dtList;
    private FragmentActivity context;
    private LayoutInflater inflater;
    private String itemID;
    public UserListAdapter(FragmentActivity context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    public UserListAdapter(FragmentActivity context, List<User> data) {
     
        this.context = context;
        this.dtList = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    public UserListAdapter(FragmentActivity context, String itemid) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.itemID = itemid;
    }
 
    private class ViewHolder {
        TextView title;
        TextView body;
        TextView desc1;
        ImageButton edit;
	    ImageButton delete;
    }
 
    public int getCount() {
        return dtList.size();
    }
    
    public void set(List<User> list) {
    	dtList = list;
        notifyDataSetChanged();
    }
    
    public void remove(User user) {
    	dtList.remove(user);
        notifyDataSetChanged();
    }
    
    public void add(User user) {
    	dtList.add(user);
        notifyDataSetChanged();
    }
    
    public void insert(User user, int index) {
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
        	vi = inflater.inflate(R.layout.user_list_item, null);
            holder = new ViewHolder();
 
            holder.title = (TextView) vi.findViewById(R.id.tvCartProductName);
            holder.body = (TextView) vi.findViewById(R.id.tvProductPrice);
            holder.desc1 = (TextView) vi.findViewById(R.id.tvTotal);
            holder.edit = (ImageButton)vi.findViewById(R.id.imageView2);
            holder.delete = (ImageButton)vi.findViewById(R.id.imageView1);
            vi.setTag(holder);
        } else {
        	 holder=(ViewHolder)vi.getTag();
        }
        
      
        final User user = (User) getItem(position);
        holder.title.setText(user.getUserName());
        holder.body.setText(user.getLevel().equals("1") ? "administrator" : "cashier" );
        if(user.getLastLogin() != null)
        	holder.desc1.setText("last login: " + Helper.dateformatID.format(user.getLastLogin()));
        else
        	holder.desc1.setText("last login: - ");
        
        holder.edit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Fragment fragment  = new UserAddFragment();
				Bundle arguments = new Bundle();
				arguments.putString(Constants.ARG_USER_ID, user.getUserID());
				
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
		            	    if(user.getUserID().equals(com.foa.pos.MainActivity.SesID))
		            	    {
		            	    	Toast.makeText(context, context.getString(R.string.data_being_used), Toast.LENGTH_SHORT).show();
		            	    }
		            	    else
		            	    {
		            	    	
		            	    	SQLiteDatabase db =  DatabaseManager.getInstance().openDatabase();
		            	    	UserDataSource ds = new UserDataSource(db);
		            	    	if(!ds.cekAvailable(user.getUserID()))
		            	    	{
		            	    		ds.delete(user.getUserID());
				        	        dtList.remove(position);
				        	        notifyDataSetChanged();
				        	      
		            	    	}
		            	    	else
		            	    	{
		            	    		Toast.makeText(context, context.getString(R.string.data_being_used), Toast.LENGTH_SHORT).show();
		            	    	}
		            	    	
		            	    	  DatabaseManager.getInstance().closeDatabase();
			        	       
		            	    }
		            		
		               }
		           }).setNegativeButton(R.string.cancel, null);
				
				builder.show();
				
			}
		});
        
        return vi;
    }
}