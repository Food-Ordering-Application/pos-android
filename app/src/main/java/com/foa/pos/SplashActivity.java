package com.foa.pos;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.foa.pos.model.IDataResultCallback;
import com.foa.pos.model.Menu;
import com.foa.pos.model.MenuGroup;
import com.foa.pos.model.MenuItem;
import com.foa.pos.model.MenuItemTopping;
import com.foa.pos.model.ToppingGroup;
import com.foa.pos.model.ToppingItem;
import com.foa.pos.network.response.MenuData;
import com.foa.pos.service.RestaurantService;
import com.foa.pos.sqlite.DatabaseHelper;
import com.foa.pos.sqlite.DatabaseManager;
import com.foa.pos.sqlite.ds.MenuGroupDataSource;
import com.foa.pos.sqlite.ds.MenuItemDataSource;
import com.foa.pos.sqlite.ds.MenuItemToppingDataSource;
import com.foa.pos.sqlite.ds.ToppingGroupDataSource;
import com.foa.pos.sqlite.ds.ToppingItemDataSource;
import com.foa.pos.utils.Constants;
import com.foa.pos.utils.Helper;
import com.foa.pos.utils.LoggerHelper;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SplashActivity extends AppCompatActivity {

    private static final int NTHREDS = 5;


    TextView syncStatus;
    Button TryAgain;
    RestaurantService restaurantService = new RestaurantService();
    SQLiteDatabase db;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        syncStatus = findViewById(R.id.syncStatusTextView);
        TryAgain = findViewById(R.id.TryAgainButton);

        restaurantService = new RestaurantService();

        db =  DatabaseManager.getInstance().openDatabase();

        Helper.initialize(this);
        Helper.write(Constants.MERCHANT_ID, "20c2c064-2457-4525-b7e4-7c2c10564e86");
        Helper.write(Constants.RESTAURANT_ID, "75d1fd95-9699-4f21-85e6-480def4d8bbb");

        getMenuData();

        TryAgain.setOnClickListener(v -> {
            TryAgain.setEnabled(false);
            getMenuData();
        });

    }

    private void getMenuData(){
        restaurantService.getMenuId(new IDataResultCallback<MenuData>() {
            @Override
            public void onSuccess(boolean success, MenuData Data) {
                getMenuAndToppingData(Data.getMenuId());
                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

            @Override
            public void onError() {

            }
        });
    }

    private void getMenuAndToppingData(String menuId){
        ExecutorService executor = Executors.newFixedThreadPool(NTHREDS);
        Runnable getMenuGroups = () -> getMenuGroups(menuId);
        Runnable getMenuItems = () -> getMenuItems(menuId);
        Runnable getToppingGroups = () -> getToppingGroups(menuId);
        Runnable getToppingItems = () -> getToppingItems(menuId);
        Runnable getMenuItemToppings = () -> getMenuItemToppings(menuId);

        executor.execute(getMenuGroups);
        executor.execute(getMenuItems);
        executor.execute(getToppingGroups);
        executor.execute(getToppingItems);
        executor.execute(getMenuItemToppings);
        executor.shutdown();

        LoggerHelper.CheckAndLogInfo(this,"Finished all threads");

    }

    private void getMenuGroups(String menuId){
        restaurantService.getMenuGroups(menuId, new IDataResultCallback<List<MenuGroup>>() {
            @Override
            public void onSuccess(boolean success, List<MenuGroup> Data) {
                saveMenuGroupToLocal(Data);
            }

            @Override
            public void onError() {

            }
        });
    }
    private void getMenuItems(String menuId){
        restaurantService.getMenuItems(menuId, new IDataResultCallback<List<MenuItem>>() {
            @Override
            public void onSuccess(boolean success, List<MenuItem> Data) {
                saveMenuItemToLocal(Data);
            }

            @Override
            public void onError() {

            }
        });
    }

    private void getToppingItems(String menuId){
        restaurantService.getToppingItems(menuId, new IDataResultCallback<List<ToppingItem>>() {
            @Override
            public void onSuccess(boolean success, List<ToppingItem> Data) {
                saveToppingItemToLocal(Data);
            }

            @Override
            public void onError() {

            }
        });
    }
    private void getToppingGroups(String menuId){
        restaurantService.getToppingGroups(menuId, new IDataResultCallback<List<ToppingGroup>>() {
            @Override
            public void onSuccess(boolean success, List<ToppingGroup> Data) {
                saveToppingGroupToLocal(Data);
            }

            @Override
            public void onError() {

            }
        });
    }
    private void getMenuItemToppings(String menuId){
        restaurantService.getMenuItemTopping(menuId, new IDataResultCallback<List<MenuItemTopping>>() {
            @Override
            public void onSuccess(boolean success, List<MenuItemTopping> Data) {
                saveMenuItemToppingToLocal(Data);
            }

            @Override
            public void onError() {

            }
        });
    }

    private void saveMenuItemToLocal(List<MenuItem> menuItems){
        MenuItemDataSource menuItemDS = new MenuItemDataSource(db);
        menuItemDS.truncate();
        for (MenuItem menuItem : menuItems) {
            menuItemDS.insert(menuItem);
        }
    }
    private void saveMenuGroupToLocal(List<MenuGroup> menuGroups){
        MenuGroupDataSource menuItemDS = new MenuGroupDataSource(db);
        menuItemDS.truncate();
        for (MenuGroup menuGroup : menuGroups) {
            menuItemDS.insert(menuGroup);
        }
    }

    private void saveToppingGroupToLocal(List<ToppingGroup> menuGroups){
        ToppingGroupDataSource toppingGroupDS = new ToppingGroupDataSource(db);
        toppingGroupDS.truncate();
        for (ToppingGroup menuGroup : menuGroups) {
            toppingGroupDS.insert(menuGroup);
        }
    }

    private void saveToppingItemToLocal(List<ToppingItem> toppingItems){
        ToppingItemDataSource toppingItemDS = new ToppingItemDataSource(db);
        toppingItemDS.truncate();
        for (ToppingItem menuGroup : toppingItems) {
            toppingItemDS.insert(menuGroup);
        }
    }

    private void saveMenuItemToppingToLocal(List<MenuItemTopping> menuItemToppings){
        MenuItemToppingDataSource menuItemToppingDS = new MenuItemToppingDataSource(db);
        menuItemToppingDS.truncate();
        for (MenuItemTopping menuItemTopping : menuItemToppings) {
            menuItemToppingDS.insert(menuItemTopping);
        }
    }




//    public void getMenuData(){
//        String restauntId = Helper.read(Constants.RESTAURANT_ID);
//        Call<ResponseAdapter<MenuGroupData>> responseCall = RetrofitClient.getInstance().getAppService()
//                .getMenuByRestaurantId(restauntId);
//        syncStatus.setText("... Đang tải menu ...");
//        responseCall.enqueue(new Callback<ResponseAdapter<MenuGroupData>>() {
//            @Override
//            public void onResponse(Call<ResponseAdapter<MenuGroupData>> call, Response<ResponseAdapter<MenuGroupData>> response) {
//                switch (response.code()) {
//                    case Constants.STATUS_CODE_SUCCESS:
//                        ResponseAdapter<MenuGroupData> res = response.body();
//                        if (res.getStatus() == Constants.STATUS_CODE_SUCCESS) {
//                            saveMenuToLocal(res.getData().getMenuGroups());
//                            Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                            finish();
//                        }
//                        break;
//                    default:
//                        syncStatus.setText("Lỗi: Đồng bộ thất bại");
//                        syncStatus.setTextColor(Color.RED);
//                        TryAgain.setVisibility(View.VISIBLE);
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<ResponseAdapter<MenuGroupData>> call, Throwable t) {
//                syncStatus.setText("Lỗi: Đồng bộ thất bại");
//                syncStatus.setTextColor(Color.RED);
//                TryAgain.setVisibility(View.VISIBLE);
//            }
//        });
//    }
//
//    public void getToppingData(){
//        String restauntId = Helper.read(Constants.RESTAURANT_ID);
//        Call<ResponseAdapter<MenuGroupData>> responseCall = RetrofitClient.getInstance().getAppService()
//                .getMenuByRestaurantId(restauntId);
//        syncStatus.setText("... Đang tải menu ...");
//        responseCall.enqueue(new Callback<ResponseAdapter<MenuGroupData>>() {
//            @Override
//            public void onResponse(Call<ResponseAdapter<MenuGroupData>> call, Response<ResponseAdapter<MenuGroupData>> response) {
//                switch (response.code()) {
//                    case Constants.STATUS_CODE_SUCCESS:
//                        ResponseAdapter<MenuGroupData> res = response.body();
//                        if (res.getStatus() == Constants.STATUS_CODE_SUCCESS) {
//                            saveMenuToLocal(res.getData().getMenuGroups());
//                            Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                        }
//                        break;
//                    default:
//                        syncStatus.setText("Lỗi: Đồng bộ thất bại");
//                        syncStatus.setTextColor(Color.RED);
//                        TryAgain.setVisibility(View.VISIBLE);
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<ResponseAdapter<MenuGroupData>> call, Throwable t) {
//                syncStatus.setText("Lỗi: Đồng bộ thất bại");
//                syncStatus.setTextColor(Color.RED);
//                TryAgain.setVisibility(View.VISIBLE);
//            }
//        });
//    }

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
