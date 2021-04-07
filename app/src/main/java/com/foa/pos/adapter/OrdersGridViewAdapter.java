package com.foa.pos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.foa.pos.R;
import com.foa.pos.entity.Item;
import com.ramotion.foldingcell.FoldingCell;

import java.util.HashSet;
import java.util.List;

@SuppressWarnings({"WeakerAccess", "unused"})
public class OrdersGridViewAdapter extends ArrayAdapter<Item> {
    List<Item> items;

    public OrdersGridViewAdapter(Context context, List<Item> objects) {
        super(context, 0, objects);
        this.items = objects;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Nullable
    @Override
    public Item getItem(int position) {
        return items.get(position);
    }


    @Override
    public long getItemId(int position) {
        return getItem(position).getID();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // get item for selected view
        Item item = getItem(position);
        // if ordersView is exists - reuse it, if not - create the new one from resource
        LinearLayout ordersView = (LinearLayout) convertView;
        ViewHolder viewHolder;
        if (ordersView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            ordersView = (LinearLayout) vi.inflate(R.layout.order_item_card, parent, false);
            // binding view parts to view holder
            viewHolder.price = ordersView.findViewById(R.id.title_price);
            viewHolder.time = ordersView.findViewById(R.id.title_time_label);
            viewHolder.date = ordersView.findViewById(R.id.title_date_label);
            viewHolder.fromAddress = ordersView.findViewById(R.id.title_from_address);
            viewHolder.toAddress = ordersView.findViewById(R.id.title_to_address);
            viewHolder.requestsCount = ordersView.findViewById(R.id.title_requests_count);
            viewHolder.pledgePrice = ordersView.findViewById(R.id.title_pledge);
            viewHolder.rightPart = ordersView.findViewById(R.id.rightPart);
            viewHolder.orderCard = ordersView.findViewById(R.id.orderscard);
            ordersView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) ordersView.getTag();
        }

        if (null == item)
            return ordersView;

        // bind data from selected element to view through view holder
        viewHolder.price.setText(item.getPrice());
        viewHolder.time.setText(item.getTime());
        viewHolder.date.setText(item.getDate());
        viewHolder.fromAddress.setText(item.getFromAddress());
        viewHolder.toAddress.setText(item.getToAddress());
        viewHolder.requestsCount.setText(String.valueOf(item.getRequestsCount()));
        viewHolder.pledgePrice.setText(item.getPledgePrice());
        return ordersView;
    }

    // View lookup cache
    private static class ViewHolder {
        LinearLayout orderCard;
        RelativeLayout rightPart;
        TextView price;
        TextView contentRequestBtn;
        TextView pledgePrice;
        TextView fromAddress;
        TextView toAddress;
        TextView requestsCount;
        TextView date;
        TextView time;
    }
}
