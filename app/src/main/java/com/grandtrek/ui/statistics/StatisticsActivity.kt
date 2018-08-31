package com.grandtrek.ui.statistics

import android.os.Bundle
import com.grandtrek.R
import com.grandtrek.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_routes.*

class StatisticsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)
        handleNavigation(navigation)
    }
}