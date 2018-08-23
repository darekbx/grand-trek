package com.grandtrek.ui.trip

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.location.Location
import com.grandtrek.usecases.Distance
import com.grandtrek.usecases.Speed
import com.grandtrek.usecases.plusAssign
import javax.inject.Inject

class TripViewModel @Inject constructor(
        private val speed: Speed,
        private val distance: Distance
): ViewModel() {

    val currentSpeed = MutableLiveData<Float>()
    val averageSpeed = MutableLiveData<Float>()
    val maximumSpeed = MutableLiveData<Float>()
    val currentDistance = MutableLiveData<Float>()

    var previousLocation: Location? = null

    fun updateLocation(location: Location) {
        updateDistance(location)
        updateSpeed(location)
        saveLocation(location)

        previousLocation = location
    }

    private fun saveLocation(location: Location) {
        // TODO
    }

    private fun updateDistance(location: Location) {
        previousLocation?.let { previousLocation ->
            distance += previousLocation.distanceTo(location)
            currentDistance.value = distance.distanceInKilometers()
        }
    }

    private fun updateSpeed(location: Location) {
        if (location.hasSpeed()) {
            speed += location.speed
            currentSpeed.value = speed.msToKmh(location.speed)
            averageSpeed.value = speed.msToKmh(speed.average())
            maximumSpeed.value = speed.msToKmh(speed.max())
        }
    }
}