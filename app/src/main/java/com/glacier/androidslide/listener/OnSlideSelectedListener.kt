package com.glacier.androidslide.listener

import com.glacier.androidslide.model.Slide

interface OnSlideSelectedListener {
    fun onSlideSelected(position: Int, slide: Slide)
}