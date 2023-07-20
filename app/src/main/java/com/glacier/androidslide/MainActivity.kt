package com.glacier.androidslide

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.glacier.androidslide.adapter.SlideAdapter
import com.glacier.androidslide.databinding.ActivityMainBinding
import com.glacier.androidslide.listener.OnSlideSelectedListener
import com.glacier.androidslide.model.ImageSlide
import com.glacier.androidslide.model.Slide
import com.glacier.androidslide.util.Mode
import com.glacier.androidslide.util.UtilManager
import com.glacier.androidslide.model.SquareSlide
import com.glacier.androidslide.util.ItemMoveCallback
import com.glacier.androidslide.util.SlideType
import com.glacier.androidslide.viewmodel.SquareSlideViewModel

class MainActivity : AppCompatActivity(), OnClickListener, OnSlideSelectedListener {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val slideViewModel: SquareSlideViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        init()
    }

    fun init() {
        binding.ivSlide.setOnClickListener(this)
        binding.mainView.setOnClickListener(this)
        binding.btnBgcolor.setOnClickListener(this)
        binding.btnAlphaMinus.setOnClickListener(this)
        binding.btnAlphaPlus.setOnClickListener(this)
        binding.btnAddSlide.setOnClickListener(this)

        setObserver()
        //slideViewModel.setNewSlide(slideType = SlideType.SQUARE)
        setRecyclerView(slideViewModel.getSlides())
    }

    fun setObserver() {
        slideViewModel.nowSlide.observe(this) {
            setSlideView(it)
        }

        slideViewModel.slides.observe(this) {
            setRecyclerView(it)
        }
    }

    private fun setSlideView(slide: Slide) {
        slide.let {
            when (slide) {
                is SquareSlide -> {
                    it as SquareSlide
                    binding.ivSlide.setImageDrawable(
                        ColorDrawable(Color.argb(it.alpha, it.R, it.G, it.B))
                    )
                    binding.tvAlphaMonitor.text = UtilManager.getAlphaToMode(it.alpha).toString()
                    binding.btnBgcolor.text = UtilManager.rgbToHex(it.R, it.G, it.B)
                    setBgColorBtnColor(it.alpha, it.R, it.G, it.B)
                }

                is ImageSlide -> {
                    // TODO :: 추후 이미지 슬라이드 처리
                }
            }

        }
    }

    private fun setBgColorBtnColor(alpha: Int, R: Int, G: Int, B: Int) {
        binding.btnBgcolor.backgroundTintList =
            ColorStateList.valueOf(Color.argb(alpha, R, G, B))
    }

    private fun setRecyclerView(slides: List<Slide>) {
        val slideAdapter = SlideAdapter(slides as MutableList<Slide>, this@MainActivity)

        with(binding.rvSlides) {
            adapter = slideAdapter
            layoutManager =
                LinearLayoutManager(baseContext)

            val itemMoveCallback = ItemMoveCallback(slideAdapter)
            val itemTouchHelper = ItemTouchHelper(itemMoveCallback)
            itemTouchHelper.attachToRecyclerView(this)
        }
    }

    fun addMainSlideView(slideType: SlideType) {
        val imageView = ImageView(baseContext)
        when (slideType) {
            SlideType.SQUARE -> imageView.setImageDrawable(ColorDrawable(Color.BLUE))
            SlideType.IMAGE -> imageView.setImageDrawable(null)
        }
        imageView.layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )

        binding.rootView.addView(imageView)

        val constraintSet = ConstraintSet()
        constraintSet.clone(binding.rootView)

        constraintSet.connect(
            imageView.id,
            ConstraintSet.TOP,
            binding.mainView.id,
            ConstraintSet.TOP
        )
        constraintSet.connect(
            imageView.id,
            ConstraintSet.START,
            binding.mainView.id,
            ConstraintSet.START
        )
        constraintSet.connect(
            imageView.id,
            ConstraintSet.BOTTOM,
            binding.mainView.id,
            ConstraintSet.BOTTOM
        )
        constraintSet.connect(
            imageView.id,
            ConstraintSet.END,
            binding.mainView.id,
            ConstraintSet.END
        )

        constraintSet.applyTo(binding.rootView)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.iv_slide -> {
                slideViewModel.setNowSlideSelected(true)
                binding.ivSlide.background =
                    AppCompatResources.getDrawable(baseContext, R.drawable.border_black)
            }

            R.id.main_view -> {
                slideViewModel.setNowSlideSelected(false)
                binding.tvAlphaMonitor.text = ""
                binding.btnBgcolor.text = ""
                binding.ivSlide.background =
                    AppCompatResources.getDrawable(baseContext, R.drawable.border_null)
            }

            R.id.btn_bgcolor -> {
                slideViewModel.editColorRandom()
            }

            R.id.btn_alpha_minus -> {
                slideViewModel.editAlpha(Mode.MINUS)
            }

            R.id.btn_alpha_plus -> {
                slideViewModel.editAlpha(Mode.PLUS)
            }

            R.id.btn_add_slide -> {
                slideViewModel.setNewSlide(SlideType.SQUARE) // 추후 랜덤으로 변경
            }
        }
    }

    override fun onSlideSelected(position: Int, slide: Slide) {
        Log.d("DBG::SELECTED", "$position $slide")
        slideViewModel.setSlideIndex(position)
    }
}