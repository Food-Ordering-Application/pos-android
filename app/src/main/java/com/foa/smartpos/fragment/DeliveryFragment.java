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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.foa.smartpos.R;
import com.foa.smartpos.adapter.CartListAdapter;
import com.foa.smartpos.adapter.DeliveryGridViewAdapter;
import com.foa.smartpos.adapter.OrderDetailListAdapter;
import com.foa.smartpos.api.RestaurantService;
import com.foa.smartpos.caching.CancelledDeliveryOrderCaching;
import com.foa.smartpos.caching.CompletedDeliveryOrderCaching;
import com.foa.smartpos.caching.ConfirmedDeliveryOrderCaching;
import com.foa.smartpos.caching.DetailDeliveryOrderCaching;
import com.foa.smartpos.dialog.EditOrderItemDialog;
import com.foa.smartpos.dialog.LoadingDialog;
import com.foa.smartpos.dialog.ScannerQRDialog;
import com.foa.smartpos.dialog.VoidNoteDialog;
import com.foa.smartpos.model.IDataResultCallback;
import com.foa.smartpos.model.Order;
import com.foa.smartpos.model.OrderItem;
import com.foa.smartpos.model.enums.OrderStatus;
import com.foa.smartpos.model.enums.OrderType;
import com.foa.smartpos.api.OrderService;
import com.foa.smartpos.model.enums.StockState;
import com.foa.smartpos.network.response.AutoConfirmData;
import com.foa.smartpos.session.NotificationOrderIdSession;
import com.foa.smartpos.sqlite.DatabaseManager;
import com.foa.smartpos.sqlite.ds.OrderDataSource;
import com.foa.smartpos.utils.Constants;
import com.foa.smartpos.utils.Debouncer;
import com.foa.smartpos.utils.Helper;
import com.google.gson.Gson;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.foa.smartpos.model.enums.OrderStatus.*;

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
    private Button btnReady;
    private Order orderSelected;
    private List<String> listOrderItemsOutStock = new ArrayList<>();
    private LoadingDialog loading;
    private LinearLayout deliveryOrderControlLayout;
    private LinearLayout progressLoading;
    private LinearLayout progressItemLoading;
    private  OrderDetailListAdapter detailAdapter;
    private OrderStatus selectedStatus = ORDERED;
    private boolean isSplitMode = false;
    private TextView emptyListTextView;
    private Switch autoConfirmSwitch;
    private ImageButton scannerQRButton;
    private TextView orderedCountTextView;
    private TextView confirmedCountTextView;
    private final Debouncer debouncer = new Debouncer();

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
        deliveryAdapter = new DeliveryGridViewAdapter(getActivity(), orderList,emptyListTextView);
        detailAdapter = new OrderDetailListAdapter(getActivity());
        orderRecyclerView.setAdapter(deliveryAdapter);
        deliveryAdapter.setOnSelectedItemListener((order, position) -> {
            detailAdapter.set(new ArrayList<>());
            if (order!=null){
                if (!isSplitMode){
                    enableSplitLayout(getActivity(),position);
                }
                isSplitMode=true;
                loadOrderDetail(order,detailLayout);
                orderSelected = order;
            }else{
                isSplitMode=false;
                disableSplitLayout(getActivity());
            }
        });

        initUpdateAutoConfirm();
        initQRScanner();
        initPusher();

        return root;
    }

    private void getOrderList(int pageNumber,OrderStatus status){
        OrderService.getAllOrder(OrderType.SALE.toString(), pageNumber,status.toString(), (success, data) -> {
            if (success){
                switch (status){
                    case ORDERED:
                        deliveryAdapter.setOrders(data);
                        orderedCountTextView.setText(String.valueOf(data.size()));
                        if(data.size()>0)
                            orderRecyclerView.smoothScrollToPosition(data.size()-1);
                        if (NotificationOrderIdSession.getInstance()!=null){
                            OrderService.getOrderById(NotificationOrderIdSession.getInstance(), (success1, data1) -> {
                                enableSplitLayout(getActivity(),data.size()-1);
                                isSplitMode = true;
                                loadOrderDetail(data1,detailLayout);
                                progressLoading.setVisibility(View.GONE);
                                deliveryAdapter.setCurrentOrderId(data1.getId());
                            });
                        }else {
                            progressLoading.setVisibility(View.GONE);
                        }
                        break;
                    case CONFIRMED:
                        confirmedCountTextView.setText(String.valueOf(data.size()));
                        ConfirmedDeliveryOrderCaching.setConfirmedDeliveryCatching(data);
                        break;
                    case READY:
                        CompletedDeliveryOrderCaching.setCompletedDeliveryCatching(data);
//                    case COMPLETED:
//                        CompletedDeliveryOrderCaching.setCompletedDeliveryCatching(data);
                        break;
                    case CANCELLED:
                        CancelledDeliveryOrderCaching.setCancelledDeliveryCatching(data);
                        break;
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        progressLoading.setVisibility(View.VISIBLE);
        getOrderList(1, ORDERED);
        getOrderList(1, CONFIRMED);
        getOrderList(1, READY);
        getOrderList(1, CANCELLED);

        RestaurantService.getAutoConfirm((success, data) -> {
            if (success){
                autoConfirmSwitch.setChecked(data.isAutoConfirm());
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
        btnReady  = root.findViewById(R.id.btnReady);
        btnConfirm.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnReady.setOnClickListener(this);
        deliveriesLayout =root.findViewById(R.id.bgDeliveries);
        detailLayout = root.findViewById(R.id.bgOrderDetail);
        deliveryOrderControlLayout = root.findViewById(R.id.deliveryOrderControlLayout);
        progressLoading = root.findViewById(R.id.progressLoading);
        progressItemLoading = root.findViewById(R.id.progressItemLoading);
        emptyListTextView = root.findViewById(R.id.emptyListTextView);
        autoConfirmSwitch = root.findViewById(R.id.autoConfirmSwitch);
        scannerQRButton = root.findViewById(R.id.scannerQRButton);
        confirmedCountTextView = root.findViewById(R.id.confirmedCountTextView);
        orderedCountTextView = root.findViewById(R.id.orderedCountTextView);
    }

    private void initUpdateAutoConfirm(){
        autoConfirmSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            debouncer.debounce(CartListAdapter.class, () ->
                    RestaurantService.updateIsAutoConfirm(autoConfirmSwitch.isChecked(), (success, data) -> {
                        if (success){
                            autoConfirmSwitch.setChecked(data.isAutoConfirm());
                        }
                    })
                    , 1000, TimeUnit.MILLISECONDS);

        });
    }

    private void initQRScanner(){
        scannerQRButton.setOnClickListener(view -> {
            ScannerQRDialog dialog = new ScannerQRDialog(getActivity());
            dialog.setQRScannerCallback(order -> {
                if (order!=null){
                    completedRB.setChecked(true);
                    deliveryAdapter.setOrders(new ArrayList<>());
                    getOrderListWithPosition(2);

                    for (int j = 0; j < radioButtonList.size(); j++) {
                        if(completedRB.getId()!=radioButtonList.get(j).getId()){
                            radioButtonList.get(j).setChecked(false);
                        }
                    }

                    List<Order> orderList  = CompletedDeliveryOrderCaching.getCompletedOrderCatching();
                    enableSplitLayout(getActivity(),orderList.size()-1);
                    isSplitMode = true;
                    loadOrderDetail(order,detailLayout);
                    progressLoading.setVisibility(View.GONE);
                    deliveryAdapter.setCurrentOrderId(order.getId());
                }
            });
            dialog.show();
        });
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
                Order updatedOrder = g.fromJson(event.getData(), Order.class);
                if (updatedOrder.getStatus() == COMPLETED){
                    if ( selectedStatus == COMPLETED){
                        deliveryAdapter.addOrder(orderSelected);
                    }else if(CompletedDeliveryOrderCaching.getCompletedOrderCatching()!=null){
                        CompletedDeliveryOrderCaching.addCompletedDeliveryCatching(orderSelected);
                    }
                }

            });

        });
    }

    private  void setGroupButtonListenter(){
        for (int i = 0; i < radioButtonList.size(); i++) {
            int finalI = i;
            radioButtonList.get(i).setOnClickListener(v -> {
                deliveryAdapter.setOrders(new ArrayList<>());
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
                displayConfirmControlLayout();
                progressLoading.setVisibility(View.VISIBLE);
                OrderService.getAllOrder(OrderType.SALE.name(), 1, ORDERED.toString(), (success, data) -> {
                    orderedCountTextView.setText(String.valueOf(data.size()));
                    deliveryAdapter.setOrders(data);
                    progressLoading.setVisibility(View.GONE);
                });
                selectedStatus= ORDERED;
                break;
            case 1:
                displayReadyControlLayout();
                List<Order> confirmedOrders = ConfirmedDeliveryOrderCaching.getConfirmedOrderCatching();
                if (confirmedOrders!=null){
                    deliveryAdapter.setOrders(confirmedOrders);
                    confirmedCountTextView.setText(String.valueOf(confirmedOrders.size()));
                    break;
                }
                progressLoading.setVisibility(View.VISIBLE);
                OrderService.getAllOrder(OrderType.SALE.name(), 1, CONFIRMED.toString() ,(success, data) -> {
                    deliveryAdapter.setOrders(data);
                    confirmedCountTextView.setText(String.valueOf(data.size()));
                    ConfirmedDeliveryOrderCaching.setConfirmedDeliveryCatching(data);
                    progressLoading.setVisibility(View.GONE);

                });
                selectedStatus= CONFIRMED;
                break;
            case 2:
                hideControlLayout();
                List<Order> completedOrders = CompletedDeliveryOrderCaching.getCompletedOrderCatching();
                if (completedOrders!=null){
                    deliveryAdapter.setOrders(completedOrders);
                    break;
                }
                OrderService.getAllOrder(OrderType.SALE.name(), 1, COMPLETED.toString(),(success, data) -> {
                    deliveryAdapter.setOrders(data);
                    CompletedDeliveryOrderCaching.setCompletedDeliveryCatching(data);
                    progressLoading.setVisibility(View.GONE);
                });
                selectedStatus= COMPLETED;
                break;
            case 3:
                hideControlLayout();
                List<Order> cancelledOrders = CancelledDeliveryOrderCaching.getCancelledOrderCatching();
                if (cancelledOrders!=null){
                    deliveryAdapter.setOrders(cancelledOrders);
                    break;
                }
                progressLoading.setVisibility(View.VISIBLE);
                OrderService.getAllOrder(OrderType.SALE.name(), 1, CANCELLED.toString(),(success, data) -> {
                    deliveryAdapter.setOrders(data);
                    CancelledDeliveryOrderCaching.setCancelledDeliveryCatching(data);
                    progressLoading.setVisibility(View.GONE);

                });
                selectedStatus= CANCELLED;
                break;
        }
    }

    private void displayConfirmControlLayout(){
        btnConfirm.setVisibility(View.VISIBLE);
        btnCancel.setVisibility(View.VISIBLE);
        btnReady.setVisibility(View.GONE);
    }

    private void displayReadyControlLayout(){
        btnConfirm.setVisibility(View.GONE);
        btnCancel.setVisibility(View.GONE);
        btnReady.setVisibility(View.VISIBLE);
    }

    private void hideControlLayout(){
        btnConfirm.setVisibility(View.GONE);
        btnCancel.setVisibility(View.GONE);
        btnReady.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ConfirmedDeliveryOrderCaching.clearInstance();
        CompletedDeliveryOrderCaching.clearInstance();
        CancelledDeliveryOrderCaching.clearInstance();
        DetailDeliveryOrderCaching.clearInstance();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnConfirm:
                OrderService.confirmOrder(orderSelected.getId(), success -> {
                    if (success){
                        Helper.disableSplitLayout(getActivity(),deliveriesLayout,detailLayout,orderRecyclerView);
                        isSplitMode = false;
                        deliveryAdapter.removeOrder(orderSelected);
                        if(ConfirmedDeliveryOrderCaching.getConfirmedOrderCatching()!=null){
                            orderSelected.setStatus(CONFIRMED);
                            ConfirmedDeliveryOrderCaching.addConfirmedDeliveryCatching(orderSelected);
                            confirmedCountTextView.setText(String.valueOf(ConfirmedDeliveryOrderCaching.getConfirmedOrderCatching().size()));
                        }
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
                        CancelledDeliveryOrderCaching.addCancelledDeliveryCatching(orderSelected);
                    });
                });
                dialog.show();
                break;
            case R.id.btnReady:
                OrderService.finnishOrder(orderSelected.getId(), success -> {
                    if (success){
                        Helper.disableSplitLayout(getActivity(),deliveriesLayout,detailLayout,orderRecyclerView);
                        isSplitMode = false;
                        deliveryAdapter.removeOrder(orderSelected);
                        if(CompletedDeliveryOrderCaching.getCompletedOrderCatching()!=null){
                            orderSelected.setStatus(COMPLETED);
                            CompletedDeliveryOrderCaching.addCompletedDeliveryCatching(orderSelected);
                        }
                    }else{
                        Toast.makeText(getActivity(), "Loi xac nhan", Toast.LENGTH_SHORT).show();
                    }
                });
                break;

        }
    }

    public void loadOrderDetail(Order order, RelativeLayout detailLayout){
        ((TextView)detailLayout.findViewById(R.id.tvOderId)).setText(String.valueOf(order.getId()));
        ((TextView)detailLayout.findViewById(R.id.qtyOrderItemTextView)).setText(Helper.formatMoney(order.getSubTotal()));
        ((TextView)detailLayout.findViewById(R.id.tvGrandTotal)).setText(Helper.formatMoney(order.getGrandTotal()));
        ((TextView)detailLayout.findViewById(R.id.tvShippingFee)).setText(Helper.formatMoney(order.getDelivery().getShippingFee()));
        ((TextView)detailLayout.findViewById(R.id.tvReceiveMoney)).setText(Helper.formatMoney(order.getGrandTotal()));
        ((TextView)detailLayout.findViewById(R.id.tvCustomerName)).setText(order.getDelivery().getCustomerName());

        Order orderCatching = DetailDeliveryOrderCaching.getOrderCatching(order);
        if (orderCatching!=null){
            detailAdapter.set(orderCatching.getOrderItems());
        }else{
            detailAdapter.set(new ArrayList<>());
            progressItemLoading.setVisibility(View.VISIBLE);
            OrderService.getOrderById(order.getId(), (success, data) -> {
                if (success){
                    detailAdapter.set(data.getOrderItems());
                    progressItemLoading.setVisibility(View.GONE);
                    DetailDeliveryOrderCaching.addDeliveryCatching(data);
                }
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
            if (selectedStatus == ORDERED) dialog.show();
        });
    }

    public void enableSplitLayout(Context context,int position) {
        final int width = Helper.getDisplayWidth();

        ViewGroup.LayoutParams param = deliveriesLayout.getLayoutParams();
        param.width = (width / 3)*2;
        deliveriesLayout.setLayoutParams(param);

        detailLayout.setVisibility(View.VISIBLE);
        ViewGroup.LayoutParams param2 = detailLayout.getLayoutParams();
        param2.width = (width / 3);
        detailLayout.setLayoutParams(param2);

        orderRecyclerView.setLayoutManager(new GridLayoutManager(context,3));
        if (position>0)
        orderRecyclerView.smoothScrollToPosition(position);
    }

    public void disableSplitLayout(Context context) {
        ViewGroup.LayoutParams param = deliveriesLayout.getLayoutParams();
        param.width = ViewGroup.LayoutParams.MATCH_PARENT;
        deliveriesLayout.setLayoutParams(param);
        detailLayout.setVisibility(View.GONE);
        orderRecyclerView.setLayoutManager(new GridLayoutManager(context,4));
    }
}