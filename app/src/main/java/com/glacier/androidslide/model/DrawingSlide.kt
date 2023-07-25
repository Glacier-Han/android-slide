package com.glacier.androidslide.model

import android.graphics.Path
import com.glacier.androidslide.DrawingView
import com.glacier.androidslide.util.SlideType

data class DrawingSlide(
    override val id: String,
    override val size: Int,
    override val alpha: Int,
    override val selected: Boolean,
    val drawingView: DrawingView?
) : Slide {
    override val type: SlideType = SlideType.DRAWING
    override fun toString(): String {
        return "$type - ($id), Side:$size, Alpha:$alpha, Selected:$selected,"
    }
}