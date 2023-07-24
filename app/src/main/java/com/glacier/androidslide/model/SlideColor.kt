package com.glacier.androidslide.model

import com.google.gson.annotations.SerializedName

data class SlideColor(
    @SerializedName("R") val r: Int,
    @SerializedName("G") val g: Int,
    @SerializedName("B") val b: Int
)