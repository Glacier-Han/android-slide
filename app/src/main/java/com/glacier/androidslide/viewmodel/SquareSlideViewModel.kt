package com.glacier.androidslide.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.glacier.androidslide.SlideManager
import com.glacier.androidslide.model.SquareSlide
import com.glacier.androidslide.util.Mode
import com.glacier.androidslide.util.UtilManager

class SquareSlideViewModel : ViewModel() {
    private val slideManager = SlideManager()

    private val _nowAlpha = MutableLiveData<Int>()
    val nowAlpha = _nowAlpha

    private val _nowSlideIndex = MutableLiveData<Int>()
    val nowSlideIndex = _nowSlideIndex



    // 초기값 설정
    init {
        _nowAlpha.value = 10
        _nowSlideIndex.value = 0
    }

    fun getNowSlide(): SquareSlide?{
        return slideManager.getSlideByIndex(_nowSlideIndex.value!!)
    }

    fun editAlpha(mode: Mode) {
        when (mode) {
            Mode.MINUS -> {
                Log.d("TEST", "MINUS")
                if (_nowAlpha.value!! > 1) {
                    _nowAlpha.value = _nowAlpha.value!! - 1
                    slideManager.editSlideAlpha(
                        _nowSlideIndex.value!!,
                        UtilManager.getAlphaMode(_nowAlpha.value!!)
                    )
                }
            }

            Mode.PLUS -> {
                if (_nowAlpha.value!! < 10) {
                    _nowAlpha.value = _nowAlpha.value!! + 1
                    slideManager.editSlideAlpha(
                        _nowSlideIndex.value!!,
                        UtilManager.getAlphaMode(_nowAlpha.value!!)
                    )
                }
            }
        }
    }

    fun editColorRandom(){
        val randomR = UtilManager.getRandomColor()[0]
        val randomG = UtilManager.getRandomColor()[1]
        val randomB = UtilManager.getRandomColor()[2]

        slideManager.editSlideColor(_nowSlideIndex.value!!, randomR, randomG, randomB)
        //setSlideView(nowSlideIndex)
    }

    fun setNewSlide() {
        val randomR = UtilManager.getRandomColor()[0]
        val randomG = UtilManager.getRandomColor()[1]
        val randomB = UtilManager.getRandomColor()[2]

        slideManager.createSlide(randomR, randomG, randomB, UtilManager.getAlphaMode(_nowAlpha.value!!))
        _nowSlideIndex.value = slideManager.getSlideCount() - 1
    }
}