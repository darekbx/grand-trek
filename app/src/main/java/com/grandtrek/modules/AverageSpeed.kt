package com.grandtrek.modules

operator fun AverageSpeed.plusAssign(entry: Float) = add(entry)

class AverageSpeed {

    private val entries = mutableListOf<Float>()

    fun add(entry: Float) {
        entries.add(entry)
    }

    fun average() = entries.sum() / entries.size
}