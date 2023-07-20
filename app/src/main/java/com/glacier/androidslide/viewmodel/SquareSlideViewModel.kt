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

    private val _nowSlide = MutableLiveData<SquareSlide>()
    val nowSlide = _nowSlide

    init {
        _nowAlpha.value = 10
        _nowSlideIndex.value = 0
    }

    fun getNowSlide(): SquareSlide? {
        _nowSlideIndex.value?.let { index ->
            _nowSlide.value = slideManager.getSlideByIndex(index)
            return _nowSlide.value
        } ?: return null
    }

    fun editAlpha(mode: Mode) {
        _nowSlideIndex.value?.let { index ->
            when (mode) {
                Mode.MINUS -> {
                    if (_nowAlpha.value!! > 1) {
                        _nowAlpha.value = _nowAlpha.value!! - 1
                        slideManager.editSlideAlpha(
                            index,
                            UtilManager.getAlphaMode(_nowAlpha.value!!)
                        )
                    }
                }

                Mode.PLUS -> {
                    if (_nowAlpha.value!! < 10) {
                        _nowAlpha.value = _nowAlpha.value!! + 1
                        slideManager.editSlideAlpha(
                            index,
                            UtilManager.getAlphaMode(_nowAlpha.value!!)
                        )
                    }
                }
            }
            getNowSlide()
        }
    }

    fun setNowSlideSelected(selected: Boolean) {
        _nowSlideIndex.value?.let { index ->
            slideManager.setNowSlideSelected(index, selected)
            getNowSlide()
        }
    }

    fun editColorRandom() {
        _nowSlideIndex.value?.let { index ->
            slideManager.editSlideColor(
                index,
                UtilManager.getRandomColor()[0],
                UtilManager.getRandomColor()[1],
                UtilManager.getRandomColor()[2]
            )
            getNowSlide()
        }
    }

    fun setNewSlide() {
        _nowAlpha.value?.let { alpha ->
            slideManager.createSlide(
                UtilManager.getRandomColor()[0],
                UtilManager.getRandomColor()[1],
                UtilManager.getRandomColor()[2],
                UtilManager.getAlphaMode(alpha)
            )

            _nowSlideIndex.value = slideManager.getSlideCount() - 1
            getNowSlide()
        }


    }
}