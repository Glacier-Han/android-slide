package com.glacier.androidslide

<<<<<<< HEAD
<<<<<<< HEAD
=======
import android.app.Activity
>>>>>>> b1d5238 (feat: 갤러리에서 이미지 선택한 후 ByteArray로 저장하여 슬라이드에 표시 기능 구현)
=======
>>>>>>> 84d1a64 (refactor: 사용하지 않는 import문 제거)
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
<<<<<<< HEAD
<<<<<<< HEAD
=======
import android.widget.ImageView
>>>>>>> b1d5238 (feat: 갤러리에서 이미지 선택한 후 ByteArray로 저장하여 슬라이드에 표시 기능 구현)
=======
>>>>>>> 84d1a64 (refactor: 사용하지 않는 import문 제거)
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.app.ActivityCompat
<<<<<<< HEAD
<<<<<<< HEAD
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
=======
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
>>>>>>> b1d5238 (feat: 갤러리에서 이미지 선택한 후 ByteArray로 저장하여 슬라이드에 표시 기능 구현)
=======
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
>>>>>>> 84d1a64 (refactor: 사용하지 않는 import문 제거)
import com.glacier.androidslide.adapter.SlideAdapter
import com.glacier.androidslide.databinding.ActivityMainBinding
import com.glacier.androidslide.listener.OnSlideDoubleClickListener
import com.glacier.androidslide.listener.OnSlideSelectedListener
import com.glacier.androidslide.model.ImageSlide
import com.glacier.androidslide.model.Slide
import com.glacier.androidslide.model.SquareSlide
import com.glacier.androidslide.util.ItemMoveCallback
import com.glacier.androidslide.util.Mode
import com.glacier.androidslide.util.SlideType
import com.glacier.androidslide.util.UtilManager
import com.glacier.androidslide.viewmodel.MainViewModel
import jp.wasabeef.glide.transformations.ColorFilterTransformation
import java.io.IOException

class MainActivity : AppCompatActivity(), OnClickListener, View.OnLongClickListener,
    OnSlideSelectedListener,
    OnSlideDoubleClickListener {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val slideViewModel: MainViewModel by viewModels()
    private var imgSelectedPosition = 0
    private var observedListSize = 0

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
        binding.btnAddSlide.setOnLongClickListener(this)

        setObserver()
        //slideViewModel.setNewSlide(slideType = SlideType.SQUARE)
        setRecyclerView(slideViewModel.getSlides())
    }

    fun setObserver() {
        slideViewModel.nowSlide.observe(this) {
            setSlideView(it)
        }

        slideViewModel.slides.observe(this) {
            if(it.size != observedListSize){
                binding.rvSlides.adapter?.notifyItemInserted(it.lastIndex)
                binding.rvSlides.scrollToPosition(it.lastIndex)
            } else {
                binding.rvSlides.adapter?.notifyDataSetChanged()
            }
            observedListSize = it.size
        }
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
                setBgColorBtnColor(slide.alpha, sColor.r, sColor.g, sColor.b)
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
                binding.btnBgcolor.text = "IMAGE"
                binding.btnBgcolor.isEnabled = false
                setBgColorBtnColor(slide.alpha, 200, 200, 200)
            }
        }
    }

    private fun setBgColorBtnColor(alpha: Int, R: Int, G: Int, B: Int) {
        binding.btnBgcolor.backgroundTintList =
            ColorStateList.valueOf(Color.argb(alpha, R, G, B))
    }

    private fun setRecyclerView(slides: List<Slide>) {
        val slideAdapter =
            SlideAdapter(slides as MutableList<Slide>, this@MainActivity, this@MainActivity)

        with(binding.rvSlides) {
            adapter = slideAdapter
            layoutManager =
                LinearLayoutManager(baseContext)

            val itemMoveCallback = ItemMoveCallback(slideAdapter)
            val itemTouchHelper = ItemTouchHelper(itemMoveCallback)
            itemTouchHelper.attachToRecyclerView(this)
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
                slideViewModel.setNewSlide(SlideType.values().random()) // 추후 랜덤으로 변경
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

    override fun onSlideSelected(position: Int, slide: Slide) {
        slideViewModel.setSlideIndex(position)
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
                            slideViewModel.editSlideImage(imgSelectedPosition, byteImage)
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

    override fun onSlideDoubleClicked(position: Int, slide: Slide) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkPermission(android.Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            checkPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
//            setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        }
        imgSelectedPosition = position
        resultLauncher.launch(intent)
    }

    override fun onLongClick(view: View?): Boolean {
        when (view?.id) {
            R.id.btn_add_slide -> {
                Log.d("DBG::RETROFIT", "sended")
                slideViewModel.getJsonSlides()
            }
        }
        return true
    }

}