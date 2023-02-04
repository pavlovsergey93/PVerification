package com.gmail.pavlovsv93.verification.ui.waitreturn

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gmail.pavlovsv93.verification.R
import com.gmail.pavlovsv93.verification.databinding.FragmentWaitingItemBinding
import com.gmail.pavlovsv93.verification.domain.KipEntity
import com.gmail.pavlovsv93.verification.utils.RemoteDiffUtils
import com.gmail.pavlovsv93.verification.utils.dataFormat
import com.gmail.pavlovsv93.verification.utils.setBackgroundStatus
import com.gmail.pavlovsv93.verification.utils.setImageDevice

class WaitingAdapter(private val click: WaitingForReturnFragment.OnClickOrSweep) :
	RecyclerView.Adapter<WaitingAdapter.WaitingViewHolder>() {

	private val listEntity: MutableList<KipEntity> = mutableListOf()

	fun setData(listData: List<KipEntity>) {
		val diffUtilCallback = RemoteDiffUtils(listEntity, listData)
		val diffResult = DiffUtil.calculateDiff(diffUtilCallback)
		listEntity.clear()
		listEntity.addAll(listData)
		diffResult.dispatchUpdatesTo(this)
	}

	inner class WaitingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		fun bind(entity: KipEntity) {
			val binding = FragmentWaitingItemBinding.bind(itemView)
			with(binding) {
				ivModel.setImageResource(setImageDevice(entity.model))
				tvModel.text = entity.model
				tvNumber.text = entity.number
				tvLocation.text = "${entity.station} ${entity.position}"
				val color: Int = setBackgroundStatus(entity.status)
				viewStatus.setBackgroundResource(color)
				tvNextData.text = dataFormat(entity.date)
				tvParameter.text = entity.parameter
				tvStatus.text = entity.status
			}
			binding.cvItem.setOnClickListener {
				click.onClick(entity)
			}
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WaitingViewHolder {
		return WaitingViewHolder(
			LayoutInflater.from(parent.context)
				.inflate(R.layout.fragment_waiting_item, parent, false) as View
		)
	}

	override fun onBindViewHolder(holder: WaitingViewHolder, position: Int) {
		holder.bind(listEntity[position])
	}

	override fun getItemCount(): Int = listEntity.size
}