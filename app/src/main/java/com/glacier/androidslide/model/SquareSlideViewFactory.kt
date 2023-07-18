package com.glacier.androidslide.model

import com.glacier.androidslide.util.UtilManager
import kotlin.random.Random

object SquareSlideViewFactory : SquareSlideFactory {
    override fun createItem(r: Int, g: Int, b: Int, alpha: Int): SquareSlide {
        val id = UtilManager.generateID(9)
        val side = Random.nextInt(0, 1000)

        return SquareSlide(id, side, r, g, b, alpha)
    }
}