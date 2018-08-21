package com.grandtrek.gps

import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.location.LocationProvider
import android.os.Bundle

class PositionProvider(val context: Context) {

    private var listener: LocationListener? = null
    val liveLocation = MutableLiveData<Location>()
    val liveStatus = MutableLiveData<Status>()
    val liveSatellites = MutableLiveData<Bundle>()

    enum class Status {
        ENABLED,
        DISABLED,
        OUT_OF_SERVICE,
        UNAVAILABLE,
        AVAILABLE
    }

    @SuppressWarnings("MissingPermission")
    fun startListening() {
        listener = object : LocationListener {

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                when (status) {
                    LocationProvider.OUT_OF_SERVICE -> liveStatus.value = Status.OUT_OF_SERVICE
                    LocationProvider.TEMPORARILY_UNAVAILABLE -> liveStatus.value = Status.UNAVAILABLE
                    LocationProvider.AVAILABLE -> {
                        liveStatus.value = Status.AVAILABLE
                        extras?.run { liveSatellites.value = this }
                    }
                }
            }

            override fun onProviderEnabled(provider: String?) {
                liveStatus.value = Status.ENABLED
            }

            override fun onProviderDisabled(provider: String?) {
                liveStatus.value = Status.DISABLED
            }

            override fun onLocationChanged(location: Location?) {
                liveLocation.value = location
            }
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0F, listener)
    }

    @SuppressWarnings("MissingPermission")
    fun lastKnownLocation() = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

    fun stopListening() {
        listener?.run {
            locationManager.removeUpdates(this)
        }
    }

    fun isLocationEnabled() = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

    private val locationManager by lazy {
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }
}