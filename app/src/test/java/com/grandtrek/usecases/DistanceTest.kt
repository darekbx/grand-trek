package com.grandtrek.usecases

import org.junit.Assert.*
import org.junit.Test

class DistanceTest {

    @Test
    fun distanceAggregation() {
        // Given
        val distance = Distance()

        // When
        distance += 0.5f
        distance += 0.8f
        distance += 0.1f

        // Then
        assertEquals(1.4f, distance.overallDistance, 0.0F)
    }
}