package com.grandtrek.data.model

import com.grandtrek.usecases.Distance
import com.grandtrek.usecases.Speed
import com.grandtrek.usecases.Time

data class Route(
        var id: Long? = null,
        var name: String? = null,
        var distance: Float = 0F,
        var averageSpeed: Float = 0F,
        var maximumSpeed: Float = 0F,
        var tripTime: Long = 0L,
        var rideTime: Long = 0L,
        var date: Long = 0L,
        var color: Int = 0
) {

    fun averageSpeedInKmH() = Speed.formatToKmH(averageSpeed)
    fun maximumSpeedInKmH() = Speed.formatToKmH(maximumSpeed)
    fun distanceInKm() = Distance.formatToKm(distance)
    fun tripTimeFormatted() = Time.secondsToTimeFormat(tripTime)
    fun rideTimeFormatted() = Time.secondsToTimeFormat(rideTime)
    fun dateFormatted() = Time.millisToDate(date)
}