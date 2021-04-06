package com.foa.pos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.foa.pos.adapter.OrdersGridViewAdapter;
import com.foa.pos.entity.Item;

import java.util.ArrayList;

public class DeliveryFragment extends Fragment {
    View root;
    GridView theGridView;
    LinearLayout ordersLayout;
    RelativeLayout detailLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_delivery, container, false);
        theGridView = root.findViewById(R.id.deliveryGridView);
        //ordersLayout =root.findViewById(R.id.bgOrders);
        //detailLayout = root.findViewById(R.id.bgOrderDetail);
        // prepare elements to display
        final ArrayList<Item> items = Item.getTestingList();

        // create custom adapter that holds elements and their state (we need hold a id's of unfolded elements for reusable elements)
        final OrdersGridViewAdapter adapter = new OrdersGridViewAdapter(getActivity(), items);

        // set elements to adapter
        theGridView.setAdapter(adapter);

        // set on click event listener to list view
        theGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                Toast.makeText(getActivity(),"Click", Toast.LENGTH_SHORT);
            }
        });
        return root;
    }
}