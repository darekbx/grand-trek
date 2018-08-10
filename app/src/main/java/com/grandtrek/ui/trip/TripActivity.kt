package com.grandtrek.ui.trip

import android.Manifest
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.grandtrek.R

class TripActivity : AppCompatActivity() {

    companion object {
        val PERMISSIONS_REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip)

        checkPermissions()
    }

    private fun checkPermissions() {
        val coarsePermission = Manifest.permission.ACCESS_COARSE_LOCATION
        val finePermission = Manifest.permission.ACCESS_FINE_LOCATION
        val coarseLocation = ContextCompat.checkSelfPermission(this, coarsePermission)
        val fineLocation = ContextCompat.checkSelfPermission(this, finePermission)

        if (coarseLocation == PackageManager.PERMISSION_DENIED || fineLocation == PackageManager.PERMISSION_DENIED) {
            requestPermissions(arrayOf(coarsePermission, finePermission), PERMISSIONS_REQUEST_CODE)
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
            when(allGranted) {
                true -> startLocationTracking()
                else -> handlePermissionsDenial()
            }
        }
    }

    private fun startLocationTracking() {
        // TODO
    }

    private fun handlePermissionsDenial() {
        // TODO
    }
}