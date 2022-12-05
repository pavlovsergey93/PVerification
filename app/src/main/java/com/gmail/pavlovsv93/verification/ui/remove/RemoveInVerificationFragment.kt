package com.gmail.pavlovsv93.verification.ui.remove

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.gmail.pavlovsv93.verification.*
import com.gmail.pavlovsv93.verification.data.realtime.entity.Entity
import com.gmail.pavlovsv93.verification.databinding.FragmentRemoveInVerificationBinding
import com.gmail.pavlovsv93.verification.domain.KipEntity
import com.gmail.pavlovsv93.verification.ui.VerificationActivity
import com.gmail.pavlovsv93.verification.ui.listdevices.adapter.ListAdapter
import com.gmail.pavlovsv93.verification.utils.AppState
import com.gmail.pavlovsv93.verification.utils.dataFormat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.Timestamp
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named

class RemoveInVerificationFragment : Fragment() {
	companion object {
		fun newInstance() = RemoveInVerificationFragment()
	}

	private var status: String? = null
	private var date: Timestamp = Timestamp.now()
	private var _binding: FragmentRemoveInVerificationBinding? = null
	private val binding get() = _binding!!
	private val viewModel: RemoveInVerificationViewModel by viewModel(named("RemoveInVerificationViewModel"))

	private val adapter: ListAdapter = ListAdapter(object : VerificationActivity.OnClickTheDevice {
		override fun onClick(kipEntity: KipEntity) {
			val data = mutableMapOf<String, Any>()
			var entity: Entity? = null
			MaterialAlertDialogBuilder(requireContext())
				.setTitle(getString(R.string.dialog_status_title))
				.setNeutralButton(getString(R.string.dialog_status_cancel)) { dialog, _ ->
					dialog.cancel()
				}.setSingleChoiceItems(statusListVerification, CHECKED_ITEM) { dialog, which ->
					status = statusListVerification[which]
					status?.let { status ->
						data[KEY_STATUS] = status
						data[KEY_DATE] = date
						entity = Entity(
							uid = kipEntity.idKip,
							model = kipEntity.model,
							number = kipEntity.number,
							parameter = kipEntity.parameter,
							date = dataFormat(Timestamp.now()),
							location = "${kipEntity.station} ${kipEntity.position}",
							status = status,
							verification = kipEntity.verification.toInt()
						)
					}
					entity?.let {
						viewModel.addEntityRealtimeList(it)
					}

					viewModel.updateDataDevice(kipEntity.idKip, data)
					dialog.dismiss()
				}.show()
			//data.put(KEY_STATUS, STATUS_REMOVED)
		}
	})

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		_binding = FragmentRemoveInVerificationBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		(requireActivity() as VerificationActivity).setOnBackPressedListener(this@RemoveInVerificationFragment)
		viewModel.getLiveData().observe(viewLifecycleOwner, Observer { state ->
			ranger(state)
		})
		val recyclerView = binding.rvVerificationList
		recyclerView.layoutManager =
			LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
		recyclerView.adapter = adapter
		viewModel.getVerifiableDevices()
	}

	private fun ranger(state: AppState) {
		when (state) {
			is AppState.OnError -> {
				Toast.makeText(requireContext(), "${state.exception.message}", Toast.LENGTH_SHORT)
					.show()
			}
			is AppState.OnLoading -> {
			}
			is AppState.OnSuccess<*> -> {
				val success = state.success as List<KipEntity>
				Log.d("WWW.ResultVerf", "$success")
				adapter.setData(success)
			}
			is AppState.OnShowMessage -> {
				Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
			}
		}

	}

	override fun onDestroy() {
		super.onDestroy()
		_binding = null
	}
}