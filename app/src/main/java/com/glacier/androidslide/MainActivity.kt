package com.glacier.androidslide

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.appcompat.content.res.AppCompatResources
import com.glacier.androidslide.databinding.ActivityMainBinding
import com.glacier.androidslide.util.Mode
import com.glacier.androidslide.util.UtilManager
import com.glacier.androidslide.model.SquareSlide

class MainActivity : AppCompatActivity(), OnClickListener {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val slideManager = SlideManager()
    private var nowAlpha = 10
    private var nowSlideIndex = 0
    private lateinit var nowSlide: SquareSlide

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        init()
    }

    fun init() {
        binding.ivSlide.setOnClickListener(this)
        binding.mainView?.setOnClickListener(this)
        binding.btnBgcolor.setOnClickListener(this)
        binding.btnAlphaMinus.setOnClickListener(this)
        binding.btnAlphaPlus.setOnClickListener(this)

        setNewSlide()
    }


    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.iv_slide -> {
                setSlideView(slideManager.getSlideCount() - 1)
                binding.ivSlide.background =
                    AppCompatResources.getDrawable(baseContext, R.drawable.border_black)
            }

            R.id.main_view -> {
                binding.tvAlphaMonitor?.text = ""
                binding.btnBgcolor.text = ""
                binding.ivSlide.background =
                    AppCompatResources.getDrawable(baseContext, R.drawable.border_null)
            }

            R.id.btn_bgcolor -> {
                editColorRandom()
            }

            R.id.btn_alpha_minus -> {
                editAlpha(Mode.MINUS)
            }

            R.id.btn_alpha_plus -> {
                editAlpha(Mode.PLUS)
            }
        }
    }

    private fun setNewSlide() {
        val randomR = UtilManager.getRandomColor()[0]
        val randomG = UtilManager.getRandomColor()[1]
        val randomB = UtilManager.getRandomColor()[2]

        nowSlide =
            slideManager.createSlide(randomR, randomG, randomB, UtilManager.getAlphaMode(nowAlpha))
        setSlideView(slideManager.getSlideCount() - 1)
        nowSlideIndex = slideManager.getSlideCount() - 1
    }

    private fun setSlideView(index: Int) {
        val slide = slideManager.getSlideByIndex(index)
        slide?.let {
            binding.ivSlide.setImageDrawable(
                ColorDrawable(
                    Color.argb(UtilManager.getAlphaMode(nowAlpha), slide.R, slide.G, slide.B)
                )
            )
            setBgColorBtnColor(UtilManager.getAlphaMode(nowAlpha), slide.R, slide.G, slide.B)
            binding.tvAlphaMonitor?.text = nowAlpha.toString()
            binding.btnBgcolor.text =
                UtilManager.rgbToHex(slide.R, slide.G, slide.B)
        }

    }

    private fun editColorRandom(){
        val randomR = UtilManager.getRandomColor()[0]
        val randomG = UtilManager.getRandomColor()[1]
        val randomB = UtilManager.getRandomColor()[2]

        slideManager.editSlideColor(nowSlideIndex, randomR, randomG, randomB)
        setSlideView(nowSlideIndex)
    }
    private fun editAlpha(mode: Mode) {
        when (mode) {
            Mode.MINUS -> {
                if (nowAlpha > 1) {
                    nowAlpha -= 1
                    slideManager.editSlideAlpha(nowSlideIndex, UtilManager.getAlphaMode(nowAlpha))
                    setSlideView(nowSlideIndex)
                    binding.tvAlphaMonitor?.text = nowAlpha.toString()
                }
            }

            Mode.PLUS -> {
                if (nowAlpha < 10) {
                    nowAlpha += 1
                    slideManager.editSlideAlpha(nowSlideIndex, UtilManager.getAlphaMode(nowAlpha))
                    setSlideView(nowSlideIndex)
                    binding.tvAlphaMonitor?.text = nowAlpha.toString()
                }
            }
        }
    }

    private fun setBgColorBtnColor(alpha: Int, R: Int, G: Int, B: Int) {
        binding.btnBgcolor.backgroundTintList =
            ColorStateList.valueOf(Color.argb(alpha, R, G, B))
    }
}