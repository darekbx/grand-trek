package com.grandtrek.data

import android.arch.lifecycle.Transformations
import android.location.Location
import com.grandtrek.data.local.AppDatabase
import com.grandtrek.data.local.entities.PointEntity
import com.grandtrek.data.local.entities.RouteEntity
import com.grandtrek.data.model.Point
import com.grandtrek.data.model.Route
import javax.inject.Inject

class Repository @Inject constructor(
        private val database: AppDatabase
) {

    fun route(routeId: Long) = Transformations.map(dao.fetchRoute(routeId), { routeEntityToModel(it) })

    fun routePoints(routeId: Long) = Transformations.map(dao.fetchRoutePoints(routeId), { list ->
        list.map { pointEntityToModel(it) }
    })

    fun routes() = Transformations.map(dao.fetchRoutes(), { list ->
        list.map { routeEntityToModel(it) }
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
            dao.updateRoute(id, name, distance, averageSpeed, maximumSpeed, color, date, tripTime, rideTime)
        }
    }

    fun deleteRoute(routeId: Long) {
        dao.deletePoints(routeId)
        dao.deleteRoute(routeId)
    }

    private fun routeEntityToModel(routeEntity: RouteEntity): Route {
        return routeEntity.let {
            return Route(it.id, it.name, it.distance, it.averageSpeed,
                    it.maximumSpeed, it.tripTime, it.rideTime, it.date, it.color)
        }
    }

    private fun pointEntityToModel(pointEntity: PointEntity): Point {
        return pointEntity.let {
            return Point(it.id, it.routeId, it.latitude, it.longitude, it.altitude, it.speed)
        }
    }

    private val dao by lazy { database.getDao() }
}