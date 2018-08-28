package com.grandtrek.data.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import com.grandtrek.data.local.entities.PointEntity
import com.grandtrek.data.local.entities.RouteEntity

@Dao
interface CommonDao {

    @Insert
    fun addRote(routeEntity: RouteEntity): Long

    @Insert
    fun addPoint(pointEntity: PointEntity)

    @Query("SELECT * FROM route")
    fun fetchRoutes(): List<RouteEntity>

    @Query("SELECT * FROM point WHERE route_id = :routeId")
    fun fetchRoutePoints(routeId: Long): List<PointEntity>

    @Update
    fun updateRoute(routeEntity: RouteEntity)

    @Query("DELETE FROM point WHERE route_id = :routeId")
    fun deletePoints(routeId: Long)

    @Query("DELETE FROM route WHERE id = :routeId")
    fun deleteRoute(routeId: Long)
}