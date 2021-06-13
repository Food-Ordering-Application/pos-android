package com.foa.smartpos.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.foa.smartpos.R;
import com.foa.smartpos.api.RestaurantService;
import com.foa.smartpos.model.IResultCallback;
import com.foa.smartpos.model.MenuItem;
import com.foa.smartpos.model.Order;
import com.foa.smartpos.model.ToppingItem;
import com.foa.smartpos.model.enums.StockState;
import com.foa.smartpos.sqlite.DatabaseManager;
import com.foa.smartpos.sqlite.ds.MenuItemDataSource;
import com.foa.smartpos.sqlite.ds.ToppingItemDataSource;
import com.foa.smartpos.utils.EnumHelper;
import com.foa.smartpos.utils.Helper;

import java.util.List;

public class SettingMenuToppingAdapter extends RecyclerView.Adapter<SettingMenuToppingAdapter.ViewHolder> {
    private List<MenuItem> menuItems;
    private List<ToppingItem> toppingItems;
    private Context context;
    private MenuItemDataSource menuItemDS;
    private ToppingItemDataSource toppingItemDS;


    public SettingMenuToppingAdapter(Context context) {
        this.context = context;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        menuItemDS = new MenuItemDataSource(db);
        toppingItemDS = new ToppingItemDataSource(db);
    }

    public void setMenuItems(List<MenuItem> menuItems){
        this.menuItems = menuItems;
        this.toppingItems = null;
        notifyDataSetChanged();

    }

    public void setToppingItems(List<ToppingItem> toppingItems){
        this.toppingItems = toppingItems;
        this.menuItems = null;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.setting_menu_topping_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (menuItems!=null){
            MenuItem menuItem = menuItems.get(position);
            holder.itemId.setText(menuItem.getId());
            holder.itemName.setText(menuItem.getName());
            holder.itemPrice.setText(String.valueOf(menuItem.getPrice()));
            holder.itemStatus.setChecked(menuItem.getStockState().equals(StockState.IN_STOCK));
            holder.itemStatus.setOnCheckedChangeListener((compoundButton, b) -> {
                StockState stockState = b? StockState.IN_STOCK: StockState.OUT_OF_STOCK;
                menuItemDS.updateStockState(menuItem.getId(),stockState);
                RestaurantService.updateMenuItemStockState(menuItem.getId(), stockState, success -> {

                });
            });
            holder.itemView.setTag(menuItem);
        }else {
            ToppingItem toppingItem = toppingItems.get(position);
            holder.itemId.setText(toppingItem.getId());
            holder.itemName.setText(toppingItem.getName());
            holder.itemPrice.setText(Helper.formatMoney(toppingItem.getPrice()));
            holder.itemStatus.setChecked(toppingItem.getStockState().equals(StockState.IN_STOCK));
            holder.itemStatus.setOnCheckedChangeListener((compoundButton, b) -> {
                StockState stockState = b? StockState.IN_STOCK: StockState.OUT_OF_STOCK;
                toppingItemDS.updateStockState(toppingItem.getId(),stockState);
                RestaurantService.updateToppingItemStockState(toppingItem.getId(), stockState, success -> {

                });
            });
            holder.itemView.setTag(toppingItem);
        }
    }

    @Override
    public int getItemCount() {
        if (menuItems!=null){
            return menuItems.size();
        }
        return toppingItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemId;
        TextView itemName;
        TextView itemPrice;
        Switch itemStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemId = itemView.findViewById(R.id.itemId);
            itemName = itemView.findViewById(R.id.itemName);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            itemStatus = itemView.findViewById(R.id.itemSatus);
        }
    }
}
