package com.gmail.pavlovsv93.verification.ui.detailsdevice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.gmail.pavlovsv93.verification.R
import com.gmail.pavlovsv93.verification.databinding.FragmentDetailsDeviceBinding
import com.gmail.pavlovsv93.verification.domain.KipEntity
import com.gmail.pavlovsv93.verification.ui.VerificationActivity
import com.gmail.pavlovsv93.verification.ui.addfragment.AddKipEntityFragment
import com.gmail.pavlovsv93.verification.utils.AppState
import com.gmail.pavlovsv93.verification.utils.dataFormat
import com.gmail.pavlovsv93.verification.utils.setBackgroundStatus
import com.gmail.pavlovsv93.verification.utils.setImageDevice
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named

class DetailsDeviceFragment : Fragment() {
	companion object {
		private const val ARG_ID_KIP_DETAILS = "DetailsDeviceFragment.ARG_ID_KIP_DETAILS"
		fun newInstance(idKip: String): DetailsDeviceFragment {
			return DetailsDeviceFragment().apply {
				arguments = Bundle().apply { putString(ARG_ID_KIP_DETAILS, idKip) }
			}
		}
	}

	private var _binding: FragmentDetailsDeviceBinding? = null
	private val binding get() = _binding!!
	private val viewModel: DetailsViewModel by viewModel(named("DetailsViewModel"))
	private var idKip: String? = null

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentDetailsDeviceBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		(requireActivity() as VerificationActivity).setOnBackPressedListener(null)
		idKip = arguments?.getString(ARG_ID_KIP_DETAILS)
		viewModel.getLiveData().observe(viewLifecycleOwner, Observer { state ->
			ranger(state)
		})
		idKip?.let { viewModel.getItemKipEntities(it) }
	}

	private fun ranger(state: AppState) {
		when (state) {
			is AppState.OnError -> {
				Snackbar.make(
					binding.root,
					state.exception.message.toString(),
					Snackbar.LENGTH_INDEFINITE
				)
					.setAction(R.string.relode) {
						idKip?.let { viewModel.getItemKipEntities(it) }
					}
					.show()
			}
			is AppState.OnLoading -> {
				binding.pdProgress.visibility = if (state.load) {
					View.VISIBLE
				} else {
					View.GONE
				}
			}
			is AppState.OnSuccess<*> -> {
				val kipEntity = state.success as KipEntity
				displayDetailDevice(kipEntity)
			}
			is AppState.OnShowMessage -> {
				Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
			}
		}
	}

	private fun displayDetailDevice(device: KipEntity) {
		with(binding) {
			tvStatus.text = device.status
			device.status.let { tvStatus.setBackgroundResource(setBackgroundStatus(it)) }
			tvModel.text = device.model
			device.model.let { ivImage.setImageResource(setImageDevice(it)) }
			tvNumber.text = device.number
			tvData.text = dataFormat(device.date)
			tvNextData.text = dataFormat(device.nextDate)
			tvLocation.text = "${device.station} ${device.position}"
			tvParameter.text = device.parameter
			tvDescription.text = "Дополнительные сведенья:\n${device.description}\n\n Информация:\n ${device.info}"
			binding.fabCorrect.setOnClickListener {
				parentFragmentManager.beginTransaction()
					.add(R.id.fcContainer,AddKipEntityFragment.updateInstance(device.idKip))
					.addToBackStack(null)
					.commit()
			}
		}
	}

	override fun onDestroy() {
		_binding = null
		super.onDestroy()
	}
}