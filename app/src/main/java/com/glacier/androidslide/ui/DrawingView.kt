package com.glacier.androidslide.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.glacier.androidslide.R
import com.glacier.androidslide.data.model.DrawingSlide
import com.glacier.androidslide.data.model.Point
import com.glacier.androidslide.util.UtilManager

class DrawingView(context: Context?, attrs: AttributeSet? = null) : View(context, attrs) {

    init {
        context?.theme?.obtainStyledAttributes(
            attrs, R.styleable.DrawingView, 0, 0
        )
    }

    private var _slide: DrawingSlide? = null
    private var rectangle = RectF()
    private var drawingPaint = setDrawColor()
    private val _path = Path()

    private val rectPaint = Paint().apply {
        color = Color.BLACK
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 4f
    }

    fun setSlide(slide: DrawingSlide) {
        _path.reset()
        if(slide.path.isNotEmpty()){
            _path.moveTo(slide.path[0].x, slide.path[0].y)
            for(point in slide.path){
                _path.lineTo(point.x, point.y)
            }
        }
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
        _slide?.let { slide ->
            canvas.drawPath(_path, drawingPaint)
            canvas.drawRect(rectangle, rectPaint)
            invalidate()
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touchX = event.x
        val touchY = event.y

        _slide?.let { slide ->
            if (slide.isDrawable) {

                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        slide.path.add(Point(touchX, touchY))
                        _path.moveTo(touchX, touchY)
                        slide.rectStartX = touchX
                        slide.rectStartY = touchY
                        slide.rectEndX = touchX
                        slide.rectEndY = touchY
                        invalidate()
                    }

                    MotionEvent.ACTION_MOVE -> {
                        slide.path.add(Point(touchX, touchY))
                        _path.lineTo(touchX, touchY)
                        slide.rectStartX = minOf(slide.rectStartX, touchX)
                        slide.rectStartY = minOf(slide.rectStartY, touchY)
                        slide.rectEndX = maxOf(slide.rectEndX, touchX)
                        slide.rectEndY = maxOf(slide.rectEndY, touchY)
                        invalidate()
                    }

                    MotionEvent.ACTION_UP -> {
                        slide.path.add(Point(touchX, touchY))
                        _path.lineTo(touchX, touchY)
                        slide.isDrawable = false
                        drawRect(slide.rectStartX, slide.rectStartY, slide.rectEndX, slide.rectEndY)
                        invalidate()
                    }
                }
            } else {
                if (event.action == MotionEvent.ACTION_UP && rectangle.contains(touchX, touchY)) {
                    drawingPaint = setDrawColor()
                    slide.paint = drawingPaint
                }
            }
        }
        return true
    }

    fun drawRect(rectStartX: Float, rectStartY: Float, rectEndX: Float, rectEndY: Float) {
        rectangle = RectF(rectStartX, rectStartY, rectEndX, rectEndY)
    }
}
