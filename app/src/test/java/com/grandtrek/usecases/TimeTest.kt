package com.grandtrek.usecases

import com.nhaarman.mockito_kotlin.whenever
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito.*

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
        assertEquals(5, time.overallTime)
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
        assertEquals(3, time.rideTime)
    }
}