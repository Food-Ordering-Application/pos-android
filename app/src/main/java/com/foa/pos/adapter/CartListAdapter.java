package com.foa.pos.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.foa.pos.R;
import com.foa.pos.model.IResultCallback;
import com.foa.pos.model.Order;
import com.foa.pos.model.OrderItem;
import com.foa.pos.model.OrderItemTopping;
import com.foa.pos.network.RetrofitClient;
import com.foa.pos.network.entity.RemoveOrderItemBody;
import com.foa.pos.network.response.LoginData;
import com.foa.pos.network.response.OrderData;
import com.foa.pos.network.response.ResponseAdapter;
import com.foa.pos.service.OrderService;
import com.foa.pos.sqlite.DatabaseManager;
import com.foa.pos.sqlite.ds.OrderDataSource;
import com.foa.pos.utils.Constants;
import com.foa.pos.utils.Debouncer;
import com.foa.pos.utils.Helper;
import com.foa.pos.utils.LoggerHelper;
import com.foa.pos.utils.LoginSession;
import com.foa.pos.utils.OrderSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartListAdapter extends BaseAdapter {

    private List<OrderItem> dtList = new ArrayList<>();
    private Activity context;
    private LayoutInflater inflater;
    private CartListener listener;
    private OrderDataSource OrderDS;
    private boolean isPayment = false;
    private LoginData loginData;
    private OrderService orderService;

    private final Debouncer debouncer = new Debouncer();


    public CartListAdapter(Activity context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        OrderDS = new OrderDataSource(db);
        loginData = LoginSession.getInstance();
        orderService = new OrderService();
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

    public void setIsPayment(boolean isPayment) {
        this.isPayment = isPayment;
    }

    public void set(List<OrderItem> list) {
        dtList = list;
        notifyDataSetChanged();
    }

    public void removeAll() {
        if (dtList.size() > 0) {
            OrderDS.deleteOrder(dtList.get(0).getOrderId());
            dtList = new ArrayList<>();
            notifyDataSetChanged();
        }

    }

    public void removeByID(String orderItemId) {
        if(loginData==null){
            removeOrderItemQuantityOffline(orderItemId);
        }else {
            removeOrderItemQuantityOnline(orderItemId, new IResultCallback() {
                @Override
                public void onSuccess(boolean success) {

                }

                @Override
                public void onError() {

                }
            });
        }
        notifyDataSetChanged();
    }

    public void add(OrderItem user) {
        dtList.add(user);
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

        if (listener != null && loginData !=null) {
            listener.onChange(OrderSession.getInstance());
        }

    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;

        if (convertView == null) {
            vi = inflater.inflate(R.layout.cart_list_item, null);
            holder = new ViewHolder();

            holder.name = vi.findViewById(R.id.tvCartProductName);
            holder.price = vi.findViewById(R.id.tvProductPrice);
            holder.qty = vi.findViewById(R.id.tvTotal);
            holder.btnMinus = vi.findViewById(R.id.btnSubQuantityCartItem);
            holder.btnPlus = vi.findViewById(R.id.btnPlusQuantityCartItem);

            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }

        if (isPayment) {
            holder.btnMinus.setVisibility(View.INVISIBLE);
            holder.btnPlus.setVisibility(View.INVISIBLE);
        }

        final OrderItem orderItem = (OrderItem) getItem(position);
        holder.name.setText(orderItem.getMenuItemName());
        holder.price.setText(Helper.decimalformat.format(orderItem.getQuantity() * orderItem.getPrice()) + " " + Helper.read(Constants.KEY_SETTING_CURRENCY_SYMBOL, Constants.VAL_DEFAULT_CURRENCY_SYMBOL));
        holder.qty.setText(String.valueOf(orderItem.getQuantity()));
        if (orderItem.getQuantity() == 1) {
            holder.btnMinus.setImageResource(R.drawable.ic_baseline_delete_24);
        } else {
            holder.btnMinus.setImageResource(R.drawable.ic_remove_circle);
        }

        if (orderItem.getQuantity() <= 0) {
            holder.btnMinus.setEnabled(false);
        }

        holder.btnMinus.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            int newQuantity = orderItem.getQuantity() - 1;
            orderItem.setQuantity(newQuantity);

            debouncer.debounce(CartListAdapter.class, () ->
                    updateOrRemoveOrderItemQuantity(orderItem) , 1000, TimeUnit.MILLISECONDS);


            long discount = (orderItem.getPrice() * orderItem.getQuantity()) * (orderItem.getDiscount() / 100);
            long subtotal = (orderItem.getPrice() * orderItem.getQuantity()) - discount;
            orderItem.setSubTotal(subtotal);
            dtList.set(position, orderItem);

            if (orderItem.getQuantity() <= 0) {
                dtList.remove(position);
                if (listener != null)
                    listener.onRemove(orderItem.getId());
                    removeByID(orderItem.getId());
            }
            notifyDataSetChanged();
        });

        holder.btnPlus.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            orderItem.setQuantity(orderItem.getQuantity() + 1);
            debouncer.debounce(CartListAdapter.class, () ->
                    updateOrderItemQuantity(orderItem), 1000, TimeUnit.MILLISECONDS);

            long discount = (orderItem.getPrice() * orderItem.getQuantity()) * (orderItem.getDiscount() / 100);
            long subtotal = calculateSubTotal(orderItem) - discount;
            orderItem.setSubTotal(subtotal);

            dtList.set(position, orderItem);
            notifyDataSetChanged();
        });

        return vi;
    }

    private void updateOrRemoveOrderItemQuantity(OrderItem orderItem){
         if(orderItem.getQuantity() >0 ) {
             updateOrderItemQuantity(orderItem);
         }
         else{
             removeOrderItemQuantity(orderItem.getId());
         }
    }

    private long calculateSubTotal(OrderItem orderItem){
       return(orderItem.getPrice() +
               orderItem.getOrderItemToppings().stream().mapToLong(OrderItemTopping::getPrice).sum())
               * orderItem.getQuantity();
    }

    private void updateOrderItemQuantity(OrderItem orderItem) {
        if (loginData != null) {
            orderService.updateOrderItemQuantityOnline(OrderSession.getInstance().getId(),orderItem.getId(), orderItem.getQuantity(), new IResultCallback() {
                @Override
                public void onSuccess(boolean success) {
                    if (success) {
                        Log.i("[CartListAdapter][Api call]", "Order item " + orderItem.getId() + "update new quantity" + orderItem.getQuantity());
                    }
                }

                @Override
                public void onError() {

                }
            });
        } else {
            OrderDS.updateOrderItemQuantity(orderItem.getId(), orderItem.getQuantity());
        }

    }


    public void setCartListener(CartListener listener) {
        this.listener = listener;
    }

    public interface CartListener {
        void onRemove(String result);

        void onChange(Order currentOrder);
    }

    private void removeOrderItemQuantity(String orderItemId) {
        if (loginData!=null){
            removeOrderItemQuantityOnline(orderItemId, new IResultCallback() {
                @Override
                public void onSuccess(boolean success) {

                }

                @Override
                public void onError() {

                }
            });
        }else {
            removeOrderItemQuantityOffline(orderItemId);
        }
    }

    private void removeOrderItemQuantityOffline(String orderItemId) {

        for (int i = 0; i < dtList.size(); i++) {
            if (dtList.get(i).getId().equals(orderItemId)) {
                OrderDS.deleteOrderItem(dtList.get(i).getId());
                dtList.remove(i);
            }
            if (dtList.size() == 0) {
                OrderDS.deleteOrder(dtList.get(i).getOrderId());
            }
        }
    }

    private void removeOrderItemQuantityOnline(String orderItemId, IResultCallback resultCallback) {
        Call<ResponseAdapter<OrderData>> responseCall = RetrofitClient.getInstance().getAppService()
                .removeOrderItem(OrderSession.getInstance().getId(),new RemoveOrderItemBody(orderItemId));
        responseCall.enqueue(new Callback<ResponseAdapter<OrderData>>() {
            @Override
            public void onResponse(Call<ResponseAdapter<OrderData>> call, Response<ResponseAdapter<OrderData>> response) {
                if (response.errorBody() != null) {
                    try {
                        Log.e("[OrderFragment][Api error]", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (response.code() == Constants.STATUS_CODE_SUCCESS) {
                    resultCallback.onSuccess(true);
                    ResponseAdapter<OrderData> res = response.body();
                    assert res != null;
                    if (res.getStatus() == Constants.STATUS_CODE_CREATED) {
                        listener.onChange(res.getData().getOrder());
                    } else {
                        LoggerHelper.CheckAndLogInfo(this,res.getMessage());
                    }
                } else {
                    resultCallback.onSuccess(false);
                    LoggerHelper.CheckAndLogInfo(this,response.errorBody().toString());
                }

            }

            @Override
            public void onFailure(Call<ResponseAdapter<OrderData>> call, Throwable t) {
                Log.e("Login Error", t.getMessage());
            }
        });
    }

}