package com.grandtrek.ui.trip.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class AltitudeChart(context: Context,attributeSet: AttributeSet) : View(context, attributeSet) {

    companion object {
        val PADDING = 14F
        val METERS_UNIT = "[m]"
        val TIME_UNIT = "[t]"
    }

    val altitudes = mutableListOf<Double>()

    private val chartAxesPaint = Paint().apply {
        isAntiAlias = true
        color = Color.DKGRAY
        textSize = 12F
        textAlign = Paint.Align.LEFT
    }

    private val chartPaint = Paint().apply {
        isAntiAlias = true
        color = Color.RED
        strokeWidth = 2F
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.run {
            drawAxes(this)
            drawAltitudes(this)
        }
    }

    private fun drawAltitudes(canvas: Canvas) {
        val altitudesSize = altitudes.size
        if (altitudesSize < 2) {
            return
        }
        var position = PADDING
        var previous = altitudes[0]
        val chunkSize = calculateChunkSize(altitudesSize)
        val ratio = calculateRatio()

        altitudes.forEach {
            val x1 = position
            val x2 = position + chunkSize
            val y1 = (previous.toFloat() - height) * ratio.toFloat()
            val y2 = (it.toFloat() - height) * ratio.toFloat()
            canvas.drawLine(x1, y1, x2, y2, chartPaint)
            previous = it
            position += chunkSize
        }
    }

    fun calculateChunkSize(altitudesSize: Int) = width.toFloat() / altitudesSize.toFloat()

    fun calculateRatio() = height.toDouble() / (altitudes.max() ?: 1.0)

    private fun drawAxes(canvas: Canvas) {
        with(canvas) {
            drawLine(PADDING, PADDING, PADDING, height.toFloat(), chartAxesPaint)
            drawLine(0F, height - PADDING, width - PADDING, height - PADDING, chartAxesPaint)

            drawText(METERS_UNIT, 6F, 12F, chartAxesPaint)
            drawText(TIME_UNIT, width - 10F, height - 10F, chartAxesPaint)
        }
    }
}