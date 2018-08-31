package com.grandtrek.usecases

operator fun Speed.plusAssign(entry: Float) = add(entry)

class Speed {

    companion object {
        val KMH_MULTIPER = 3.6F

        fun formatToKmH(speedInMs: Float) = "%.2f km/h".format(speedInMs * KMH_MULTIPER)
    }

    private val entries = mutableListOf<Float>()

    fun add(entry: Float) {
        entries.add(entry)
    }

    fun average() = entries.sum() / entries.size

    fun max() = entries.max() ?: 0F

    fun msToKmhFormatted(value: Float) = Speed.formatToKmH(value)
    fun msToKmh(value: Float) = value * KMH_MULTIPER
}