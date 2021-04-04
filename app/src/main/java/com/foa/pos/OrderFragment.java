package com.foa.pos;

import android.bluetooth.BluetoothDevice;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.foa.pos.adapter.CartListAdapter;
import com.foa.pos.adapter.CategorySpinnerMenuAdapter;
import com.foa.pos.adapter.ProductGridAdapter;
import com.foa.pos.entity.Cart;
import com.foa.pos.entity.Order;
import com.foa.pos.entity.OrderDetails;
import com.foa.pos.entity.Product;
import com.foa.pos.entity.ProductCategory;
import com.foa.pos.sqlite.DatabaseHelper;
import com.foa.pos.sqlite.DatabaseManager;
import com.foa.pos.sqlite.ds.ProductCategoryDataSource;
import com.foa.pos.sqlite.ds.ProductDataSource;
import com.foa.pos.utils.Constants;
import com.foa.pos.utils.Helper;
import com.foa.pos.widget.CustomConfirm;
import com.nineoldandroids.animation.Animator;
import com.zj.btsdk.BluetoothService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderFragment extends Fragment implements View.OnClickListener{

    private RelativeLayout menuWrapper;
    private RelativeLayout cartWrapper;
    private GridView menuGrid;
    private  ListView menuList;
    private ProductGridAdapter menuadapter;
    private CartListAdapter cartadapter;
    private ListView cartList;

    private TextView txtTotal;
    private TextView txtDiscount;
    private TextView txtTotalPay;
    private TextView txtTotalPay2;
    private EditText txtKeyword;
    private EditText txtPayment;
    private TextView txtChange;
    private TextView txtempty;

    private Button btnCancel;
    private Button btnOrder;
    private Button btnSave;
    private Button btnCancelCheckout;
    private Button btnPay;
    private ImageButton btnToggleList;

    RadioGroup radioGroup;

    private Spinner spinnerDiscount;
    //private Spinner spinnerCategory;
    private CategorySpinnerMenuAdapter spinneradapter;

    private ScrollView scroll;
    private ImageView arrow;

    private RelativeLayout cartContainer;
    private RelativeLayout checkOutContainer;

    private boolean isCheckout = false;
    private Double total = 0.0;

    private BluetoothService mService = null;
    private BluetoothDevice con_dev = null;
    private ProductDataSource DS;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }
    View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root =  inflater.inflate(R.layout.fragment_order, container, false);
        // Inflate the layout for this fragment
        Helper.initialize(getActivity().getBaseContext());

        menuWrapper = (RelativeLayout)root.findViewById(R.id.bgMenu);
        cartWrapper = (RelativeLayout)root.findViewById(R.id.bgCart);

        menuGrid = (GridView)root.findViewById(R.id.gridView1);
        menuList = root.findViewById(R.id.listView2);


        menuadapter = new ProductGridAdapter(getActivity());

        menuGrid.setAdapter(menuadapter);
        menuList.setAdapter(menuadapter);

        initLayout();
        root.setVisibility(View.VISIBLE);

        DatabaseManager.initializeInstance(new DatabaseHelper(getActivity()));
        SQLiteDatabase db =  DatabaseManager.getInstance().openDatabase();
        DS = new ProductDataSource(db);
        menuadapter.set(DS.getAll("",""));


        //spinnerCategory = (Spinner)root.findViewById(R.id.spinner1);
        spinneradapter = new CategorySpinnerMenuAdapter(getActivity());
        //spinnerCategory.setAdapter(spinneradapter);

        ProductCategoryDataSource catds = new ProductCategoryDataSource(db);
        ArrayList<ProductCategory> catList = catds.getAll();
        ProductCategory ct = new ProductCategory();
        ct.setCategoryID("");
        ct.setCategoryName("All");
        catList.add(0, ct);
        spinneradapter.set(catList);

        //load categories for radio group

         radioGroup = root.findViewById(R.id.categoryGroup);
            addRadioButtons(radioGroup,catList);
            radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                    RadioButton item = root.findViewById(checkedId);
                    String cateName = item.getText().toString();
                    menuadapter.set(DS.getAll(txtKeyword.getText().toString(),DS.getIdByName(cateName)));
            });



        //DatabaseManager.getInstance().closeDatabase();

        cartadapter = new CartListAdapter(getActivity());
        cartList = (ListView)root.findViewById(R.id.listView1);
        cartList.setAdapter(cartadapter);

        txtTotal = (TextView)root.findViewById(R.id.tvTotal);
        txtTotalPay = (TextView)root.findViewById(R.id.tvTotalPay);
        txtTotalPay2 = (TextView)root.findViewById(R.id.textView5);
        txtKeyword = (EditText)root.findViewById(R.id.editText1);
        txtPayment = (EditText)root.findViewById(R.id.editText2);
        txtChange = (TextView)root.findViewById(R.id.textView8);

        btnCancel = (Button)root.findViewById(R.id.btnClearCart);
        btnSave = (Button)root.findViewById(R.id.btnSave);
        btnOrder = (Button)root.findViewById(R.id.btnOrder);
        btnCancelCheckout = (Button)root.findViewById(R.id.btnCancelCheckout);
        btnPay = (Button)root.findViewById(R.id.btnCheckout);
        btnToggleList = (ImageButton)root.findViewById(R.id.imageView2);

        menuGrid.setOnItemClickListener(gridOnlick);
        menuList.setOnItemClickListener(gridOnlick);
        btnCancel.setOnClickListener(cancelOnlick);
        btnSave.setOnClickListener(saveOnlick);
        btnCancelCheckout.setOnClickListener(cancelCheckOutOnlick);
        btnPay.setOnClickListener(payOnlick);
        btnOrder.setOnClickListener(orderOnlick);
        btnToggleList.setOnClickListener(toogleOnclick);

        cartContainer = (RelativeLayout)root.findViewById(R.id.cartContainer);
        checkOutContainer = (RelativeLayout)root.findViewById(R.id.checkOutContainer);

        //.setOnItemSelectedListener(spinnerCategoryOnChange);


        cartadapter.setCartListener(new CartListAdapter.CartListener() {

            @Override
            public void onRemove(String result) {
                // TODO Auto-generated method stub
                menuadapter.setSelection(result);
                if(cartadapter.getCount() == 0)
                    txtempty.setVisibility(View.VISIBLE);
                else
                    txtempty.setVisibility(View.GONE);
            }

            @Override
            public void onChange(List<Cart> list) {
                // TODO Auto-generated method stub
                double mtotal = 0;
                for (int i = 0; i < list.size(); i++) {
                    double sub = (list.get(i).getPrice()*list.get(i).getQty());
                    double discount =  sub * (list.get(i).getDiscount()/100);
                    double subtotal = sub - discount;
                    mtotal += subtotal;
                }

                total = mtotal;
                txtTotal.setText(Helper.decimalformat.format(mtotal)+ " "+Helper.read(Constants.KEY_SETTING_CURRENCY_SYMBOL, Constants.VAL_DEFAULT_CURRENCY_SYMBOL));

                txtTotalPay.setText(txtTotal.getText().toString());
                txtTotalPay2.setText(txtTotal.getText().toString());

                if(cartadapter.getCount() == 0)
                    txtempty.setVisibility(View.VISIBLE);
                else
                    txtempty.setVisibility(View.GONE);
            }
        });


        txtKeyword.addTextChangedListener(keywordOnchange);
        txtPayment.addTextChangedListener(paymentOnchange);


        scroll = (ScrollView)root.findViewById(R.id.scrollView1);
        arrow = (ImageView)root.findViewById(R.id.imageView4);


        TextView t1 =(TextView)root.findViewById(R.id.tvCartProductName);
        TextView t2 =(TextView)root.findViewById(R.id.tvProductPrice);
        TextView t4 =(TextView)root.findViewById(R.id.textView4);
        TextView t6 =(TextView)root.findViewById(R.id.textView6);
        TextView t7 =(TextView)root.findViewById(R.id.textView7);
        txtempty =(TextView)root.findViewById(R.id.textView9);

        //t1.setTypeface(Helper.OpenSansBold);
        t2.setTypeface(Helper.OpenSansBold);
        t4.setTypeface(Helper.OpenSansBold);
        t6.setTypeface(Helper.OpenSansBold);
        t7.setTypeface(Helper.OpenSansBold);
        txtempty.setTypeface(Helper.openSansLightItalic);

        txtKeyword.setTypeface(Helper.openSansLightItalic);
        txtTotal.setTypeface(Helper.OpenSansBold);
        txtTotalPay2.setTypeface(Helper.OpenSansBold);
        txtChange.setTypeface(Helper.OpenSansBold);

        //btnCancel.setTypeface(Helper.OpenSansSemibold);
        btnOrder.setTypeface(Helper.OpenSansSemibold);
        btnSave.setTypeface(Helper.OpenSansSemibold);
        btnCancelCheckout.setTypeface(Helper.OpenSansSemibold);
        btnPay.setTypeface(Helper.OpenSansSemibold);
        txtPayment.setTypeface(Helper.openSansLight);
        //----

        return root;
    }

    public void addRadioButtons(RadioGroup radioGroup, List<ProductCategory> categoryList) {
        radioGroup.setOrientation(LinearLayout.HORIZONTAL);
        //
        for (int i = 0; i < categoryList.size(); i++) {
            RadioButton rdbtn = new RadioButton(getActivity());
            rdbtn.setId(View.generateViewId());
            rdbtn.setText(categoryList.get(i).getCategoryName());
            rdbtn.setOnClickListener(this);
            rdbtn.setBackgroundResource(R.drawable.radio_button_selector);
            rdbtn.setTextColor(R.drawable.radio_button_selector_text);
            rdbtn.setPadding(50,30,50,30);
            rdbtn.setGravity(Gravity.CENTER);
            rdbtn.setButtonDrawable(android.R.color.transparent);
            rdbtn.setElevation(4);
            rdbtn.setMinWidth(150);
            if (i==0) rdbtn.setChecked(true);


            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams. WRAP_CONTENT ,
                    LinearLayout.LayoutParams. WRAP_CONTENT ) ;
            layoutParams.leftMargin = 20;
            layoutParams.rightMargin = 20;
            radioGroup.addView(rdbtn,layoutParams);
        }
    }

    private void initLayout() {

        final int width = Helper.getDisplayWidth()-140;
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(cartWrapper.getLayoutParams());
        param.width = (width / 3);
        cartWrapper.setLayoutParams(param);
        LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(menuWrapper.getLayoutParams());
        param2.width = (width / 3)*2;
        menuWrapper.setLayoutParams(param2);


    }


    private AdapterView.OnItemClickListener gridOnlick = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO Auto-generated method stub

            if(!isCheckout)
            {

                Product product = (Product) menuadapter.getItem(position);
                menuadapter.setSelection(product.getProductID());

                if(menuadapter.isSelected(product.getProductID()))
                {
                    Cart cart = new Cart();
                    cart.setProductID(product.getProductID());
                    cart.setProductName(product.getProductName());
                    cart.setPrice(product.getPrice());
                    cart.setDiscount(product.getDiscount());
                    cart.setQty(1);

                    double discount = cart.getPrice() * (product.getDiscount()/100);
                    double subtotal = cart.getPrice() - discount;
                    cart.setSubtotal(subtotal);
                    cartadapter.add(cart);
                }
                else
                {
                    cartadapter.removeByID(product.getProductID());
                }
            }
        }
    };

    private final View.OnClickListener cancelOnlick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub

            cartadapter.removeAll();
            menuadapter.reset();
