package com.grandtrek.ui

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.grandtrek.ui.routes.RoutesFragment
import com.grandtrek.ui.statistics.StatisticsFragment
import com.grandtrek.ui.trip.TripFragment

class PagerAdpater(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int) = when (position) {
        0 -> TripFragment()
        1 -> RoutesFragment()
        2 -> StatisticsFragment()
        else -> null
    }

    override fun getCount() = 3
}
