package com.glacier.androidslide

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.glacier.androidslide.util.UtilManager

class DrawingView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    init {
        context?.theme?.obtainStyledAttributes(
            attrs,
            R.styleable.DrawingView,
            0, 0
        )
    }

    private var checkDrawn = false
    private val path = Path()
    private val rectangles = mutableListOf<RectF>()
    private var rectStartX = Float.MAX_VALUE
    private var rectStartY = Float.MAX_VALUE
    private var rectEndX = Float.MIN_VALUE
    private var rectEndY = Float.MIN_VALUE

    private val drawingPaint = Paint().apply {
        color = Color.rgb(
            UtilManager.getRandomColor()[0],
            UtilManager.getRandomColor()[1],
            UtilManager.getRandomColor()[2]
        )
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 5f
    }

    private val rectPaint = Paint().apply {
        color = Color.BLACK
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 4f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawPath(path, drawingPaint)
        for (rect in rectangles) {
            canvas.drawRect(rect, rectPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!checkDrawn) {
            val touchX = event.x
            val touchY = event.y

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    path.moveTo(touchX, touchY)
                    rectStartX = touchX
                    rectStartY = touchY
                    rectEndX = touchX
                    rectEndY = touchY
                    invalidate()

                }

                MotionEvent.ACTION_MOVE -> {
                    path.lineTo(touchX, touchY)
                    rectStartX = minOf(rectStartX, touchX)
                    rectStartY = minOf(rectStartY, touchY)
                    rectEndX = maxOf(rectEndX, touchX)
                    rectEndY = maxOf(rectEndY, touchY)
                    invalidate()

                }

                MotionEvent.ACTION_UP -> {
                    path.lineTo(touchX, touchY)
                    checkDrawn = true
                    drawRect()
                    invalidate()
                }
            }
        }

        return true
    }

    private fun drawRect() {
        val rect = RectF(rectStartX, rectStartY, rectEndX, rectEndY)
        rectangles.add(rect)
    }

    fun resetDrawing() {
        rectangles.clear()
        invalidate()
    }
}
