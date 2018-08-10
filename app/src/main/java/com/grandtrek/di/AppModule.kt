package com.grandtrek.di

import android.content.Context
import com.grandtrek.GrandTrekApplication
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
}