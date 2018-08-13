package com.grandtrek.gps

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.location.LocationProvider
import android.os.Bundle

class PositionProvider(val context: Context) {

    private lateinit var listener: LocationListener

    enum class Status {
        ENABLED,
        DISABLED,
        OUT_OF_SERVICE,
        UNAVAILABLE,
        AVAILABLE
    }

    @SuppressWarnings("MissingPermission")
    fun startListening(
            onLocation: (location: Location) -> Unit,
            onStatus: ((status: Status) -> Unit)?,
            onSatellites: ((satellites: Bundle) -> Unit)?) {
        listener = object : LocationListener {

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                when(status) {
                    LocationProvider.OUT_OF_SERVICE -> onStatus?.run { this(Status.OUT_OF_SERVICE) }
                    LocationProvider.TEMPORARILY_UNAVAILABLE -> onStatus?.run { this(Status.UNAVAILABLE) }
                    LocationProvider.AVAILABLE -> {
                        onStatus?.run { this(Status.AVAILABLE) }
                        onSatellites?.run { extras?.run(onSatellites) }
                    }
                }
            }

            override fun onProviderEnabled(provider: String?) {
                onStatus?.run { this(Status.ENABLED) }
            }

            override fun onProviderDisabled(provider: String?) {
                onStatus?.run { this(Status.DISABLED) }
            }

            override fun onLocationChanged(location: Location?) {
                location?.run(onLocation)
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

    private val locationManager by lazy {
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }
}