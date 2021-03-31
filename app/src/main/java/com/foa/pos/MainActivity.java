package com.foa.pos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.foa.pos.sqlite.DatabaseHelper;
import com.foa.pos.sqlite.DatabaseManager;
import com.google.android.material.navigationrail.NavigationRailView;

public class MainActivity extends AppCompatActivity implements  NavigationRailView.OnNavigationItemSelectedListener {
	public static String SesID;
	AppCompatActivity appCompatActivity = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		appCompatActivity = this;
		DatabaseManager.initializeInstance(new DatabaseHelper(this));

		NavigationRailView navigationRailView = (NavigationRailView) findViewById(R.id.navigation_rail);
		navigationRailView.setOnNavigationItemSelectedListener(this);

	}


	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item) {
		switch (item.getItemId()){
			case R.id.navigation_dashboard:
				Toast.makeText(this, "dashboard", Toast.LENGTH_SHORT).show();
				 appCompatActivity.getSupportFragmentManager().beginTransaction()
						.setReorderingAllowed(true)
						.replace(R.id.content, DashboardFragment.class, null)
						.commit();
				return true;
			case R.id.navigation_order:
				appCompatActivity.getSupportFragmentManager().beginTransaction()
						.setReorderingAllowed(true)
						.replace(R.id.content, OrderFragment.class, null)
						.commit();
				Toast.makeText(this, "order", Toast.LENGTH_SHORT).show();
				return true;
			case R.id.navigation_delivery:

				appCompatActivity.getSupportFragmentManager().beginTransaction()
						.setReorderingAllowed(true)
						.replace(R.id.content, DeliveryFragment.class, null)
						.commit();
				Toast.makeText(this, "delivery", Toast.LENGTH_SHORT).show();
				return true;
			case R.id.navigation_setting:

				appCompatActivity.getSupportFragmentManager().beginTransaction()
						.setReorderingAllowed(true)
						.replace(R.id.content, SettingFragment.class, null)
						.commit();
				Toast.makeText(this, "setiing", Toast.LENGTH_SHORT).show();
				return true;
			default: return false;
		}
	}

//	@Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//
//		Intent intent = null;
//		switch ( v.getId()) {
//		case R.id.btnMasterData:
//			intent = new Intent(com.foa.pos.MainActivity.this, MasterListActivity.class);
//			break;
//		case R.id.btnQuickOrder:
//			intent = new Intent(com.foa.pos.MainActivity.this, QuickOrderActivity.class);
//			break;
//		case R.id.btnSetting:
//			intent = new Intent(com.foa.pos.MainActivity.this,SettingListActivity.class);
//			break;
//		case R.id.btnLogout:
//			 logout();
//			break;
//		default:
//			break;
//		}
//
//		if(intent != null)
//		{
//			startActivity(intent);
//			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//		}
//	}
	
	private void logout()
	{
		
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(com.foa.pos.MainActivity.this);
        alertDialog.setTitle(getString(R.string.confirmation));
        alertDialog.setMessage(getString(R.string.logout_confirmation));
        alertDialog.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	SesID = null;
            	Toast.makeText(com.foa.pos.MainActivity.this,getString(R.string.logout_success), Toast.LENGTH_SHORT).show();
        		Intent intent = new Intent(com.foa.pos.MainActivity.this, LoginActivity.class);
        		startActivity(intent);
        		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        		finish();
            }
        });
 
        alertDialog.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	dialog.cancel();
            }
        });
 
        alertDialog.show();

	}
}
