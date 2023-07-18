package com.glacier.androidslide.model


interface SquareSlideFactory {
    fun createItem(r: Int, g: Int, b: Int, alpha: Int): SquareSlide
}
