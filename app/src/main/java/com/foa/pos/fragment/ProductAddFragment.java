package com.foa.pos.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.foa.pos.R;
import com.foa.pos.adapter.CategorySpinnerAdapter;
import com.foa.pos.dummy.MasterContent;
import com.foa.pos.entity.Product;
import com.foa.pos.entity.ProductCategory;
import com.foa.pos.sqlite.DatabaseManager;
import com.foa.pos.sqlite.ds.ProductCategoryDataSource;
import com.foa.pos.sqlite.ds.ProductDataSource;
import com.foa.pos.utils.Constants;
import com.foa.pos.utils.Helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ProductAddFragment extends Fragment {
	private MasterContent.DummyItem mItem;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments().containsKey(Constants.ARG_ITEM_ID)) {
			mItem = MasterContent.ITEM_MAP.get(getArguments().getString(Constants.ARG_ITEM_ID));
		}
	}
	
	private EditText txtName;
	private EditText txtPrice;
	private EditText txtDiscount;
	private EditText txtDescription;
	
	private RadioGroup radioGroup;
	private RadioButton radio1;
	private RadioButton radio2;
			
	private ImageButton btnSave;
	private boolean isEdit = false;
	private String lastCode;
	private String lastName;
	private TextView subtitle;
	
	private ImageView icon;
	private Bitmap bitmapImage;
	
	private Spinner spinnerCategory;
	private CategorySpinnerAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_product_add,container, false);
		
		TextView title = (TextView)rootView.findViewById(R.id.item_detail);
		subtitle = (TextView)rootView.findViewById(R.id.subtitle);
		TextView chev = (TextView)rootView.findViewById(R.id.chevron);
		
		TextView t1 = (TextView)rootView.findViewById(R.id.tvCartProductName);
		TextView t2 = (TextView)rootView.findViewById(R.id.tvProductPrice);
		TextView t3 = (TextView)rootView.findViewById(R.id.tvTotal);
		TextView t4 = (TextView)rootView.findViewById(R.id.textView4);
		TextView t5 = (TextView)rootView.findViewById(R.id.textView5);
		TextView t6 = (TextView)rootView.findViewById(R.id.textView6);
		TextView t7 = (TextView)rootView.findViewById(R.id.textView7);
		
		if (mItem != null) {
			title.setText(mItem.content);
		}
		
		subtitle.setTypeface(Helper.OpenSansSemibold);
		chev.setTypeface(Helper.OpenSansSemibold);
		title.setTypeface(Helper.OpenSansSemibold);
		
		t1.setTypeface(Helper.OpenSansRegular);
		t2.setTypeface(Helper.OpenSansRegular);
		t3.setTypeface(Helper.OpenSansRegular);
		t4.setTypeface(Helper.OpenSansRegular);
		t5.setTypeface(Helper.OpenSansRegular);
		t6.setTypeface(Helper.OpenSansRegular);
		t7.setTypeface(Helper.OpenSansRegular);
		
		txtName = (EditText)rootView.findViewById(R.id.editText2);
		txtName.setTypeface(Helper.OpenSansRegular);
		
		txtPrice = (EditText)rootView.findViewById(R.id.editText1);
		txtPrice.setTypeface(Helper.OpenSansRegular);
		
		txtDescription = (EditText)rootView.findViewById(R.id.editText4);
		txtDescription.setTypeface(Helper.OpenSansRegular);
		
		txtDiscount = (EditText)rootView.findViewById(R.id.editText5);
		txtDiscount.setTypeface(Helper.OpenSansRegular);
		
		radio1 = (RadioButton)rootView.findViewById(R.id.radio0);
		radio2 = (RadioButton)rootView.findViewById(R.id.radio1);
		
		radio1.setTypeface(Helper.OpenSansRegular);
		radio2.setTypeface(Helper.OpenSansRegular);
		
		radioGroup = (RadioGroup)rootView.findViewById(R.id.radioGroup1);
		
		btnSave = (ImageButton)rootView.findViewById(R.id.imageButton1);
		btnSave.setOnClickListener(saveOnclick);
		
		icon = (ImageView)rootView.findViewById(R.id.imageView1);
		icon.setOnClickListener(iconOnclick);
		
		spinnerCategory = (Spinner)rootView.findViewById(R.id.spinner1);
		adapter = new CategorySpinnerAdapter(getActivity());
		spinnerCategory.setAdapter(adapter);
		populateField();
		
		return rootView;
	}
	

	private void populateField()
	{
		
		SQLiteDatabase db =  DatabaseManager.getInstance().openDatabase();
		ProductCategoryDataSource catds = new ProductCategoryDataSource(db);
		adapter.set(catds.getAll());
		
		if (getArguments().containsKey(Constants.ARG_PRODUCT_ID)) {
			isEdit = true;
			lastCode = getArguments().getString(Constants.ARG_PRODUCT_ID);
			subtitle.setText(getString(R.string.edit));
			
			
	        ProductDataSource ds = new ProductDataSource(db);
	        
	        Product dt =  ds.get(lastCode);
	        txtName.setText(dt.getProductName());
	        txtPrice.setText(Helper.decimalformat2.format(dt.getPrice()));
	        txtDiscount.setText(Helper.decimalformat2.format(dt.getDiscount()));
	        txtDescription.setText(dt.getDescription());
	        
	        radio1.setChecked(true);
	        if(dt.getStatus().equals("0"))
	        	radio2.setChecked(true);
	        	
	        if(dt.getImage() != null)
        	{
	        	File imgFile = new File(dt.getImage());
	    		if(imgFile.exists()){
	    			icon.setImageDrawable(Drawable.createFromPath(imgFile.getAbsolutePath()));
	    		}
	    		else
	    		{
	    			icon.setImageResource(R.drawable.ic_noimage_square);
	    		}
        	}
	        else
	        {
	        	icon.setImageResource(R.drawable.ic_noimage_square);
	        }
	        
	        spinnerCategory.setSelection( adapter.indexOf(dt.getCategoryID()));
	        lastName = dt.getProductName();
	        
		}
		
		DatabaseManager.getInstance().closeDatabase();
	}
	
	private OnClickListener saveOnclick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String name = txtName.getText().toString();
			String price = txtPrice.getText().toString();
			String description = txtDescription.getText().toString();
			String discount = txtDiscount.getText().toString();
			
			ProductCategory cat =   (ProductCategory) spinnerCategory.getSelectedItem();
			String category = cat.getCategoryID();
			
			if(name.equals("") || price.equals("") || description.equals("") || category == null)
			{
				Toast.makeText(getActivity(), getString(R.string.error_field_empty), Toast.LENGTH_SHORT).show();
				return;
			}
			
			try {
				if(Double.parseDouble(price) < 0)
				{
					Toast.makeText(getActivity(), getString(R.string.price_less_zero), Toast.LENGTH_SHORT).show();
					return;
				}
					
			} catch (Exception e) {
				Toast.makeText(getActivity(), getString(R.string.invalid_price_format), Toast.LENGTH_SHORT).show();
				return;
			}
			
			if(!discount.equals(""))
			{
				try {
					if(Double.parseDouble(discount) < 0)
					{
						Toast.makeText(getActivity(), getString(R.string.discount_less_zero), Toast.LENGTH_SHORT).show();
						return;
					}
						
				} catch (Exception e) {
					Toast.makeText(getActivity(), getString(R.string.invalid_discount_format), Toast.LENGTH_SHORT).show();
					return;
				}
			}
			
			
			SQLiteDatabase db =  DatabaseManager.getInstance().openDatabase();
			ProductDataSource ds = new ProductDataSource(db);
	        
			Product data = new Product();
			
			data.setProductID(isEdit ? lastCode : Helper.getProductID());
			data.setProductName(name);
			data.setStatus(radio1.isChecked() ? "1" : "0");
			data.setMerchantID(Helper.read(Constants.KEY_SETTING_MERCHANT_ID));
			data.setRefID(com.foa.pos.MainActivity.SesID);
			data.setImage(saveImage(Helper.getMD5(String.valueOf(System.currentTimeMillis()))));
			data.setPrice(Double.parseDouble(price));
			data.setDiscount(discount.equals("") ? 0 : Double.parseDouble(discount));
			data.setDescription(description);
			data.setCategoryID(category);
			data.setBranchID(Helper.read(Constants.KEY_SETTING_BRANCH_ID));
			if(isEdit)
			{
				if(!lastName.equals(name))
				{
					if(ds.cekName(name))
					{
						Toast.makeText(getActivity(), getString(R.string.name_exist), Toast.LENGTH_SHORT).show();
						return;
					}
				}
				
				ds.update(data,lastCode);
			}
			else
			{
				Log.e("INSERT", "OK");
				if(ds.cekName(name))
				{
					Toast.makeText(getActivity(), getString(R.string.name_exist), Toast.LENGTH_SHORT).show();
					return;
				}
				
				ds.insert(data);
			}
			
			
			DatabaseManager.getInstance().closeDatabase();
		
			Toast.makeText(getActivity(), getString(R.string.save_succeed), Toast.LENGTH_SHORT).show();
			getFragmentManager().popBackStack();
			hideKeyboard();
			
		}
	};
	
