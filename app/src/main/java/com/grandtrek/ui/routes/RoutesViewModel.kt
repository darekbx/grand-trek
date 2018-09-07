package com.grandtrek.ui.routes

import android.arch.lifecycle.ViewModel
import com.grandtrek.data.Repository
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import javax.inject.Inject

class RoutesViewModel @Inject constructor(
        private val repository: Repository
): ViewModel() {

    fun fetchRoutes() = repository.routes()

    fun deleteRoute(routeId: Long) {
        repository.deleteRoute(routeId)
    }
}