package com.glacier.androidslide.listener

import com.glacier.androidslide.model.Slide
import com.glacier.androidslide.util.SlideType

interface OnSlideSelectedListener {
    fun onSlideSelected(position: Int, slide: Slide)
}