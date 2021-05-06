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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
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

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.foa.pos.adapter.CartListAdapter;
import com.foa.pos.adapter.ProductGridAdapter;
import com.foa.pos.adapter.PromotionListAdapter;
import com.foa.pos.model.Order;
import com.foa.pos.model.OrderItem;
import com.foa.pos.model.MenuItem;
import com.foa.pos.model.ProductCategory;
import com.foa.pos.model.Promotion;
import com.foa.pos.network.RetrofitClient;
import com.foa.pos.network.response.LoginResponse;
import com.foa.pos.network.response.OrderResponse;
import com.foa.pos.network.response.StaffLogin;
import com.foa.pos.sqlite.DatabaseHelper;
import com.foa.pos.sqlite.DatabaseManager;
import com.foa.pos.sqlite.ds.OrderDataSource;
import com.foa.pos.sqlite.ds.ProductCategoryDataSource;
import com.foa.pos.sqlite.ds.ProductDataSource;
import com.foa.pos.utils.Constants;
import com.foa.pos.utils.Helper;
import com.foa.pos.utils.LoginSession;
import com.foa.pos.widget.PickToppingDialog;
import com.nineoldandroids.animation.Animator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderFragment extends Fragment implements Toolbar.OnMenuItemClickListener {
    private View root;

    private RelativeLayout menuWrapper;
    private RelativeLayout cartWrapper;
    private RelativeLayout checkOutContainer;

    private GridView menuGrid;
    private ListView menuList;
    private ListView cartList;
    private RecyclerView promotionsRecyclerView;
    private RadioGroup radioGroup;

    private ProductGridAdapter menuAdapter;
    private CartListAdapter cartAdapter;
    private PromotionListAdapter promotionAdapter;

    private TextView txtTotal;
    private TextView grandTotal;
    private EditText txtKeyword;
    private EditText txtPayment;
    private TextView txtChange;
    private TextView txtEmpty;

    private Button btnOrder;
    private ImageButton btnToggleList;

    private ProductDataSource ProductDS;
    private OrderDataSource OrderDS;

    private boolean isCheckout = false;
    private long total = 0;

    private Order currentOrder = null;
    private ArrayList<OrderItem> orderItemsList = new ArrayList<>();

    private StaffLogin staffLogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        staffLogin = LoginSession.getInstance();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_order, container, false);
        Helper.initialize(requireActivity().getBaseContext());

        initFindView();
        initLayout();

        //Toolbar setting
        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.right_layout_menu);
        toolbar.setOnMenuItemClickListener(this);

        //Init sqlite data source
        DatabaseManager.initializeInstance(new DatabaseHelper(getActivity()));
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ProductDS = new ProductDataSource(db);
        OrderDS = new OrderDataSource(db);

        //Set menu
        menuAdapter = new ProductGridAdapter(requireActivity());
        menuGrid.setAdapter(menuAdapter);
        menuList.setAdapter(menuAdapter);
        menuAdapter.set(ProductDS.getAll("", ""));

        //Set category radio group button
        ProductCategoryDataSource catds = new ProductCategoryDataSource(db);
        ArrayList<ProductCategory> catList = catds.getAll();
        ProductCategory ct = new ProductCategory();
        ct.setCategoryID("");
        ct.setCategoryName("All");
        catList.add(0, ct);
        radioGroup = root.findViewById(R.id.categoryGroup);
        addRadioButtons(radioGroup, catList);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton item = root.findViewById(checkedId);
            String cateName = item.getText().toString();
            menuAdapter.set(ProductDS.getAll(txtKeyword.getText().toString(), ProductDS.getIdByName(cateName)));
        });

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

    private void initFindView() {
        menuWrapper = root.findViewById(R.id.bgMenu);
        cartWrapper = root.findViewById(R.id.bgCart);
        menuGrid = root.findViewById(R.id.gridView1);
        menuList = root.findViewById(R.id.listView2);
        txtEmpty = root.findViewById(R.id.textView9);
        txtTotal = root.findViewById(R.id.tvTotal);
        grandTotal = root.findViewById(R.id.tvTotalPay);
        txtKeyword = root.findViewById(R.id.editText1);
        txtChange = root.findViewById(R.id.textView8);
        btnOrder = root.findViewById(R.id.btnOrder);
        btnToggleList = root.findViewById(R.id.imageView2);
        checkOutContainer = root.findViewById(R.id.checkOutContainer);
        promotionsRecyclerView = root.findViewById(R.id.promotionRecyclerView);
        cartList = root.findViewById(R.id.listView1);
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

    private void addRadioButtons(RadioGroup radioGroup, List<ProductCategory> categoryList) {
        radioGroup.setOrientation(LinearLayout.HORIZONTAL);
        //
        for (int i = 0; i < categoryList.size(); i++) {
            RadioButton rdbtn = new RadioButton(getActivity());
            rdbtn.setId(View.generateViewId());
            rdbtn.setText(categoryList.get(i).getCategoryName());
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
            if (cartAdapter.getCount() == 0){
                txtEmpty.setVisibility(View.VISIBLE);
                currentOrder = null;
            }else
                txtEmpty.setVisibility(View.GONE);
        }

        @Override
        public void onChange(List<OrderItem> list) {
            // TODO Auto-generated method stub
            long mtotal = 0;
            for (int i = 0; i < list.size(); i++) {
                long sub = (list.get(i).getPrice() * list.get(i).getQuantity());
                long discount = sub * (list.get(i).getDiscount() / 100);
                long subtotal = sub - discount;
                mtotal += subtotal;
            }

            total = mtotal;
            txtTotal.setText(Helper.formatMoney(mtotal));
            grandTotal.setText(txtTotal.getText().toString());
            OrderDS.updateSumaryOrderInfo(currentOrder.getId(),mtotal,mtotal);

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

            if (!isCheckout) {
                MenuItem product = (MenuItem) menuAdapter.getItem(position);
                PickToppingDialog pickToppingDialog = new PickToppingDialog(getActivity(), product);
                pickToppingDialog.setPickToppingistener(result -> {
                    menuAdapter.setSelection(product.getId());
                    if (menuAdapter.isSelected(product.getId())) {
                        if (staffLogin!=null){
                            createOrderAndFirstOrderItemOnline(Helper.createOrderItem(product,0));
                        }else{
                            updateOrderOffline(currentOrder,product);
                        }

                    }
                });
                if (!menuAdapter.isSelected(product.getId())) {
                    pickToppingDialog.show();
                } else {
                    menuAdapter.setSelection(product.getId());
                    cartAdapter.removeByID(product.getId());
                    if(cartAdapter.getCount()==0){
                        currentOrder =null;
                    }
                }

            }
        }
    };

    private void updateOrderOffline(Order currentOrder, MenuItem product){
        if(cartAdapter.getCount()==0){
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
        OrderItem orderItem = Helper.createOrderItem(product,cartAdapter.getCount());

        orderItemsList.add(orderItem);
        currentOrder.addOrderItemPrice(orderItem.getPrice()*orderItem.getQuantity());
        cartAdapter.add(orderItem);

        //update data
        OrderDS.insertOrderItem(orderItem);
        OrderDS.updateSumaryOrderInfo(currentOrder.getId(),currentOrder.getSubTotal(),currentOrder.getSubTotal());
    }

    private void createOrderAndFirstOrderItemOnline(OrderItem orderItem){
        Call<Object> responseCall = RetrofitClient.getInstance().getAppService()
                .createOrderAndAddFirstOrderItem(staffLogin.getBearerAccessToken(),orderItem,Helper.read(Constants.RESTAURANT_ID),"");
        responseCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.code() ==Constants.STATUS_CODE_SUCCESS){
                    Object res = response.body();
//                        if (res.getStatus() == Constants.STATUS_CODE_SUCCESS) {
//                            currentOrder =  res.getData().getOrder();
//                            Toast.makeText(getActivity(), currentOrder.getId(), Toast.LENGTH_SHORT).show();
//                        }else{
//                            Log.e("[Order fragment]","Create order fail");
//                        }
                }else {
                    Log.e("[Order fragment]","Create order fail");
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e("Login Error", t.getMessage());
            }
        });
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
            isCheckout = true;
            //showCheckout();
            Intent intent = new Intent(getActivity(), PaymentActivity.class);
            intent.putExtra(Constants.PUT_ORDER_ID, currentOrder.getId());
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