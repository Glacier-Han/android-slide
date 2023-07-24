package com.glacier.androidslide.model

import com.glacier.androidslide.util.SlideType
import com.google.gson.annotations.SerializedName

data class SquareSlide(
    override val id: String,
    override val size: Int,
    override val alpha: Int,
    override val selected: Boolean,
    val color: SlideColor

    ) : Slide {
    override val type: SlideType = SlideType.SQUARE
    override fun toString(): String {
        return "$type - ($id), Side:$size, R:${color.r}, G:${color.g}, B:${color.b}, Alpha:$alpha, Selected:$selected"
    }
}