package com.foa.pos;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.foa.pos.model.MenuGroup;
import com.foa.pos.model.MenuItem;
import com.foa.pos.network.RetrofitClient;
import com.foa.pos.network.response.MenuData;
import com.foa.pos.network.response.ResponseAdapter;
import com.foa.pos.sqlite.DatabaseHelper;
import com.foa.pos.sqlite.DatabaseManager;
import com.foa.pos.sqlite.ds.MenuGroupDataSource;
import com.foa.pos.sqlite.ds.MenuItemDataSource;
import com.foa.pos.utils.Constants;
import com.foa.pos.utils.Helper;
import com.foa.pos.widget.LoadingDialog;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    TextView syncStatus;
    Button TryAgain;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        syncStatus = findViewById(R.id.syncStatusTextView);
        TryAgain = findViewById(R.id.TryAgainButton);

        Helper.initialize(this);
        Helper.write(Constants.MERCHANT_ID, "20c2c064-2457-4525-b7e4-7c2c10564e86");
        Helper.write(Constants.RESTAURANT_ID, "75d1fd95-9699-4f21-85e6-480def4d8bbb");

        getMenuData();

        TryAgain.setOnClickListener(v -> {
            TryAgain.setEnabled(false);
            getMenuData();
        });

    }


    public void getMenuData(){
        String restauntId = Helper.read(Constants.RESTAURANT_ID);
        Call<ResponseAdapter<MenuData>> responseCall = RetrofitClient.getInstance().getAppService()
                .getMenuByRestaurantId(restauntId);
        syncStatus.setText("... Đang tải menu ...");
        responseCall.enqueue(new Callback<ResponseAdapter<MenuData>>() {
            @Override
            public void onResponse(Call<ResponseAdapter<MenuData>> call, Response<ResponseAdapter<MenuData>> response) {
                switch (response.code()) {
                    case Constants.STATUS_CODE_SUCCESS:
                        ResponseAdapter<MenuData> res = response.body();
                        if (res.getStatus() == Constants.STATUS_CODE_SUCCESS) {
                            saveMenuToLocal(res.getData().getMenuGroups());
                            Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                        break;
                    default:
                        syncStatus.setText("Lỗi: Đồng bộ thất bại");
                        syncStatus.setTextColor(Color.RED);
                        TryAgain.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Call<ResponseAdapter<MenuData>> call, Throwable t) {
                syncStatus.setText("Lỗi: Đồng bộ thất bại");
                syncStatus.setTextColor(Color.RED);
                TryAgain.setVisibility(View.VISIBLE);
            }
        });
    }

    private void saveMenuToLocal(List<MenuGroup> menuGroups){
        DatabaseManager.initializeInstance(new DatabaseHelper(this));
        SQLiteDatabase db =  DatabaseManager.getInstance().openDatabase();
        MenuItemDataSource menuItemDS = new MenuItemDataSource(db);
        MenuGroupDataSource menuGroupDS = new MenuGroupDataSource(db);

        menuItemDS.truncate();
        menuGroupDS.truncate();

        for (MenuGroup menuGroup: menuGroups) {
            menuGroupDS.insert(menuGroup);
            for (MenuItem menuItem : menuGroup.getMenuItems()) {
                menuItem.setGroupId(menuGroup.getId());
                menuItemDS.insert(menuItem);
            }
        }


    }

}
