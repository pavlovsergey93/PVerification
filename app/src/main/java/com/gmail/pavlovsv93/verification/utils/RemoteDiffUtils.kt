package com.gmail.pavlovsv93.verification.utils

import androidx.recyclerview.widget.DiffUtil
import com.gmail.pavlovsv93.verification.domain.KipEntity

class RealtimeDiffUtils(
	private val oldList: List<KipEntity>,
	private val newList: List<KipEntity>
) : DiffUtil.Callback() {
	override fun getOldListSize(): Int = oldList.size

	override fun getNewListSize(): Int = newList.size

	override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
		return oldList[oldItemPosition].idKip == newList[newItemPosition].idKip
	}

	override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
		return oldList[oldItemPosition].number == newList[newItemPosition].number
				&& oldList[oldItemPosition].date == newList[newItemPosition].date
				&& oldList[oldItemPosition].model == newList[newItemPosition].model
				&& oldList[oldItemPosition].parameter == newList[newItemPosition].parameter
				&& oldList[oldItemPosition].keyStation == newList[newItemPosition].keyStation
				&& oldList[oldItemPosition].station == newList[newItemPosition].station
				&& oldList[oldItemPosition].position == newList[newItemPosition].position
				&& oldList[oldItemPosition].description == newList[newItemPosition].description
				&& oldList[oldItemPosition].nextDate == newList[newItemPosition].nextDate
				&& oldList[oldItemPosition].status == newList[newItemPosition].status
				&& oldList[oldItemPosition].verification == newList[newItemPosition].verification
				&& oldList[oldItemPosition].info == newList[newItemPosition].info
	}
}