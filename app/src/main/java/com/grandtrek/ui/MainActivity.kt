package com.grandtrek.ui

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.grandtrek.R
import com.grandtrek.ui.trip.TripFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        handleNavigation()

        pager.adapter = pageAdapter
        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                selectActiveMenuItem(position)
            }
        })
    }

    fun selectActiveMenuItem(position: Int) {
        with(navigation.menu) {
            (0..size()).forEach { getItem(position).isChecked = false }
            getItem(position).isChecked = true
        }
    }

    fun handleNavigation() {
        navigation.setOnNavigationItemSelectedListener {
            when {
                it.itemId == R.id.action_trip -> pager.currentItem = 0
                it.itemId == R.id.action_routes && checkIfCanChange() -> pager.currentItem = 1
                it.itemId == R.id.action_statistics && checkIfCanChange() -> pager.currentItem = 2
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    private fun checkIfCanChange(): Boolean {
        val currentFragment = pageAdapter.instantiateItem(pager, pager.currentItem)
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
    }

    private val pageAdapter by lazy { PagerAdpater(supportFragmentManager) }
}