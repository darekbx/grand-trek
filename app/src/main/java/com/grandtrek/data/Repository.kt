package com.grandtrek.data

import android.arch.lifecycle.Transformations
import android.location.Location
import com.grandtrek.data.local.AppDatabase
import com.grandtrek.data.local.entities.PointEntity
import com.grandtrek.data.local.entities.RouteEntity
import com.grandtrek.data.model.Route
import javax.inject.Inject

class Repository @Inject constructor(
        private val database: AppDatabase
) {

    fun routes() = Transformations.map(dao.fetchRoutes(), { list ->
        list.map {
            Route(it.id, it.name, it.distance, it.averageSpeed,
                    it.maximumSpeed, it.tripTime, it.date, it.color)
        }
    })

    fun createEmptyRoute(): Long {
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
        with(routeEntity) {
            dao.updateRoute(id, name, distance, averageSpeed, maximumSpeed, color, date, tripTime)
        }
    }

    fun deleteRoute(routeId: Long) {
        dao.deletePoints(routeId)
        dao.deleteRoute(routeId)
    }

    private val dao by lazy { database.getDao() }
}