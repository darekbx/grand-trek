package com.grandtrek.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.grandtrek.ui.route.RouteViewModel
import com.grandtrek.ui.routes.RoutesViewModel
import com.grandtrek.ui.trip.TripViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelFactory.ViewModelKey(TripViewModel::class)
    internal abstract fun bindTripViewModel(viewModel: TripViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelFactory.ViewModelKey(RoutesViewModel::class)
    internal abstract fun bindRoutesViewModel(viewModel: RoutesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelFactory.ViewModelKey(RouteViewModel::class)
    internal abstract fun bindRouteViewModel(viewModel: RouteViewModel): ViewModel
}