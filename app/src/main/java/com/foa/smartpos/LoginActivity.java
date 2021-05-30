package com.foa.smartpos;

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

import com.foa.smartpos.network.RetrofitClient;
import com.foa.smartpos.network.entity.LoginBody;
import com.foa.smartpos.network.response.LoginData;
import com.foa.smartpos.network.response.ResponseAdapter;
import com.foa.smartpos.network.utils.NetworkStatus;
import com.foa.smartpos.network.utils.NetworkUtils;
import com.foa.smartpos.sqlite.DatabaseHelper;
import com.foa.smartpos.sqlite.DatabaseManager;
import com.foa.smartpos.utils.Constants;
import com.foa.smartpos.utils.Helper;
import com.foa.smartpos.dialog.LoadingDialog;
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
		btnLogin =  findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(this);

		wrapperLogin = findViewById(R.id.loginWrapper);
		btnSaleOffline = findViewById(R.id.OfflineSaleModeButton);
		txtusername = findViewById(R.id.txtUserName);
		txtpassword = findViewById(R.id.txtPassword);
		Helper.initialize(this);
		DatabaseManager.initializeInstance(new DatabaseHelper(this));
		loading = new LoadingDialog(this);

		btnSaleOffline.setOnClickListener(v -> goNext());
		String bearerAccessToken = Helper.read(Constants.BEARER_ACCESS_TOKEN);
		if (bearerAccessToken!= null){
			RetrofitClient.getInstance().setAuthorizationHeader(bearerAccessToken);
			goNext();
		}

	}

	private void goNext(){
		Intent intent;
		if(NetworkUtils.getConnectivityStatusString(context)== NetworkStatus.CONNETED){
			intent = new Intent(LoginActivity.this,SplashActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

		}else{
			intent = new Intent(LoginActivity.this,MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		}

		startActivity(intent);
		finish();
	}



	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		String username = txtusername.getText().toString();
		String password = txtpassword.getText().toString();
		username = "hoangcashier";
		password= "123123123";


		if (username.equals("") || password.equals("")) {
			YoYo.with(Techniques.Shake).duration(700).playOn(findViewById(R.id.loginWrapper));
			Toast.makeText(com.foa.smartpos.LoginActivity.this, getString(R.string.error_field_empty), Toast.LENGTH_SHORT).show();

			return;
		}

		loading.show();
		Call<ResponseAdapter<LoginData>> responseCall = RetrofitClient.getInstance().getAppService()
				.login(new LoginBody(username, password,"6587f789-8c76-4a2e-9924-c14fc30629ef"));
		responseCall.enqueue(new Callback<ResponseAdapter<LoginData>>() {
			@Override
			public void onResponse(Call<ResponseAdapter<LoginData>> call, Response<ResponseAdapter<LoginData>> response) {
				switch (response.code()) {
					case Constants.STATUS_CODE_SUCCESS:
						ResponseAdapter<LoginData> res = response.body();
					if (res.getStatus() == Constants.STATUS_CODE_SUCCESS) {
						Helper.setLoginData(res.getData());
						RetrofitClient.getInstance().setAuthorizationHeader(res.getData().getBearerAccessToken());

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

								goNext();
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
				loading.dismiss();
			}

			@Override
			public void onFailure(Call<ResponseAdapter<LoginData>> call, Throwable t) {
				Log.e("Login Error", t.getMessage());
				Helper.showFailNotification(context,loading,wrapperLogin,getString(R.string.login_failed));

			}
		});
	}


}
