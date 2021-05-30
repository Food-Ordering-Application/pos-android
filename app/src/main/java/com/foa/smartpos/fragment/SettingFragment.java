package com.foa.smartpos.fragment;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.foa.smartpos.R;
import com.foa.smartpos.adapter.SettingMenuToppingAdapter;
import com.foa.smartpos.model.MenuItem;
import com.foa.smartpos.model.ToppingItem;
import com.foa.smartpos.sqlite.DatabaseManager;
import com.foa.smartpos.sqlite.ds.MenuItemDataSource;
import com.foa.smartpos.sqlite.ds.ToppingItemDataSource;

import java.util.List;

public class SettingFragment extends Fragment {
    private View root;
    private RecyclerView itemRecycler;
    private RadioGroup settingOptionRadioGroup;
    private List<MenuItem> menuItems;
    private List<ToppingItem> toppingItems;
    private  SettingMenuToppingAdapter adapter;
    private ToppingItemDataSource toppingItemDS;
    private MenuItemDataSource menuItemDS;
    private EditText searchEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root =  inflater.inflate(R.layout.fragment_setting, container, false);
        init();
        return root;
    }

    private void init(){
        itemRecycler = root.findViewById(R.id.itemRecyclerView);
        settingOptionRadioGroup = root.findViewById(R.id.settingOptionRadioGroup);
        searchEditText = root.findViewById(R.id.searchEditText);
        searchEditText.addTextChangedListener(keywordOnchange);

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        adapter = new SettingMenuToppingAdapter(getActivity());
        itemRecycler.setAdapter(adapter);
        itemRecycler.setLayoutManager(layoutManager);

        menuItemDS = new MenuItemDataSource(db);
        menuItems = menuItemDS.getAll("","");

        adapter.setMenuItems(menuItems);
        settingOptionRadioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            RadioButton item = root.findViewById(i);
            if (item.getText().toString().equals("MÓN ĂN")){
                adapter.setMenuItems(menuItems);
            }else{
                adapter.setToppingItems(toppingItems);
            }
        });

        toppingItemDS = new ToppingItemDataSource(db);
        toppingItems = toppingItemDS.getAll("");
    }

    private final TextWatcher keywordOnchange = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
            RadioButton item = root.findViewById(settingOptionRadioGroup.getCheckedRadioButtonId());
            String settingName = item.getText().toString();
            if (settingName.equals("MÓN ĂN")){
                adapter.setMenuItems(menuItemDS.getAll(searchEditText.getText().toString()));
            }else {
                adapter.setToppingItems(toppingItemDS.getAll(searchEditText.getText().toString()));
            }

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // TODO Auto-generated method stub
        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
        }
    };
}