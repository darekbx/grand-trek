package com.grandtrek.usecases

import com.nhaarman.mockito_kotlin.whenever
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(sdk = intArrayOf(Config.NEWEST_SDK))
@RunWith(RobolectricTestRunner::class)
class TimeTest {

    @Test
    fun overallTime() {
        // Given
        val time = spy(Time())
        doReturn(50L).whenever(time).getInterval()

        // When
        time.start()
        runBlocking { delay(200) }

        // Then
        time.stop()
        assertEquals(6, time.overallTimeValue)
    }

    @Test
    fun isRidingTime() {
        // Given
        val time = spy(Time())
        doReturn(50L).whenever(time).getInterval()

        // When
        time.start()
        time.isRiding = true
        runBlocking { delay(110) }
        time.isRiding = false
        runBlocking { delay(100) }

        // Then
        time.stop()
        assertEquals(3, time.rideTimeValue)
    }

    @Test
    fun secondsToTimeFormat() {
        // Given
        val time = Time()

        // When / Then
        assertEquals("0:00:00", time.secondsToTimeFormat(0))
        assertEquals("0:00:54", time.secondsToTimeFormat(54))
        assertEquals("0:01:00", time.secondsToTimeFormat(60))
        assertEquals("0:01:07", time.secondsToTimeFormat(67))
        assertEquals("0:04:43", time.secondsToTimeFormat(283))
        assertEquals("1:00:00", time.secondsToTimeFormat(3600))
        assertEquals("1:03:09", time.secondsToTimeFormat(3789))
        assertEquals("2:09:49", time.secondsToTimeFormat(7789))
    }
}