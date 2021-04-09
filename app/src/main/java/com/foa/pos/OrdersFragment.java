package com.foa.pos;

import android.opengl.Visibility;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
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
import com.foa.pos.utils.Helper;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class OrdersFragment extends Fragment {
    View root;
    GridView theGridView;
    LinearLayout ordersLayout;
    RelativeLayout detailLayout;
    long itemSelectedId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root=  inflater.inflate(R.layout.fragment_orders, container, false);
        theGridView = root.findViewById(R.id.mainGridView);
        ordersLayout =root.findViewById(R.id.bgOrders);
        detailLayout = root.findViewById(R.id.bgOrderDetail);

        Helper.initialize(getActivity().getBaseContext());
        // prepare elements to display
        final ArrayList<Item> items = Item.getTestingList();

        final OrdersGridViewAdapter adapter = new OrdersGridViewAdapter(getActivity(), items);

        // set elements to adapter
        theGridView.setAdapter(adapter);

        // set on click event listener to list view

        theGridView.setOnItemClickListener((parent, view, position, id) -> {
            Toast.makeText(getActivity(),"Click", Toast.LENGTH_SHORT).show();
            theGridView.setNumColumns(3);
            Log.e("view id",view+"");
            initSplitLayout(parent);
            view.setSelected(true);
        });


        return root;
    }

    private void initSplitLayout(View view) {
        final int width = Helper.getDisplayWidth()-140;
        view.setSelected(true);
        ViewGroup.LayoutParams param = ordersLayout.getLayoutParams();
        param.width = (width / 3)*2;
        ordersLayout.setLayoutParams(param);
        ViewGroup.LayoutParams param2 = detailLayout.getLayoutParams();
        param2.width = (width / 3);
        detailLayout.setLayoutParams(param2);
        detailLayout.setVisibility(View.VISIBLE);
        view.setSelected(true);
    }
}