private OnClickListener iconOnclick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			chooseAction();
		}
	};
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != getActivity().RESULT_OK) return;

        switch (requestCode) {
            case Constants.PICK_IMAGE_FROM_GALLERY:
            	Uri iconUri = data.getData();
                Cursor cursor = getActivity().getContentResolver().query(iconUri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null);
                cursor.moveToFirst();
                iconUri = Uri.parse(cursor.getString(0));

                File f = new File(iconUri.getPath());
                Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
                float scale = Helper.scaleFactor(bitmap.getWidth());
				bitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth()*scale), (int) (bitmap.getHeight()*scale), false);
                icon.setImageBitmap(bitmap);
                bitmapImage = bitmap;
                break;
            case Constants.PICK_IMAGE_FROM_CAMERA:

                Bitmap bitmap2 = (Bitmap) data.getExtras().get("data");
                float scale2 = Helper.scaleFactor(bitmap2.getWidth());
                bitmap2 = Bitmap.createScaledBitmap(bitmap2, (int) (bitmap2.getWidth()*scale2), (int) (bitmap2.getHeight()*scale2), false);
                icon.setImageBitmap(bitmap2);
                bitmapImage = bitmap2;
                break;
        }
	}

	private void chooseAction()
	{
		final CharSequence[] items = { getString(R.string.from_camera), getString(R.string.from_gallery),getString(R.string.cancel) };

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(getString(R.string.complete_action_using));
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
					if (items[item].equals(getString(R.string.from_camera))) {
						Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						startActivityForResult(intent, Constants.PICK_IMAGE_FROM_CAMERA);
					} else if (items[item].equals(getString(R.string.from_gallery))) {
						Intent intent = new Intent(
								Intent.ACTION_PICK,
								MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
						intent.setType("image/*");
						startActivityForResult(
								Intent.createChooser(intent, getString(R.string.select_file)), Constants.PICK_IMAGE_FROM_GALLERY);
					} else if (items[item].equals(getString(R.string.cancel))) {
						dialog.dismiss();
					}
			}
		});
		builder.show();
	}
	
	private String saveImage(String name)
	{
		String res = null;
		if(bitmapImage != null)
		{
			File dest = new File(Helper.getAppDir(), name);
			
			Bitmap bitmap = bitmapImage;
			FileOutputStream out;
			try {
				
				out = new FileOutputStream(dest);
				float scale = Helper.scaleFactor(bitmap.getWidth());
				bitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth()*scale), (int) (bitmap.getHeight()*scale), false);
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
			    out.flush();
			    out.close();
			    res = dest.toString();
			    
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		}
		
		return res;
		
	}
	
	private void hideKeyboard() {   
		 
	    View view = getActivity().getCurrentFocus();
	    if (view != null) {
	        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
	        inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	    }
	}
	
	
}

