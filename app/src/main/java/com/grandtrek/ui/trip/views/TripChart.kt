package com.grandtrek.ui.trip.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class TripChart(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    companion object {
        val PADDING = 14F
        val METERS_UNIT = "[m]"
        val TIME_UNIT = "[t]"
    }

    val altitudes = mutableListOf<Float>()
    val speeds = mutableListOf<Float>()

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
            drawValues(this, altitudes, Color.argb(80, 0, 0, 0))
            drawValues(this, speeds, Color.argb(150, 240, 60, 50))
        }
    }

    private fun drawValues(canvas: Canvas, values: List<Float>, color: Int) {
        val itemsCount = values.size
        if (itemsCount < 2) {
            return
        }
        chartPaint.color = color

        val paddedHeight = height - PADDING.times(2)
        val reverseHeight = height - PADDING
        val chunkSize = calculateChunkSize(values).toFloat()
        val max = values.max() ?: 1F
        val min = values.min() ?: 1F
        val heightRatio = (max - min) / paddedHeight

        var position = PADDING
        var previous = values[0]

        values.forEach {
            val x1 = position
            val x2 = position + chunkSize
            val y1 = reverseHeight - (previous - min) / heightRatio
            val y2 = reverseHeight - (it - min) / heightRatio
            canvas.drawLine(x1, y1, x2, y2, chartPaint)
            previous = it
            position += chunkSize
        }
    }

    fun calculateChunkSize(list: List<Float>) = (width - PADDING.toDouble()) / list.size.dec()

    private fun drawAxes(canvas: Canvas) {
        with(canvas) {
            drawLine(PADDING, PADDING, PADDING, height.toFloat(), chartAxesPaint)
            drawLine(0F, height - PADDING, width - PADDING, height - PADDING, chartAxesPaint)

            drawText(METERS_UNIT, 6F, 12F, chartAxesPaint)
            drawText(TIME_UNIT, width - 10F, height - 10F, chartAxesPaint)
        }
    }
}