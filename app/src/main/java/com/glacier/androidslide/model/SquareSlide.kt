package com.glacier.androidslide.model

data class SquareSlide(
    val id: String,
    val side: Int,
    val r: Int,
    val g: Int,
    val b: Int,
    val alpha: Int,
    val selected: Boolean = true
) {
    override fun toString(): String {
        return "($id), Side:$side, R:$r, G:$g, B:$b, Alpha:$alpha, Selected:$selected"
    }
}