package com.glacier.androidslide

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import androidx.activity.viewModels
import androidx.appcompat.content.res.AppCompatResources
import com.glacier.androidslide.databinding.ActivityMainBinding
import com.glacier.androidslide.model.ImageSlide
import com.glacier.androidslide.model.Slide
import com.glacier.androidslide.util.Mode
import com.glacier.androidslide.util.UtilManager
import com.glacier.androidslide.model.SquareSlide
import com.glacier.androidslide.util.SlideType
import com.glacier.androidslide.viewmodel.SquareSlideViewModel

class MainActivity : AppCompatActivity(), OnClickListener {

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

        setObserver()
        slideViewModel.setNewSlide(slideType = SlideType.SQUARE)
    }

    fun setObserver() {
        slideViewModel.nowSlide.observe(this) {
            setSlideView(it)
        }
    }

    private fun setSlideView(slide: Slide) {
        slide.let {
            when(slide){
                is SquareSlide -> {
                    it as SquareSlide
                    binding.ivSlide.setImageDrawable(ColorDrawable(Color.argb(it.alpha, it.R, it.G, it.B)))
                    binding.btnBgcolor.text = UtilManager.rgbToHex(it.R, it.G, it.B)
                    setBgColorBtnColor(it.alpha, it.R, it.G, it.B)
                }

                is ImageSlide -> {
                    // TODO :: 추후 이미지 슬라이드 처리
                }
            }

            binding.tvAlphaMonitor.text = slideViewModel.nowAlpha.value.toString()
        }
    }

    private fun setBgColorBtnColor(alpha: Int, R: Int, G: Int, B: Int) {
        binding.btnBgcolor.backgroundTintList =
            ColorStateList.valueOf(Color.argb(alpha, R, G, B))
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
        }
    }
}