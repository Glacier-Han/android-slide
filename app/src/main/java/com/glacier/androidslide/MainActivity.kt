package com.glacier.androidslide

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.glacier.androidslidesofteer.model.SquareSlide
import com.glacier.androidslidesofteer.model.SquareSlideViewFactory
import com.glacier.androidslidesofteer.util.UtilManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val squareSlides = mutableListOf<SquareSlide>()
        squareSlides.add(SquareSlideViewFactory.createItem(UtilManager.generateID(9)))
        squareSlides.add(SquareSlideViewFactory.createItem(UtilManager.generateID(9)))
        squareSlides.add(SquareSlideViewFactory.createItem(UtilManager.generateID(9)))
        squareSlides.add(SquareSlideViewFactory.createItem(UtilManager.generateID(9)))

        for (i in 0..3) {
            Log.d("DBG::SLIDE", "Rect${i + 1} ${squareSlides[i]}")
        }
    }
}