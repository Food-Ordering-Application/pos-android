package com.foa.pos.fragment;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.foa.pos.R;
import com.foa.pos.dummy.MasterContent;
import com.foa.pos.entity.ProductCategory;
import com.foa.pos.sqlite.DatabaseManager;
import com.foa.pos.sqlite.ds.ProductCategoryDataSource;
import com.foa.pos.utils.Constants;
import com.foa.pos.utils.Helper;

public class CategoryAddFragment extends Fragment {
	private MasterContent.DummyItem mItem;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments().containsKey(Constants.ARG_ITEM_ID)) {
			mItem = MasterContent.ITEM_MAP.get(getArguments().getString(Constants.ARG_ITEM_ID));
		}
		
	}
	
	
	private EditText txtName;
	
	private ImageButton btnSave;
	private boolean isEdit = false;
	private String lastCode;
	private String lastName;
	private TextView subtitle;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_category_add,container, false);
		
		TextView title = (TextView)rootView.findViewById(R.id.item_detail);
		subtitle = (TextView)rootView.findViewById(R.id.subtitle);
		TextView chev = (TextView)rootView.findViewById(R.id.chevron);
		
		TextView t2 = (TextView)rootView.findViewById(R.id.tvProductPrice);
		
		
		if (mItem != null) {
			title.setText(mItem.content);
		}
		
		subtitle.setTypeface(Helper.OpenSansSemibold);
		chev.setTypeface(Helper.OpenSansSemibold);
		title.setTypeface(Helper.OpenSansSemibold);
		
		t2.setTypeface(Helper.OpenSansRegular);
		
		
		txtName = (EditText)rootView.findViewById(R.id.editText2);
		txtName.setTypeface(Helper.OpenSansRegular);
		
		btnSave = (ImageButton)rootView.findViewById(R.id.btnPlusQuantityCartItem);
		btnSave.setOnClickListener(saveOnclick);
		
		
		populateField();
		
		return rootView;
	}
	

	private void populateField()
	{
		if (getArguments().containsKey(Constants.ARG_CATEGORY_ID)) {
			isEdit = true;
			lastCode = getArguments().getString(Constants.ARG_CATEGORY_ID);
			subtitle.setText(getString(R.string.edit));
			
			SQLiteDatabase db =  DatabaseManager.getInstance().openDatabase();
	        ProductCategoryDataSource ds = new ProductCategoryDataSource(db);
	        
	        ProductCategory dt =  ds.get(lastCode);
	        txtName.setText(dt.getCategoryName());
	        lastName = dt.getCategoryName();
	        DatabaseManager.getInstance().closeDatabase();;
		}
	}
	
	private OnClickListener saveOnclick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String name = txtName.getText().toString();
			
			if(name.equals(""))
			{
				Toast.makeText(getActivity(), getString(R.string.error_field_empty), Toast.LENGTH_SHORT).show();
				return;
			}
			
			SQLiteDatabase db =  DatabaseManager.getInstance().openDatabase();
			ProductCategoryDataSource ds = new ProductCategoryDataSource(db);
	        
			ProductCategory data = new ProductCategory();
			
			data.setCategoryID(isEdit ? lastCode : Helper.getCategoryID());
			data.setCategoryName(name);
			
			
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
				if(ds.cekName(name))
				{
					Toast.makeText(getActivity(), getString(R.string.name_exist), Toast.LENGTH_SHORT).show();
					return;
				}
				
				ds.insert(data);
			}
			
			
			DatabaseManager.getInstance().closeDatabase();;
		
			Toast.makeText(getActivity(), getString(R.string.save_succeed), Toast.LENGTH_SHORT).show();
			getFragmentManager().popBackStack();
			hideKeyboard();
			
		}
	};
	
	private void hideKeyboard() {   
		 
	    View view = getActivity().getCurrentFocus();
	    if (view != null) {
	        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
	        inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	    }
	}
	
	
}

