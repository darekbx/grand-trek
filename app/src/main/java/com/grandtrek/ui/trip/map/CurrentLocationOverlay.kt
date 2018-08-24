package com.grandtrek.ui.trip.map

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Overlay

class CurrentLocationOverlay : Overlay() {

    companion object {
        val RADIUS = 12F
    }

    var currentPosition = GeoPoint(0.0, 0.0)
    val paint = Paint().apply {
        color = Color.RED
        isAntiAlias = true
    }

    override fun draw(c: Canvas?, osmv: MapView?, shadow: Boolean) {
        osmv?.run {
            val outPoint = Point()
            projection.toPixels(currentPosition, outPoint)

            with(outPoint) {
                c?.drawCircle(x.toFloat(), y.toFloat(), RADIUS, paint)
            }
        }
    }

}