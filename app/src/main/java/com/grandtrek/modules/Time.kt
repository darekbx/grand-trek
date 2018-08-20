package com.grandtrek.modules

import java.util.*
import java.util.concurrent.TimeUnit

open class Time {

    private var timer: Timer? = null

    var overallTime = 0
    var rideTime = 0
    var isRiding = false

    private val task = object : TimerTask() {
        override fun run() {
            overallTime++
            if (isRiding) {
                rideTime++
            }
        }
    }

    fun start() {
        reset()
        timer = Timer()
        timer?.scheduleAtFixedRate(task, 0, getInterval())
    }

    fun stop() {
        timer?.run {
            cancel()
        }
        timer = null
    }

    fun reset() {
        overallTime = 0
        rideTime = 0
    }

    fun getInterval() = 1L//TimeUnit.SECONDS.toMillis(1)
}