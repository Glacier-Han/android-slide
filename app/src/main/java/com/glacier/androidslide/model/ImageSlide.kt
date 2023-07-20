package com.glacier.androidslide.model

import com.glacier.androidslide.util.SlideType

data class ImageSlide(
    override val id: String,
    override val side: Int,
    override val alpha: Int,
    override val selected: Boolean,
    val image: ByteArray,
) : Slide {
    override val slideType: SlideType = SlideType.IMAGE
    override fun toString(): String {
        return "$slideType - ($id), Side:$side, Image:$image, Alpha:$alpha, Selected:$selected"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ImageSlide

        if (!image.contentEquals(other.image)) return false

        return true
    }

    override fun hashCode(): Int {
        return image.contentHashCode()
    }
}