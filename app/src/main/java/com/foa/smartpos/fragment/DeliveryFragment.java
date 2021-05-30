package com.foa.smartpos.fragment;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.foa.smartpos.catching.DeliveryOrderCatching;
import com.foa.smartpos.dialog.EditOrderItemDialog;
import com.foa.smartpos.dialog.VoidNoteDialog;
import com.foa.smartpos.model.Order;
import com.foa.smartpos.model.OrderItem;
import com.foa.smartpos.model.enums.OrderStatus;
import com.foa.smartpos.model.enums.OrderType;
import com.foa.smartpos.api.OrderService;
import com.foa.smartpos.sqlite.DatabaseManager;
import com.foa.smartpos.sqlite.ds.OrderDataSource;
import com.foa.smartpos.utils.Helper;
import com.pusher.pushnotifications.PushNotifications;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    private DeliveryGridViewAdapter adapter;
    private Button btnConfirm;
    private Button btnCancel;
    private Order orderSelected;
    private List<String> listOrderItemsOutStock = new ArrayList<>();

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

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),4);
        orderRecyclerView.setLayoutManager(layoutManager);
        adapter = new DeliveryGridViewAdapter(getActivity(), orderList);
        orderRecyclerView.setAdapter(adapter);
        adapter.setOnSelectedItemListener(order -> {
            if (order!=null){
                enableSplitLayout(getActivity());
                loadOrderDetail(order,detailLayout);
                orderSelected = order;
            }else{
                disableSplitLayout(getActivity());
            }
        });
        OrderService.getAllOrder("SALE", 1, (success, data) -> {
            if (success){

                adapter.setOrders(data.stream().filter(p -> p.getStatus()!=null && p.getStatus().equals(OrderStatus.ORDERED) ).collect(Collectors.toList()));
            }
        });

        PushNotifications.setOnMessageReceivedListenerForVisibleActivity(getActivity(), remoteMessage -> {
           String orderId = remoteMessage.getData().get("orderId");
            OrderService.getOrderById(orderId, (success, data) -> adapter.addOrder(data));

		});

        return root;
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
    }

    private  void setGroupButtonListenter(){
        for (int i = 0; i < radioButtonList.size(); i++) {
            int finalI = i;
            radioButtonList.get(i).setOnClickListener(v -> {
                getOrderListWithPosition(finalI);
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
                OrderService.getAllOrder(OrderType.SALE.name(), 1, (success, data) -> {

                });
                break;
            case 1:
                OrderService.getAllOrder(OrderType.SALE.name(), 1, (success, data) -> {

                });
                break;
            case 2:
                OrderService.getAllOrder(OrderType.SALE.name(), 1, (success, data) -> {

                });
                break;
            case 3:
                OrderService.getAllOrder(OrderType.SALE.name(), 1, (success, data) -> {

                });
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
                    }else{
                        Toast.makeText(getActivity(), "Loi xac nhan", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.btnCancel:
                VoidNoteDialog dialog = new VoidNoteDialog(getActivity());
                dialog.setNoteReceiveListener(note -> {
                    OrderService.voidOrder(orderSelected.getId(), listOrderItemsOutStock, note, success -> {

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

        OrderDetailListAdapter adapter = new OrderDetailListAdapter(getActivity());

        Order orderCatching = DeliveryOrderCatching.getOrderCatching(order);
        if (orderCatching!=null){
            adapter.set(orderCatching.getOrderItems());
        }else{
            OrderService.getOrderById(order.getId(), (success, data) -> {
                adapter.set(data.getOrderItems());
                DeliveryOrderCatching.addDeliveryCatching(data);
            });
        }

        ListView detailsListView = detailLayout.findViewById(R.id.listOrderDetails);
        detailsListView.setAdapter(adapter);
        detailsListView.setOnItemClickListener((parent, view, position, id) -> {
            OrderItem item =(OrderItem) detailsListView.getItemAtPosition(position);
            EditOrderItemDialog dialog = new EditOrderItemDialog(getActivity(),item);
            dialog.setOutOfProductListener(isChecked -> {
                adapter.notifyDataSetChanged();
                if (isChecked){
                    listOrderItemsOutStock.add(item.getId());
                }else{
                    listOrderItemsOutStock.remove(item.getId());
                }
                if (listOrderItemsOutStock.size()>0){
                    btnConfirm.setVisibility(View.GONE);
                }
            });
            dialog.show();
        });
    }

    public void enableSplitLayout(Context context) {
        final int width = Helper.getDisplayWidth();
        ViewGroup.LayoutParams param = deliveriesLayout.getLayoutParams();
        param.width = (width / 3)*2;
        deliveriesLayout.setLayoutParams(param);
        ViewGroup.LayoutParams param2 = detailLayout.getLayoutParams();
        param2.width = (width / 3);
        detailLayout.setLayoutParams(param2);
        detailLayout.setVisibility(View.VISIBLE);
        orderRecyclerView.setLayoutManager(new GridLayoutManager(context,3));
    }

    public void disableSplitLayout(Context context) {
        ViewGroup.LayoutParams param = deliveriesLayout.getLayoutParams();
        param.width = ViewGroup.LayoutParams.MATCH_PARENT;
        detailLayout.setLayoutParams(param);
        detailLayout.setVisibility(View.GONE);
        orderRecyclerView.setLayoutManager(new GridLayoutManager(context,4));
    }
}