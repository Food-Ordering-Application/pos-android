package com.foa.pos;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.foa.pos.fragment.CategoryListFragment;
import com.foa.pos.fragment.ProductListFragment;
import com.foa.pos.utils.Constants;


public class SettingFragment extends Fragment implements SettingControlFragment.Callbacks {

	private  View root;
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.activity_master_twopane,container,false);
		Log.e("view",""+ root);
		SettingControlFragment settingControlFragment = new SettingControlFragment();
		settingControlFragment.setmCallbacks(this);
		getActivity().getSupportFragmentManager().beginTransaction()
				.setReorderingAllowed(true)
				.replace(R.id.master_list, settingControlFragment, null)
				.commit();

		settingControlFragment.setActivateOnItemClick(true);
		return root;
	}

	@Override
	public void onItemSelected(String id) {
			setScreen(id);
	}
	
	private void setScreen(String id)
	{
//		while (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0){
//		    getActivity().getSupportFragmentManager().popBackStackImmediate();
//		}
		
		Bundle arguments = new Bundle();
		arguments.putString(Constants.ARG_ITEM_ID, id);
		
		Fragment fragment  = new CategoryListFragment();
		String tag = "";

		 if(id.equals("2"))
			fragment = new CategoryListFragment();
		else if(id.equals("3"))
			fragment = new ProductListFragment();
	
		fragment.setArguments(arguments);
		getActivity().getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.master_detail_container, fragment,tag)
		.commit();
	}
}
