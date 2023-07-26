package com.glacier.androidslide

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.glacier.androidslide.adapter.SlideAdapter
import com.glacier.androidslide.data.model.DrawingSlide
import com.glacier.androidslide.data.model.ImageSlide
import com.glacier.androidslide.data.model.Slide
import com.glacier.androidslide.data.model.SquareSlide
import com.glacier.androidslide.listener.OnSlideDoubleClickListener
import com.glacier.androidslide.util.UtilManager
import jp.wasabeef.glide.transformations.ColorFilterTransformation

@BindingAdapter("colorTint")
fun setTint(view: ImageView, color: Int) {
    view.setColorFilter(color)
}

@BindingAdapter("setImage")
fun setImage(view: ImageView, image: ByteArray) {
    Glide.with(view.context).load(image).error(R.drawable.outline_image_24).override(50, 50)
        .into(view)
}

@BindingAdapter("doubleClickListener", "slide")
fun onDoubleClicked(
    view: ImageView,
    doubleClickListener: OnSlideDoubleClickListener,
    slide: Slide
) {
    Log.d("TEST", "TEST")
    val gestureDetector = GestureDetector(view.context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onDoubleTap(e: MotionEvent): Boolean {
            doubleClickListener.onSlideDoubleClicked(slide)
            return true
        }
    })

    view.setOnTouchListener { _, event ->
        gestureDetector.onTouchEvent(event)
    }

}

@BindingAdapter("setSlide")
fun setSlide(view: ImageView, slide: Slide) {
    when (slide) {
        is SquareSlide -> {
            view.visibility = View.VISIBLE
            val sColor = slide.color
            view.setImageDrawable(
                ColorDrawable(Color.argb(slide.alpha, sColor.r, sColor.g, sColor.b))
            )
        }

        is ImageSlide -> {
            view.visibility = View.VISIBLE
            Glide.with(view.context).load(slide.image)
                .transform(ColorFilterTransformation(Color.argb(255 - slide.alpha, 255, 255, 255)))
                .error(R.drawable.outline_image_24)
                .into(view)
        }

        is DrawingSlide -> {
            view.visibility = View.GONE
        }
    }
}

@BindingAdapter("setAdapter")
fun setAdapter(view: RecyclerView, adapter: SlideAdapter) {
    if (view.adapter == null) {
        view.adapter = adapter
    }
    view.adapter?.notifyItemInserted(adapter.itemCount - 1)
    view.scrollToPosition(adapter.itemCount - 1)
}

