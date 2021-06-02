package com.foa.smartpos.fragment;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.foa.smartpos.R;
import com.foa.smartpos.adapter.DeliveryGridViewAdapter;
import com.foa.smartpos.adapter.OrderDetailListAdapter;
import com.foa.smartpos.catching.CancelledDeliveryOrderCatching;
import com.foa.smartpos.catching.CompletedDeliveryOrderCatching;
import com.foa.smartpos.catching.ConfirmedDeliveryOrderCatching;
import com.foa.smartpos.catching.DetailDeliveryOrderCatching;
import com.foa.smartpos.dialog.EditOrderItemDialog;
import com.foa.smartpos.dialog.LoadingDialog;
import com.foa.smartpos.dialog.VoidNoteDialog;
import com.foa.smartpos.model.Order;
import com.foa.smartpos.model.OrderItem;
import com.foa.smartpos.model.enums.OrderStatus;
import com.foa.smartpos.model.enums.OrderType;
import com.foa.smartpos.api.OrderService;
import com.foa.smartpos.model.enums.StockState;
import com.foa.smartpos.sqlite.DatabaseManager;
import com.foa.smartpos.sqlite.ds.OrderDataSource;
import com.foa.smartpos.utils.Constants;
import com.foa.smartpos.utils.Helper;
import com.google.gson.Gson;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.PusherEvent;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;
import com.pusher.pushnotifications.PushNotifications;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DeliveryFragment extends Fragment implements View.OnClickListener{
    private View root;
    private Context context;
    private RecyclerView orderRecyclerView;
    private LinearLayout deliveriesLayout;
    private RelativeLayout detailLayout;
    private RadioButton waitingAcceptRB;
    private RadioButton inProcessRB;
    private RadioButton completedRB;
    private RadioButton denyRadioButton;
    private List<RadioButton> radioButtonList;
    private OrderDataSource DS;
    private List<Order> orderList = new ArrayList<>();
    private DeliveryGridViewAdapter deliveryAdapter;
    private Button btnConfirm;
    private Button btnCancel;
    private Order orderSelected;
    private List<String> listOrderItemsOutStock = new ArrayList<>();
    private LoadingDialog loading;
    private LinearLayout deliveryOrderControlLayout;
    private LinearLayout progressLoading;
    private  OrderDetailListAdapter detailAdapter;
    private boolean isConfirmMode = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root =  inflater.inflate(R.layout.fragment_delivery, container, false);
        init();
        setGroupButtonListenter();
        //get data
        SQLiteDatabase db =  DatabaseManager.getInstance().openDatabase();
        DS = new OrderDataSource(db);
        context = getActivity();
        loading = new LoadingDialog(getActivity());

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),4);
        orderRecyclerView.setLayoutManager(layoutManager);
        deliveryAdapter = new DeliveryGridViewAdapter(getActivity(), orderList);
        detailAdapter = new OrderDetailListAdapter(getActivity());
        orderRecyclerView.setAdapter(deliveryAdapter);
        deliveryAdapter.setOnSelectedItemListener(order -> {
            detailAdapter.set(new ArrayList<>());
            if (order!=null){
                enableSplitLayout(getActivity());
                loadOrderDetail(order,detailLayout);
                orderSelected = order;
            }else{
                disableSplitLayout(getActivity());
            }
        });
