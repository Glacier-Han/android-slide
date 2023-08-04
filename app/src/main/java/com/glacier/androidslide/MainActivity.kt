package com.glacier.androidslide

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View.GONE
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.glacier.androidslide.data.model.Slide
import com.glacier.androidslide.databinding.ActivityMainBinding
import com.glacier.androidslide.listener.OnSlideDoubleClickListener
import com.glacier.androidslide.listener.OnSlideSelectedListener
import com.glacier.androidslide.viewmodel.MainViewModel
import jp.wasabeef.glide.transformations.ColorFilterTransformation
import java.io.IOException

class MainActivity : AppCompatActivity(), OnSlideSelectedListener,
    OnSlideDoubleClickListener {

    private val slideViewModel: MainViewModel by viewModels()
    private val CODE_PERMISSION_IMAGE = 1112

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

    }

    fun checkPermission(permission: String) {
        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(permission), CODE_PERMISSION_IMAGE)
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
            Toast.makeText(applicationContext, getString(R.string.error_msg_imageload), Toast.LENGTH_SHORT)
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
        slideViewModel.setSlideIndex(position)
    }

}