package com.grandtrek.usecases

operator fun Speed.plusAssign(entry: Float) = add(entry)

class Speed {

    companion object {
        val KMH_MULTIPER = 3.6F
    }

    private val entries = mutableListOf<Float>()

    fun add(entry: Float) {
        entries.add(entry)
    }

    fun average() = entries.sum() / entries.size

    fun max() = entries.max() ?: 0F

    fun msToKmh(value: Float) = value * KMH_MULTIPER
}