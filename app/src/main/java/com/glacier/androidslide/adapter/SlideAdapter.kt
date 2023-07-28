package com.glacier.androidslide.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.glacier.androidslide.R
import com.glacier.androidslide.data.enums.SlideType
import com.glacier.androidslide.data.model.DrawingSlide
import com.glacier.androidslide.data.model.ImageSlide
import com.glacier.androidslide.data.model.Slide
import com.glacier.androidslide.data.model.SquareSlide
import com.glacier.androidslide.databinding.ItemSlideDrawingBinding
import com.glacier.androidslide.databinding.ItemSlideImageBinding
import com.glacier.androidslide.databinding.ItemSlideSquareBinding
import com.glacier.androidslide.listener.ItemMoveListener
import com.glacier.androidslide.listener.OnSlideSelectedListener

class SlideAdapter(
    private val slides: MutableList<Slide>,
    private val listener: OnSlideSelectedListener,
) :
    RecyclerView.Adapter<ViewHolder>(), ItemMoveListener {
    private var popupMenu: PopupMenu? = null

    companion object {
        private const val VIEW_TYPE_SQUARE = 0
        private const val VIEW_TYPE_IMAGE = 1
        private const val VIEW_TYPE_DRAWING = 2
    }

    private fun showPopUpMenu(viewHolder: ViewHolder, position: Int) {
        val context = viewHolder.itemView.context
        if (popupMenu == null) {
            popupMenu = PopupMenu(context, viewHolder.itemView).apply {
                menuInflater.inflate(R.menu.slide_item_menu, menu)
            }
        } else {
            popupMenu?.let {
                it.setOnMenuItemClickListener { item ->
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
            }
            popupMenu?.show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            VIEW_TYPE_SQUARE -> SquareSlideViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), R.layout.item_slide_square, parent, false
                )
            )

            VIEW_TYPE_IMAGE -> ImageSlideViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), R.layout.item_slide_image, parent, false
                )
            )

            VIEW_TYPE_DRAWING -> DrawingSlideViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), R.layout.item_slide_drawing, parent, false
                )
            )

            else -> throw IllegalArgumentException("Invalid ViewType")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val slide = slides[position]

        holder.itemView.setOnLongClickListener {
            showPopUpMenu(holder, position)
            return@setOnLongClickListener true
        }


        when (holder) {
            is SquareSlideViewHolder -> {
                holder.bind(slide as SquareSlide)
            }

            is ImageSlideViewHolder -> {
                holder.bind(slide as ImageSlide)
            }

            is DrawingSlideViewHolder -> {
                holder.bind(slide as DrawingSlide)
            }
        }
    }

    override fun getItemCount(): Int {
        return slides.size
    }

    override fun getItemViewType(position: Int): Int {
        val slide = slides[position]
        return when (slide.type) {
            SlideType.SQUARE -> VIEW_TYPE_SQUARE
            SlideType.IMAGE -> VIEW_TYPE_IMAGE
            SlideType.DRAWING -> VIEW_TYPE_DRAWING
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        val slide = slides[fromPosition]
        slides.removeAt(fromPosition)
        slides.add(toPosition, slide)

        notifyItemMoved(fromPosition, toPosition)
        notifyItemChanged(fromPosition)
        notifyItemChanged(toPosition)
        return true
    }

    inner class SquareSlideViewHolder(private val binding: ItemSlideSquareBinding) :
        ViewHolder(binding.root) {
        fun bind(squareSlide: SquareSlide) {
            binding.slideIndex = adapterPosition + 1
            binding.colorTint = Color.argb(
                squareSlide.alpha,
                squareSlide.color.r,
                squareSlide.color.g,
                squareSlide.color.b
            )

            itemView.setOnClickListener {
                listener.onSlideSelected(adapterPosition, squareSlide)
            }
        }
    }

    inner class ImageSlideViewHolder(private val binding: ItemSlideImageBinding) :
        ViewHolder(binding.root) {
        fun bind(imageSlide: ImageSlide) {
            binding.slideIndex = adapterPosition + 1
            binding.slide = imageSlide

            itemView.setOnClickListener {
                listener.onSlideSelected(adapterPosition, imageSlide)
            }


        }
    }

    inner class DrawingSlideViewHolder(private val binding: ItemSlideDrawingBinding) :
        ViewHolder(binding.root) {
        fun bind(drawingSlide: DrawingSlide) {
            binding.slideIndex = adapterPosition + 1
            itemView.setOnClickListener {
                listener.onSlideSelected(adapterPosition, drawingSlide)
            }
        }
    }

    fun showToast(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}