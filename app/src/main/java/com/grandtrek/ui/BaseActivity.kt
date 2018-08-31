package com.grandtrek.ui

import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {

    open fun handleNavigation(bottomNavigationView: BottomNavigationView) {
     /*   bottomNavigationView.setOnNavigationItemSelectedListener {
            val clazz = when (it.itemId) {
                R.id.action_trip -> TripActivity::class.java
                R.id.action_routes -> RoutesFragment::class.java
                R.id.action_statistics -> StatisticsFragment::class.java
                else -> TripActivity::class.java
            }
            if (this@BaseActivity::class.java != clazz) {
                val intent = Intent(this, clazz)
                        .apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
                startActivity(intent)
            }
            return@setOnNavigationItemSelectedListener true
        }

        bottomNavigationView.selectedItemId = when  {
            this is TripActivity -> R.id.action_trip
            this is RoutesFragment -> R.id.action_routes
            this is StatisticsFragment -> R.id.action_statistics
            else -> R.id.action_trip
        }*/
    }
}