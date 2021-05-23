package com.foa.pos.fragment;

import android.app.DatePickerDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.foa.pos.R;
import com.foa.pos.adapter.OrdersListViewAdapter;
import com.foa.pos.sqlite.DatabaseManager;
import com.foa.pos.sqlite.ds.OrderDataSource;
import com.foa.pos.utils.Helper;

import java.util.Calendar;

public class OrdersFragment extends Fragment {
    View root;
    Calendar date;
    ListView theListView;
    LinearLayout ordersLayout;
    RelativeLayout detailLayout;
    TextView dateFromTextView;
    TextView dateToTextView;
    Spinner orderTypeSpinner;

    OrderDataSource DS;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root=  inflater.inflate(R.layout.fragment_orders, container, false);
        theListView = root.findViewById(R.id.orderGridView);
        ordersLayout =root.findViewById(R.id.bgOrders);
        detailLayout = root.findViewById(R.id.bgOrderDetail);
        dateFromTextView = root.findViewById(R.id.dateFrom);
        dateToTextView = root.findViewById(R.id.dateTo);
        orderTypeSpinner = root.findViewById(R.id.orderTypeSpinner);

        String currentDate = Helper.dateFormat.format(Calendar.getInstance().getTime());
        dateFromTextView.setText(currentDate);
        dateToTextView.setText(currentDate);

        dateFromTextView.setOnClickListener(onPickDateFrom);
        dateToTextView.setOnClickListener(onPickDateTo);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item,
                new String[]{"Tại quán","Giao hàng"});
        orderTypeSpinner.setAdapter(spinnerAdapter);
        orderTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Helper.initialize(getActivity().getBaseContext());
        SQLiteDatabase db =  DatabaseManager.getInstance().openDatabase();
        DS = new OrderDataSource(db);
        final OrdersListViewAdapter adapter = new OrdersListViewAdapter(getActivity(), DS.getAllOrder());
        // set elements to adapter
        theListView.setAdapter(adapter);
        theListView.setOnItemClickListener((parent, view, position, id) -> {
            Toast.makeText(getActivity(), "Click", Toast.LENGTH_SHORT).show();
        });
        return root;
    }

    private final View.OnClickListener onPickDateFrom = v -> showDateTimePicker(true);
    private final View.OnClickListener onPickDateTo = v -> showDateTimePicker(false);

    public void showDateTimePicker(boolean isDateFrom) {
        Calendar date = Calendar.getInstance();
        new DatePickerDialog(getActivity(), (view, year, monthOfYear, dayOfMonth) -> {
            date.set(year, monthOfYear, dayOfMonth);
            String dateString = Helper.dateFormat.format(date.getTime());
            if (isDateFrom) dateFromTextView.setText(dateString);
            else dateToTextView.setText(dateString);
        }, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE)).show();
    }
}