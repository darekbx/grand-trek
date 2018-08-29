package com.grandtrek.ui.trip

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.location.Location
import com.grandtrek.data.Repository
import com.grandtrek.data.local.entities.PointEntity
import com.grandtrek.data.local.entities.RouteEntity
import com.grandtrek.extensions.toGeoPoint
import com.grandtrek.usecases.Distance
import com.grandtrek.usecases.Speed
import com.grandtrek.usecases.Time
import com.grandtrek.usecases.plusAssign
import kotlinx.coroutines.experimental.async
import org.osmdroid.util.GeoPoint
import java.util.*
import javax.inject.Inject

class TripViewModel @Inject constructor(
        private val speed: Speed,
        private val distance: Distance,
        private val time: Time,
        private val repository: Repository
): ViewModel() {

    private var routeId: Long? = null

    val currentSpeed = MutableLiveData<Float>()
    val averageSpeed = MutableLiveData<Float>()
    val maximumSpeed = MutableLiveData<Float>()
    val currentDistance = MutableLiveData<Float>()

    var points = mutableListOf<GeoPoint>()
    var previousLocation: Location? = null

    fun startTime() {
        time.start()
    }

    fun stopTime() {
        time.stop()
    }

    fun setIsRiding(isRiding: Boolean) {
        time.isRiding = isRiding
    }

    fun pauseOnOff() {
        time.pauseOnOff()
    }

    fun secondsToTimeFormat(secondsIn: Long?) = time.secondsToTimeFormat(secondsIn)

    fun overallTime() = time.overallTime
    fun rideTime() = time.rideTime

    fun createRoute() {
        async {
            routeId = repository.createEmptyRoute()
        }
    }

    fun updateRecordedRoute(name: String, color: Int) {
        val route = RouteEntity(
                id = routeId,
                name = name,
                distance = distance.overallDistance,
                averageSpeed = speed.average(),
                maximumSpeed = speed.max(),
                color = color,
                date = Calendar.getInstance().timeInMillis,
                tripTime = time.overallTimeValue)
        async {
            repository.updateRoute(route)
        }
    }

    fun discardRoute() {
        async {
            routeId?.run {
                repository.deleteRoute(this)
            }
        }
    }

    fun updateLocation(location: Location) {
        updateDistance(location)
        updateSpeed(location)
        saveLocation(location)

        val geoPoint = location.toGeoPoint()
        points.add(geoPoint)

        previousLocation = location
    }

    private fun saveLocation(location: Location) {
        async {
            routeId?.let { routeId ->
                repository.addPointToRoute(location, routeId)
            }
        }
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