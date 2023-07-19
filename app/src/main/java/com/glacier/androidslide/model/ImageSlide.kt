package com.glacier.androidslide.model

import com.glacier.androidslide.util.SlideType

data class ImageSlide(
    override val id: String,
    override var side: Int,
    override var alpha: Int,
    override var selected: Boolean,
    var image: String,
) : Slide {
    override val slideType: SlideType = SlideType.IMAGE
    override fun toString(): String {
        return "$slideType - ($id), Side:$side, Image:$image, Alpha:$alpha, Selected:$selected"
    }
}