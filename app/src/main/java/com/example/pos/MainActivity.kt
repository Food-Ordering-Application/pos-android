package com.example.pos

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.pos.ui.home.HomeFragment
import com.example.pos.ui.menu.MenuFragment
import com.example.pos.ui.order.OrderFragment
import com.roughike.bottombar.BottomBar


//
//import android.os.Bundle
//import com.google.android.material.bottomnavigation.BottomNavigationView
//import androidx.appcompat.app.AppCompatActivity
//import androidx.fragment.app.commit
//import androidx.navigation.findNavController
//import androidx.navigation.ui.AppBarConfiguration
//import androidx.navigation.ui.setupActionBarWithNavController
//import androidx.navigation.ui.setupWithNavController
//import com.example.pos.ui.dashboard.DashboardFragment
//import com.google.android.material.navigation.NavigationView
//import com.google.android.material.navigationrail.NavigationRailView
//
//class MainActivity : AppCompatActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        val navView: NavigationRailView = findViewById(R.id.navigation_rail)
////
////        val navController = findNavController(R.id.nav_host_fragment)
////         //Passing each menu ID as a set of Ids because each
////         //menu should be considered as top level destinations.
////        val appBarConfiguration = AppBarConfiguration(setOf(
////                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications))
////        setupActionBarWithNavController(navController, appBarConfiguration)
////        navView.setupWithNavController(navController)
//        navView.setOnNavigationItemReselectedListener { item->
//            when(item.itemId){
//                R.id.navigation_home ->{
//                    true
//                }
//                R.id.navigation_dashboard->{
//                    supportFragmentManager.commit{
//                        setReorderingAllowed(true)
//                        add<DashboardFragment>(R.id.nav_host_fragment)
//                    }
//                }
//                R.id.navigation_notifications ->{
//                    true
//                }
//                else -> false
//            }
//        }
//    }
//}
class MainActivity : AppCompatActivity() {
    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomBar = findViewById<View>(R.id.bottomBar) as BottomBar
        bottomBar.selectTabAtPosition(0)
        bottomBar.setOnTabSelectListener { tabId ->
            when(tabId){
                R.id.tab_home ->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.contentContainer, HomeFragment())
                        .commit()
                }
                R.id.tab_menu ->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.contentContainer, MenuFragment())
                        .commit()
                }
                R.id.tab_order ->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.contentContainer, OrderFragment())
                        .commit()
                }
                else -> supportFragmentManager.beginTransaction()
                    .replace(R.id.contentContainer, HomeFragment())
                    .commit()
            }

        }
    }
}