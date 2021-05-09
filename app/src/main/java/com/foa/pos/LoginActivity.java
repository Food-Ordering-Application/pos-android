package com.foa.pos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.foa.pos.network.RetrofitClient;
import com.foa.pos.network.entity.LoginBody;
import com.foa.pos.network.response.LoginData;
import com.foa.pos.network.response.ResponseAdapter;
import com.foa.pos.utils.Constants;
import com.foa.pos.utils.Helper;
import com.foa.pos.utils.LoginSession;
import com.foa.pos.widget.LoadingDialog;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends Activity implements OnClickListener {

	Context context;
	private EditText txtusername;
	private EditText txtpassword;
	private Button btnLogin;
	private Button btnSaleOffline;
	LinearLayout wrapperLogin;
	private LoadingDialog loading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		context = this;
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(this);

		wrapperLogin = findViewById(R.id.loginWrapper);
		btnSaleOffline = findViewById(R.id.OfflineSaleModeButton);
		txtusername = (EditText) findViewById(R.id.txtUserName);
		txtpassword = (EditText) findViewById(R.id.txtPassword);

		loading = new LoadingDialog(this);

		//temp
		Helper.initialize(getBaseContext());
		Helper.write(Constants.MERCHANT_ID, "20c2c064-2457-4525-b7e4-7c2c10564e86");
		Helper.write(Constants.RESTAURANT_ID, "dea72dac-6bcd-4bd8-b3a8-70565b36e0d5");

		btnSaleOffline.setOnClickListener(v -> goNext());
//		if (Helper.read(Constants.CASHIER_ID)!= null){
//			goNext();
//		}

	}

	private void goNext(){
		startActivity( new Intent(LoginActivity.this,MainActivity.class));
	}



	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		//String username = txtusername.getText().toString();
		//String password = txtpassword.getText().toString();
		String username = "hoangcashier";
		String password= "123123123";


		if (username.equals("") || password.equals("")) {
			YoYo.with(Techniques.Shake).duration(700).playOn(findViewById(R.id.loginWrapper));
			Toast.makeText(com.foa.pos.LoginActivity.this, getString(R.string.error_field_empty), Toast.LENGTH_SHORT).show();

			return;
		}

		loading.show();
		Call<ResponseAdapter<LoginData>> responseCall = RetrofitClient.getInstance().getAppService()
				.login(new LoginBody(username, password,Helper.read(Constants.RESTAURANT_ID)));
		responseCall.enqueue(new Callback<ResponseAdapter<LoginData>>() {
			@Override
			public void onResponse(Call<ResponseAdapter<LoginData>> call, Response<ResponseAdapter<LoginData>> response) {
				switch (response.code()) {
					case Constants.STATUS_CODE_SUCCESS:
						ResponseAdapter<LoginData> res = response.body();
					if (res.getStatus() == Constants.STATUS_CODE_SUCCESS) {
						LoginSession.getInstance().setStaffLogin(res.getData());
						RetrofitClient.getInstance().setAuthorizationHeader(res.getData().getBearerAccessToken());
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
					}else{
						Helper.showFailNotification(context,loading,wrapperLogin,getString(R.string.login_failed));
					}
						break;

					case Constants.STATUS_CODE_UNAUTHORIZED:
						Helper.showFailNotification(context,loading,wrapperLogin,getString(R.string.error_user_or_password));
						break;
					case Constants.STATUS_CODE_FORBIDDEN:
						Helper.showFailNotification(context,loading,wrapperLogin,getString(R.string.error_restaurant));
						break;
					default:
						Helper.showFailNotification(context,loading,wrapperLogin,getString(R.string.login_failed));
				}

			}

			@Override
			public void onFailure(Call<ResponseAdapter<LoginData>> call, Throwable t) {
				Log.e("Login Error", t.getMessage());
				Helper.showFailNotification(context,loading,wrapperLogin,getString(R.string.login_failed));

			}
		});
	}

}
