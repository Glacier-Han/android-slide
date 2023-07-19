package com.glacier.androidslide.model

import com.glacier.androidslide.util.SlideType

interface Slide {
    val slideType: SlideType
    val id: String
    var side: Int
    var alpha: Int
    var selected: Boolean
}
