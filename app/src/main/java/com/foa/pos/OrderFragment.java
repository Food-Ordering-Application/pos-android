package com.foa.pos;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.foa.pos.adapter.CartListAdapter;
import com.foa.pos.adapter.ProductGridAdapter;
import com.foa.pos.adapter.PromotionListAdapter;
import com.foa.pos.model.IDataResultCallback;
import com.foa.pos.model.MenuGroup;
import com.foa.pos.model.Order;
import com.foa.pos.model.OrderItem;
import com.foa.pos.model.MenuItem;
import com.foa.pos.model.OrderItemTopping;
import com.foa.pos.model.Promotion;
import com.foa.pos.network.response.LoginData;
import com.foa.pos.service.OrderService;
import com.foa.pos.sqlite.DatabaseManager;
import com.foa.pos.sqlite.ds.OrderDataSource;
import com.foa.pos.sqlite.ds.MenuGroupDataSource;
import com.foa.pos.sqlite.ds.MenuItemDataSource;
import com.foa.pos.sqlite.ds.OrderItemToppingDataSource;
import com.foa.pos.utils.Constants;
import com.foa.pos.utils.Helper;
import com.foa.pos.utils.LoginSession;
import com.foa.pos.utils.OrderSession;
import com.foa.pos.widget.PickToppingDialog;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OrderFragment extends Fragment implements Toolbar.OnMenuItemClickListener {
    private View root;

    private RelativeLayout menuWrapper;
    private RelativeLayout cartWrapper;

    private GridView menuGrid;
    private ListView menuList;
    private ListView cartList;
    private RecyclerView promotionsRecyclerView;
    private RadioGroup radioGroup;

    private ProductGridAdapter menuAdapter;
    private CartListAdapter cartAdapter;
    private PromotionListAdapter promotionAdapter;

    private TextView txtTotal;
    private TextView txtGrandTotal;
    private EditText txtKeyword;
    private TextView txtEmpty;
    private Button btnOrder;
    private ImageButton btnToggleList;
    private MenuItemDataSource ProductDS;
    private OrderDataSource OrderDS;
    private OrderItemToppingDataSource OrderToppingsDS;
    private LoginData loginData;
    private OrderService orderService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        loginData = LoginSession.getInstance();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_order, container, false);
        Helper.initialize(requireActivity().getBaseContext());

        initFindView();
        initLayout();

        orderService = new OrderService();

        //Toolbar setting
        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.right_layout_menu);
        toolbar.setOnMenuItemClickListener(this);





        //Set cart
        cartAdapter = new CartListAdapter(requireActivity());

        cartList.setAdapter(cartAdapter);

        //Set promotion recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        promotionsRecyclerView.setLayoutManager(layoutManager);
        promotionAdapter = new PromotionListAdapter(requireActivity(), Promotion.getPromotionListSample());
        promotionsRecyclerView.setAdapter(promotionAdapter);

        //Init all click listener
        initListener();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            ProductDS = new MenuItemDataSource(db);
            OrderDS = new OrderDataSource(db);
            OrderToppingsDS = new OrderItemToppingDataSource(db);

            //Set menu
            menuAdapter = new ProductGridAdapter(requireActivity());
            menuGrid.setAdapter(menuAdapter);
            menuList.setAdapter(menuAdapter);
            menuAdapter.set(ProductDS.getAll("", ""));

            //Set category radio group button
            MenuGroupDataSource catds = new MenuGroupDataSource(db);
            ArrayList<MenuGroup> catList = catds.getAll();
            MenuGroup ct = new MenuGroup();
            ct.setId("");
            ct.setName("All");
            catList.add(0, ct);
            addRadioButtons(radioGroup, catList);
            radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                RadioButton item = root.findViewById(checkedId);
                String cateName = item.getText().toString();
                menuAdapter.set(ProductDS.getAll(txtKeyword.getText().toString(), ProductDS.getIdByName(cateName)));
            });

        });
        executorService.shutdown();
    }

    private void initFindView() {
        menuWrapper = root.findViewById(R.id.bgMenu);
        cartWrapper = root.findViewById(R.id.bgCart);
        menuGrid = root.findViewById(R.id.gridView1);
        menuList = root.findViewById(R.id.listView2);
        txtEmpty = root.findViewById(R.id.textView9);
        txtTotal = root.findViewById(R.id.tvTotal);
        txtGrandTotal = root.findViewById(R.id.tvTotalPay);
        txtKeyword = root.findViewById(R.id.editText1);
        btnOrder = root.findViewById(R.id.btnOrder);
        btnToggleList = root.findViewById(R.id.imageView2);
        promotionsRecyclerView = root.findViewById(R.id.promotionRecyclerView);
        cartList = root.findViewById(R.id.listView1);
        radioGroup = root.findViewById(R.id.categoryGroup);
    }

    private void initListener() {
        menuGrid.setOnItemClickListener(gridOnlick);
        menuList.setOnItemClickListener(gridOnlick);
        btnOrder.setOnClickListener(orderOnlick);
        btnToggleList.setOnClickListener(toogleOnclick);
        cartAdapter.setCartListener(cartOnItemClick);
        promotionAdapter.setItemClickListener(promotionOnItemClick);
        //cartList.setOnItemClickListener(editOrderOnitemClick);

        // Handle input change
        txtKeyword.addTextChangedListener(keywordOnchange);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemClick(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clear_cart:
                cartAdapter.removeAll();
                menuAdapter.reset();
                return true;
            case R.id.exit:
                Toast.makeText(getActivity(), "Exit clicked", Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
    }

    private void addRadioButtons(RadioGroup radioGroup, List<MenuGroup> categoryList) {
        radioGroup.setOrientation(LinearLayout.HORIZONTAL);
        //
        for (int i = 0; i < categoryList.size(); i++) {
            RadioButton rdbtn = new RadioButton(getActivity());
            rdbtn.setId(View.generateViewId());
            rdbtn.setText(categoryList.get(i).getName());
            rdbtn.setBackgroundResource(R.drawable.radio_button_selector);
            rdbtn.setTextColor(R.drawable.radio_button_selector_text);
            rdbtn.setPadding(50, 30, 50, 30);
            rdbtn.setGravity(Gravity.CENTER);
            rdbtn.setButtonDrawable(android.R.color.transparent);
            rdbtn.setElevation(4);
            rdbtn.setMinWidth(150);
            if (i == 0) rdbtn.setChecked(true);


            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.leftMargin = 20;
            layoutParams.rightMargin = 20;
            radioGroup.addView(rdbtn, layoutParams);
        }
    }

    private void initLayout() {
        final int width = Helper.getDisplayWidth();
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(cartWrapper.getLayoutParams());
        param.width = (width / 3);
        cartWrapper.setLayoutParams(param);
        LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(menuWrapper.getLayoutParams());
        param2.width = (width / 3) * 2;
        menuWrapper.setLayoutParams(param2);
        root.setVisibility(View.VISIBLE);
    }

    // REGION ON CLICK LISTENER
//
//    private final AdapterView.OnItemClickListener editOrderOnitemClick = new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            EditOrderItemDialog dialog = new EditOrderItemDialog(requireActivity(),
//                    (MenuItem) cartList.getSelectedItem());
//            dialog.setOutOfProductListener(result -> {
//                if (result){
//
//                }else{
//
//                }
//            });
//        }
//    };

    private void updateStatistic(Order order) {
        txtTotal.setText(Helper.formatMoney(order.getGrandTotal()));
        txtGrandTotal.setText(Helper.formatMoney(order.getGrandTotal()));
    }

    private final PromotionListAdapter.OnItemClickListener promotionOnItemClick = new PromotionListAdapter.OnItemClickListener() {
        @Override
        public void onPick(Promotion item) {
            Toast.makeText(getActivity(), "Choose promotion", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRemove(Promotion item) {
            Toast.makeText(getActivity(), "Remove promotion", Toast.LENGTH_SHORT).show();
        }
    };

    private final CartListAdapter.CartListener cartOnItemClick = new CartListAdapter.CartListener() {
        @Override
        public void onRemove(String result) {
            // TODO Auto-generated method stub
            menuAdapter.setSelection(result);
            if (cartAdapter.getCount() == 0) {
                txtEmpty.setVisibility(View.VISIBLE);
            } else{
                txtEmpty.setVisibility(View.GONE);
            }

        }

        @Override
        public void onChange(Order currentOrder) {
            if (loginData == null) {
                OrderDS.updateSumaryOrderInfo(currentOrder.getId(), currentOrder.getGrandTotal(), currentOrder.getGrandTotal());
            }

            if (cartAdapter.getCount() == 0)
                txtEmpty.setVisibility(View.VISIBLE);
            else
                txtEmpty.setVisibility(View.GONE);
        }
    };

    private final AdapterView.OnItemClickListener gridOnlick = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO Auto-generated method stub

            MenuItem menuItem = (MenuItem) menuAdapter.getItem(position);

            PickToppingDialog pickToppingDialog = new PickToppingDialog(getActivity(), menuItem);

            if (!menuAdapter.isSelected(menuItem.getId())) {
                    pickToppingDialog.show();
            } else {
                menuAdapter.setSelection(menuItem.getId());
                cartAdapter.removeByID(menuItem.getId());
                if (cartAdapter.getCount() == 0) {
                    OrderSession.clearInstance();
                }
            }

            pickToppingDialog.setPickToppingistener(orderItemToppingList -> {
                menuAdapter.setSelection(menuItem.getId());
                if (menuAdapter.isSelected(menuItem.getId())) {
                    OrderItem orderItem = Helper.createSendOrderItem(menuItem,orderItemToppingList);
                    cartAdapter.add(orderItem);
                    if (loginData != null) {
                        if (cartAdapter.getCount()==1){
                            orderService.createOrderAndFirstOrderItemOnline(orderItem, new IDataResultCallback<Order>() {
                                @Override
                                public void onSuccess(boolean success, Order order) {
                                    if (success){
                                        OrderSession.setInstance(order);
                                        cartAdapter.set(order.getOrderItems());
                                        updateStatistic(order);
                                    }else{
                                        Toast.makeText(getActivity(), "That bai", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override
                                public void onError() {

                                }
                            });
                        }else{
                            orderService.addOrderItemOnline(orderItem, new IDataResultCallback<Order>() {
                                @Override
                                public void onSuccess(boolean success, Order order) {
                                    if (success){
                                        OrderSession.setInstance(order);
                                        cartAdapter.set(order.getOrderItems());
                                        updateStatistic(order);
                                    }else{
                                        Toast.makeText(getActivity(), "That bai", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override
                                public void onError() {

                                }
                            });

                        }

                    } else {
                        updateOrderOffline(OrderSession.getInstance(), menuItem, orderItemToppingList);
                    }

                }
            });

        }
    };

    private void updateOrderOffline(Order currentOrder, MenuItem product, List<OrderItemTopping> orderItemToppings) {
        int size = cartAdapter.getCount();
        if (cartAdapter.getCount() == 1) {
            Date dt = new Date();
            currentOrder = new Order();
            currentOrder.setId(Helper.getOrderID());
            currentOrder.setRestaurantId(Helper.read(Constants.KEY_SETTING_BRANCH_ID, ""));
            currentOrder.setCashierId(com.foa.pos.MainActivity.SesID);
            currentOrder.setCreatedAt(dt);
            currentOrder.setNote("");
            currentOrder.setUpdatedAt(dt);
            //udpate data
            OrderDS.insertOrder(currentOrder);
        }
        OrderItem orderItem = Helper.createOrderItem(product, cartAdapter.getCount(), currentOrder.getId());
        orderItem.setOrderItemToppings(setOrderItemToppingIds(orderItemToppings));
        currentOrder.addOrderItemPrice(orderItem.getPrice() * orderItem.getQuantity());
        OrderSession.setInstance(currentOrder);
        updateStatistic(currentOrder);
        //update data
        OrderDS.insertOrderItem(orderItem);
        OrderDS.updateSumaryOrderInfo(currentOrder.getId(), currentOrder.getSubTotal(), currentOrder.getSubTotal());

    }

    private List<OrderItemTopping>  setOrderItemToppingIds(List<OrderItemTopping> orderItemToppings){
        for (int i = 0; i < orderItemToppings.size() ; i++) {
            orderItemToppings.get(i).setId(Helper.getOrderToppingId(i));
        }
        return orderItemToppings;
    }





    private final TextWatcher keywordOnchange = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
            //ProductCategory cat =   (ProductCategory) spinnerCategory.getSelectedItem();
            RadioButton item = root.findViewById(radioGroup.getCheckedRadioButtonId());
            String cateName = item.getText().toString();
            menuAdapter.set(ProductDS.getAll(txtKeyword.getText().toString(), ProductDS.getIdByName(cateName)));
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


    private final View.OnClickListener orderOnlick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (cartAdapter.getCount() == 0) {
                Toast.makeText(getActivity(), "Chọn ít nhất 1 chọn sản phẩm", Toast.LENGTH_SHORT).show();
                return;
            }
            //showCheckout();
            Intent intent = new Intent(getActivity(), PaymentActivity.class);
            startActivity(intent);
        }
    };

    private final View.OnClickListener toogleOnclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (Helper.read(Constants.KEY_SETTING_MENU, Constants.VAL_DEFAULT_MENU).equals("GRID")) {
                Helper.write(Constants.KEY_SETTING_MENU, "LIST");
                btnToggleList.setImageResource(R.drawable.ic_listview);
                menuAdapter.setMenu("LIST");
                menuGrid.setVisibility(View.GONE);
                menuList.setVisibility(View.VISIBLE);
            } else {
                Helper.write(Constants.KEY_SETTING_MENU, "GRID");
                btnToggleList.setImageResource(R.drawable.ic_gridview);
                menuAdapter.setMenu("GRID");
                menuList.setVisibility(View.GONE);
                menuGrid.setVisibility(View.VISIBLE);
            }
        }
    };

}