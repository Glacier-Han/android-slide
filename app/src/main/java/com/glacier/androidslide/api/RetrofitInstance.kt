package com.glacier.androidslide.api

import com.glacier.androidslide.data.model.Slide
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitInstance {

    private const val BASE_URL = "https://public.codesquad.kr/"

    private val gson = GsonBuilder()
        .registerTypeAdapter(Slide::class.java, JsonSlideDeserializer())
        .create()

    private val client = Retrofit
        .Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    fun getInstance(): Retrofit {
        return client
    }

}