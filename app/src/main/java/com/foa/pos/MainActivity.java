package com.foa.pos;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.foa.pos.receiver.NetworkReceiver;
import com.foa.pos.sqlite.DatabaseHelper;
import com.foa.pos.sqlite.DatabaseManager;
import com.foa.pos.utils.LoginSession;
import com.foa.pos.utils.OrderSession;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
	private AppBarConfiguration mAppBarConfiguration;
	private BroadcastReceiver networkReceiver = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);


		DrawerLayout drawer = findViewById(R.id.drawer_layout);
		NavigationView navigationView = findViewById(R.id.nav_view);

		mAppBarConfiguration = new AppBarConfiguration.Builder(
				R.id.navigation_order, R.id.navigation_delivery, R.id.navigation_manaorder)
				.setOpenableLayout(drawer)
				.build();
		NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
		NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
		NavigationUI.setupWithNavController(navigationView, navController);
		navigationView.bringToFront();

		navigationView.getMenu().findItem(R.id.navigation_logout).setOnMenuItemClickListener(menuItem -> {
			logout();
			return true;
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		networkReceiver = new NetworkReceiver();
		registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(networkReceiver);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (networkReceiver!= null)
		unregisterReceiver(networkReceiver);
	}

	@Override
	public boolean onSupportNavigateUp() {
		NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
		return NavigationUI.navigateUp(navController, mAppBarConfiguration)
				|| super.onSupportNavigateUp();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.right_layout_menu, menu);
		return true;
	}
	
	private void logout()
	{
		LoginSession.clearInstance();
		OrderSession.clearInstance();
		Intent intent = new Intent(MainActivity.this, LoginActivity.class);
		startActivity(intent);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		finish();
	}

	@Override
	public void onBackPressed() {

	}

}
