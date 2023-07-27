package com.glacier.androidslide.util

import android.content.Context
import android.content.SharedPreferences
import com.glacier.androidslide.api.JsonSlideDeserializer
import com.glacier.androidslide.data.model.Slide
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

class SlideSaveManager(private val context: Context) {

    private val PREF_NAME = "SlideSavePreferences"
    private val KEY_SLIDE_SAVE = "slide_save"

    val gson = GsonBuilder()
        .registerTypeAdapter(Slide::class.java, JsonSlideDeserializer())
        .create()

    fun saveSlideList(slideList: List<Slide>) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("SlidePreferences", Context.MODE_PRIVATE)
        val jsonSlideList = gson.toJson(slideList)
        sharedPreferences.edit().putString("SlideList", jsonSlideList).apply()
    }

    fun getSlideList(): List<Slide> {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("SlidePreferences", Context.MODE_PRIVATE)
        val jsonSlideList = sharedPreferences.getString("SlideList", null)
        return if (jsonSlideList != null) {
            val type = object : TypeToken<List<Slide>>() {}.type
            gson.fromJson(jsonSlideList, type)
        } else {
            emptyList()
        }
    }
}