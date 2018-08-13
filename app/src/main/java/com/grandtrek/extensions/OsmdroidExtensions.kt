package com.grandtrek.extensions

import android.location.Location
import org.osmdroid.util.GeoPoint

fun Location.toGeoPoint() = GeoPoint(latitude, longitude)