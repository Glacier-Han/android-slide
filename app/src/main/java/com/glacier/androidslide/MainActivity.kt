package com.glacier.androidslide

import android.view.View.VISIBLE
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.glacier.androidslide.adapter.SlideAdapter
import com.glacier.androidslide.data.enums.Mode
import com.glacier.androidslide.data.enums.SlideType
import com.glacier.androidslide.data.model.DrawingSlide
import com.glacier.androidslide.data.model.ImageSlide
import com.glacier.androidslide.data.model.Slide
import com.glacier.androidslide.data.model.SquareSlide
import com.glacier.androidslide.databinding.ActivityMainBinding
import com.glacier.androidslide.listener.OnSlideDoubleClickListener
import com.glacier.androidslide.listener.OnSlideSelectedListener
import com.glacier.androidslide.util.ItemMoveCallback
import com.glacier.androidslide.util.UtilManager
import com.glacier.androidslide.viewmodel.MainViewModel
import jp.wasabeef.glide.transformations.ColorFilterTransformation
import java.io.IOException

class MainActivity : AppCompatActivity(), OnClickListener, OnSlideSelectedListener,
    OnSlideDoubleClickListener {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val slideViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bind : ActivityMainBinding = DataBindingUtil.setContentView(this,
            R.layout.activity_main
        )

        slideViewModel.setNewSlide()
        bind.viewModel = slideViewModel
        bind.activity = this
        slideViewModel.slides.observe(this){
            bind.viewModel = slideViewModel
        }

        slideViewModel.nowSlide.observe(this){
            bind.slide = it
        }

        init()
    }

    fun init() {
        binding.ivSlide.setOnClickListener(this)
        binding.mainView.setOnClickListener(this)

    }


    private fun setSlideView(slide: Slide) {
        when (slide) {
            is SquareSlide -> {
                ConstraintSet().apply {
                    clone(binding.rootView)
                    connect(
                        binding.ivSlide.id,
                        ConstraintSet.BOTTOM,
                        binding.rootView.id,
                        ConstraintSet.BOTTOM
                    )
                    applyTo(binding.rootView)
                }

                val sColor = slide.color
                binding.ivSlide.setImageDrawable(
                    ColorDrawable(Color.argb(slide.alpha, sColor.r, sColor.g, sColor.b))
                )
                binding.tvAlphaMonitor.text = UtilManager.getAlphaToMode(slide.alpha).toString()
                binding.btnBgcolor.text = UtilManager.rgbToHex(sColor.r, sColor.g, sColor.b)
                binding.btnBgcolor.isEnabled = true
                binding.ivSlide.visibility = VISIBLE
                binding.dvDrawing.visibility = GONE
            }

            is ImageSlide -> {
                ConstraintSet().apply {
                    clone(binding.rootView)
                    clear(binding.ivSlide.id, ConstraintSet.BOTTOM)
                    applyTo(binding.rootView)
                }

                Glide.with(applicationContext).load(slide.image)
                    .transform(ColorFilterTransformation(Color.argb(255 - slide.alpha, 255, 255, 255)))
                    .error(R.drawable.outline_image_24)
                    .into(binding.ivSlide)

                binding.tvAlphaMonitor.text = UtilManager.getAlphaToMode(slide.alpha).toString()
                binding.btnBgcolor.text = getString(R.string.text_image)
                binding.btnBgcolor.isEnabled = false
                binding.ivSlide.visibility = VISIBLE
                binding.dvDrawing.visibility = GONE
            }

            is DrawingSlide -> {
                binding.btnBgcolor.text = getString(R.string.text_drawing)
                binding.ivSlide.visibility = GONE
                binding.dvDrawing.visibility = VISIBLE
                binding.dvDrawing.setSlide(slide)
            }
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
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

        }
    }

    fun checkPermission(permission: String) {
        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(permission), 1112)
        }
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        {
            if (it.resultCode == RESULT_OK) {
                it.data?.let { result ->
                    val imageUri: Uri? = result.data
                    if (imageUri != null) {
                        val byteImage = getByteArrayFromUri(baseContext, imageUri)
                        if (byteImage != null) {
                            slideViewModel.editSlideImage(slideViewModel.nowSlideIndex, byteImage)
                        }
                    }
                }
            }
        }

    private fun getByteArrayFromUri(context: Context, uri: Uri): ByteArray? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val byteArray = inputStream?.readBytes()
            inputStream?.close()
            byteArray
        } catch (e: IOException) {
            Toast.makeText(applicationContext, "ERROR: 사진을 불러오는데 실패하였습니다!", Toast.LENGTH_SHORT)
                .show()
            null
        }
    }

    override fun onSlideDoubleClicked(slide: Slide) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkPermission(android.Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            checkPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        resultLauncher.launch(intent)
    }

    override fun onSlideSelected(position: Int, slide: Slide) {
        slideViewModel.nowSlideIndex = position
        slideViewModel.getSlideWithIndex(position)
    }

}