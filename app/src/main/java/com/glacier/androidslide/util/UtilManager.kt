package com.glacier.androidslide.util

import com.glacier.androidslide.data.model.ImageSlide
import com.glacier.androidslide.data.model.SquareSlide
import kotlin.random.Random


class UtilManager {
    companion object {

        private val alphaMap: Map<Int, Int> = mapOf(
            1 to 25,
            2 to 51,
            3 to 76,
            4 to 102,
            5 to 127,
            6 to 153,
            7 to 178,
            8 to 204,
            9 to 229,
            10 to 255
        )

        fun generateID(length: Int): String {
            val charset = "abcdefghijklmnopqrstuvwsyz0123456789"
            val randomString = (1..length)
                .map { charset[Random.nextInt(charset.length)] }
                .joinToString("")

            // chunk -> 리스트 하나를 주어진 size의 리스트로 나누는 함수
            return randomString.chunked(3).joinToString("-")
        }

        fun getRandomColor(): List<Int> {
            val r = Random.nextInt(0, 256)
            val g = Random.nextInt(0, 256)
            val b = Random.nextInt(0, 256)
            return listOf(r, g, b)
        }

        fun rgbToHex(r: Int, g: Int, b: Int): String {
            val hexR = Integer.toHexString(r).uppercase()
            val hexG = Integer.toHexString(g).uppercase()
            val hexB = Integer.toHexString(b).uppercase()

            return "0x" + hexR.padStart(2, '0') + hexG.padStart(2, '0') + hexB.padStart(2, '0')
        }

        fun getModeToAlpha(mode: Int): Int {
            return alphaMap[mode]!!
        }

        fun getAlphaToMode(alpha: Int): Int {
            val reversedAlphaMap: Map<Int, Int> = alphaMap.entries.associate { (k, v) -> v to k }
            return  reversedAlphaMap[alpha]!!
        }

        fun SlideManager.copy(): SlideManager {
            val copiedSlideList = this.getAllSlides().map { slide ->
                when (slide) {
                    is SquareSlide -> slide.copy()
                    is ImageSlide -> slide.copy()
                    else -> slide
                }
            }.toMutableList()

            val copiedManager = SlideManager()
            copiedManager.slideList.addAll(copiedSlideList)
            return copiedManager
        }
    }
}