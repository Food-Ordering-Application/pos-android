package com.foa.pos.fragment;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.foa.pos.R;
import com.foa.pos.adapter.UserListAdapter;
import com.foa.pos.dummy.MasterContent;
import com.foa.pos.entity.User;
import com.foa.pos.sqlite.DatabaseManager;
import com.foa.pos.sqlite.ds.UserDataSource;
import com.foa.pos.utils.Constants;
import com.foa.pos.utils.Helper;

import java.util.ArrayList;

public class UserListFragment extends Fragment implements OnClickListener {
	private MasterContent.DummyItem mItem;
	private ListView lv;
	private UserListAdapter adapter;
	private ArrayList<User> dtlist;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(Constants.ARG_ITEM_ID)) {
			mItem = MasterContent.ITEM_MAP.get(getArguments().getString(Constants.ARG_ITEM_ID));
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_user_list,container, false);
		
		lv = (ListView)rootView.findViewById(R.id.listView1);
		adapter = new UserListAdapter(getActivity(),mItem.id);
		
		popolateAdapter();
		lv.setAdapter(adapter);
				
		TextView title = (TextView)rootView.findViewById(R.id.item_detail);
		title.setTypeface(Helper.OpenSansSemibold);
		if (mItem != null) {
			title.setText(mItem.content);
		}
		
		rootView.findViewById(R.id.imageButton1).setOnClickListener(this);;
		
		return rootView;
	}
	
	private void popolateAdapter()
	{
		SQLiteDatabase db =  DatabaseManager.getInstance().openDatabase();
        UserDataSource ds = new UserDataSource(db);
        dtlist = ds.getAll();
     
		adapter.set(dtlist);
		DatabaseManager.getInstance().closeDatabase();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Fragment fragment  = new UserAddFragment();
		Bundle arguments = new Bundle();
		arguments.putString(Constants.ARG_ITEM_ID, mItem.id);
		fragment.setArguments(arguments);
		getFragmentManager().beginTransaction()
		.addToBackStack("add")
		.replace(R.id.master_detail_container, fragment).commit();
	}

}


