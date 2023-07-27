package com.glacier.androidslide.data.model

import com.glacier.androidslide.data.enums.SlideType

sealed interface Slide {
    val type: SlideType
    val id: String
    val size: Int
    val alpha: Int
    val selected: Boolean
}