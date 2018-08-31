package com.grandtrek.usecases

operator fun Distance.plusAssign(entry: Float) = add(entry)

class Distance {

    companion object {
        fun formatToKm(distanceInM: Float) = "%.1f km".format(distanceInM / 1000)
    }

    var overallDistance = 0F

    fun add(meters: Float) {
        overallDistance += meters
    }

    fun distanceInKilometersFormatted() =  Distance.formatToKm(overallDistance)
}