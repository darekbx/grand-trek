package com.grandtrek.usecases

import android.arch.lifecycle.MutableLiveData
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

open class Time {

    companion object {
        val TIME_FORMAT = "%d:%02d:%02d"
        val DATE_FORMAT = "dd-mm-yyyy"

        fun secondsToTimeFormat(secondsIn: Long?): String {
            return secondsIn?.run {
                var seconds = secondsIn
                val minuteSeconds = TimeUnit.MINUTES.toSeconds(1)
                val hourSeconds = TimeUnit.HOURS.toSeconds(1)

                val hours = seconds / TimeUnit.HOURS.toSeconds(1)
                seconds -= hours * hourSeconds

                val minutes = seconds / minuteSeconds
                seconds -= minutes * minuteSeconds

                return TIME_FORMAT.format(hours, minutes, seconds)
            } ?: ""
        }

        fun millisToDate(timeStamp: Long) = dateFprmatter.format(timeStamp)

        private val dateFprmatter by lazy {
            SimpleDateFormat(DATE_FORMAT)
        }
    }

    private var timer: Timer? = null

    var overallTimeValue = 0L
    var rideTimeValue = 0L
    var overallTime = MutableLiveData<Long>()
    var rideTime = MutableLiveData<Long>()
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

    fun stop() {
        timer?.run { cancel() }
        timer = null
    }

    fun reset() {
        overallTimeValue = 0
        rideTimeValue = 0
    }

    fun secondsToTimeFormat(secondsIn: Long?): String {
        return secondsIn?.run {
            var seconds = secondsIn
            val minuteSeconds = TimeUnit.MINUTES.toSeconds(1)
            val hourSeconds = TimeUnit.HOURS.toSeconds(1)

            val hours = seconds / TimeUnit.HOURS.toSeconds(1)
            seconds -= hours * hourSeconds

            val minutes = seconds / minuteSeconds
            seconds -= minutes * minuteSeconds

            return TIME_FORMAT.format(hours, minutes, seconds)
        } ?: ""
    }

    private fun handleTick() {
        overallTimeValue++
        overallTime.postValue(overallTimeValue)
        if (isRiding) {
            rideTimeValue++
            rideTime.postValue(rideTimeValue)
        }
    }

    open fun getInterval() = TimeUnit.SECONDS.toMillis(1)
}