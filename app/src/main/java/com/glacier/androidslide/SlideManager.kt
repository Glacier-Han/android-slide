package com.glacier.androidslide

import com.glacier.androidslide.model.Slide
import com.glacier.androidslide.model.SquareSlide
import com.glacier.androidslide.model.SquareSlideViewFactory
import com.glacier.androidslide.util.SlideType

class SlideManager() {
    private val slideList: MutableList<Slide> = mutableListOf()

    fun createSlide(r: Int, g: Int, b: Int, alpha: Int, slideType: SlideType): Slide {
        val newSlide = SquareSlideViewFactory.createItem(r, g, b, alpha, slideType)
        slideList.add(newSlide)
        return newSlide
    }

    fun getSlideCount(): Int {
        return slideList.size
    }

    fun getAllSlides(): List<Slide> {
        return slideList
    }

    fun getSlideByIndex(index: Int): Slide? {
        return if (slideList.size > 0) {
            slideList[index]
        } else {
            null
        }
    }

    fun editSquareSlideColor(index: Int, r: Int, g: Int, b: Int) {
        if (slideList.isNotEmpty()) {
            val slide = slideList[index] as SquareSlide
            with(slide) {
                R = r
                G = g
                B = b
            }
        }
    }

    fun editSlideAlpha(index: Int, alpha: Int) {
        if (slideList.isNotEmpty()) {
            slideList[index].alpha = alpha
        }
    }

    fun setNowSlideSelected(index: Int, selected: Boolean) {
        if (slideList.isNotEmpty()) {
            slideList[index].selected = selected
        }
    }
}
