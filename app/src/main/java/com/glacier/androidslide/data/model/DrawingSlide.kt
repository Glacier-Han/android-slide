package com.glacier.androidslide.data.model

import android.graphics.Paint
import com.glacier.androidslide.data.enums.SlideType
import com.google.gson.Gson

data class DrawingSlide(
    override val id: String,
    override val size: Int,
    override val alpha: Int,
    override val selected: Boolean,
    val path: MutableList<Point>,
    var isDrawable: Boolean,
    var paint: Paint,
    var rectStartX: Float,
    var rectStartY: Float,
    var rectEndX: Float,
    var rectEndY: Float
) : Slide {
    override val type: SlideType = SlideType.DRAWING

    override fun toString(): String {
        return "$type - ($id), Side:$size, Alpha:$alpha, Selected:$selected, Rect: $rectStartX, $rectStartY, $rectEndX, $rectEndY ${path}."
    }
}