package com.grandtrek.modules

import java.util.*
import java.util.concurrent.TimeUnit

open class Time {

    private var timer: Timer? = null

    var overallTime = 0
    var rideTime = 0
    var isRiding = false

    fun start() {
        reset()
        timer = Timer().apply {
            scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    handleTick()
                }
            }, 0, getInterval())
        }
    }

    private fun handleTick() {
        overallTime++
        if (isRiding) {
            rideTime++
        }
    }

    fun stop() {
        timer?.run { cancel() }
        timer = null
    }

    fun reset() {
        overallTime = 0
        rideTime = 0
    }

    open fun getInterval() = TimeUnit.SECONDS.toMillis(1)
}