//        PushNotifications.setOnMessageReceivedListenerForVisibleActivity(getActivity(), remoteMessage -> {
//           String orderId = remoteMessage.getData().get("orderId");
//            OrderService.getOrderById(orderId, (success, data) -> deliveryAdapter.addOrder(data));
//
//		});
//
        initPusher();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        progressLoading.setVisibility(View.VISIBLE);
        OrderService.getAllOrder(OrderType.SALE.toString(), 1,OrderStatus.ORDERED.toString(), (success, data) -> {
            if (success){
                deliveryAdapter.setOrders(data);
                progressLoading.setVisibility(View.GONE);
            }
        });
    }

    private void  init(){
        orderRecyclerView = root.findViewById(R.id.deliveryRecyclerView);
        waitingAcceptRB = root.findViewById(R.id.waitingAcceptRadioBuutton);
        inProcessRB = root.findViewById(R.id.inProcessRadioBuutton);
        completedRB = root.findViewById(R.id.completedRadioBuutton);
        denyRadioButton = root.findViewById(R.id.denyAcceptRadioBuutton);
        radioButtonList = Arrays.asList(waitingAcceptRB,inProcessRB,completedRB,denyRadioButton);
        btnConfirm  = root.findViewById(R.id.btnConfirm);
        btnCancel  = root.findViewById(R.id.btnCancel);
        btnConfirm.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        deliveriesLayout =root.findViewById(R.id.bgDeliveries);
        detailLayout = root.findViewById(R.id.bgOrderDetail);
        deliveryOrderControlLayout = root.findViewById(R.id.deliveryOrderControlLayout);
        progressLoading = root.findViewById(R.id.progressLoading);
    }

    private void initPusher(){
        PusherOptions options = new PusherOptions();
        options.setCluster("ap1");

        Pusher pusher = new Pusher("29ff5ecb5e2501177186", options);

        pusher.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                Log.i("Pusher", "State changed from " + change.getPreviousState() +
                        " to " + change.getCurrentState());
            }

            @Override
            public void onError(String message, String code, Exception e) {
                Log.i("Pusher", "There was a problem connecting! " +
                        "\ncode: " + code +
                        "\nmessage: " + message +
                        "\nException: " + e
                );
            }
        }, ConnectionState.ALL);

        Channel channel = pusher.subscribe("orders_"+Helper.read(Constants.RESTAURANT_ID));

        channel.bind("new-order", event -> {
            getActivity().runOnUiThread(() -> {
                Gson g = new Gson();
                Order newOrder = g.fromJson(event.getData(), Order.class);
                deliveryAdapter.addOrder(newOrder);
            });

        });

        channel.bind("order-status", event -> {
            getActivity().runOnUiThread(() -> {
                Gson g = new Gson();
                Order newOrder = g.fromJson(event.getData(), Order.class);
                
            });

        });
    }

    private  void setGroupButtonListenter(){
        for (int i = 0; i < radioButtonList.size(); i++) {
            int finalI = i;
            radioButtonList.get(i).setOnClickListener(v -> {
                getOrderListWithPosition(finalI);
                disableSplitLayout(getActivity());
                for (int j = 0; j < radioButtonList.size(); j++) {
                    if(v.getId()!=radioButtonList.get(j).getId()){
                        radioButtonList.get(j).setChecked(false);
                    }
                }
            });
        }
    }

    private void getOrderListWithPosition(int position){
        switch (position){
            case 0:
                deliveryOrderControlLayout.setVisibility(View.VISIBLE);
                progressLoading.setVisibility(View.VISIBLE);
                OrderService.getAllOrder(OrderType.SALE.name(), 1, OrderStatus.ORDERED.toString(), (success, data) -> {
                    deliveryAdapter.setOrders(data);
                    progressLoading.setVisibility(View.GONE);
                });
                isConfirmMode=true;
                break;
            case 1:
                deliveryOrderControlLayout.setVisibility(View.INVISIBLE);
                List<Order> confirmedorders = ConfirmedDeliveryOrderCatching.getConfirmedOrderCatching();
                if (confirmedorders!=null){
                    deliveryAdapter.setOrders(confirmedorders);
                    break;
                }
                progressLoading.setVisibility(View.VISIBLE);
                OrderService.getAllOrder(OrderType.SALE.name(), 1,OrderStatus.CONFIRMED.toString() ,(success, data) -> {
                    deliveryAdapter.setOrders(data);
                    ConfirmedDeliveryOrderCatching.setConfirmedDeliveryCatching(data);
                    progressLoading.setVisibility(View.GONE);

                });
                isConfirmMode=false;
                break;
            case 2:
                deliveryOrderControlLayout.setVisibility(View.INVISIBLE);
                List<Order> completedOrders = CompletedDeliveryOrderCatching.getCompletedOrderCatching();
                if (completedOrders!=null){
                    deliveryAdapter.setOrders(completedOrders);
                    break;
                }
                progressLoading.setVisibility(View.VISIBLE);
                OrderService.getAllOrder(OrderType.SALE.name(), 1, OrderStatus.COMPLETED.toString(),(success, data) -> {
                    deliveryAdapter.setOrders(data);
                    CompletedDeliveryOrderCatching.setCompletedDeliveryCatching(data);
                    progressLoading.setVisibility(View.GONE);
                });
                isConfirmMode=false;
                break;
            case 3:
                deliveryOrderControlLayout.setVisibility(View.INVISIBLE);
                List<Order> cancelledOrders = CancelledDeliveryOrderCatching.getCancelledOrderCatching();
                if (cancelledOrders!=null){
                    deliveryAdapter.setOrders(cancelledOrders);
                    break;
                }
                progressLoading.setVisibility(View.VISIBLE);
                OrderService.getAllOrder(OrderType.SALE.name(), 1, OrderStatus.CANCELLED.toString(),(success, data) -> {
                    deliveryAdapter.setOrders(data);
                    CancelledDeliveryOrderCatching.setCancelledDeliveryCatching(data);
                    progressLoading.setVisibility(View.GONE);

                });
                isConfirmMode=false;
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnConfirm:
                OrderService.confirmOrder(orderSelected.getId(), success -> {
                    if (success){
                        Helper.disableSplitLayout(getActivity(),deliveriesLayout,detailLayout,orderRecyclerView);
                        deliveryAdapter.removeOrder(orderSelected);
                    }else{
                        Toast.makeText(getActivity(), "Loi xac nhan", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.btnCancel:
                VoidNoteDialog dialog = new VoidNoteDialog(getActivity());
                dialog.setNoteReceiveListener(note -> {
                    loading.show();
                    OrderService.voidOrder(orderSelected.getId(), listOrderItemsOutStock, note, success -> {
                        loading.dismiss();
                        dialog.dismiss();
                        deliveryAdapter.removeOrder(orderSelected);
                    });
                });
                dialog.show();
                break;

        }
    }

    public void loadOrderDetail(Order order, RelativeLayout detailLayout){
        ((TextView)detailLayout.findViewById(R.id.tvTotal)).setText(Helper.formatMoney(order.getGrandTotal()));
        ((TextView)detailLayout.findViewById(R.id.tvTotalPay)).setText(Helper.formatMoney(order.getGrandTotal()));
        ((TextView)detailLayout.findViewById(R.id.tvOderId)).setText(String.valueOf(order.getId()));
        ((TextView)detailLayout.findViewById(R.id.tvReceiveMoney)).setText(Helper.formatMoney(order.getGrandTotal()));
        if((TextView)detailLayout.findViewById(R.id.tvChange)!=null){
            ((TextView)detailLayout.findViewById(R.id.tvChange)).setText(Helper.formatMoney(0));
        }

        Order orderCatching = DetailDeliveryOrderCatching.getOrderCatching(order);
        if (orderCatching!=null){
            detailAdapter.set(orderCatching.getOrderItems());
        }else{
            OrderService.getOrderById(order.getId(), (success, data) -> {
                detailAdapter.set(data.getOrderItems());
                DetailDeliveryOrderCatching.addDeliveryCatching(data);
            });
        }

        ListView detailsListView = detailLayout.findViewById(R.id.listOrderDetails);
        detailsListView.setAdapter(detailAdapter);
        detailsListView.setOnItemClickListener((parent, view, position, id) -> {
            OrderItem item =(OrderItem) detailsListView.getItemAtPosition(position);
            EditOrderItemDialog dialog = new EditOrderItemDialog(getActivity(),item);
            dialog.setOutOfProductListener(isChecked -> {
                StockState stockState  = isChecked? StockState.OUT_OF_STOCK:StockState.IN_STOCK;
                item.setStockState(stockState);
                detailAdapter.updateStockStateOrderItem(item.getId(),stockState);
                if (isChecked){
                    listOrderItemsOutStock.add(item.getId());
                }else{
                    listOrderItemsOutStock.remove(item.getId());
                }
                if (listOrderItemsOutStock.size()>0){
                    btnConfirm.setVisibility(View.GONE);
                }else{
                    btnConfirm.setVisibility(View.VISIBLE);
                }
            });
            if (isConfirmMode) dialog.show();
        });
    }

    public void enableSplitLayout(Context context) {
        final int width = Helper.getDisplayWidth();

        ViewGroup.LayoutParams param = deliveriesLayout.getLayoutParams();
        param.width = (width / 3)*2;
        deliveriesLayout.setLayoutParams(param);

        detailLayout.setVisibility(View.VISIBLE);
        ViewGroup.LayoutParams param2 = detailLayout.getLayoutParams();
        param2.width = (width / 3);
        detailLayout.setLayoutParams(param2);

        orderRecyclerView.setLayoutManager(new GridLayoutManager(context,3));
    }

    public void disableSplitLayout(Context context) {
        ViewGroup.LayoutParams param = deliveriesLayout.getLayoutParams();
        param.width = ViewGroup.LayoutParams.MATCH_PARENT;
        deliveriesLayout.setLayoutParams(param);
        detailLayout.setVisibility(View.GONE);
        orderRecyclerView.setLayoutManager(new GridLayoutManager(context,4));
    }
}