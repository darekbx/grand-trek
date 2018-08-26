package com.grandtrek.ui.trip.map

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Overlay

class CustomOverlay : Overlay() {

    companion object {
        val RADIUS = 12F
        val LINE_SIZE = 6F
    }

    var currentPosition = GeoPoint(0.0, 0.0)
    var points: MutableList<GeoPoint>? = null

    private val outFirstPoint = Point()
    private val outSecondPoint = Point()

    private val dotPaint = Paint().apply {
        color = Color.RED
        isAntiAlias = true
    }
    private val linePaint = Paint().apply {
        color = Color.RED
        isAntiAlias = true
        strokeWidth = LINE_SIZE
    }

    override fun draw(c: Canvas?, osmv: MapView?, shadow: Boolean) {
        osmv?.run {
            drawCurrentLocation(c, this)
            drawLine(c, this)
        }
    }

    private fun drawLine(c: Canvas?, mapView: MapView) {
        var previousPoint: GeoPoint? = null
        points?.forEach { actualPoint ->

            if (previousPoint == null) {
                previousPoint = actualPoint
                return@forEach
            }

            with(mapView.projection) {
                toPixels(previousPoint, outFirstPoint)
                toPixels(actualPoint, outSecondPoint)
            }
            c?.drawLine(
                    outFirstPoint.x.toFloat(), outFirstPoint.y.toFloat(),
                    outSecondPoint.x.toFloat(), outSecondPoint.y.toFloat(),
                    linePaint)

            previousPoint = actualPoint
        }
    }

    private fun drawCurrentLocation(c: Canvas?, mapView: MapView) {
        mapView.projection.toPixels(currentPosition, outFirstPoint)
        with(outFirstPoint) {
            c?.drawCircle(x.toFloat(), y.toFloat(), RADIUS, dotPaint)
        }
    }
}