package com.glacier.androidslide.listener

import com.glacier.androidslide.model.Slide
import com.glacier.androidslide.util.SlideType

interface OnSlideDoubleClickListener {
    fun onSlideDoubleClicked(position: Int, slide: Slide)
}