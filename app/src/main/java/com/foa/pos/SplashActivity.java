package com.foa.pos;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
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

        getMenuData();

        TryAgain.setOnClickListener(v -> getMenuData());
    }


    public void getMenuData(){
        Helper.initialize(this);
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
                            startActivity(new Intent(SplashActivity.this, LoginActivity.class));

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

        for (MenuGroup menuGroup: menuGroups) {
            menuGroupDS.insert(menuGroup);
            for (MenuItem menuItem : menuGroup.getMenuItems()) {
                menuItem.setGroupId(menuGroup.getId());
                menuItemDS.insert(menuItem);
            }
        }


    }

}
