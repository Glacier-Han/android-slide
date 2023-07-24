package com.glacier.androidslide.api

import com.glacier.androidslide.model.JsonSlides
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface JsonSlideApi {
    @GET("jk/softeer-bootcamp/image-slides.json")
    fun getImageSlides() : Call<JsonSlides>

    @GET("jk/softeer-bootcamp/square-only-slides.json")
    fun getSquareSlides() : Call<JsonSlides>

    @GET
    fun getImageByteArray(@Url imageUrl: String): Call<ResponseBody>
}