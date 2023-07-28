package com.glacier.androidslide.model

import com.glacier.androidslide.data.enums.SlideType

interface Slide {
    val type: SlideType
    val id: String
    val size: Int
    val alpha: Int
    val selected: Boolean
}
