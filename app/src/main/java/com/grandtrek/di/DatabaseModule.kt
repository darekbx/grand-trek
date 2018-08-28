package com.grandtrek.di

import android.arch.persistence.room.Room
import android.content.Context
import com.grandtrek.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun providesAppDatabase(context: Context) =
            Room
                    .databaseBuilder(context.applicationContext, AppDatabase::class.java, AppDatabase.DB_NAME)
                    .allowMainThreadQueries()
                    .build()
}