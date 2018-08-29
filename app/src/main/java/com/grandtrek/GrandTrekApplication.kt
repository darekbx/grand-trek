package com.grandtrek

import android.app.Application
import com.facebook.stetho.Stetho
import com.grandtrek.di.AppComponent
import com.grandtrek.di.AppModule
import com.grandtrek.di.DaggerAppComponent

class GrandTrekApplication : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
        appComponent = DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .build()
    }
}