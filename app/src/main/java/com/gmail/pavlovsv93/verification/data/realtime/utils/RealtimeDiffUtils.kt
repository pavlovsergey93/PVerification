package com.gmail.pavlovsv93.verification.data.realtime.utils

import androidx.recyclerview.widget.DiffUtil
import com.gmail.pavlovsv93.verification.data.realtime.entity.Entity

class RealtimeDiffUtils(
	private val oldList: List<Entity>,
	private val newList: List<Entity>
) : DiffUtil.Callback() {
	override fun getOldListSize(): Int = oldList.size

	override fun getNewListSize(): Int = newList.size

	override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
		return oldList[oldItemPosition].uid == newList[newItemPosition].uid
	}

	override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
		return oldList[oldItemPosition].model == newList[newItemPosition].model
				&& oldList[oldItemPosition].date == newList[newItemPosition].date
				&& oldList[oldItemPosition].number == newList[newItemPosition].number
				&& oldList[oldItemPosition].location == newList[newItemPosition].location
				&& oldList[oldItemPosition].status == newList[newItemPosition].status
				&& oldList[oldItemPosition].parameter == newList[newItemPosition].parameter
	}
}