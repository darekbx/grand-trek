package com.grandtrek.usecases

import org.junit.Test

import org.junit.Assert.*

class SpeedTest {

    @Test
    fun average_max() {
        // Given
        val speed = Speed()

        // When
        with(speed) {
            add(10F)
            add(30F)
            add(20F)
            add(20F)
        }

        // Then
        assertEquals(20F, speed.average())
        assertEquals(30F, speed.max())
    }

    @Test
    fun average_customOperator() {
        // Given
        val speed = Speed()

        // When
        speed += 5f
        speed += 2f
        speed += 3f
        speed += 10f

        // Then
        assertEquals(5F, speed.average())
    }

    @Test
    fun msToKmh() {
        // Given
        val speed = Speed()

        // When
        val result = speed.msToKmh(5F)

        // Then
        assertEquals(18F, result, 0F)
    }
}