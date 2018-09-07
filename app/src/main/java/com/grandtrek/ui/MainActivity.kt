package com.grandtrek.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.grandtrek.R
import com.grandtrek.ui.routes.RoutesFragment
import com.grandtrek.ui.trip.TripFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        handleNavigation()

        navigation.selectedItemId = R.id.action_trip
    }
    fun handleNavigation() {
        navigation.setOnNavigationItemSelectedListener {
            setFragment(when {
                it.itemId == R.id.action_trip -> TripFragment()
                it.itemId == R.id.action_routes && checkIfCanChange() -> RoutesFragment()
                else -> null
            })
            return@setOnNavigationItemSelectedListener true
        }
    }

    private fun setFragment(fragment: Fragment?) {
        fragment?.let { fragment ->
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, fragment, fragment::class.java.simpleName)
                    .commit()
        }
    }

    private fun checkIfCanChange(): Boolean {
        val currentFragment = supportFragmentManager.findFragmentByTag(TripFragment::class.java.simpleName)
        return when (currentFragment) {
            is TripFragment -> {
                val isRecording = currentFragment.isRecording().also { isRecording ->
                    if (isRecording) {
                        Toast.makeText(this, R.string.stop_recording, Toast.LENGTH_SHORT).show()
                    }
                }
                return !isRecording
            }
            else -> false
        }
        return true
    }
}