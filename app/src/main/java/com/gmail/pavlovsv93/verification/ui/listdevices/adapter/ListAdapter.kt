package com.gmail.pavlovsv93.verification.ui.listdevices.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gmail.pavlovsv93.verification.R
import com.gmail.pavlovsv93.verification.databinding.KipEntityItemBinding
import com.gmail.pavlovsv93.verification.domain.KipEntity
import com.gmail.pavlovsv93.verification.ui.VerificationActivity
import com.gmail.pavlovsv93.verification.utils.AppDiffUtil
import com.gmail.pavlovsv93.verification.utils.dataFormat
import com.gmail.pavlovsv93.verification.utils.setBackgroundStatus
import com.gmail.pavlovsv93.verification.utils.setImageDevice

class ListAdapter(private val onClick: VerificationActivity.OnClickTheDevice) :
	RecyclerView.Adapter<ListAdapter.ListDevicesViewHolder>() {
	private val displayList: MutableList<KipEntity> = mutableListOf()
	@SuppressLint("NotifyDataSetChanged")
	fun setData(list: List<KipEntity>) {
		val diffUtil = AppDiffUtil(displayList, list)
		val diffResult = DiffUtil.calculateDiff(diffUtil)
		displayList.clear()
		displayList.addAll(list)
		diffResult.dispatchUpdatesTo(this)
	}

	inner class ListDevicesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		fun bind(data: KipEntity) {
			val binding = KipEntityItemBinding.bind(itemView)
			binding.cvItem.setOnClickListener {
				onClick.onClick(data)
			}
			with(binding) {
				data.status.let { viewStatus.setBackgroundResource(setBackgroundStatus(it)) }
				data.model.let { ivModel.setImageResource(setImageDevice(it)) }
				tvModel.text = data.model
				tvData.text = dataFormat(data.date)
				tvNextData.text = dataFormat(data.nextDate)
				tvLocation.text = "${data.station} ${data.position}"
				tvParameter.text = data.parameter
				tvNumber.text = data.number
				tvInfo.text = data.info
			}
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListDevicesViewHolder {
		return ListDevicesViewHolder(
			LayoutInflater.from(parent.context)
				.inflate(R.layout.kip_entity_item, parent, false) as View
		)
	}

	override fun onBindViewHolder(holder: ListDevicesViewHolder, position: Int) {
		holder.bind(displayList[position])
	}

	override fun getItemCount(): Int = displayList.size
}