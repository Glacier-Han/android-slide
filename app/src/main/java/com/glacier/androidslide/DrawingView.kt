package com.glacier.androidslide

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.glacier.androidslide.model.DrawingSlide
import com.glacier.androidslide.util.UtilManager

class DrawingView(context: Context?, attrs: AttributeSet? = null) : View(context, attrs) {

    init {
        context?.theme?.obtainStyledAttributes(
            attrs, R.styleable.DrawingView, 0, 0
        )
    }

    private var _slide: DrawingSlide? = null
    private var rectangle = RectF()
    private val drawingPaint = setDrawColor()

    private val rectPaint = Paint().apply {
        color = Color.BLACK
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 4f
    }

    fun setSlide(slide: DrawingSlide) {
        rectangle = RectF()
        _slide = slide
        drawRect(slide.rectStartX, slide.rectStartY, slide.rectEndX, slide.rectEndY)
        invalidate()
    }

    fun setDrawColor(): Paint {
        return Paint().apply {
            color = Color.rgb(
                UtilManager.getRandomColor()[0],
                UtilManager.getRandomColor()[1],
                UtilManager.getRandomColor()[2]
            )
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = 5f
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        _slide?.let {slide ->
            canvas.drawPath(slide.path, drawingPaint)
            canvas.drawRect(rectangle, rectPaint)
            invalidate()
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        _slide?.let { slide ->
            if (slide.isDrawable) {
                val touchX = event.x
                val touchY = event.y

                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        slide.path.moveTo(touchX, touchY)
                        slide.rectStartX = touchX
                        slide.rectStartY = touchY
                        slide.rectEndX = touchX
                        slide.rectEndY = touchY
                        invalidate()
                    }

                    MotionEvent.ACTION_MOVE -> {
                        slide.path.lineTo(touchX, touchY)
                        slide.rectStartX = minOf(slide.rectStartX, touchX)
                        slide.rectStartY = minOf(slide.rectStartY, touchY)
                        slide.rectEndX = maxOf(slide.rectEndX, touchX)
                        slide.rectEndY = maxOf(slide.rectEndY, touchY)
                        invalidate()
                    }

                    MotionEvent.ACTION_UP -> {
                        slide.path.lineTo(touchX, touchY)
                        slide.isDrawable = false
                        drawRect(slide.rectStartX, slide.rectStartY, slide.rectEndX, slide.rectEndY)
                        invalidate()
                    }
                }
            }
        }
        return true
    }

    fun drawRect(rectStartX: Float, rectStartY: Float, rectEndX: Float, rectEndY: Float) {
        rectangle = RectF(rectStartX, rectStartY, rectEndX, rectEndY)
    }
}
