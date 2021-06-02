package com.foa.smartpos.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.foa.smartpos.R;
import com.foa.smartpos.SplashActivity;
import com.foa.smartpos.adapter.CartListAdapter;
import com.foa.smartpos.adapter.ProductGridAdapter;
import com.foa.smartpos.adapter.PromotionListAdapter;
import com.foa.smartpos.model.MenuGroup;
import com.foa.smartpos.model.Order;
import com.foa.smartpos.model.OrderItem;
import com.foa.smartpos.model.MenuItem;
import com.foa.smartpos.model.OrderItemTopping;
import com.foa.smartpos.model.Promotion;
import com.foa.smartpos.model.enums.OrderStatus;
import com.foa.smartpos.api.OrderService;
import com.foa.smartpos.sqlite.DatabaseManager;
import com.foa.smartpos.sqlite.ds.OrderDataSource;
import com.foa.smartpos.sqlite.ds.MenuGroupDataSource;
import com.foa.smartpos.sqlite.ds.MenuItemDataSource;
import com.foa.smartpos.utils.Constants;
import com.foa.smartpos.utils.Helper;
import com.foa.smartpos.utils.OrderHelper;
import com.foa.smartpos.session.OrderSession;
import com.foa.smartpos.dialog.PaymentDialog;
import com.foa.smartpos.dialog.PickToppingDialog;
import com.foa.smartpos.utils.RecyclerItemTouchHelper;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderFragment extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    private View root;
    private RelativeLayout menuWrapper;
    private RelativeLayout cartWrapper;
    private GridView menuGrid;
    private RecyclerView cartRecyclerview;
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
    private Button btnClearOrder;
    private ImageButton btnManualSync;
    private MenuItemDataSource ProductDS;
    private OrderDataSource OrderDS;
    private OrderService orderService;
    private List<OrderItem> orderItems = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        initRecyclerView();

        //Set promotion recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        promotionsRecyclerView.setLayoutManager(layoutManager);
        promotionAdapter = new PromotionListAdapter(requireActivity(), Promotion.getPromotionListSample());
        promotionsRecyclerView.setAdapter(promotionAdapter);

        //Init all click listener
        initListener();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ProductDS = new MenuItemDataSource(db);
        OrderDS = new OrderDataSource(db);

        //Set menu
        menuAdapter = new ProductGridAdapter(requireActivity());
        menuGrid.setAdapter(menuAdapter);
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

        btnClearOrder.setOnClickListener(view -> {
            clearOrder();
        });

        return root;
    }

    private void initRecyclerView(){
        cartRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        cartRecyclerview.setItemAnimator(new DefaultItemAnimator());
        cartRecyclerview.addItemDecoration(new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL));
        cartAdapter = new CartListAdapter(requireActivity());
        cartRecyclerview.setAdapter(cartAdapter);
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(cartRecyclerview);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void initFindView() {
        menuWrapper = root.findViewById(R.id.bgMenu);
        cartWrapper = root.findViewById(R.id.bgCart);
        menuGrid = root.findViewById(R.id.gridView1);
        txtEmpty = root.findViewById(R.id.textView9);
        txtTotal = root.findViewById(R.id.tvQty);
        txtGrandTotal = root.findViewById(R.id.tvTotalPay);
        txtKeyword = root.findViewById(R.id.editText1);
        btnOrder = root.findViewById(R.id.btnOrder);
        promotionsRecyclerView = root.findViewById(R.id.promotionRecyclerView);
        cartRecyclerview = root.findViewById(R.id.cartListView);
        radioGroup = root.findViewById(R.id.categoryGroup);
        btnClearOrder = root.findViewById(R.id.btnClearOrder);
        btnManualSync = root.findViewById(R.id.btnManualSync);
    }

    private void initListener() {
        menuGrid.setOnItemClickListener(menuItemOnClick);
        btnOrder.setOnClickListener(orderOnlick);
        cartAdapter.setCartListener(cartOnItemClick);
        promotionAdapter.setItemClickListener(promotionOnItemClick);
        //cartList.setOnItemClickListener(editOrderOnitemClick);
        txtKeyword.addTextChangedListener(keywordOnchange);
        btnManualSync.setOnClickListener(syncClick);
    }

    private void clearOrder(){
        if (cartAdapter.getCount()>0){
            cartAdapter.removeAll();
            menuAdapter.reset();
            btnClearOrder.setVisibility(View.GONE);
            txtEmpty.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void addRadioButtons(RadioGroup radioGroup, List<MenuGroup> categoryList) {
        radioGroup.setOrientation(LinearLayout.HORIZONTAL);
        //
        for (int i = 0; i < categoryList.size(); i++) {
            RadioButton rdbtn = new RadioButton(getActivity());
            rdbtn.setId(View.generateViewId());
            rdbtn.setText(categoryList.get(i).getName());
            rdbtn.setBackgroundResource(R.drawable.radio_button_selector);
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

    private void updateStatistic(Order order) {
        if (order!=null){
            txtTotal.setText(Helper.formatMoney(order.getSubTotal()));
            txtGrandTotal.setText(Helper.formatMoney(order.getGrandTotal()));
        }else{
            txtTotal.setText(Helper.formatMoney(0));
            txtGrandTotal.setText(Helper.formatMoney(0));
        }

    }

    private final View.OnClickListener syncClick = view -> {
        startActivity(new Intent(getActivity(), SplashActivity.class));
    };

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
        public void onChange() {
            Order order = OrderSession.getInstance();
            updateStatistic(order);
            if (cartAdapter.getCount() == 0){
                btnClearOrder.setVisibility(View.GONE);
                txtEmpty.setVisibility(View.VISIBLE);
            }else{
                btnClearOrder.setVisibility(View.VISIBLE);
                txtEmpty.setVisibility(View.GONE);
            }

        }
    };

    private final AdapterView.OnItemClickListener menuItemOnClick = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO Auto-generated method stub

            MenuItem menuItem = (MenuItem) menuAdapter.getItem(position);
            PickToppingDialog pickToppingDialog = new PickToppingDialog(getActivity(), menuItem);
            pickToppingDialog.show();

            pickToppingDialog.setPickToppingistener(orderItemToppingList -> {
                    updateOrder(OrderSession.getInstance(), menuItem, orderItemToppingList);
            });

        }
    };

    private void updateOrder(Order currentOrder, MenuItem product, List<OrderItemTopping> orderItemToppings) {
        if (cartAdapter.getCount() == 0 || OrderSession.getInstance()==null) {
            Date dt = new Date();
            currentOrder = new Order();
            currentOrder.setId(Helper.getOrderID());
            currentOrder.setRestaurantId(Helper.read(Constants.RESTAURANT_ID));
            currentOrder.setCashierId(Helper.read(Constants.CASHIER_ID));
            currentOrder.setStatus(OrderStatus.DRAFT);
            currentOrder.setCreatedAt(dt);
            currentOrder.setNote("");
            currentOrder.setUpdatedAt(dt);

            OrderDS.insertOrder(currentOrder);
        }

        OrderItem orderItem = Helper.createOrderItem(product,orderItemToppings, currentOrder.getId());
        orderItem = OrderHelper.updateOrderItemStatistic(orderItem);
        cartAdapter.add(orderItem);
        currentOrder.addOrderItem(orderItem);
        currentOrder = OrderHelper.updateOrderStatistic(currentOrder);
        OrderSession.setInstance(currentOrder);

        updateStatistic(currentOrder);

        //update data
        OrderDS.insertOrderItem(orderItem);
        OrderDS.updateSumaryOrderInfo(currentOrder.getId(), currentOrder.getSubTotal());

    }

    private final TextWatcher keywordOnchange = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
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

            PaymentDialog paymentDialog = new PaymentDialog(getActivity());
            paymentDialog.setPaymentListener(result -> {
                if (result){
                    cartAdapter.removeAll();
                    OrderSession.setInstance(null);
                    updateStatistic(null);
                }
            });
            paymentDialog.show(getActivity().getSupportFragmentManager(),null);
        }
    };


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof CartListAdapter.ViewHolder) {
            // get the removed item name to display it in snack bar
            String name = cartAdapter.getDataList().get(viewHolder.getAdapterPosition()).getMenuItemName();

            // backup of removed item for undo purpose
            final OrderItem deletedItem = cartAdapter.getDataList().get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            cartAdapter.removeItem(viewHolder.getAdapterPosition());

             //showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(root, name + " đã xoá khỏi đơn hàng!", Snackbar.LENGTH_LONG);
            snackbar.setAction("Hoàn tác", view -> {

                // undo is selected, restore the deleted item
                cartAdapter.restoreItem(deletedItem, deletedIndex);
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
}