package com.glacier.androidslide.data.model

data class JsonSlides(
    val title: String,
    val creator: String,
    val slides: List<Slide>
)