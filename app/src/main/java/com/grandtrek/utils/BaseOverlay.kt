package com.grandtrek.utils

import android.graphics.Canvas
import android.graphics.Paint
import org.osmdroid.api.IGeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Overlay

open abstract class BaseOverlay : Overlay() {

    companion object {
        val RADIUS = 12F
        val LINE_SIZE = 6F
    }

    var points: MutableList<IGeoPoint>? = null

    protected val outFirstPoint = android.graphics.Point()
    protected val outSecondPoint = android.graphics.Point()

    abstract  fun getPaint(): Paint

    override fun draw(canvas: Canvas?, osmv: MapView?, shadow: Boolean) {
        osmv?.let { osmv ->
            canvas?.let { canvas ->
                drawLine(canvas, osmv)
            }
        }
    }

    protected fun drawLine(canvas: Canvas, mapView: MapView) {
        var previousPoint: IGeoPoint? = null
        points?.forEach { actualPoint ->

            if (previousPoint == null) {
                previousPoint = actualPoint
                return@forEach
            }

            with(mapView.projection) {
                toPixels(previousPoint, outFirstPoint)
                toPixels(actualPoint, outSecondPoint)
            }
            canvas.drawLine(
                    outFirstPoint.x.toFloat(), outFirstPoint.y.toFloat(),
                    outSecondPoint.x.toFloat(), outSecondPoint.y.toFloat(),
                    getPaint())

            previousPoint = actualPoint
        }
    }
}