//            if(cartadapter.getCount() != 0)
//            {
//                cartadapter.removeAll();
//                menuadapter.reset();
//            }
//            else
//            {
//                getActivity().finish();
//                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//            }
        }
    };

//    private final AdapterView.OnItemSelectedListener spinnerCategoryOnChange = new AdapterView.OnItemSelectedListener() {
//
//        @Override
//        public void onItemSelected(AdapterView<?> parent, View view,
//                                   int position, long id) {
//            // TODO Auto-generated method stub
//            ProductCategory c = (ProductCategory) spinneradapter.getItem(position);
//            //menuadapter.setFilter(txtKeyword.getText().toString(), c.getCategoryID());
//            menuadapter.set(DS.getAll(txtKeyword.getText().toString(),c.getCategoryID()));
//        }
//
//        @Override
//        public void onNothingSelected(AdapterView<?> parent) {
//            // TODO Auto-generated method stub
//        }
//    };

    private TextWatcher keywordOnchange = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
            //ProductCategory cat =   (ProductCategory) spinnerCategory.getSelectedItem();
            RadioButton item = root.findViewById(radioGroup.getCheckedRadioButtonId());
            String cateName = item.getText().toString();
            menuadapter.set(DS.getAll(txtKeyword.getText().toString(),DS.getIdByName(cateName)));
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

    private boolean canScroll(ScrollView scrollView) {
        View child = (View) scrollView.getChildAt(0);
        if (child != null) {
            int childHeight = (child).getHeight();
            return scrollView.getHeight() < childHeight + scrollView.getPaddingTop() + scrollView.getPaddingBottom();
        }
        return false;
    }

    public void resizeArrow()
    {
        scroll.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if(canScroll(scroll))
                {
                    LayoutParams param = new LayoutParams(arrow.getLayoutParams());
                    param.height = arrow.getHeight()/2;
                    param.width = arrow.getWidth()/2;
                    param.bottomMargin = 16;
                    param.topMargin = 16;
                    param.gravity  = Gravity.CENTER;
                    arrow.setLayoutParams(param);
                }
            }
        });

    }

    private View.OnClickListener cancelCheckOutOnlick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            isCheckout = false;
            showCart();
        }
    };



    private View.OnClickListener payOnlick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if(txtPayment.getText().toString().equals(""))
            {
                Toast.makeText(getActivity(), getString(R.string.enter_payment), Toast.LENGTH_SHORT).show();
                return;
            }

            Date dt = new Date();

            Order order = new Order();
            order.setOrderID(Helper.getOrderID());
            order.setBranchID(Helper.read(Constants.KEY_SETTING_BRANCH_ID,""));
            order.setUserID(com.foa.pos.MainActivity.SesID);
            order.setCreatedOn(dt);
            order.setDescription("");
            order.setUpdatedOn(dt);

            ArrayList<OrderDetails> detaillist = new ArrayList<OrderDetails>();
            double subtotal = 0;
            double disc = 0;
            for (int i = 0; i < cartadapter.getCount(); i++) {
                Cart cart = (Cart) cartadapter.getItem(i);
                OrderDetails detail = new OrderDetails();
                detail.setDetailID(Helper.getOrderDetailID(i));
                detail.setDiscount(cart.getDiscount());
                detail.setName(cart.getProductName());
                detail.setQty(cart.getQty());
                detail.setPrice(cart.getPrice());
                detail.setProductID(cart.getProductID());
                detail.setOrderID(order.getOrderID());
                detaillist.add(detail);

                subtotal += detail.getQty() * detail.getPrice();
                disc += subtotal * (detail.getDiscount()/100);
            }

            order.setOrderDetails(detaillist);

            order.setDiscount(disc);

            double sub = subtotal - disc;
            double tax = sub * (Double.parseDouble(Helper.read(Constants.KEY_SETTING_TAX, Constants.VAL_DEFAULT_TAX))/100);

            order.setTax(tax);
            order.setAmount(sub + tax);


            CustomConfirm con = new CustomConfirm(getActivity(),order,mService);
            con.setConfirmListener(new CustomConfirm.ConfirmListener() {
                @Override
                public void onFinish(String result) {
                    // TODO Auto-generated method stub
                    ClearForm();
                    Toast.makeText(getActivity(),"Thanh toán thành công", Toast.LENGTH_SHORT).show();
                }
            });
            con.show();

        }
    };

    private View.OnClickListener orderOnlick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if(cartadapter.getCount() == 0)
            {
                Toast.makeText(getActivity(), "Chọn ít nhất 1 chọn sản phẩm", Toast.LENGTH_SHORT).show();
                return;
            }
            isCheckout = true;
            showCheckout();
        }
    };

    private View.OnClickListener saveOnlick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub

        }
    };

    private TextWatcher paymentOnchange = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
            try {
                if(!s.toString().equals(""))
                {
                    double pay = Integer.parseInt(s.toString());
                    double change = pay - total;
                    txtChange.setText(Helper.decimalformat.format(change)+" "+Helper.read(Constants.KEY_SETTING_CURRENCY_SYMBOL, Constants.VAL_DEFAULT_CURRENCY_SYMBOL) );
                }
                else
                {
                    txtChange.setText(Helper.decimalformat.format(0)+" "+Helper.read(Constants.KEY_SETTING_CURRENCY_SYMBOL, Constants.VAL_DEFAULT_CURRENCY_SYMBOL));
                }


            } catch (Exception e) {
                // TODO: handle exception
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

        }
    };




    private void showCart()
    {
        YoYo.with(Techniques.FadeOutDown).duration(700).withListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator arg0) {
                // TODO Auto-generated method stub
                btnCancelCheckout.setEnabled(false);
                btnPay.setEnabled(false);
            }
            @Override
            public void onAnimationRepeat(Animator arg0) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onAnimationEnd(Animator arg0) {
                // TODO Auto-generated method stub
                checkOutContainer.setVisibility(View.GONE);
                btnOrder.setEnabled(true);
                //.setEnabled(true);
            }

            @Override
            public void onAnimationCancel(Animator arg0) {
                // TODO Auto-generated method stub

            }
        }).playOn(checkOutContainer);
    }

    private void showCheckout()
    {
        YoYo.with(Techniques.FadeInDown).duration(1000).withListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator arg0) {
                // TODO Auto-generated method stub
                checkOutContainer.setVisibility(View.VISIBLE);
                btnOrder.setEnabled(false);
                //btnCancel.setEnabled(false);
            }

            @Override
            public void onAnimationRepeat(Animator arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animator arg0) {
                // TODO Auto-generated method stub
                btnPay.setEnabled(true);
                btnCancelCheckout.setEnabled(true);
            }

            @Override
            public void onAnimationCancel(Animator arg0) {
                // TODO Auto-generated method stub

            }
        }).playOn(checkOutContainer);
    }

    private void ClearForm()
    {
        cartadapter.removeAll();
        txtPayment.setText("");
        showCart();
        txtKeyword.setText("");
        ((RadioButton)radioGroup.getChildAt(0)).setChecked(true);
        //spinnerCategory.setSelection(0);
        menuadapter.unCheckAll();
        isCheckout = false;
    }

    private View.OnClickListener toogleOnclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(Helper.read(Constants.KEY_SETTING_MENU, Constants.VAL_DEFAULT_MENU).equals("GRID"))
            {
                Helper.write(Constants.KEY_SETTING_MENU,"LIST");
                btnToggleList.setImageResource(R.drawable.ic_listview);
                menuadapter.setMenu("LIST");
                menuGrid.setVisibility(View.GONE);
                menuList.setVisibility(View.VISIBLE);


            }
            else
            {
                Helper.write(Constants.KEY_SETTING_MENU,"GRID");
                btnToggleList.setImageResource(R.drawable.ic_gridview);
                menuadapter.setMenu("GRID");
                menuList.setVisibility(View.GONE);
                menuGrid.setVisibility(View.VISIBLE);
            }
        }
    };


    @Override
    public void onClick(View v) {
        Log.d("radioclick", " Name " + ((RadioButton)v).getText() +" Id is "+v.getId());
    }
}