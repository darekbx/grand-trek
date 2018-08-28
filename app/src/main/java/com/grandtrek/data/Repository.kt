package com.grandtrek.data

import android.location.Location
import com.grandtrek.data.local.AppDatabase
import com.grandtrek.data.local.entities.PointEntity
import com.grandtrek.data.local.entities.RouteEntity
import javax.inject.Inject

class Repository @Inject constructor(
        private val database: AppDatabase
) {

    fun createEmptyRoute() : Long {
        val route = RouteEntity()
        val addedRouteId = dao.addRote(route)
        return addedRouteId
    }

    fun addPointToRoute(location: Location, routeId: Long) {
        with(location) {
            val point = PointEntity(null, routeId, latitude, longitude, altitude, speed)
            dao.addPoint(point)
        }
    }

    fun updateRoute(routeEntity: RouteEntity) {
        dao.updateRoute(routeEntity)
    }

    fun deleteRoute(routeId: Long) {
        dao.deletePoints(routeId)
        dao.deleteRoute(routeId)
    }

    private val dao by lazy { database.getDao() }
}