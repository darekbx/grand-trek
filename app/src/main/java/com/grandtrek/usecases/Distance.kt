package com.grandtrek.usecases

operator fun Distance.plusAssign(entry: Float) = add(entry)

class Distance {

    var overallDistance = 0F

    fun add(meters: Float) {
        overallDistance += meters
    }
}