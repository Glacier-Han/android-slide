package com.glacier.androidslidesofteer.model

import kotlin.random.Random


object SquareSlideViewFactory : SquareSlideFactory {
    override fun createItem(id_: String): SquareSlide {
        val side = Random.nextInt(0, 1000)
        val r = Random.nextInt(0, 255)
        val g = Random.nextInt(0, 255)
        val b = Random.nextInt(0, 255)
        val alpha = Random.nextInt(0, 10)

        return SquareSlide(id_, side, r, g, b, alpha)
    }
}