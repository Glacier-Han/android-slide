package com.glacier.androidslide.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.glacier.androidslide.SlideManager
import com.glacier.androidslide.model.Slide
import com.glacier.androidslide.util.Mode
import com.glacier.androidslide.util.SlideType
import com.glacier.androidslide.util.UtilManager

class MainViewModel : ViewModel() {
    private val slideManager = SlideManager()

    var nowAlpha: Int = 10
    var nowSlideIndex: Int = 0

    private val _nowSlide = MutableLiveData<Slide>()
    val nowSlide = _nowSlide

    private val _slides = MutableLiveData<List<Slide>>()
    val slides = _slides

    val adapter by lazy { SlideAdapter(_slides.value as MutableList<Slide>, ::onSlideSelected) }

    fun getNowSlide(): Slide? {
        return slideManager.getSlideByIndex(nowSlideIndex)
    }

    fun getSlideWithIndex(index: Int): Slide? {
        _nowSlide.value = slideManager.getSlideByIndex(index)
        nowSlideIndex = index
        nowAlpha = UtilManager.getAlphaToMode(_nowSlide.value?.alpha!!)
        getNowSlideData()
        return _nowSlide.value
    }

    fun getSlidesData(): List<Slide> {
        _slides.value = slideManager.getAllSlides()
        return _slides.value!!
    }

    fun setSlideIndex(index: Int) {
        nowSlideIndex = index
        getSlideWithIndex(index)
    }

    fun editAlpha(mode: Mode) {
        when (mode) {
            Mode.MINUS -> {
                if (nowAlpha > 1) {
                    nowAlpha -= 1
                    slideManager.editSlideAlpha(
                        nowSlideIndex,
                        UtilManager.getModeToAlpha(nowAlpha)
                    )
                }
            }

            Mode.PLUS -> {
                if (nowAlpha < 10) {
                    nowAlpha += 1
                    slideManager.editSlideAlpha(
                        nowSlideIndex,
                        UtilManager.getModeToAlpha(nowAlpha)
                    )
                }

            }
        }

        getNowSlideData()
        getSlidesData()
    }

    fun setNowSlideSelected(selected: Boolean) {
        slideManager.setNowSlideSelected(nowSlideIndex, selected)
        getNowSlideData()
    }

    fun editColorRandom() {
        slideManager.editSquareSlideColor(
            nowSlideIndex,
            UtilManager.getRandomColor()[0],
            UtilManager.getRandomColor()[1],
            UtilManager.getRandomColor()[2]
        )
        getNowSlide()

    }

    fun setNewSlide() {
        slideManager.createSlide(
            UtilManager.getRandomColor()[0],
            UtilManager.getRandomColor()[1],
            UtilManager.getRandomColor()[2],
            255,
            slideType
        )

        nowSlideIndex = slideManager.getSlideCount() - 1
        getNowSlideData()
        getSlidesData()
    }

    fun editSlideImage(index: Int, image: ByteArray){
        slideManager.editSlideImage(index, image)
        getNowSlideData()
        getSlidesData()
    }

}