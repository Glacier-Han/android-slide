package com.glacier.androidslide.util

import com.glacier.androidslide.data.enums.SlideType
import com.glacier.androidslide.data.factory.SquareSlideViewFactory
import com.glacier.androidslide.data.model.ImageSlide
import com.glacier.androidslide.data.model.Slide
import com.glacier.androidslide.data.model.SlideColor
import com.glacier.androidslide.data.model.SquareSlide

class SlideManager() {
    var slideList: MutableList<Slide> = mutableListOf()

    fun createSlide(r: Int, g: Int, b: Int, alpha: Int, image: ByteArray, slideType: SlideType): Slide {
        val newSlide = SquareSlideViewFactory.createItem(r, g, b, alpha, image, slideType)
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
        return slideList.getOrNull(index)
    }

    fun editSquareSlideColor(index: Int, r: Int, g: Int, b: Int) {
        if (slideList.isNotEmpty()) {
            val slide = slideList[index] as SquareSlide
            val updatedSlide = slide.copy(color = SlideColor(r = r, g = g, b = b))
            slideList[index] = updatedSlide
        }
    }

    fun editSlideAlpha(index: Int, alpha: Int) {
        if (slideList.isNotEmpty()) {
            val slide = slideList[index]
            if (slide is SquareSlide) {
                slideList[index] = slide.copy(alpha = alpha)
            } else if (slide is ImageSlide) {
                slideList[index] = slide.copy(alpha = alpha)
            }
        }
    }

    fun setNowSlideSelected(index: Int, selected: Boolean) {
        if (slideList.isNotEmpty()) {
            val slide = slideList[index]
            if (slide is SquareSlide) {
                slideList[index] = slide.copy(selected = selected)
            } else if (slide is ImageSlide) {
                slideList[index] = slide.copy(selected = selected)
            }
        }
    }

    fun editSlideImage(index: Int, image: ByteArray) {
        if (slideList.isNotEmpty()) {
            val slide = slideList[index] as ImageSlide
            slideList[index] = slide.copy(image = image)
        }
    }

    fun resetSlide(){
        slideList.clear()
    }
}
