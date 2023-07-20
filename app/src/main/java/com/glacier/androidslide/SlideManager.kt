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

    fun getAllSlides(): List<SquareSlide> {
        return slideList
    }

    fun getSlideByIndex(index: Int): SquareSlide? {
        return slideList.getOrElse(index) { null }
    }

    fun editSlideColor(index: Int, r: Int, g: Int, b: Int) {
        if (slideList.isNotEmpty()) {
            val slide = slideList[index]
            val updatedSlide = slide.copy(r = r, g = g, b = b)
            slideList[index] = updatedSlide
        }
    }

    fun editSlideAlpha(index: Int, alpha: Int) {
        if (slideList.isNotEmpty()) {
            val slide = slideList[index]
            val updatedSlide = slide.copy(alpha = alpha)
            slideList[index] = updatedSlide
        }
    }

    fun setNowSlideSelected(index: Int, selected: Boolean) {
        if (slideList.isNotEmpty()) {
            val slide = slideList[index]
            val updatedSlide = slide.copy(selected = selected)
            slideList[index] = updatedSlide
        }
    }
}
