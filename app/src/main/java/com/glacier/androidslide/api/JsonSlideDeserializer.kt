package com.glacier.androidslide.api

<<<<<<< HEAD
import android.graphics.Paint
import android.graphics.Path
import com.glacier.androidslide.data.model.DrawingSlide
import com.glacier.androidslide.data.model.ImageSlide
import com.glacier.androidslide.data.model.Point
=======
import com.glacier.androidslide.data.model.ImageSlide
>>>>>>> 39563cc89fb526ba1d896a39a4342e94737f800b
import com.glacier.androidslide.data.model.Slide
import com.glacier.androidslide.data.model.SlideColor
import com.glacier.androidslide.data.model.SquareSlide
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
        return when (val slideType = jsonObject?.get("type")?.asString?.uppercase()) {
            "IMAGE" -> context?.deserialize(jsonObject, ImageSlide::class.java) ?: ImageSlide(
                "",
                0,
                0,
                false,
                byteArrayOf(),
                ""
            )

            "SQUARE" -> context?.deserialize(jsonObject, SquareSlide::class.java) ?: SquareSlide(
                "",
                0,
                0,
                false,
                SlideColor(0, 0, 0)
            )

            "DRAWING" -> context?.deserialize(jsonObject, DrawingSlide::class.java) ?: DrawingSlide(
                "",
                0,
                0,
                false,
                mutableListOf(),
                true,
                Paint(),
                0f,0f,0f,0f
            )

            else -> throw JsonParseException("ERR: NO $slideType MODEL")
        }
    }
}