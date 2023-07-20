package com.glacier.androidslide.listener

interface ItemMoveListener {
    fun onItemMove(fromPosition: Int, toPosition: Int): Boolean
}