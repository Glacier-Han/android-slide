package com.glacier.androidslide.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.glacier.androidslide.R
import com.glacier.androidslide.databinding.ItemSlideImageBinding
import com.glacier.androidslide.databinding.ItemSlideSquareBinding
import com.glacier.androidslide.listener.ItemMoveListener
import com.glacier.androidslide.listener.OnSlideDoubleClickListener
import com.glacier.androidslide.listener.OnSlideSelectedListener
import com.glacier.androidslide.model.ImageSlide
import com.glacier.androidslide.model.Slide
import com.glacier.androidslide.model.SquareSlide
import com.glacier.androidslide.util.SlideType

class SlideAdapter(
    private val slides: MutableList<Slide>,
    private val listener: OnSlideSelectedListener,
    private val doubleClickListener: OnSlideDoubleClickListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), ItemMoveListener {

    companion object {
        private const val VIEW_TYPE_SQUARE = 0
        private const val VIEW_TYPE_IMAGE = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = when (viewType) {
            VIEW_TYPE_SQUARE -> ItemSlideSquareBinding.inflate(inflater, parent, false)
            VIEW_TYPE_IMAGE -> ItemSlideImageBinding.inflate(inflater, parent, false)
            else -> throw IllegalArgumentException("Invalid ViewType")
        }
        return when (viewType) {
            VIEW_TYPE_SQUARE -> SquareSlideViewHolder(binding as ItemSlideSquareBinding)
            VIEW_TYPE_IMAGE -> ImageSlideViewHolder(binding as ItemSlideImageBinding)
            else -> throw IllegalArgumentException("Invalid ViewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val slide = slides[position]

        holder.itemView.setOnLongClickListener {
            val context = holder.itemView.context
            PopupMenu(context, holder.itemView).apply {
                menuInflater.inflate(R.menu.slide_item_menu, menu)
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.menu_hard_back -> {
                            showToast(
                                context,
                                context.getString(R.string.menu_toast_hard_back)
                            )

                            val lastPosition = slides.size - 1
                            if (position < lastPosition) {
                                val slideToMove = slides.removeAt(position)
                                slides.add(slideToMove)
                                notifyItemMoved(position, lastPosition)
                                notifyItemRangeChanged(position, slides.size - position)
                            }
                            true
                        }

                        R.id.menu_back -> {
                            showToast(context, context.getString(R.string.menu_toast_back))

                            val nextPosition = position + 1
                            if (position < slides.size - 1) {
                                val slideToMove = slides.removeAt(position)
                                slides.add(nextPosition, slideToMove)
                                notifyItemMoved(position, nextPosition)
                                notifyItemRangeChanged(position - 1, 3)
                            }
                            true
                        }

                        R.id.menu_hard_front -> {
                            showToast(context, context.getString(R.string.menu_toast_hard_front))

                            if (position > 0) {
                                val slideToMove = slides.removeAt(position)
                                slides.add(0, slideToMove)
                                notifyItemMoved(position, 0)
                                notifyItemRangeChanged(0, position + 1)
                            }
                            true
                        }

                        R.id.menu_front -> {
                            showToast(context, context.getString(R.string.menu_toast_front))

                            if (position > 0) {
                                val prevPosition = position - 1
                                val slideToMove = slides.removeAt(position)
                                slides.add(prevPosition, slideToMove)
                                notifyItemMoved(position, prevPosition)
                                notifyItemRangeChanged(prevPosition, 3)
                            }
                            true
                        }

                        else -> false
                    }
                }
                show()
            }

            return@setOnLongClickListener true
        }


        when (holder) {
            is SquareSlideViewHolder -> {
                val squareSlide = slide as SquareSlide
                holder.bind(squareSlide)
            }

            is ImageSlideViewHolder -> {
                val imageSlide = slide as ImageSlide
                holder.bind(imageSlide)
            }
        }
    }

    override fun getItemCount(): Int {
        return slides.size
    }

    override fun getItemViewType(position: Int): Int {
        val slide = slides[position]
        return when (slide.slideType) {
            SlideType.SQUARE -> VIEW_TYPE_SQUARE
            SlideType.IMAGE -> VIEW_TYPE_IMAGE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        val slide = slides[fromPosition]
        slides.removeAt(fromPosition)
        slides.add(toPosition, slide)

        Log.d("DTEST", "TEST")
        notifyItemMoved(fromPosition, toPosition)
        notifyItemChanged(fromPosition)
        notifyItemChanged(toPosition)
        return true
    }

    inner class SquareSlideViewHolder(private val binding: ItemSlideSquareBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(squareSlide: SquareSlide) {
            // todo: 정사각형 슬라이드 처리하기

            itemView.setOnClickListener {
                listener.onSlideSelected(adapterPosition, squareSlide)
            }

            binding.tvSlideNumber.text = "${adapterPosition + 1}"
            binding.ivSlide.setColorFilter(
                Color.argb(
                    squareSlide.alpha,
                    squareSlide.color.r,
                    squareSlide.color.g,
                    squareSlide.color.b
                )
            )
        }
    }

    inner class ImageSlideViewHolder(private val binding: ItemSlideImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imageSlide: ImageSlide) {
            // todo: 이미지 슬라이드 처리하기
            Glide.with(binding.root.context).load(imageSlide.image).error(R.drawable.outline_image_24).override(50,50).into(binding.ivSlide)

            itemView.setOnClickListener {
                listener.onSlideSelected(adapterPosition, imageSlide)
            }

            itemView.setOnTouchListener(object : View.OnTouchListener {
                private val gestureDetector =
                    GestureDetector(itemView.context, object : GestureDetector.SimpleOnGestureListener() {
                        override fun onDoubleTap(e: MotionEvent): Boolean {
                            doubleClickListener.onSlideDoubleClicked(adapterPosition, slides[adapterPosition])
                            return true
                        }
                    })

                override fun onTouch(v: View?, event: MotionEvent): Boolean {
                    return gestureDetector.onTouchEvent(event)
                }
            })

            binding.tvSlideNumber.text = "${adapterPosition + 1}"
        }
    }

    fun showToast(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}
