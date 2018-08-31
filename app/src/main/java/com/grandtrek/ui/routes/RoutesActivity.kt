package com.grandtrek.ui.routes

import android.os.Bundle
import com.grandtrek.R
import com.grandtrek.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_routes.*

class RoutesActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_routes)
        handleNavigation(navigation)
    }
}