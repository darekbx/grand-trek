package com.grandtrek.data.model

import com.grandtrek.usecases.Time

data class Route(
        var id: Long? = null,
        var name: String? = null,
        var distance: Float = 0F,
        var averageSpeed: Float = 0F,
        var maximumSpeed: Float = 0F,
        var tripTime: Long = 0L,
        var date: Long = 0L,
        var color: Int = 0
) {

    fun averageSpeedInKmH() = "%.2f km/h".format(averageSpeed * 3.6F)
    fun distanceInKm() = "%.1f km".format(distance / 1000)
    fun tripTimeFormatted() = Time.secondsToTimeFormat(tripTime)
}