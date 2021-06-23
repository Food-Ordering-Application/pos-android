package com.foa.smartpos;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.foa.smartpos.model.MenuGroup;
import com.foa.smartpos.model.MenuItem;
import com.foa.smartpos.model.MenuItemTopping;
import com.foa.smartpos.model.ToppingGroup;
import com.foa.smartpos.model.ToppingItem;
import com.foa.smartpos.api.RestaurantService;
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

    Boolean isMenuSuccess = null;
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
        db = DatabaseManager.getInstance().openDatabase();

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
            if (isMenuSuccess==null || !isMenuSuccess) {
                getMenuData();
            } else {

                if (isMenuGroupSuccess == null||!isMenuGroupSuccess) {
                    getMenuGroups = () -> getMenuGroups(menuId);
                    executor.execute(getMenuGroups);
                }
                if (isMenuItemSuccess == null||!isMenuItemSuccess) {
                    getMenuItems = () -> getMenuItems(menuId);
                    executor.execute(getMenuItems);
                }

                if (isToppingGroupSuccess == null||!isToppingGroupSuccess) {
                    getToppingGroups = () -> getToppingGroups(menuId);
                    executor.execute(getToppingGroups);
                }

                if (isToppingGroupSuccess == null|| !isToppingGroupSuccess) {
                    getToppingItems = () -> getToppingItems(menuId);
                    executor.execute(getToppingItems);
                }

                if (isMenuItemToppingSuccess!=null||!isMenuItemToppingSuccess) {
                    getMenuItemToppings = () -> getMenuItemToppings(menuId);
                    executor.execute(getMenuItemToppings);
                }
            }

            executor.shutdown();
            try {
                executor.awaitTermination(200, TimeUnit.MICROSECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

    }

    private void getMenuData() {
        restaurantService.getMenuId((success, data) -> {
            if (success) {
                menuId = data.getMenuId();
                getMenuAndToppingData(data.getMenuId());
            } else {
                loadedView();
            }
        });
    }

    private void checkSyncStatus() {
        if (isMenuItemSuccess == null || isMenuGroupSuccess == null || isToppingGroupSuccess == null
                || isToppingItemSuccess == null || isMenuItemToppingSuccess == null) return;
        if (isMenuItemSuccess && isMenuGroupSuccess && isToppingGroupSuccess
                && isToppingItemSuccess && isMenuItemToppingSuccess) {

//            executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
//            Runnable downloadMenuItemsImage = () -> menuItems.forEach(item->{
//                Helper.downloadImageAndSave(item.getImage(),item.getId());
//            });
//            executor.execute(downloadMenuItemsImage);

            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            loadedView();
        }
    }

    private void getMenuAndToppingData(String menuId) {
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

        LoggerHelper.CheckAndLogInfo(this, "Finished all threads");
    }

    private void loadingView() {
        btnTryAgain.setVisibility(View.GONE);
        statusMessage.setVisibility(View.GONE);
        loadingProgress.setVisibility(View.VISIBLE);
    }

    private void loadedView() {
        statusMessage.setVisibility(View.VISIBLE);
        loadingProgress.setVisibility(View.GONE);
        btnTryAgain.setVisibility(View.VISIBLE);
    }

    private void getMenuGroups(String menuId) {
        restaurantService.getMenuGroups(menuId, (success, data) -> {
            if (success) {
                isMenuGroupSuccess = true;
                saveMenuGroupToLocal(data);
                checkSyncStatus();
            }else {
                isMenuGroupSuccess=false;
            }
        });
    }

    private void getMenuItems(String menuId) {
        restaurantService.getMenuItems(menuId, (success, data) -> {
            if (success) {
                isMenuItemSuccess = true;
                menuItems = data;
                saveMenuItemToLocal(data);
                checkSyncStatus();
            } else {
                isMenuItemSuccess=false;
                loadedView();
            }

        });
    }

    private void getToppingItems(String menuId) {
        restaurantService.getToppingItems(menuId, (success, data) -> {
            if (success) {
                isToppingItemSuccess = true;
                saveToppingItemToLocal(data);
                checkSyncStatus();
            } else {
                isToppingItemSuccess=false;
                loadedView();
            }
        });
    }

    private void getToppingGroups(String menuId) {
        restaurantService.getToppingGroups(menuId, (success, data) -> {
            if (success) {
                isToppingGroupSuccess = true;
                saveToppingGroupToLocal(data);
                checkSyncStatus();
            } else {
                isToppingGroupSuccess=false;
                loadedView();
            }
        });
    }

    private void getMenuItemToppings(String menuId) {
        restaurantService.getMenuItemTopping(menuId, (success, data) -> {
            if (success) {
                isMenuItemToppingSuccess = true;
                saveMenuItemToppingToLocal(data);
                checkSyncStatus();
            } else {
                isMenuItemToppingSuccess=false;
                loadedView();
            }
        });
    }

    private void saveMenuItemToLocal(List<MenuItem> menuItems) {
        if (menuItems == null) {
            isMenuItemSuccess = false;
            return;
        }
        MenuItemDataSource menuItemDS = new MenuItemDataSource(db);
        menuItemDS.truncate();
        for (MenuItem menuItem : menuItems) {
            menuItemDS.insert(menuItem);
        }
    }

    private void saveMenuGroupToLocal(List<MenuGroup> menuGroups) {
        if (menuGroups == null) {
            isMenuGroupSuccess = false;
            return;
        }
        MenuGroupDataSource menuItemDS = new MenuGroupDataSource(db);
        menuItemDS.truncate();
        for (MenuGroup menuGroup : menuGroups) {
            menuItemDS.insert(menuGroup);
        }
    }

    private void saveToppingGroupToLocal(List<ToppingGroup> menuGroups) {
        if (menuGroups == null) {
            isToppingGroupSuccess = false;
            return;
        }
        ToppingGroupDataSource toppingGroupDS = new ToppingGroupDataSource(db);
        toppingGroupDS.truncate();
        for (ToppingGroup menuGroup : menuGroups) {
            toppingGroupDS.insert(menuGroup);
        }
    }

    private void saveToppingItemToLocal(List<ToppingItem> toppingItems) {
        if (toppingItems == null) {
            isToppingItemSuccess = false;
            return;
        }
        ToppingItemDataSource toppingItemDS = new ToppingItemDataSource(db);
        toppingItemDS.truncate();
        for (ToppingItem menuGroup : toppingItems) {
            toppingItemDS.insert(menuGroup);
        }
    }

    private void saveMenuItemToppingToLocal(List<MenuItemTopping> menuItemToppings) {
        if (menuItemToppings == null) {
            isMenuItemToppingSuccess = false;
            return;
        }
        MenuItemToppingDataSource menuItemToppingDS = new MenuItemToppingDataSource(db);
        menuItemToppingDS.truncate();
        for (MenuItemTopping menuItemTopping : menuItemToppings) {
            menuItemToppingDS.insert(menuItemTopping);
        }
    }
}
