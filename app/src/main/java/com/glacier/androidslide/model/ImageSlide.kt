package com.glacier.androidslide.model

import com.glacier.androidslide.util.SlideType
import com.google.gson.annotations.SerializedName

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