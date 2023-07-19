package com.glacier.androidslide.model

import com.glacier.androidslide.util.SlideType

data class SquareSlide(
    override val id: String,
    override var side: Int,
    override var alpha: Int,
    override var selected: Boolean,
    var R: Int,
    var G: Int,
    var B: Int,

) : Slide {
    override val slideType: SlideType = SlideType.SQUARE
    override fun toString(): String {
        return "$slideType - ($id), Side:$side, R:$R, G:$G, B:$B, Alpha:$alpha, Selected:$selected"
    }
}