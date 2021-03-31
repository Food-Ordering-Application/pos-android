package com.foa.pos;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.foa.pos.entity.User;
import com.foa.pos.sqlite.DatabaseHelper;
import com.foa.pos.sqlite.DatabaseManager;
import com.foa.pos.sqlite.ds.UserDataSource;
import com.foa.pos.utils.Helper;
import com.foa.pos.widget.LoadingDialog;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;

public class LoginActivity extends Activity implements OnClickListener {
	private EditText txtusername;
	private EditText txtpassword;
	private Button btnLogin;
	private LoadingDialog loading;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		btnLogin = (Button)findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(this);
		
		txtusername = (EditText)findViewById(R.id.txtUserName);
		txtpassword = (EditText)findViewById(R.id.txtPassword);
		
		txtusername.setTypeface(Helper.OpenSansRegular);
		txtpassword.setTypeface(Helper.OpenSansRegular);
		btnLogin.setTypeface(Helper.OpenSansSemibold);
		
		loading = new LoadingDialog(this);
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		String username = txtusername.getText().toString();
		String password = txtpassword.getText().toString();
		
		if(username.equals("") || password.equals(""))
		{
			YoYo.with(Techniques.Shake).duration(700).playOn(findViewById(R.id.loginWrapper));
			Toast.makeText(com.foa.pos.LoginActivity.this, getString(R.string.error_field_empty), Toast.LENGTH_SHORT).show();
			return;
		}
		
		AsyncAction save = new AsyncAction();
		save.execute(txtusername.getText().toString(),txtpassword.getText().toString());
	}
	
	public class AsyncAction extends AsyncTask<String, String, String>
	{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			loading.show();
		}
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String result = "0";
			
			String username = params[0];
			String password = params[1];
			
		
			DatabaseManager.initializeInstance(new DatabaseHelper(com.foa.pos.LoginActivity.this));
			SQLiteDatabase db =  DatabaseManager.getInstance().openDatabase();
			UserDataSource DS = new UserDataSource(db);
			final User usr = DS.auth(username, password);
			DatabaseManager.getInstance().closeDatabase();

			try {
				
				if(usr.getUserID() != null)
				{
					result = usr.getUserID() ;
				}
				
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return result;
		}
		
		@Override
		protected void onPostExecute(final String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			loading.dismiss();
			if(result.equals("0"))
			{
				YoYo.with(Techniques.Shake).duration(700).playOn(findViewById(R.id.loginWrapper));
				Toast.makeText(com.foa.pos.LoginActivity.this, getString(R.string.error_user_or_password), Toast.LENGTH_SHORT).show();
			}
			else
			{
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
			
		}
		
	}
}
