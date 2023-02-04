package com.gmail.pavlovsv93.verification.ui.listdevices.bottomsheet.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.gmail.pavlovsv93.verification.*
import com.gmail.pavlovsv93.verification.databinding.FragmentBottomSheetFilterBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import devices.ListDevicesFragment

class BottomSheetFilterFragment : BottomSheetDialogFragment() {

	companion object {
		const val KEY_FILTER_PARAMETER = "KEY_FILTER_PARAMETER"
		const val ARG_FILTER_PARAMETER = "ARG_FILTER_PARAMETER"
		private const val ARG_FILTER = "ARG_FILTER"
		const val TAG = "BottomSheetFilterFragment"
		fun newInstance(filter: FilterEntity?) = BottomSheetFilterFragment().apply {
			arguments = Bundle().apply {
				putParcelable(ARG_FILTER, filter)
			}
		}
	}

	private var _binding: FragmentBottomSheetFilterBinding? = null
	private val binding get() = _binding!!

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		_binding = FragmentBottomSheetFilterBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		val filter: FilterEntity? = arguments?.getParcelable(ARG_FILTER)
		if (filter != null) {
			initView(filter)
		} else {
			initView()
		}
		listenIsCheckedSwitch()
		pushFilterParameters()
		binding.btnCancelFilter.setOnClickListener {
			initView()
			parentFragmentManager.beginTransaction()
				.replace(R.id.fcContainer, ListDevicesFragment.newInstance())
				.remove(this)
				.commit()
		}
	}

	private fun pushFilterParameters() {
		binding.btnApplyFilter.setOnClickListener {
			val filter = getFilterParameters()
			val data: Bundle = Bundle().apply {
				putParcelable(ARG_FILTER_PARAMETER, filter)
			}
			parentFragmentManager.setFragmentResult(KEY_FILTER_PARAMETER, data)
			parentFragmentManager.beginTransaction().remove(this).commit()
		}
	}

	private fun getFilterParameters(): FilterEntity {
		var model: String? = null
		var station: String? = null
		var parameter: String? = null
		var status: String? = null
		val isCheckedData =
			if (binding.sAscendingDate.isChecked && !binding.sDescendingDate.isChecked) {
				true
			} else if (!binding.sAscendingDate.isChecked && binding.sDescendingDate.isChecked) {
				false
			} else {
				null
			}
		if (binding.etModel.text.isNotEmpty()) {
			model = binding.etModel.text.toString()
		}
		if (binding.etParameter.text.isNotEmpty()) {
			parameter = binding.etParameter.text.toString()
		}
		if (binding.etStation.text.isNotEmpty()) {
			station = binding.etStation.text.toString()
		}
		if (binding.etStatus.text.isNotEmpty()) {
			status = binding.etStatus.text.toString()
		}
		return FilterEntity(
			isCheckedData = isCheckedData,
			model = model,
			station = station,
			parameter = parameter,
			status = status
		)
	}

	private fun listenIsCheckedSwitch() {
		binding.sAscendingDate.setOnCheckedChangeListener { _, isChecked ->
			if (isChecked) {
				binding.sDescendingDate.isChecked = false
			}
		}
		binding.sDescendingDate.setOnCheckedChangeListener { _, isChecked ->
			if (isChecked) {
				binding.sAscendingDate.isChecked = false
			}
		}
	}

	private fun initView() {
		setAdapterEditTexts()
		binding.etModel.setText(null, false)
		binding.etParameter.setText(null, false)
		binding.etStation.setText(null, false)
		binding.etStatus.setText(null, false)
	}
	private fun initView(filter: FilterEntity) {
		setAdapterEditTexts()
		binding.etModel.setText(filter.model, false)
		binding.etParameter.setText(filter.parameter, false)
		binding.etStation.setText(filter.station, false)
		binding.etStatus.setText(filter.status, false)
		filter.isCheckedData?.let {
			if(it){
				binding.sAscendingDate.isChecked = true
			} else{
				binding.sDescendingDate.isChecked = true
			}
		}
	}

	private fun setAdapterEditTexts(){
		val adapterModel = ArrayAdapter(requireContext(), R.layout.edit_text_list_item, modelList)
		binding.etModel.setAdapter(adapterModel)
		val adapterParameter =
			ArrayAdapter(requireContext(), R.layout.edit_text_list_item, parameterList)
		binding.etParameter.setAdapter(adapterParameter)
		val adapterStation = ArrayAdapter(requireContext(), R.layout.edit_text_list_item, station)
		binding.etStation.setAdapter(adapterStation)
		val adapterStatus = ArrayAdapter(requireContext(), R.layout.edit_text_list_item, statusList)
		binding.etStatus.setAdapter(adapterStatus)
		binding.sAscendingDate.isChecked = false
		binding.sDescendingDate.isChecked = false
	}

	override fun onDestroy() {
		_binding = null
		super.onDestroy()
	}
}