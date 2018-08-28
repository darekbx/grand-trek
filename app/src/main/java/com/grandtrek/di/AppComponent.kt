package com.grandtrek.di

import com.grandtrek.ui.trip.TripActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class, ViewModelModule::class, DatabaseModule::class))
interface AppComponent {

    fun inject(tripActivity: TripActivity)
}