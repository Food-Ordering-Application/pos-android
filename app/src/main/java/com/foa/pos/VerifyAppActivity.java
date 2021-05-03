package com.foa.pos;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.foa.pos.network.RetrofitClient;
import com.foa.pos.network.response.LoginResponse;
import com.foa.pos.network.response.VerifyAppResponse;
import com.foa.pos.utils.Constants;
import com.foa.pos.utils.Helper;
import com.foa.pos.widget.LoadingDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.provider.Settings.Secure;
import android.widget.Toast;

import static com.foa.pos.utils.Helper.getContext;

public class VerifyAppActivity extends AppCompatActivity {
    Context context;
    EditText txtInput1;
    EditText txtInput2;
    EditText txtInput3;
    Button btnVerify;
    LoadingDialog loading;
    LinearLayout verifyWrapper;
    private String androidId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_app);
        context = this;
        Helper.initialize(getBaseContext());
        androidId = Secure.getString(getContext().getContentResolver(),Secure.ANDROID_ID);

        txtInput1 = findViewById(R.id.txtInput1);
        txtInput2 = findViewById(R.id.txtInput2);
        txtInput3 = findViewById(R.id.txtInput3);
        btnVerify = findViewById(R.id.btnVerify);
        verifyWrapper = findViewById(R.id.verifyWrapper);
        loading = new LoadingDialog(this);

        if (!Helper.read(Constants.RESTAURANT_ID).isEmpty()){
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
}