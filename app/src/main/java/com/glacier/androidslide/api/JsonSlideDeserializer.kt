package com.glacier.androidslide.api

import com.glacier.androidslide.model.ImageSlide
import com.glacier.androidslide.model.Slide
import com.glacier.androidslide.model.SlideColor
import com.glacier.androidslide.model.SquareSlide
import com.glacier.androidslide.util.SlideType
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type

class JsonSlideDeserializer : JsonDeserializer<Slide> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Slide {
        val jsonObject = json?.asJsonObject
        return when (val slideType = jsonObject?.get("type")?.asString) {
            "Image" -> context?.deserialize(jsonObject, ImageSlide::class.java) ?: ImageSlide(
                "",
                0,
                0,
                false,
                byteArrayOf(),
                ""
            )


            "Square" -> context?.deserialize(jsonObject, SquareSlide::class.java) ?: SquareSlide(
                "",
                0,
                0,
                false,
                SlideColor(0, 0, 0)
            )

            else -> throw JsonParseException("ERR: NO $slideType MODEL")
        }
    }
}