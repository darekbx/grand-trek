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
import kotlin.math.max

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as GrandTrekApplication).appComponent.inject(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory)[TripViewModel::class.java]

        with(positionProvider) {
            liveLocation.observe(this@TripActivity, Observer { location ->
                location?.run {
                    updateLocation(this)
                }
            })
            // TODO: liveStatus, liveStaellites
        }

        with(viewModel) {
            currentSpeed.observe(this@TripActivity, Observer {  })
            averageSpeed.observe(this@TripActivity, Observer {  })
            maximumSpeed.observe(this@TripActivity, Observer {  })
            currentDistance.observe(this@TripActivity, Observer {  })
        }

        handlePermissions()
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
            setZoom(zoom)
            animateTo(location.toGeoPoint())
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