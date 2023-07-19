package com.glacier.androidslide.model

data class ImageSlide(
    val id: String,
    var side: Int,
    var image: String,
    var alpha: Int,
    var selected: Boolean = true
) {
    override fun toString(): String {
        return "($id), Side:$side, image:$image, Alpha:$alpha, Selected:$selected"
    }
}