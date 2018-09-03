package com.grandtrek.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.grandtrek.data.local.entities.PointEntity
import com.grandtrek.data.local.entities.RouteEntity

@Database(entities = arrayOf(RouteEntity::class, PointEntity::class), version = 2)
abstract class AppDatabase: RoomDatabase() {

    companion object {
        val DB_NAME = "grand_trek"
    }

    abstract fun getDao(): CommonDao
}