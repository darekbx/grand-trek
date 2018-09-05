package com.grandtrek.ui.route

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.grandtrek.utils.BaseOverlay
import org.osmdroid.api.IGeoPoint
import org.osmdroid.views.MapView

class RouteMapOverlay : BaseOverlay() {

    val customPaint = Paint().apply {
        color = Color.RED
        isAntiAlias = true
        strokeWidth = LINE_SIZE
    }

    fun updateColor(color: Int) {
        customPaint.color = color
    }

    override fun getPaint() = customPaint

    override fun draw(canvas: Canvas?, osmv: MapView?, shadow: Boolean) {
        super.draw(canvas, osmv, shadow)
        osmv?.let { osmv ->
            canvas?.let { canvas ->
                drawLine(canvas, osmv)
                drawStartAndEnd(canvas, osmv)
            }
        }
    }

    fun drawStartAndEnd(canvas: Canvas, mapView: MapView) {
        points?.run {
            first()?.let { drawDot(canvas, it, mapView) }
            last()?.let { drawDot(canvas, it, mapView) }
        }
    }

    private fun drawDot(canvas: Canvas, point: IGeoPoint, mapView: MapView) {
        with(mapView.projection) {
            toPixels(point, outFirstPoint)
        }
        canvas.drawCircle(outFirstPoint.x.toFloat(), outFirstPoint.y.toFloat(), RADIUS, getPaint())
    }
}