package com.glacier.androidslide

import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import kotlin.random.Random

class SlideManagerTest {

    private lateinit var slideManager: SlideManager

    @Before
    fun setUp() {
        slideManager = SlideManager()
    }

    @Test
    fun createSlide_Count_1() {
        slideManager.createSlide(0, 255, 0, 255)
        assertEquals(1, slideManager.getSlideCount())
    }

    @Test
    fun createSlide_Count_2() {
        slideManager.createSlide(0, 255, 0, 255)
        slideManager.createSlide(125, 255, 0, 200)
        assertEquals(2, slideManager.getSlideCount())
    }

    @Test
    fun createSlide_RandomIdIsVaild_False() {
        // 테스트를 위해 100000개 생성 후 각각 id가 다른지 비교
        for (i in 0..100000) {
            slideManager.createSlide(
                Random.nextInt(0, 255),
                Random.nextInt(0, 255),
                Random.nextInt(0, 255),
                Random.nextInt(0, 255)
            )
        }

        val idSet = mutableSetOf<String>()
        var isSameId = false

        for (slide in slideManager.getAllSlides()) {
            if (idSet.contains(slide.id)) {
                isSameId = true
            }
            idSet.add(slide.id)
        }

        assertEquals(false, isSameId)

    }

    @Test
    fun getSlideByIndex_Valid_True() {
        slideManager.createSlide(0, 255, 255, 255)
        val targetSlide = slideManager.getSlideByIndex(0)

        assertEquals(targetSlide?.alpha, 255)
        assertEquals(targetSlide?.R, 0)
        assertEquals(targetSlide?.G, 255)
        assertEquals(targetSlide?.B, 255)
    }

    @Test
    fun editSlideColor_Valid_True() {
        slideManager.createSlide(205, 255, 200, 255)
        slideManager.editSlideColor(0, 255, 200, 0)

        val target = slideManager.getSlideByIndex(0)
        assertEquals(target?.R, 255)
        assertEquals(target?.G, 200)
        assertEquals(target?.B, 0)
    }

    @Test
    fun editSlideAlpha_Valid_True() {
        slideManager.createSlide(205, 255, 200, 255)
        slideManager.editSlideAlpha(0, 125)

        val target = slideManager.getSlideByIndex(0)
        assertEquals(target?.alpha, 125)
    }
}