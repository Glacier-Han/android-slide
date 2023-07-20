package com.glacier.androidslidesofteer.model

data class SquareSlide(
    private val id: String,
    private val side: Int,
    private val R: Int,
    private val G: Int,
    private val B: Int,
    private val alpha: Int,
) {
    override fun toString(): String {
        return "($id), Side:$side, R:$R, G:$G, B:$B, Alpha:$alpha"
    }
}