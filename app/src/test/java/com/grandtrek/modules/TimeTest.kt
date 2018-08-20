package com.grandtrek.modules

import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito.*

class TimeTest {

    @Test
    fun overallTime() {
        // Given
        val time = Time()

        // When
        time.start()
        runBlocking {
            delay(5)
        }

        // Then
        time.stop()
        assertEquals(5, time.overallTime)

    }
}