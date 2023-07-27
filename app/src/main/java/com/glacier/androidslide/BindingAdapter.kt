package com.glacier.androidslide

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.glacier.androidslide.adapter.SlideAdapter
import com.glacier.androidslide.data.model.DrawingSlide
import com.glacier.androidslide.data.model.ImageSlide
import com.glacier.androidslide.data.model.Slide
import com.glacier.androidslide.data.model.SquareSlide
import com.glacier.androidslide.listener.OnSlideDoubleClickListener
import com.glacier.androidslide.util.ItemMoveCallback
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
    Log.d("TEST", "test")

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

@BindingAdapter("slide")
fun setDrawingView(view: DrawingView, slide: Slide){
    if(slide is DrawingSlide){
        view.setSlide(slide)
    }
}

@BindingAdapter("itemMover")
fun setItemHelper(view: RecyclerView, moverCallback: ItemMoveCallback) {
    val helper = ItemTouchHelper(moverCallback)
    helper.attachToRecyclerView(view)
}

@BindingAdapter("setAdapter", "activity")
fun setAdapter(view: RecyclerView, slides: MutableList<Slide>, activity: MainActivity) {
    if (view.adapter == null) {
        view.adapter = SlideAdapter(slides, activity)
    }
    view.adapter?.notifyItemInserted(view.adapter!!.itemCount - 1)
    view.scrollToPosition(view.adapter!!.itemCount - 1)
}

@BindingAdapter("bgColor")
fun setBgColor(view: AppCompatButton, slide: Slide) {
    if(slide is SquareSlide){
        view.text = UtilManager.rgbToHex(slide.color.r, slide.color.g, slide.color.b)
        view.backgroundTintList =
            ColorStateList.valueOf(Color.argb(slide.alpha, slide.color.r, slide.color.g, slide.color.b))
    }else{
        view.text = ""
        view.backgroundTintList =
            ColorStateList.valueOf(Color.LTGRAY)
    }
}

@BindingAdapter("slide")
fun setAlphaMonitor(view: TextView, slide: Slide){
    view.text = UtilManager.getAlphaToMode(slide.alpha).toString()
}

@BindingAdapter("onDoubleClick", "activity", "slide")
fun setOnDoubleClickListener(view: View, onDoubleClick: () -> Unit, onSlideDoubleClickListener: OnSlideDoubleClickListener, slide: Slide) {
    if(slide is ImageSlide){
        val gestureDetector = GestureDetector(view.context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDoubleTap(e: MotionEvent): Boolean {
                onDoubleClick.invoke()
                onSlideDoubleClickListener.onSlideDoubleClicked(slide)
                return true
            }
        })

        view.setOnTouchListener { v, event ->
            gestureDetector.onTouchEvent(event)
            true
        }
    }
}

