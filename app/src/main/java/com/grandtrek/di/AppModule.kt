package com.grandtrek.di

import android.content.Context
import com.grandtrek.GrandTrekApplication
import com.grandtrek.gps.PositionProvider
import com.grandtrek.permissions.PermissionsHelper
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
    fun providePositionProvider(): PositionProvider = PositionProvider(application.applicationContext)

    @Provides
    fun providePermissionsHelper(): PermissionsHelper = PermissionsHelper(application.applicationContext)
}