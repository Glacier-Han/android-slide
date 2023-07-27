package com.glacier.androidslide.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.glacier.androidslide.R
import com.glacier.androidslide.data.model.DrawingSlide
import com.glacier.androidslide.data.model.ImageSlide
import com.glacier.androidslide.data.model.Slide
import com.glacier.androidslide.data.model.SquareSlide
import com.glacier.androidslide.listener.OnSlideDoubleClickListener
import com.glacier.androidslide.ui.DrawingView
import com.glacier.androidslide.ui.MainActivity
import com.glacier.androidslide.util.ItemMoveCallback
import com.glacier.androidslide.util.UtilManager
import com.glacier.androidslide.viewmodel.MainViewModel
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
    val gestureDetector =
        GestureDetector(view.context, object : GestureDetector.SimpleOnGestureListener() {
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
    if(slide.selected){
        view.setBackgroundResource(R.drawable.border_black)
    }else{
        view.setBackgroundResource(R.drawable.border_null)
    }

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
fun setDrawingView(view: DrawingView, slide: Slide) {
    if (slide is DrawingSlide) {
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
    if (slide is SquareSlide) {
        view.text = UtilManager.rgbToHex(slide.color.r, slide.color.g, slide.color.b)
        view.backgroundTintList =
            ColorStateList.valueOf(
                Color.argb(
                    slide.alpha,
                    slide.color.r,
                    slide.color.g,
                    slide.color.b
                )
            )
    } else {
        view.text = ""
        view.backgroundTintList =
            ColorStateList.valueOf(Color.LTGRAY)
    }
}

@BindingAdapter("slide")
fun setAlphaMonitor(view: TextView, slide: Slide) {
    view.text = UtilManager.getAlphaToMode(slide.alpha).toString()
}

@BindingAdapter("onDoubleClick", "activity", "slide", "viewModel")
fun setOnDoubleClickListener(
    view: View,
    onDoubleClick: () -> Unit,
    onSlideDoubleClickListener: OnSlideDoubleClickListener,
    slide: Slide,
    viewModel: MainViewModel
) {
    val gestureDetector =
        GestureDetector(view.context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDoubleTap(e: MotionEvent): Boolean {
                if (slide is ImageSlide) {
                    onDoubleClick.invoke()
                    onSlideDoubleClickListener.onSlideDoubleClicked(slide)
                }
                return true
            }

            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                viewModel.setNowSlideSelected(true)
                return super.onSingleTapConfirmed(e)
            }
        })

    view.setOnTouchListener { _, event ->
        gestureDetector.onTouchEvent(event)
        true
    }
}

@BindingAdapter("syncSlide", "viewModel")
fun syncItemChanged(recyclerView: RecyclerView, syncSlide: Slide, viewModel: MainViewModel) {
    recyclerView.adapter?.let {
        val adapter = it as SlideAdapter
        adapter.notifyItemChanged(viewModel.slides.value?.indexOf(syncSlide)!!)
    }
}

@BindingAdapter("app:layout_constraintBottom_toBottomOf")
fun setBottomConstraint(view: ImageView, applyConstraint: Boolean) {
    val layoutParams = view.layoutParams as? ConstraintLayout.LayoutParams
    if (layoutParams != null) {
        if (applyConstraint) {
            layoutParams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
        } else {
            layoutParams.bottomToBottom = view.context.resources.getIdentifier("main_view", "id", view.context.packageName)
        }
        view.layoutParams = layoutParams
    }
}



