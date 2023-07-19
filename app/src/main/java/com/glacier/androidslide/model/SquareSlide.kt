package com.glacier.androidslide.model

data class SquareSlide(
    val id: String,
    var side: Int,
    var R: Int,
    var G: Int,
    var B: Int,
    var alpha: Int,
    var selected: Boolean = true
) : SlideData {
    override fun toString(): String {
        return "($id), Side:$side, R:$R, G:$G, B:$B, Alpha:$alpha, Selected:$selected"
    }
}