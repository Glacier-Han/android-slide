package com.glacier.androidslide.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.glacier.androidslide.SlideManager
import com.glacier.androidslide.api.JsonSlideApi
import com.glacier.androidslide.api.RetrofitInstance
import com.glacier.androidslide.model.ImageSlide
import com.glacier.androidslide.model.JsonSlides
import com.glacier.androidslide.model.Slide
import com.glacier.androidslide.model.SquareSlide
import com.glacier.androidslide.util.Mode
import com.glacier.androidslide.util.SlideType
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


    fun getNowSlide() {
        _nowSlide.value = slideManager.getSlideByIndex(nowSlideIndex)
    }

    fun getSlideWithIndex(index: Int): Slide? {
        nowSlideIndex = index
        _nowSlide.value = slideManager.getSlideByIndex(index)
        nowAlpha = UtilManager.getAlphaToMode(_nowSlide.value?.alpha!!)
        return _nowSlide.value
    }

    fun getSlides(): List<Slide> {
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

        getNowSlide()
        getSlides()
    }

    fun setNowSlideSelected(selected: Boolean) {
        slideManager.setNowSlideSelected(nowSlideIndex, selected)
        getNowSlide()
    }

    fun editColorRandom() {
        slideManager.editSquareSlideColor(
            nowSlideIndex,
            UtilManager.getRandomColor()[0],
            UtilManager.getRandomColor()[1],
            UtilManager.getRandomColor()[2]
        )
        slides.postValue(slides.value)
        getNowSlide()

    }

    fun setNewSlide(slideType: SlideType) {
        slideManager.createSlide(
            UtilManager.getRandomColor()[0],
            UtilManager.getRandomColor()[1],
            UtilManager.getRandomColor()[2],
            255,
            ByteArray(0),
            slideType
        )

        nowSlideIndex = slideManager.getSlideCount() - 1
        getNowSlide()
        getSlides()
    }

    fun editSlideImage(index: Int, image: ByteArray) {
        slideManager.editSlideImage(index, image)
        getNowSlide()
        getSlides()
    }

    fun getJsonSlides() {
        val api = RetrofitInstance.getInstance().create(JsonSlideApi::class.java)
        val apiMode = listOf(api.getSquareSlides(), api.getImageSlides())
        apiMode.random().enqueue(object : Callback<JsonSlides> {
            override fun onResponse(call: Call<JsonSlides>, response: Response<JsonSlides>) {
                Log.d("DBG::RETROFIT", "res: ")
                response.body()?.slides?.let { slides ->
                    Log.d("DBG::RETROFIT", "res: $slides")
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

                    getNowSlide()
                    getSlides()
                }

            }

            override fun onFailure(call: Call<JsonSlides>, t: Throwable) {
                Log.d("DBG::RETROFIT", "err: $t")
            }
        })
    }

    fun loadImageUrlToByteArray(imageUrl: String) {
        val api = RetrofitInstance.getInstance().create(JsonSlideApi::class.java)
        api.getImageByteArray(imageUrl).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    response.body()?.bytes()?.let { image ->
                        slideManager.createSlide(0, 0, 0, 255, image, SlideType.IMAGE)
                        getNowSlide()
                        getSlides()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("DBG::RETROFIT", "err: $t")
            }
        })
    }
}