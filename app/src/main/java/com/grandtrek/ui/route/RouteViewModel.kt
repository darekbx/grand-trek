package com.grandtrek.ui.route

import android.arch.lifecycle.ViewModel
import com.grandtrek.data.Repository
import javax.inject.Inject

class RouteViewModel @Inject constructor(
        private val repository: Repository
): ViewModel() {

    fun fetchRoute(routeId: Long) = repository.route(routeId)
    fun fetchRoutePoints(routeId: Long) = repository.routePoints(routeId)
}