package com.grandtrek.gps

import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.location.*
import android.os.Bundle

class PositionProvider(val context: Context) {

    companion object {
        val MINIMUM_INTERVAL = 1000L
    }

    private var listener: LocationListener? = null
    val liveLocation = MutableLiveData<Location>()
    val liveStatus = MutableLiveData<Status>()
    val liveSatellites = MutableLiveData<Int>()

    enum class Status {
        ENABLED,
        DISABLED,
        OUT_OF_SERVICE,
        UNAVAILABLE,
        AVAILABLE,
        DISCOVERING
    }

    private val gnssStatusCallback = object : GnssStatus.Callback() {

        override fun onSatelliteStatusChanged(status: GnssStatus?) {
            super.onSatelliteStatusChanged(status)
            status?.run {
                liveSatellites.value = this.satelliteCount
            }
        }
    }

    @SuppressWarnings("MissingPermission")
    fun startListening() {
        liveStatus.value = Status.DISCOVERING
        listener = object : LocationListener {

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                when (status) {
                    LocationProvider.OUT_OF_SERVICE -> liveStatus.value = Status.OUT_OF_SERVICE
                    LocationProvider.TEMPORARILY_UNAVAILABLE -> liveStatus.value = Status.UNAVAILABLE
                    LocationProvider.AVAILABLE -> liveStatus.value = Status.AVAILABLE
                }
            }

            override fun onProviderEnabled(provider: String?) {
                liveStatus.value = Status.ENABLED
            }

            override fun onProviderDisabled(provider: String?) {
                liveStatus.value = Status.DISABLED
            }

            override fun onLocationChanged(location: Location?) {
                liveStatus.value = Status.AVAILABLE
                liveLocation.value = location
            }
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_INTERVAL, 0F, listener)
        locationManager.registerGnssStatusCallback(gnssStatusCallback)
    }

    @SuppressWarnings("MissingPermission")
    fun lastKnownLocation() = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

    fun stopListening() {
        listener?.run {
            locationManager.removeUpdates(this)
            locationManager.unregisterGnssStatusCallback(gnssStatusCallback)
        }
    }

    fun isLocationEnabled() = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

    private val locationManager by lazy {
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }
}