package com.foa.pos;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.foa.pos.adapter.OrdersGridViewAdapter;
import com.foa.pos.sqlite.DatabaseHelper;
import com.foa.pos.sqlite.DatabaseManager;
import com.foa.pos.sqlite.ds.OrderDataSource;
import com.foa.pos.utils.Helper;

public class OrdersFragment extends Fragment {
    View root;
    ListView theListView;
    LinearLayout ordersLayout;
    RelativeLayout detailLayout;
    long itemSelectedId;

    OrderDataSource DS;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root=  inflater.inflate(R.layout.fragment_orders, container, false);
        theListView = root.findViewById(R.id.orderGridView);
        ordersLayout =root.findViewById(R.id.bgOrders);
        detailLayout = root.findViewById(R.id.bgOrderDetail);

        Helper.initialize(getActivity().getBaseContext());
        SQLiteDatabase db =  DatabaseManager.getInstance().openDatabase();
        DS = new OrderDataSource(db);
        final OrdersGridViewAdapter adapter = new OrdersGridViewAdapter(getActivity(), DS.getAllOrder());
        // set elements to adapter
        theListView.setAdapter(adapter);
        theListView.setOnItemClickListener((parent, view, position, id) -> {
            Toast.makeText(getActivity(), "Click", Toast.LENGTH_SHORT).show();
        });
        return root;
    }



}