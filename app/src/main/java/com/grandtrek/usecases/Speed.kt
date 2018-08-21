package com.grandtrek.usecases

operator fun Speed.plusAssign(entry: Float) = add(entry)

class Speed {

    private val entries = mutableListOf<Float>()

    fun add(entry: Float) {
        entries.add(entry)
    }

    fun average() = entries.sum() / entries.size

    fun max() = entries.max()
}