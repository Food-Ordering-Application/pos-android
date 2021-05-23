package com.foa.pos.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.foa.pos.R;
import com.foa.pos.model.MenuItem;
import com.foa.pos.utils.Constants;
import com.foa.pos.utils.Helper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProductGridAdapter extends BaseAdapter {
 
    private List<MenuItem> dtList = new ArrayList<MenuItem>();
    private List<String> selected = new ArrayList<>();

    private Activity context;
    private LayoutInflater inflater;

    public ProductGridAdapter(Activity context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    private class ViewHolder {
        TextView name;
        TextView price;
        ImageView image;
    }
 
    public int getCount() {
        return dtList.size();
    }

    public void setSelection(String id) {
        int index = selected.indexOf(id);
        if(index == -1)
            selected.add(id);
        else
            selected.remove(id);

        this.notifyDataSetChanged();
    }

    public boolean isSelected(String id) {
        return selected.contains(id);
    }
    

    public void set(List<MenuItem> list) {
    	dtList = list;
    	this.notifyDataSetChanged();
    }
    
    public void remove(MenuItem user) {
        dtList.remove(user);
        this.notifyDataSetChanged();
    }

    
    public void unCheckAll() {
        selected.clear();
        this.notifyDataSetChanged();
    }
    
    public void reset() {
        selected.clear();
        this.notifyDataSetChanged();
    }
    
    public void add(MenuItem product) {
    	dtList.add(product);
        this.notifyDataSetChanged();
    }
    
    public void insert(MenuItem product, int index) {
        dtList.add(index, product);
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
            vi = inflater.inflate(R.layout.product_grid_item, null);

            holder = new ViewHolder();
 
            holder.name = vi.findViewById(R.id.tvCartProductName);
            holder.price = vi.findViewById(R.id.tvProductPrice);
            holder.image = vi.findViewById(R.id.imageView1);
            
            vi.setTag(holder);

        } else {
        	 holder=(ViewHolder)vi.getTag();
        }
        
        final MenuItem menuItem = (MenuItem) getItem(position);
        holder.name.setText(menuItem.getCategoryName());
        holder.price.setText(Helper.formatMoney((menuItem.getPrice())));

        if(menuItem.getImage() != null)
    	{
        	File imgFile = new File(Helper.getAppDir()+File.separator+menuItem.getId());
    		if(imgFile.exists()){
    			holder.image.setImageDrawable(Drawable.createFromPath(imgFile.getAbsolutePath()));
    		}
    	}
        
        return vi;
    }
}