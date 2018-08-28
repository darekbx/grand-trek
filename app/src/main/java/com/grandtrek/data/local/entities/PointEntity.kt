package com.grandtrek.data.local.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "point")
data class PointEntity(
        @PrimaryKey(autoGenerate = true) var id: Long? = null,
        @ColumnInfo(name = "route_id") var routeId: Long = 0,
        @ColumnInfo(name = "latitude") var latitude: Double = 0.0,
        @ColumnInfo(name = "longitude") var longitude: Double = 0.0,
        @ColumnInfo(name = "altitude") var altitude: Double = 0.0,
        @ColumnInfo(name = "speed") var speed: Float = 0F
)