package com.foa.smartpos;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.foa.smartpos.model.Order;
import com.foa.smartpos.receiver.BackgroundJobReceiver;
import com.foa.smartpos.receiver.NetworkReceiver;
import com.foa.smartpos.session.BackgroundJobSession;
import com.foa.smartpos.session.NotificationOrderIdSession;
import com.foa.smartpos.utils.Constants;
import com.foa.smartpos.utils.Helper;
import com.foa.smartpos.session.OrderSession;
import com.google.android.material.navigation.NavigationView;
import com.pusher.pushnotifications.PushNotifications;


public class MainActivity extends AppCompatActivity {
	private final int REQUEST_ID = 1;
	private AppBarConfiguration mAppBarConfiguration;
	private BroadcastReceiver networkReceiver = null;
	private Button manualSyncButton;
	public static NewDeliveryOrderListener listener  ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);


		DrawerLayout drawer = findViewById(R.id.drawer_layout);
		NavigationView navigationView = findViewById(R.id.nav_view);

		mAppBarConfiguration = new AppBarConfiguration.Builder(
				R.id.navigation_order, R.id.navigation_delivery, R.id.navigation_manaorder,R.id.navigation_setting)
				.setOpenableLayout(drawer)
				.build();
		NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

		if(NotificationOrderIdSession.getInstance()!=null){
				Bundle bundle = new Bundle();
				bundle.putString("orderId", NotificationOrderIdSession.getInstance());
				navController.navigate(R.id.navigation_delivery,bundle);
		}

		NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
		NavigationUI.setupWithNavController(navigationView, navController);
		navigationView.bringToFront();

		navigationView.getMenu().findItem(R.id.navigation_logout).setOnMenuItemClickListener(menuItem -> {
			logout();
			return true;
		});



		BackgroundJobSession.resetInstance(this,0);
	}

	@Override
	protected void onResume() {
		super.onResume();
		networkReceiver = new NetworkReceiver();
		registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

		PushNotifications.start(getApplicationContext(), "77650b88-b6b2-4178-9fc2-95c36493470d");
		PushNotifications.addDeviceInterest("orders_"+Helper.read(Constants.RESTAURANT_ID));
		//PushNotifications.stop();
		//PushNotifications.addDeviceInterest("orders_"+Helper.read(Constants.RESTAURANT_ID));
		//PushNotifications.clearDeviceInterests();

	}

	@Override
	protected void onPause() {
		super.onPause();
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
		OrderSession.clearInstance();
		Helper.clearLoginData();
		Intent intent = new Intent(MainActivity.this, LoginActivity.class);
		startActivity(intent);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		finish();
	}

	@Override
	public void onBackPressed() {

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		BackgroundJobSession.clearInstance();
	}

	public static void setNewDeliveryOrderListener(NewDeliveryOrderListener newDeliveryOrderListener){
		listener = newDeliveryOrderListener;
	}

	public interface NewDeliveryOrderListener{
		void onReceive(Order order);
	}

}
