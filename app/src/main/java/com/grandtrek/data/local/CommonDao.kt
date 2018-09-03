package com.grandtrek.data.local

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.grandtrek.data.local.entities.PointEntity
import com.grandtrek.data.local.entities.RouteEntity

@Dao
interface CommonDao {

    @Insert
    fun addRote(routeEntity: RouteEntity): Long

    @Insert
    fun addPoint(pointEntity: PointEntity)

    @Query("SELECT * FROM route WHERE id = :routeId")
    fun fetchRoute(routeId: Long): LiveData<RouteEntity>

    @Query("SELECT * FROM route")
    fun fetchRoutes(): LiveData<List<RouteEntity>>

    @Query("SELECT * FROM point WHERE route_id = :routeId")
    fun fetchRoutePoints(routeId: Long): LiveData<List<PointEntity>>

    @Query("""UPDATE route
        SET
        name = :name, distance = :distance, average_spped = :averageSpeed,
        maximum_spped = :maximumSpeed, triptime = :tripTime, rideTime = :rideTime,
        date = :date, color = :color
        WHERE id = :routeId""")
    fun updateRoute(routeId: Long?, name: String?, distance: Float, averageSpeed: Float,
                    maximumSpeed: Float, color: Int, date: Long, tripTime: Long, rideTime: Long)

    @Query("DELETE FROM point WHERE route_id = :routeId")
    fun deletePoints(routeId: Long)

    @Query("DELETE FROM route WHERE id = :routeId")
    fun deleteRoute(routeId: Long)
}