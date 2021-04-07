package com.foa.pos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.foa.pos.adapter.OrdersGridViewAdapter;
import com.foa.pos.entity.Item;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class DeliveryFragment extends Fragment {
    View root;
    GridView theGridView;
    LinearLayout ordersLayout;
    RelativeLayout detailLayout;
    RadioButton waitingAcceptRB;
    RadioButton inProcessRB;
    RadioButton completedRB;
    RadioButton denyRadioButton;
    List<RadioButton> radioButtonList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root =  inflater.inflate(R.layout.fragment_delivery, container, false);
        init();
        setGroupButtonListenter();
        final ArrayList<Item> items = Item.getTestingList();
        final OrdersGridViewAdapter adapter = new OrdersGridViewAdapter(getActivity(), items);
        theGridView.setAdapter(adapter);
        theGridView.setOnItemClickListener((adapterView, view, pos, l) -> Toast.makeText(getActivity(),"Click", Toast.LENGTH_SHORT));
        return root;
    }

    private void  init(){
        theGridView = root.findViewById(R.id.deliveryGridView);
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