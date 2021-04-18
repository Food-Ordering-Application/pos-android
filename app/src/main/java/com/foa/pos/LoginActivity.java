package com.foa.pos;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.foa.pos.network.RetrofitClient;
import com.foa.pos.network.login.LoginResponse;
import com.foa.pos.utils.Constants;
import com.foa.pos.utils.Helper;
import com.foa.pos.widget.LoadingDialog;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends Activity implements OnClickListener {
	private EditText txtusername;
	private EditText txtpassword;
	private Button btnLogin;
	private LoadingDialog loading;

	String result = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(this);

		txtusername = (EditText) findViewById(R.id.txtUserName);
		txtpassword = (EditText) findViewById(R.id.txtPassword);

		txtusername.setTypeface(Helper.OpenSansRegular);
		txtpassword.setTypeface(Helper.OpenSansRegular);
		btnLogin.setTypeface(Helper.OpenSansSemibold);

		loading = new LoadingDialog(this);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
					checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
					checkSelfPermission(Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED ||
					checkSelfPermission(Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED ||
					checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
				requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN}, 1001);
			}
		}
	}

	private void goNext() {
		Intent intent = new Intent(LoginActivity.this, MainActivity.class);
		startActivity(intent);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		finish();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

		boolean allgranted = true;
		for (int i = 0; i < grantResults.length; i++) {
			if (PackageManager.PERMISSION_GRANTED != grantResults[i]) {
				allgranted = false;
			}
		}
		if (!allgranted) {
			Toast.makeText(LoginActivity.this, "All permission are required", Toast.LENGTH_LONG).show();
			finish();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		String username = txtusername.getText().toString();
		String password = txtpassword.getText().toString();


		if (username.equals("") || password.equals("")) {
			YoYo.with(Techniques.Shake).duration(700).playOn(findViewById(R.id.loginWrapper));
			Toast.makeText(com.foa.pos.LoginActivity.this, getString(R.string.error_field_empty), Toast.LENGTH_SHORT).show();
			return;
		}

		loading.show();
		Call<LoginResponse> responseCall = RetrofitClient.getInstance().getAppService().login(username, password);
		responseCall.enqueue(new Callback<LoginResponse>() {
			@Override
			public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
				switch (response.code()) {
					case Constants.STATUS_CODE_NOT_FOUND:
						loading.dismiss();
						Toast.makeText(LoginActivity.this, "Yêu cầu thất bại", Toast.LENGTH_SHORT).show();
						break;

					case Constants.STATUS_CODE_SUCCESS:
					LoginResponse res = response.body();
					if (res.getStatus() == Constants.STATUS_CODE_SUCCESS) {
						result = res.getData().getUser().getId();

						loading.dismiss();
						btnLogin.setEnabled(false);
						YoYo.with(Techniques.FadeOutDown).interpolate(new OvershootInterpolator()).duration(500).withListener(new AnimatorListener() {
							@Override
							public void onAnimationStart(Animator arg0) {
								// TODO Auto-generated method stub
							}

							@Override
							public void onAnimationRepeat(Animator arg0) {
								// TODO Auto-generated method stub
							}

							@Override
							public void onAnimationEnd(Animator arg0) {
								// TODO Auto-generated method stub
								com.foa.pos.MainActivity.SesID = result;
								Intent intent = new Intent(com.foa.pos.LoginActivity.this, com.foa.pos.MainActivity.class);
								startActivity(intent);
								overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
								finish();
							}

							@Override
							public void onAnimationCancel(Animator arg0) {
								// TODO Auto-generated method stub
							}
						}).playOn(findViewById(R.id.loginWrapper));
					}
					break;
				}

			}

			@Override
			public void onFailure(Call<LoginResponse> call, Throwable t) {
				Log.e("Login Error", t.getMessage());
				loading.dismiss();
				YoYo.with(Techniques.Shake).duration(700).playOn(findViewById(R.id.loginWrapper));
				Toast.makeText(com.foa.pos.LoginActivity.this, getString(R.string.error_user_or_password), Toast.LENGTH_SHORT).show();

			}
		});
	}


	//DatabaseManager.initializeInstance(new DatabaseHelper(com.foa.pos.LoginActivity.this));
	//SQLiteDatabase db =  DatabaseManager.getInstance().openDatabase();
	//UserDataSource DS = new UserDataSource(db);
	//final User usr = DS.auth(username, password);
	//DatabaseManager.getInstance().closeDatabase();

//			try {
//
//				if(usr.getId() != null)
//				{
//					result = usr.getId() ;
//				}
//
//				Thread.sleep(3000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}

}
