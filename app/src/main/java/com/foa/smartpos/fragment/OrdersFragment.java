package com.foa.smartpos.fragment;

import android.app.DatePickerDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.foa.smartpos.R;
import com.foa.smartpos.adapter.OrdersListViewAdapter;
import com.foa.smartpos.api.OrderService;
import com.foa.smartpos.model.enums.OrderStatus;
import com.foa.smartpos.model.enums.OrderType;
import com.foa.smartpos.sqlite.DatabaseManager;
import com.foa.smartpos.sqlite.ds.OrderDataSource;
import com.foa.smartpos.utils.Helper;

import java.util.Calendar;
import java.util.Date;

public class OrdersFragment extends Fragment {
    View root;
    Calendar dateStart;
    Calendar dateEnd;
    ListView theListView;
    LinearLayout ordersLayout;
    RelativeLayout detailLayout;
    TextView dateFromTextView;
    TextView dateToTextView;
    Spinner orderTypeSpinner;
    Button btnSearch;
    LinearLayout progressLoading;

    String startDate;
    String endDate;

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
        btnSearch = root.findViewById(R.id.btnSearch);
        progressLoading = root.findViewById(R.id.progressLoading);

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
        String dateNow = Helper.dateTimeformat.format(new Date());
        final OrdersListViewAdapter adapter = new OrdersListViewAdapter(getActivity(), DS.getAllOrder(dateNow,dateNow));
        // set elements to adapter
        theListView.setAdapter(adapter);
        theListView.setOnItemClickListener((parent, view, position, id) -> {
            Toast.makeText(getActivity(), "Click", Toast.LENGTH_SHORT).show();
        });

        adapter.setData(DS.getAllOrder(startDate,endDate));
        btnSearch.setOnClickListener(view -> {
            progressLoading.setVisibility(View.VISIBLE);
            if (orderTypeSpinner.getSelectedItem().equals("Tại quán")){
                adapter.setData(DS.getAllOrder(startDate,endDate));
                progressLoading.setVisibility(View.GONE);

            }else{
                OrderService.getAllOrder(OrderType.SALE.toString(), 1, (success, data) -> {
                    adapter.setData(data);
                    progressLoading.setVisibility(View.GONE);
                });
            }

        });

        return root;
    }

    private final View.OnClickListener onPickDateFrom = v -> showDateStartTimePicker();
    private final View.OnClickListener onPickDateTo = v -> showDateEndTimePicker();

    public void showDateStartTimePicker() {
        Calendar dateStart = Calendar.getInstance();
        new DatePickerDialog(getActivity(), (view, year, monthOfYear, dayOfMonth) -> {
            dateStart.set(year, monthOfYear, dayOfMonth);
            String dateString = Helper.dateFormat.format(dateStart.getTime());
            String dateSQLiteString = Helper.dateSQLiteFormat.format(dateStart.getTime());
            dateFromTextView.setText(dateString);
            startDate = dateSQLiteString;

        }, dateStart.get(Calendar.YEAR), dateStart.get(Calendar.MONTH), dateStart.get(Calendar.DATE)).show();
    }

    public void showDateEndTimePicker() {
        Calendar dateEnd = Calendar.getInstance();
        new DatePickerDialog(getActivity(), (view, year, monthOfYear, dayOfMonth) -> {
            dateEnd.set(year, monthOfYear, dayOfMonth);
            String dateString = Helper.dateFormat.format(dateEnd.getTime());
            dateEnd.set(year, monthOfYear+1, dayOfMonth);
            String dateSQLiteString = Helper.dateSQLiteFormat.format(dateEnd.getTime());
            dateFromTextView.setText(dateString);
            endDate = dateSQLiteString;

        }, dateEnd.get(Calendar.YEAR), dateEnd.get(Calendar.MONTH), dateEnd.get(Calendar.DATE)).show();
    }


}