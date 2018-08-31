package com.grandtrek.ui

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.grandtrek.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        handleNavigation()

        pager.adapter = pageAdapter
        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {}

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}

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
            when (it.itemId) {
                R.id.action_trip -> pager.currentItem = 0
                R.id.action_routes -> pager.currentItem = 1
                R.id.action_statistics -> pager.currentItem = 2
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    private val pageAdapter by lazy { PagerAdpater(supportFragmentManager) }
}