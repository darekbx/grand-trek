package com.grandtrek.di

import com.grandtrek.ui.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {

    fun inject(mainActivity: MainActivity)
}