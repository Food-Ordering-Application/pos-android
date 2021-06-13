package com.foa.smartpos;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.foa.smartpos.network.RetrofitClient;
import com.foa.smartpos.network.response.VerifyAppResponse;
import com.foa.smartpos.session.NotificationOrderIdSession;
import com.foa.smartpos.utils.Constants;
import com.foa.smartpos.utils.Helper;
import com.foa.smartpos.dialog.LoadingDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.provider.Settings.Secure;
import android.widget.Toast;

import static com.foa.smartpos.utils.Helper.getContext;

public class VerifyAppActivity extends AppCompatActivity {
    private Context context;
    private EditText txtInput1;
    private EditText txtInput2;
    private EditText txtInput3;
    private Button btnVerify;
    private LoadingDialog loading;
    private LinearLayout verifyWrapper;
    private String androidId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_app);
        context = this;
        Helper.initialize(getBaseContext());

        if(Helper.read(Constants.RESTAURANT_ID)!=null){
           if(getIntent().getExtras()!=null){
               String orderId  = getIntent().getExtras().getString("orderId");
               if (orderId!=null){
                   NotificationOrderIdSession.setInstance(orderId);
               }
           }
            Intent intent  = new Intent(VerifyAppActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        androidId = Secure.getString(getContext().getContentResolver(),Secure.ANDROID_ID);
        txtInput1 = findViewById(R.id.txtInput1);
        txtInput2 = findViewById(R.id.txtInput2);
        txtInput3 = findViewById(R.id.txtInput3);
        btnVerify = findViewById(R.id.btnVerify);
        verifyWrapper = findViewById(R.id.verifyWrapper);
        loading = new LoadingDialog(this);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN}, 1001);
            }
        }

        if (Helper.read(Constants.RESTAURANT_ID)!=null || true){//temp
            startActivity(new Intent(VerifyAppActivity.this, LoginActivity.class));
        }

        txtInput1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()==4){
                    txtInput2.requestFocus();
                }
            }
        });
        txtInput2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()==4){
                    txtInput3.requestFocus();
                }
            }
        });
        txtInput3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()==4){
                    txtInput3.clearFocus();
                }
            }
        });

        btnVerify.setOnClickListener(v -> {
           String key = txtInput1.getText().toString() + "-"
                   + txtInput2.getText().toString() + "-"
                   +txtInput3.getText().toString();
            loading.show();
            Call<VerifyAppResponse> responseCall = RetrofitClient.getInstance().getAppService().verifyAppKey(key,androidId);
            responseCall.enqueue(new Callback<VerifyAppResponse>() {
                @Override
                public void onResponse(Call<VerifyAppResponse> call, Response<VerifyAppResponse> response) {

                    switch (response.code()){
                        case Constants.STATUS_CODE_SUCCESS:
                            loading.dismiss();
                            Toast.makeText(context, getString(R.string.activation_succeed), Toast.LENGTH_SHORT).show();
                            Helper.write(Constants.RESTAURANT_ID, response.body().getData().getRestaurantId());
                            Helper.write(Constants.MERCHANT_ID, response.body().getData().getMerchantId());
                            startActivity(new Intent(VerifyAppActivity.this,LoginActivity.class));
                            break;
                        case Constants.STATUS_CODE_NOT_FOUND:
                            Helper.showFailNotification(context,loading,verifyWrapper,getString(R.string.activation_failed));
                            break;
                        default:
                            Helper.showFailNotification(context,loading,verifyWrapper,getString(R.string.activation_failed));
                    }
                }

                @Override
                public void onFailure(Call<VerifyAppResponse> call, Throwable t) {
                    Helper.showFailNotification(context,loading,verifyWrapper,getString(R.string.activation_failed));
                }
            });
        });
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
            Toast.makeText(VerifyAppActivity.this, "All permission are required", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}