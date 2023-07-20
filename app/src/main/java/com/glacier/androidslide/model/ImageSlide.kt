package com.glacier.androidslide.model

import com.glacier.androidslide.util.SlideType

data class ImageSlide(
    override val id: String,
    override val side: Int,
    override val alpha: Int,
    override val selected: Boolean,
    val image: String,
) : Slide {
    override val slideType: SlideType = SlideType.IMAGE
    override fun toString(): String {
        return "$slideType - ($id), Side:$side, Image:$image, Alpha:$alpha, Selected:$selected"
    }
}