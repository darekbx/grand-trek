package com.grandtrek.ui.trip

import android.location.Location
import com.grandtrek.usecases.Distance
import com.grandtrek.usecases.Speed
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(sdk = intArrayOf(Config.NEWEST_SDK))
@RunWith(RobolectricTestRunner::class)
class TripViewModelTest {

    @Test
    fun updateLocation() {
        // Given
        val speed = Speed()
        val distance = Distance()
        val viewModel = TripViewModel(speed, distance)

        // When
        val location1 = mock<Location> {
            on { hasSpeed() } doReturn true
            on { getSpeed() } doReturn 10F
            on { distanceTo(any()) } doReturn 150F
        }
        viewModel.updateLocation(location1)

        val location2 = mock<Location> {
            on { hasSpeed() } doReturn true
            on { getSpeed() } doReturn 20F
        }
        viewModel.updateLocation(location2)

        // Then
        assertEquals(150F, distance.overallDistance)
        assertEquals(20f, speed.max())
        assertEquals(15f, speed.average())
        assertEquals(72F, viewModel.maximumSpeed.value)
        assertEquals(54F, viewModel.averageSpeed.value)
    }
}