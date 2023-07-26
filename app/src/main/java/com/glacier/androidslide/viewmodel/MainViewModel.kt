package com.glacier.androidslide.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.glacier.androidslide.SlideManager
import com.glacier.androidslide.adapter.SlideAdapter
import com.glacier.androidslide.api.JsonSlideApi
import com.glacier.androidslide.api.RetrofitInstance
import com.glacier.androidslide.data.enums.Mode
import com.glacier.androidslide.data.enums.SlideType
import com.glacier.androidslide.data.model.ImageSlide
import com.glacier.androidslide.data.model.JsonSlides
import com.glacier.androidslide.data.model.Slide
import com.glacier.androidslide.data.model.SquareSlide
import com.glacier.androidslide.listener.OnSlideSelectedListener
import com.glacier.androidslide.util.UtilManager
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val slideManager = SlideManager()

    var nowAlpha: Int = 10
    var nowSlideIndex: Int = 0

    private val _nowSlide = MutableLiveData<Slide>()
    val nowSlide = _nowSlide

    private val _slides = MutableLiveData<List<Slide>>()
    val slides = _slides

    val adapter by lazy { SlideAdapter(_slides.value as MutableList<Slide>, ::onSlideSelected) }

    fun getNowSlideData() {
        _nowSlide.value = slideManager.getSlideByIndex(nowSlideIndex)
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
        slides.postValue(slides.value)
        getNowSlideData()

    }

    fun setNewSlide() {
        slideManager.createSlide(
            UtilManager.getRandomColor()[0],
            UtilManager.getRandomColor()[1],
            UtilManager.getRandomColor()[2],
            255,
            ByteArray(0),
            SlideType.values().random()
        )

        nowSlideIndex = slideManager.getSlideCount() - 1
        getNowSlideData()
        getSlidesData()
    }

    fun editSlideImage(index: Int, image: ByteArray) {
        slideManager.editSlideImage(index, image)
        getNowSlideData()
        getSlidesData()
    }

    fun getJsonSlides(): Boolean {
        val api = RetrofitInstance.getInstance().create(JsonSlideApi::class.java)
        val apiMode = listOf(api.getSquareSlides(), api.getImageSlides())
        apiMode.random().enqueue(object : Callback<JsonSlides> {
            override fun onResponse(call: Call<JsonSlides>, response: Response<JsonSlides>) {
                response.body()?.slides?.let { slides ->
                    for (slide in slides) {
                        when (slide) {
                            is ImageSlide -> {
                                loadImageUrlToByteArray(slide.url)
                            }

                            is SquareSlide -> {
                                slideManager.createSlide(
                                    slide.color.r,
                                    slide.color.g,
                                    slide.color.b,
                                    255,
                                    ByteArray(0),
                                    SlideType.SQUARE
                                )
                            }
                        }
                    }

                    getNowSlideData()
                    getSlidesData()
                }

            }

            override fun onFailure(call: Call<JsonSlides>, t: Throwable) {
            }
        })
        return true
    }

    fun loadImageUrlToByteArray(imageUrl: String) {
        val api = RetrofitInstance.getInstance().create(JsonSlideApi::class.java)
        api.getImageByteArray(imageUrl).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    response.body()?.bytes()?.let { image ->
                        Log.d("TEST",image.toString())
                        slideManager.createSlide(0, 0, 0, 255, image, SlideType.IMAGE)
                        getNowSlideData()
                        getSlidesData()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            }
        })
    }

    private fun onSlideSelected(position: Int, slide: Slide) {
        getSlideWithIndex(position)
    }


}