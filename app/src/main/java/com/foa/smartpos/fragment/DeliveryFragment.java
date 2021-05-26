package com.foa.smartpos.fragment;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.foa.smartpos.MainActivity;
import com.foa.smartpos.R;
import com.foa.smartpos.adapter.DeliveryGridViewAdapter;
import com.foa.smartpos.model.Order;
import com.foa.smartpos.service.OrderService;
import com.foa.smartpos.sqlite.DatabaseManager;
import com.foa.smartpos.sqlite.ds.OrderDataSource;
import com.foa.smartpos.utils.DeliveryOrderQueue;
import com.google.firebase.messaging.RemoteMessage;
import com.pusher.pushnotifications.PushNotificationReceivedListener;
import com.pusher.pushnotifications.PushNotifications;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DeliveryFragment extends Fragment{
    View root;
    RecyclerView orderRecyclerView;
    LinearLayout ordersLayout;
    RelativeLayout detailLayout;
    RadioButton waitingAcceptRB;
    RadioButton inProcessRB;
    RadioButton completedRB;
    RadioButton denyRadioButton;
    List<RadioButton> radioButtonList;
    OrderDataSource DS;
    List<Order> orderList = new ArrayList<>();
    DeliveryGridViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root =  inflater.inflate(R.layout.fragment_delivery, container, false);
        init();
        setGroupButtonListenter();
        //get data
        SQLiteDatabase db =  DatabaseManager.getInstance().openDatabase();
        DS = new OrderDataSource(db);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),4);
        orderRecyclerView.setLayoutManager(layoutManager);
        adapter = new DeliveryGridViewAdapter(getActivity(), orderList);
        adapter.setOrders(DeliveryOrderQueue.getInstance());
        orderRecyclerView.setAdapter(adapter);

        PushNotifications.setOnMessageReceivedListenerForVisibleActivity(getActivity(), remoteMessage -> {
            OrderService.getOrderById("5a087577-8679-4e6b-92a2-259fab9cc125", (success, data) -> {
                if (success){
                    DeliveryOrderQueue.getInstance().add(data);
                    adapter.addOrder(data);
                }
            });
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
    }

    private  void setGroupButtonListenter(){
        for (int i = 0; i < radioButtonList.size(); i++) {
            radioButtonList.get(i).setOnClickListener(v -> {
                for (int j = 0; j < radioButtonList.size(); j++) {
                    if(v.getId()!=radioButtonList.get(j).getId()){
                        radioButtonList.get(j).setChecked(false);
                    }
                }
            });
        }
    }

}