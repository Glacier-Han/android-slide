package com.glacier.androidslide.data.factory

import com.glacier.androidslide.data.enums.SlideType
import com.glacier.androidslide.data.model.Slide


interface SquareSlideFactory {
    fun createItem(r: Int, g: Int, b: Int, alpha: Int, image: ByteArray, slideType: SlideType): Slide
}
