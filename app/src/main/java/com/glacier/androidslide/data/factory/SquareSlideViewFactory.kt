package com.glacier.androidslide.data.factory

import android.graphics.Paint
import android.graphics.Path
import com.glacier.androidslide.data.enums.SlideType
import com.glacier.androidslide.data.model.DrawingSlide
import com.glacier.androidslide.data.model.ImageSlide
import com.glacier.androidslide.data.model.Slide
import com.glacier.androidslide.data.model.SlideColor
import com.glacier.androidslide.data.model.SquareSlide
import com.glacier.androidslide.util.UtilManager
import kotlin.random.Random

object SquareSlideViewFactory : SquareSlideFactory {
    override fun createItem(r: Int, g: Int, b: Int, alpha: Int, image: ByteArray, slideType: SlideType): Slide {
        val id = UtilManager.generateID(9)
        val side = Random.nextInt(0, 1000)

        return when(slideType){
            SlideType.SQUARE -> SquareSlide(id, side, alpha, true, SlideColor(r, g, b))
            SlideType.IMAGE -> ImageSlide(id, side, alpha, true, image, "")
            SlideType.DRAWING -> DrawingSlide(id, side, alpha, true, Path(),true, Paint(),0f,0f,0f,0f)
        }
    }
}