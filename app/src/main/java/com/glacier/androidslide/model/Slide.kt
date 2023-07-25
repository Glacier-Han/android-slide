package com.glacier.androidslide.model

import com.glacier.androidslide.util.SlideType

interface Slide {
    val slideType: SlideType
    val id: String
    val side: Int
    val alpha: Int
    val selected: Boolean
}
