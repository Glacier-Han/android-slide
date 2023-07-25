package com.glacier.androidslide.model

import android.graphics.Paint
import android.graphics.Path
import com.glacier.androidslide.DrawingView
import com.glacier.androidslide.util.SlideType
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