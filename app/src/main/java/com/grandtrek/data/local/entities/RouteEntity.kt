package com.grandtrek.data.local.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "route")
data class RouteEntity(
        @PrimaryKey(autoGenerate = true) var id: Long? = null,
        @ColumnInfo(name = "name") var name: String? = null,
        @ColumnInfo(name = "distance") var distance: Float = 0F,
        @ColumnInfo(name = "average_spped") var averageSpeed: Float = 0F,
        @ColumnInfo(name = "maximum_spped") var maximumSpeed: Float = 0F,
        @ColumnInfo(name = "triptime") var tripTime: Long = 0L,
        @ColumnInfo(name = "date") var date: Long = 0L,
        @ColumnInfo(name = "color") var color: Int = 0
)