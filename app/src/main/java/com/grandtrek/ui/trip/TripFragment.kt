package com.grandtrek.ui.trip

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.grandtrek.GrandTrekApplication
import com.grandtrek.R
import com.grandtrek.extensions.toGeoPoint
import com.grandtrek.gps.PositionProvider
import com.grandtrek.permissions.PermissionsHelper
import com.grandtrek.ui.trip.dialogs.SaveRouteDialog
import com.grandtrek.ui.trip.map.TripOverlay
import com.grandtrek.usecases.TripMap
import com.grandtrek.utils.UiUtils
import kotlinx.android.synthetic.main.fragment_trip.*
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.MapTileProviderBasic
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.overlay.TilesOverlay
import java.io.File
import javax.inject.Inject

class TripFragment : Fragment() {

    companion object {
        val PERMISSIONS_REQUEST_CODE = 100
        val MINIMUM_RECORDED_SPEED = 2f
    }

    @Inject
    lateinit var positionProvider: PositionProvider

    @Inject
    lateinit var permissionsHandler: PermissionsHelper

    @Inject
    lateinit var tripMap: TripMap

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    internal lateinit var viewModel: TripViewModel

    val zoom = TripMap.DEFAULT_ZOOM
    var isAutoPositionOn = true
    val currentLocationOverlay = TripOverlay()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_trip, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity?.application as GrandTrekApplication)?.appComponent.inject(this)
        activity?.getWindow()?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        viewModel = ViewModelProviders.of(this, viewModelFactory)[TripViewModel::class.java]

        handlePositionProviderUpdates()
        handleViewModelUpdates()
        handleTimeUpdates()
        handlePermissions()

        handleButtons()
    }

    fun isRecording() = viewModel.isRecording

    fun handleButtons() {
        play_stop_button.setOnClickListener { onStartStop() }
        auto_position.setOnClickListener { onAutoPositionSwitch() }
        use_offline_map.setOnClickListener { onUseOfflineMap() }
    }

    fun handlePositionProviderUpdates() {
        with(positionProvider) {
            liveLocation.observe(this@TripFragment, Observer { location ->
                location?.run {
                    updateLocation(this)
                }
            })
            liveStatus.observe(this@TripFragment, Observer { updateStatus() })
            liveSatellites.observe(this@TripFragment, Observer { updateStatus() })
        }
    }

    fun handleViewModelUpdates() {
        with(viewModel) {
            currentSpeed.observe(this@TripFragment, Observer {
                updateIsRiding(it)
                updateCurrentSpeedText(it)
            })
            averageSpeed.observe(this@TripFragment, Observer { updateAverageSpeedText(it) })
            maximumSpeed.observe(this@TripFragment, Observer { updateMaximumSpeedText(it) })
            currentDistance.observe(this@TripFragment, Observer { updateCurrentDistanceText(it) })
        }
    }

    fun handleTimeUpdates() {
        with(viewModel) {
            overallTime().observe(this@TripFragment, Observer { updateTripTimeView(it) })
            rideTime().observe(this@TripFragment, Observer { updateRideTimeView(it) })
        }
    }

    fun updateRideTimeView(it: Long?) {
        ride_time.text = viewModel.secondsToTimeFormat(it)
    }

    fun updateTripTimeView(it: Long?) {
        trip_time.text = viewModel.secondsToTimeFormat(it)
    }

    fun updateCurrentDistanceText(value: String?) {
        current_distance.text = value
    }

    fun updateMaximumSpeedText(value: String?) {
        maximum_speed.text = value
    }

    fun updateAverageSpeedText(value: String?) {
        average_speed.text = value
    }

    fun updateCurrentSpeedText(it: Float?) {
        current_speed.text = formatIntKmH(it)
    }

    override fun onDestroy() {
        super.onDestroy()
        positionProvider.stopListening()
        viewModel.stopTime()
    }

    override fun onPause() {
        super.onPause()
        if (map_view != null) {
            map_view.onPause()
        }
    }

    fun onStartStop() {
        viewModel.isRecording = !viewModel.isRecording
        changeButtonsState()
        handleRecordingState()

        if (viewModel.isRecording) {
            viewModel.createRoute()
        } else {
            context?.run {
                SaveRouteDialog(this,
                        { name, color -> viewModel.updateRecordedRoute(name, color) },
                        { viewModel.discardRoute() }).show()
            }
        }
    }

    fun changeButtonsState() {
        with(play_stop_button) {
            when (viewModel.isRecording) {
                true -> {
                    setImageResource(R.drawable.ic_stop)
                    tag = true
                }
                false -> {
                    setImageResource(R.drawable.ic_play)
                    tag = false
                }
            }
        }
    }

    fun onAutoPositionSwitch() {
        isAutoPositionOn = !isAutoPositionOn
    }

    fun onUseOfflineMap() {
        addRemoveOfflineOverlay()
    }

    fun addRemoveOfflineOverlay() {
        val offlineOverlayIndex = map_view.overlays.indexOfFirst { it is TilesOverlay }
        when (use_offline_map.isEnabled) {
            true -> map_view.overlays.add(0, offlineMapsOverlay())
            else -> map_view.overlays.removeAt(offlineOverlayIndex)
        }
        map_view.invalidate()
    }

    private fun formatIntKmH(value: Float?) = "${value?.toInt()}"

    private fun updateStatus() {
        with(positionProvider) {
            status.text = getString(R.string.status_format,
                    liveSatellites.value ?: 0,
                    liveStatus.value?.toString())

            if (liveStatus.value == PositionProvider.Status.AVAILABLE) {
                play_stop_button.show()
            }
        }
    }

    private fun updateIsRiding(speed: Float?) {
        speed?.run {
            viewModel.setIsRiding(this > 0)
        }
    }

    private fun handleRecordingState() {
        when (viewModel.isRecording) {
            true -> viewModel.startTime()
            else -> viewModel.stopTime()
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
        context?.run {
            UiUtils.showDialog(this, R.string.message_enable_location, { activity?.finish() })
        }
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
        if (location.speed >= MINIMUM_RECORDED_SPEED) {
            notifyNewLocation(location)
        } else {
            updateCurrentSpeedText(0F)
            updateIsRiding(0F)
        }
        with(map_view.controller) {
            if (isAutoPositionOn) {
                setZoom(zoom)
                animateTo(location.toGeoPoint())
            }
        }
    }

    fun notifyNewLocation(location: Location) {
        val geoPoint = location.toGeoPoint()
        viewModel.updateLocation(location)
        currentLocationOverlay.currentPosition = geoPoint
        currentLocationOverlay.points = viewModel.points
        updateAltitude(location)
    }

    fun updateAltitude(location: Location) {
        with(altitude_chart) {
            altitudes.add(location.altitude.toFloat())
            speeds.add(location.speed)
            invalidate()
        }
    }

    private fun initializeMap() {
        with(context) {
            Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
            Configuration.getInstance().osmdroidBasePath = File(TripMap.OSM_BASE_PATH)
        }

        with(map_view) {
            setTileSource(TileSourceFactory.MAPNIK)
            setBuiltInZoomControls(true)
            setMultiTouchControls(true)
            setMaxZoomLevel(TripMap.MAX_ZOOM)
            overlays.add(currentLocationOverlay)
        }

        val lastLocation = positionProvider.lastKnownLocation()
        lastLocation?.let { lastLocation ->
            with(map_view.controller) {
                setZoom(zoom)
                setCenter(lastLocation.toGeoPoint())
            }
        }
    }

    fun offlineMapsOverlay(): TilesOverlay? {
        return context?.run {
            val providerOffline = MapTileProviderBasic(this).apply {
                tileSource = tripMap.offlineMapSource()
            }
            val overlayOffline = TilesOverlay(providerOffline, this).apply {
                loadingBackgroundColor = Color.TRANSPARENT
                loadingLineColor = Color.TRANSPARENT
            }
            return overlayOffline
        } ?: null
    }

    private fun handlePermissionsDenial() {
        context?.run {
            UiUtils.showDialog(this, R.string.message_enable_permissions, { activity?.finish() })
        }
    }
}