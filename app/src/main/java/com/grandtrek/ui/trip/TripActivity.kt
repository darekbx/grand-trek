package com.grandtrek.ui.trip

import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import com.grandtrek.GrandTrekApplication
import com.grandtrek.R
import com.grandtrek.gps.PositionProvider
import com.grandtrek.permissions.PermissionsHelper
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as GrandTrekApplication).appComponent.inject(this)

        handlePermissions()
    }

    override fun onDestroy() {
        super.onDestroy()
        positionProvider.stopListening()
    }

    override fun onPause() {
        super.onPause()
        if (map_view != null) {
            map_view.onPause()
        }
    }

    private fun handlePermissions() {
        when (permissionsHandler.hasAllPermissions()) {
            true -> initializeTrip()
            else -> requestPermissions(permissionsHandler.requiredPermissions, PERMISSIONS_REQUEST_CODE)
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
        positionProvider.startListening(
                { location -> },
                { status -> },
                { sattelites -> })
        initializeMap()
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
        }
    }

    private fun initializeLayout() {
        setContentView(R.layout.activity_trip)
    }

    private fun handlePermissionsDenial() {
        // TODO
    }
}