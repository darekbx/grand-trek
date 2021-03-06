package com.grandtrek.di

import android.content.Context
import com.grandtrek.GrandTrekApplication
import com.grandtrek.data.Repository
import com.grandtrek.data.local.AppDatabase
import com.grandtrek.gps.PositionProvider
import com.grandtrek.usecases.Speed
import com.grandtrek.usecases.Time
import com.grandtrek.permissions.PermissionsHelper
import com.grandtrek.usecases.Distance
import com.grandtrek.usecases.TripMap
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val application: GrandTrekApplication) {

    @Provides
    @Singleton
    fun provideApplication(): GrandTrekApplication = application

    @Provides
    fun provideContext(): Context = application

    @Provides
    fun providePositionProvider() = PositionProvider(application.applicationContext)

    @Provides
    fun providePermissionsHelper() = PermissionsHelper(application.applicationContext)

    @Provides
    fun provideSpeed() = Speed()

    @Provides
    fun provideTime() = Time()

    @Provides
    fun provideDistance() = Distance()

    @Provides
    fun provideTripMap() = TripMap()

    @Provides
    fun provideRepository(appDatabase: AppDatabase) = Repository(appDatabase)
}