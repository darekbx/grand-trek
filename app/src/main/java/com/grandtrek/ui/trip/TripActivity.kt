package com.grandtrek.ui.trip

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.pm.PackageManager
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import com.grandtrek.GrandTrekApplication
import com.grandtrek.R
import com.grandtrek.extensions.toGeoPoint
import com.grandtrek.gps.PositionProvider
import com.grandtrek.usecases.Time
import com.grandtrek.permissions.PermissionsHelper
import com.grandtrek.usecases.TripMap
import com.grandtrek.utils.UiUtils
import kotlinx.android.synthetic.main.activity_trip.*
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import javax.inject.Inject

class TripActivity : AppCompatActivity() {

    companion object {
        val PERMISSIONS_REQUEST_CODE = 100
    }

    @Inject
    lateinit var positionProvider: PositionProvider

    @Inject
    lateinit var permissionsHandler: PermissionsHelper

    @Inject
    lateinit var time: Time

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    internal lateinit var viewModel: TripViewModel

    val zoom = TripMap.DEFAULT_ZOOM
    var isRecording = false
    var isAutoPositionOn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as GrandTrekApplication).appComponent.inject(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory)[TripViewModel::class.java]

        handlePositionProviderUpdates()
        handleViewModelUpdates()
        handleTimeUpdates()
        handlePermissions()
    }

    fun handlePositionProviderUpdates() {
        with(positionProvider) {
            liveLocation.observe(this@TripActivity, Observer { location ->
                location?.run {
                    updateLocation(this)
                }
            })
            liveStatus.observe(this@TripActivity, Observer { updateStatus() })
            liveSatellites.observe(this@TripActivity, Observer { updateStatus() })
        }
    }

    fun handleViewModelUpdates() {
        with(viewModel) {
            currentSpeed.observe(this@TripActivity, Observer {
                updateIsRiding(it)
                updateCurrentSpeedText(it)
            })
            averageSpeed.observe(this@TripActivity, Observer { updateAverageSpeedText(it) })
            maximumSpeed.observe(this@TripActivity, Observer { updateMaximumSpeedText(it) })
            currentDistance.observe(this@TripActivity, Observer { updateCurrentDistanceText(it) })
        }
    }

    fun handleTimeUpdates() {
        with(time) {
            overallTime.observe(this@TripActivity, Observer { updateTripTimeView(it) })
            rideTime.observe(this@TripActivity, Observer { updateRideTimeView(it) })
        }
    }

    fun updateRideTimeView(it: Long?) {
        ride_time.text = time.secondsToTimeFormat(it)
    }

    fun updateTripTimeView(it: Long?) {
        trip_time.text = time.secondsToTimeFormat(it)
    }

    fun updateCurrentDistanceText(it: Float?) {
        current_distance.text = formatKm(it)
    }

    fun updateMaximumSpeedText(it: Float?) {
        maximum_speed.text = formatKmH(it)
    }

    fun updateAverageSpeedText(it: Float?) {
        average_speed.text = formatKmH(it)
    }

    fun updateCurrentSpeedText(it: Float?) {
        current_speed.text = formatIntKmH(it)
    }

    override fun onDestroy() {
        super.onDestroy()
        positionProvider.stopListening()
        time.stop()
    }

    override fun onPause() {
        super.onPause()
        if (map_view != null) {
            map_view.onPause()
        }
    }

    fun onStartStop(v: View) {
        isRecording = !isRecording
        handleRecordingState()
    }

    fun onPause(v: View) {
    }

    fun onAutoPositionSwitch() {
        isAutoPositionOn = !isAutoPositionOn
    }

    private fun formatIntKmH(value: Float?) = "${value?.toInt()}"
    private fun formatKmH(value: Float?) = "%.2f km/h".format(value)
    private fun formatKm(value: Float?) = "%.1f km".format(value)

    private fun updateStatus() {
        with(positionProvider) {
            status.text = getString(R.string.status_format,
                    liveSatellites.value ?: 0,
                    liveStatus.value?.toString())
        }
    }

    private fun updateIsRiding(speed: Float?) {
        speed?.run {
            time.isRiding = this > 0
        }
    }

    private fun handleRecordingState() {
        when (isRecording) {
            true -> time.start()
            else -> time.stop()
        }
    }

    private fun handlePermissions() {
        when (permissionsHandler.hasAllPermissions()) {
            true -> checkLocationEnabled()
            else -> requestPermissions(permissionsHandler.requiredPermissions, PERMISSIONS_REQUEST_CODE)
        }
    }

    private fun checkLocationEnabled() {
        when (positionProvider.isLocationEnabled()) {
            true -> initializeTrip()
            else -> showEnableLocationDialog()
        }
    }

    private fun showEnableLocationDialog() {
        UiUtils.showDialog(this, R.string.message_enable_location, { finish() })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_CODE -> validatePermssionsResult(grantResults)
        }
    }

    private fun validatePermssionsResult(grantResults: IntArray) {
        if (grantResults.isNotEmpty()) {
            val allGranted = grantResults.all { it == PackageManager.PERMISSION_GRANTED }
            when (allGranted) {
                true -> initializeTrip()
                else -> handlePermissionsDenial()
            }
        }
    }

    private fun initializeTrip() {
        positionProvider.startListening()
        initializeMap()
    }

    private fun updateLocation(location: Location) {
        viewModel.updateLocation(location)
        with(map_view.controller) {
            if (zoom < 1) {
                setZoom(zoom)
            }
            if (isAutoPositionOn) {
                animateTo(location.toGeoPoint())
            }
        }
    }

    private fun initializeMap() {
        with(applicationContext) {
            Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
        }
        initializeLayout()

        with(map_view) {
            setTileSource(TileSourceFactory.MAPNIK)
            setBuiltInZoomControls(true)
            setMultiTouchControls(true)
            setMaxZoomLevel(TripMap.MAX_ZOOM)
        }

        val lastLocation = positionProvider.lastKnownLocation()
        lastLocation?.let { lastLocation ->
            with(map_view.controller) {
                setZoom(zoom)
                setCenter(lastLocation.toGeoPoint())
            }
        }
    }

    private fun initializeLayout() {
        setContentView(R.layout.activity_trip)
    }

    private fun handlePermissionsDenial() {
        UiUtils.showDialog(this, R.string.message_enable_permissions, { finish() })
    }
}