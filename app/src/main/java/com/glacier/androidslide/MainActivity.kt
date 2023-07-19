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
import com.glacier.androidslide.util.Mode
import com.glacier.androidslide.util.UtilManager
import com.glacier.androidslide.model.SquareSlide
import com.glacier.androidslide.viewmodel.SquareSlideViewModel

class MainActivity : AppCompatActivity(), OnClickListener {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val slideManager = SlideManager()
    private var nowAlpha = 10
    private var nowSlideIndex = 0
    private lateinit var nowSlide: SquareSlide

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
        slideViewModel.setNewSlide()

    }

    fun setObserver(){
        slideViewModel.nowAlpha.observe(this) {
            binding.tvAlphaMonitor.text = it.toString()
        }

        slideViewModel.nowSlide.observe(this){
            setSlideView(it)
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.iv_slide -> {
                binding.ivSlide.background =
                    AppCompatResources.getDrawable(baseContext, R.drawable.border_black)
            }

            R.id.main_view -> {
                binding.tvAlphaMonitor.text = ""
                binding.btnBgcolor.text = ""
                binding.ivSlide.background =
                    AppCompatResources.getDrawable(baseContext, R.drawable.border_null)
            }

            R.id.btn_bgcolor -> {
                editColorRandom()
            }

            R.id.btn_alpha_minus -> {
                slideViewModel.editAlpha(Mode.MINUS)
            }

            R.id.btn_alpha_plus -> {
                slideViewModel.editAlpha(Mode.PLUS)
            }
        }
    }

    private fun setSlideView(slide: SquareSlide) {
        slide.let {
            Log.d("TEST", "$slide ${slideViewModel.nowSlideIndex.value} ${it.alpha}")
            binding.ivSlide.setImageDrawable(ColorDrawable(Color.argb(it.alpha, it.R, it.G, it.B)))
            setBgColorBtnColor(it.alpha, it.R, it.G, it.B)
            binding.btnBgcolor.text = UtilManager.rgbToHex(it.R, it.G, it.B)
        }

    }

    private fun editColorRandom() {
        val randomR = UtilManager.getRandomColor()[0]
        val randomG = UtilManager.getRandomColor()[1]
        val randomB = UtilManager.getRandomColor()[2]

        slideManager.editSlideColor(nowSlideIndex, randomR, randomG, randomB)
    }

    private fun setBgColorBtnColor(alpha: Int, R: Int, G: Int, B: Int) {
        binding.btnBgcolor.backgroundTintList =
            ColorStateList.valueOf(Color.argb(alpha, R, G, B))
    }
}