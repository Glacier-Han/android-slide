package com.glacier.androidslide

import com.glacier.androidslide.model.SquareSlide
import com.glacier.androidslide.model.SquareSlideViewFactory

class SlideManager() {
    private val slideList: MutableList<SquareSlide> = mutableListOf()

    fun createSlide(r: Int, g: Int, b: Int, alpha: Int): SquareSlide {
        val newSlide = SquareSlideViewFactory.createItem(r, g, b, alpha)
        slideList.add(newSlide)
        return newSlide
    }

    fun getSlideCount(): Int {
        return slideList.size
    }

    fun getSlideByIndex(index: Int): SquareSlide? {
        return if (slideList.size > 0) {
            slideList[index]
        } else {
            null
        }
    }

    fun editSlideColor(index: Int, r: Int, g: Int, b: Int) {
        if (slideList.isNotEmpty()) {
            slideList[index].R = r
            slideList[index].G = g
            slideList[index].B = b
        }
    }

    fun editSlideAlpha(index: Int, alpha: Int) {
        if (slideList.isNotEmpty()) {
            slideList[index].alpha = alpha
        }
    }
}