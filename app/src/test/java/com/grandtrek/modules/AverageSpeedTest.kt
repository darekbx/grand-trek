package com.grandtrek.modules

import org.junit.Test

import org.junit.Assert.*

class AverageSpeedTest {

    @Test
    fun average() {
        // Given
        val averageSpeed = AverageSpeed()

        // When
        with(averageSpeed) {
            add(10F)
            add(30F)
            add(20F)
            add(20F)
        }

        // Then
        assertEquals(20F, averageSpeed.average())
    }

    @Test
    fun average_customOperator() {
        // Given
        val averageSpeed = AverageSpeed()

        // When
        averageSpeed += 5f
        averageSpeed += 2f
        averageSpeed += 3f
        averageSpeed += 10f

        // Then
        assertEquals(5F, averageSpeed.average())
    }
}