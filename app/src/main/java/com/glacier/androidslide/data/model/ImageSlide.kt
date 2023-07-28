package com.glacier.androidslide.data.model

import com.glacier.androidslide.data.enums.SlideType

data class ImageSlide(
    override val id: String,
    override val size: Int,
    override val alpha: Int,
    override val selected: Boolean,
    val image: ByteArray,
    val url: String
) : Slide {
    override val type: SlideType = SlideType.IMAGE

    override fun toString(): String {
        return "$type - ($id), Side:$size, Image:$image, Alpha:$alpha, Selected:$selected, Url: $url"
    }
}