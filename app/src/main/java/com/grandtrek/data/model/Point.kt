package com.grandtrek.data.model

import com.grandtrek.usecases.Speed
import org.osmdroid.api.IGeoPoint

data class Point(
        var id: Long? = null,
        var routeId: Long = 0,
        var latitudePoint: Double = 0.0,
        var longitudePoint: Double = 0.0,
        var altitude: Double = 0.0,
        var speed: Float = 0F
): IGeoPoint {

    override fun getLongitude() = longitudePoint

    override fun getLatitude() = latitudePoint

    override fun getLongitudeE6() = longitudePoint.toInt()

    override fun getLatitudeE6() = latitudePoint.toInt()

    fun speedInKmH() = Speed.formatToKmH(speed)
}