package com.grandtrek.ui.trip.map

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.grandtrek.utils.BaseOverlay
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

class TripOverlay : BaseOverlay() {

    var currentPosition = GeoPoint(0.0, 0.0)

    private val customPaint = Paint().apply {
        color = Color.RED
        isAntiAlias = true
        strokeWidth = LINE_SIZE
    }

    override fun getPaint() = customPaint

    override fun draw(canvas: Canvas?, osmv: MapView?, shadow: Boolean) {
        super.draw(canvas, osmv, shadow)
        osmv?.run {
            drawCurrentLocation(canvas, this)
        }
    }

    private fun drawCurrentLocation(c: Canvas?, mapView: MapView) {
        mapView.projection.toPixels(currentPosition, outFirstPoint)
        with(outFirstPoint) {
            c?.drawCircle(x.toFloat(), y.toFloat(), RADIUS, getPaint())
        }
    }
}