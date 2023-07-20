package com.glacier.androidslide.model

import com.glacier.androidslide.util.SlideType

data class SquareSlide(
    override val id: String,
    override val side: Int,
    override val alpha: Int,
    override val selected: Boolean,
    val r: Int,
    val g: Int,
    val b: Int,

) : Slide {
    override val slideType: SlideType = SlideType.SQUARE
    override fun toString(): String {
        return "$slideType - ($id), Side:$side, R:$r, G:$g, B:$b, Alpha:$alpha, Selected:$selected"
    }
}