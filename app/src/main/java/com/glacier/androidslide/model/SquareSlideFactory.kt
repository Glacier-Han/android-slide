package com.glacier.androidslide.model

import com.glacier.androidslide.util.SlideType


interface SquareSlideFactory {
    fun createItem(r: Int, g: Int, b: Int, alpha: Int, slideType: SlideType): Slide
}
