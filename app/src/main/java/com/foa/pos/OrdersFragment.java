package com.foa.pos;

import android.database.sqlite.SQLiteDatabase;
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
import com.foa.pos.entity.Order;
import com.foa.pos.sqlite.DatabaseHelper;
import com.foa.pos.sqlite.DatabaseManager;
import com.foa.pos.sqlite.ds.OrderDataSource;
import com.foa.pos.sqlite.ds.ProductDataSource;
import com.foa.pos.utils.Helper;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class OrdersFragment extends Fragment {
    View root;
    GridView theGridView;
    LinearLayout ordersLayout;
    RelativeLayout detailLayout;
    long itemSelectedId;

    OrderDataSource DS;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root=  inflater.inflate(R.layout.fragment_orders, container, false);
        theGridView = root.findViewById(R.id.mainGridView);
        ordersLayout =root.findViewById(R.id.bgOrders);
        detailLayout = root.findViewById(R.id.bgOrderDetail);

        Helper.initialize(getActivity().getBaseContext());
        DatabaseManager.initializeInstance(new DatabaseHelper(getActivity()));
        SQLiteDatabase db =  DatabaseManager.getInstance().openDatabase();
        DS = new OrderDataSource(db);
        final OrdersGridViewAdapter adapter = new OrdersGridViewAdapter(getActivity(), DS.getAll());
        // set elements to adapter
        theGridView.setAdapter(adapter);
        theGridView.setOnItemClickListener((parent, view, position, id) -> {
            Toast.makeText(getActivity(), "Click", Toast.LENGTH_SHORT).show();
        });
        return root;
    }

    private void enableSplitLayout() {
        final int width = Helper.getDisplayWidth()-140;
        ViewGroup.LayoutParams param = ordersLayout.getLayoutParams();
        param.width = (width / 3)*2;
        ordersLayout.setLayoutParams(param);
        ViewGroup.LayoutParams param2 = detailLayout.getLayoutParams();
        param2.width = (width / 3);
        detailLayout.setLayoutParams(param2);
        detailLayout.setVisibility(View.VISIBLE);
    }

}