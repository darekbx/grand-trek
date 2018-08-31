package com.grandtrek.ui

import android.content.Intent
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import com.grandtrek.R
import com.grandtrek.ui.routes.RoutesActivity
import com.grandtrek.ui.statistics.StatisticsActivity
import com.grandtrek.ui.trip.TripActivity

open class BaseActivity : AppCompatActivity() {

    open fun handleNavigation(bottomNavigationView: BottomNavigationView) {
        bottomNavigationView.setOnNavigationItemSelectedListener {
            val clazz = when (it.itemId) {
                R.id.action_trip -> TripActivity::class.java
                R.id.action_routes -> RoutesActivity::class.java
                R.id.action_statistics -> StatisticsActivity::class.java
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
            this is RoutesActivity -> R.id.action_routes
            this is StatisticsActivity -> R.id.action_statistics
            else -> R.id.action_trip
        }
    }
}