package com.gmail.pavlovsv93.verification.utils.swipe

interface ItemTouchHelperAdapter {
    fun onItemCorrect(position: Int)
}
interface ItemTouchHelperViewHolder {
    fun onItemSelected()
    fun onItemClear()
}