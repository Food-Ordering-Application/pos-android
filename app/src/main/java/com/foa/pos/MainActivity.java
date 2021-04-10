package com.foa.pos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.foa.pos.entity.ProductCategory;
import com.foa.pos.sqlite.DatabaseHelper;
import com.foa.pos.sqlite.DatabaseManager;
import com.foa.pos.sqlite.ds.ProductCategoryDataSource;
import com.foa.pos.utils.Helper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.navigationrail.NavigationRailView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationRailView.OnNavigationItemSelectedListener{
	public static String SesID;
	AppCompatActivity appCompatActivity = null;
	Fragment preFragment = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		appCompatActivity = this;
		DatabaseManager.initializeInstance(new DatabaseHelper(this));
		NavigationRailView navView = findViewById(R.id.nav_view);
		navView.setOnNavigationItemSelectedListener(this);
		navView.getMenu().findItem(R.id.navigation_manaorder).setChecked(true);
	}
	
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

	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item) {
		switch (item.getItemId()) {
			case R.id.navigation_delivery:
				addFragment(new DeliveryFragment());
				return true;
			case R.id.navigation_manaorder:
				addFragment(new OrdersFragment());
				return true;
			case R.id.navigation_setting:
				addFragment(new SettingFragment());
				return true;
			case R.id.navigation_order:
				addFragment(new OrderFragment());
				return true;
			default:
				return false;
		}
	}

	private void addFragment (Fragment fragment){
		appCompatActivity.getSupportFragmentManager().beginTransaction()
				.setReorderingAllowed(true)
				.replace(R.id.nav_host_fragment, fragment, null)
				.commit();
	}
}
