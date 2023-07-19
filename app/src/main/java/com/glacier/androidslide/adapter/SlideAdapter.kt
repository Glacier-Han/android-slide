package com.glacier.androidslide.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.glacier.androidslide.databinding.ItemSlideImageBinding
import com.glacier.androidslide.databinding.ItemSlideSquareBinding
import com.glacier.androidslide.listener.OnSlideSelectedListener
import com.glacier.androidslide.model.ImageSlide
import com.glacier.androidslide.model.Slide
import com.glacier.androidslide.model.SquareSlide
import com.glacier.androidslide.util.SlideType

class SlideAdapter(private val slides: List<Slide>, private val listener: OnSlideSelectedListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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

        holder.itemView.setOnClickListener {
            listener.onSlideSelected(position, slide)
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

    inner class SquareSlideViewHolder(private val binding: ItemSlideSquareBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(squareSlide: SquareSlide) {
            // todo: 정사각형 슬라이드 처리하기
            binding.tvSlideNumber.text = "${adapterPosition + 1}"
        }
    }

    inner class ImageSlideViewHolder(private val binding: ItemSlideImageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(imageSlide: ImageSlide) {
            // todo: 이미지 슬라이드 처리하기
            binding.tvSlideNumber.text = "${adapterPosition + 1}"
        }
    }
}
