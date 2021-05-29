package com.foa.smartpos;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.foa.smartpos.model.MenuGroup;
import com.foa.smartpos.model.MenuItem;
import com.foa.smartpos.model.MenuItemTopping;
import com.foa.smartpos.model.ToppingGroup;
import com.foa.smartpos.model.ToppingItem;
import com.foa.smartpos.service.RestaurantService;
import com.foa.smartpos.sqlite.DatabaseHelper;
import com.foa.smartpos.sqlite.DatabaseManager;
import com.foa.smartpos.sqlite.ds.MenuGroupDataSource;
import com.foa.smartpos.sqlite.ds.MenuItemDataSource;
import com.foa.smartpos.sqlite.ds.MenuItemToppingDataSource;
import com.foa.smartpos.sqlite.ds.ToppingGroupDataSource;
import com.foa.smartpos.sqlite.ds.ToppingItemDataSource;
import com.foa.smartpos.utils.Constants;
import com.foa.smartpos.utils.Helper;
import com.foa.smartpos.utils.LoggerHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SplashActivity extends AppCompatActivity {

    private static final int NTHREDS = 5;


    TextView syncStatus;
    Button btnTryAgain;
    RestaurantService restaurantService = new RestaurantService();
    SQLiteDatabase db;

    String menuId;
    ExecutorService executor;
    Runnable getMenuGroups;
    Runnable getMenuItems;
    Runnable getToppingGroups;
    Runnable getToppingItems;
    Runnable getMenuItemToppings;

    Boolean isMenuItemSuccess = null;
    Boolean isMenuGroupSuccess = null;
    Boolean isToppingItemSuccess = null;
    Boolean isToppingGroupSuccess = null;
    Boolean isMenuItemToppingSuccess = null;

    ProgressBar loadingProgress;
    TextView statusMessage;
    List<MenuItem> menuItems = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        btnTryAgain = findViewById(R.id.TryAgainButton);
        loadingProgress = findViewById(R.id.loadingProgress);
        statusMessage = findViewById(R.id.statusMessage);

        restaurantService = new RestaurantService();
        DatabaseManager.initializeInstance(new DatabaseHelper(this));
        db =  DatabaseManager.getInstance().openDatabase();

        Helper.initialize(this);
        Helper.write(Constants.MERCHANT_ID, "20c2c064-2457-4525-b7e4-7c2c10564e86");
        Helper.write(Constants.RESTAURANT_ID, "6587f789-8c76-4a2e-9924-c14fc30629ef");

//        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);

        getMenuData();

        btnTryAgain.setOnClickListener(v -> {
           loadingView();
               executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
               if (isMenuItemSuccess==null||!isMenuItemSuccess){
                    getMenuGroups = () -> getMenuGroups(menuId);
                   executor.execute(getMenuGroups);
               }
               if (isMenuItemSuccess==null|| !isMenuGroupSuccess){
                    getMenuItems = () -> getMenuItems(menuId);
                   executor.execute(getMenuItems);
               }

               if (isMenuItemSuccess==null || !isToppingGroupSuccess){
                    getToppingGroups = () -> getToppingGroups(menuId);
                   executor.execute(getToppingGroups);
               }

               if(isMenuItemSuccess==null || !isToppingItemSuccess){
                    getToppingItems = () -> getToppingItems(menuId);
                   executor.execute(getToppingItems);
               }

               if (isMenuItemSuccess==null || !isMenuItemToppingSuccess){
                    getMenuItemToppings = () -> getMenuItemToppings(menuId);
                    executor.execute(getMenuItemToppings);
               }
               executor.shutdown();
            try {
                executor.awaitTermination(200, TimeUnit.MICROSECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

    }

    private void getMenuData(){
        restaurantService.getMenuId((success, data) -> {
            if (success){
                menuId = data.getMenuId();
                getMenuAndToppingData(data.getMenuId());
            }else {
                loadedView();
            }
        });
    }

    private void checkSyncStatus(){
        if (isMenuItemSuccess==null || isMenuGroupSuccess==null || isToppingGroupSuccess==null
                || isToppingItemSuccess==null || isMenuItemToppingSuccess==null) return;
        if (isMenuItemSuccess && isMenuGroupSuccess && isToppingGroupSuccess
                && isToppingItemSuccess && isMenuItemToppingSuccess ) {

//            executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
//            Runnable downloadMenuItemsImage = () -> menuItems.forEach(item->{
//                Helper.downloadImageAndSave(item.getImage(),item.getId());
//            });
//            executor.execute(downloadMenuItemsImage);

            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }else {
            loadedView();
        }
    }

    private void getMenuAndToppingData(String menuId){
        executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
         getMenuGroups = () -> getMenuGroups(menuId);
         getMenuItems = () -> getMenuItems(menuId);
         getToppingGroups = () -> getToppingGroups(menuId);
         getToppingItems = () -> getToppingItems(menuId);
         getMenuItemToppings = () -> getMenuItemToppings(menuId);

        executor.execute(getMenuGroups);
        executor.execute(getMenuItems);
        executor.execute(getToppingGroups);
        executor.execute(getToppingItems);
        executor.execute(getMenuItemToppings);

        try {
            executor.awaitTermination(200, TimeUnit.MICROSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        LoggerHelper.CheckAndLogInfo(this,"Finished all threads");
    }

    private void loadingView(){
        btnTryAgain.setVisibility(View.GONE);
        statusMessage.setVisibility(View.GONE);
        loadingProgress.setVisibility(View.VISIBLE);
    }

    private void loadedView(){
        statusMessage.setVisibility(View.VISIBLE);
        loadingProgress.setVisibility(View.GONE);
        btnTryAgain.setVisibility(View.VISIBLE);
    }

    private void getMenuGroups(String menuId){
        restaurantService.getMenuGroups(menuId, (success, data) -> {
            if (success) {
                isMenuGroupSuccess = true;
                saveMenuGroupToLocal(data);
                checkSyncStatus();
            }
        });
    }
    private void getMenuItems(String menuId){
        restaurantService.getMenuItems(menuId, (success, data) -> {
            if (success){
                isMenuItemSuccess = true;
                menuItems = data;
                saveMenuItemToLocal(data);
                checkSyncStatus();
            }else {
                loadedView();
            }

        });
    }

    private void getToppingItems(String menuId){
        restaurantService.getToppingItems(menuId, (success, data) -> {
            if (success) {
                isToppingItemSuccess = true;
                saveToppingItemToLocal(data);
                checkSyncStatus();
            }else {
                loadedView();
            }
        });
    }
    private void getToppingGroups(String menuId){
        restaurantService.getToppingGroups(menuId, (success, data) -> {
            if (success) {
                isToppingGroupSuccess = true;
                saveToppingGroupToLocal(data);
                checkSyncStatus();
            }else {
                loadedView();
            }
        });
    }
    private void getMenuItemToppings(String menuId){
        restaurantService.getMenuItemTopping(menuId, (success, data) -> {
            if (success) {
                isMenuItemToppingSuccess = true;
                saveMenuItemToppingToLocal(data);
                checkSyncStatus();
            }else {
                loadedView();
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
