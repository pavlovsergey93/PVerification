package com.gmail.pavlovsv93.verification.ui.waitreturn

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.gmail.pavlovsv93.verification.*
import com.gmail.pavlovsv93.verification.data.realtime.entity.Entity
import com.gmail.pavlovsv93.verification.databinding.FragmentWaitingForReturnBinding
import com.gmail.pavlovsv93.verification.ui.VerificationActivity
import com.gmail.pavlovsv93.verification.utils.AppState
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.Timestamp
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named
import java.util.*

class WaitingForReturnFragment : Fragment() {
	interface OnClickOrSweep {
		fun onClick(entity: Entity)
	}

	companion object {
		fun newInstance() = WaitingForReturnFragment()
	}

	private var status: String? = null
	private val viewModel: WaitingForReturnViewModel by viewModel(named("WaitingForReturnViewModel"))
	private var _binding: FragmentWaitingForReturnBinding? = null
	private val binding get() = _binding!!
	private val data: MutableMap<String, Any> = mutableMapOf()

	private val adapter: WaitingAdapter = WaitingAdapter(object : OnClickOrSweep {
		override fun onClick(entity: Entity) {
			when (entity.status) {
				STATUS_REMOVED -> {
					status = STATUS_VERIFICATION
				}
				STATUS_VERIFICATION -> {
					showDialog(entity, statusListWaitingReturn)
				}
				else -> {
					showDialog(entity, statusListWaiting)
				}
			}
			if (entity.status != STATUS_REJECTED && entity.status != STATUS_INSTALLED) {
				status?.let { viewModel.updateKipEntity(entity, it) }
			}
			viewModel.getAllEntity()
		}
	})

	private fun updateDataDevice(entity: Entity, status: String) {
		data[KEY_STATUS] = status
		data[KEY_DATE] = Timestamp.now()
		if (status == STATUS_INSTALLED || status == STATUS_SAVED) {
			callDataPicker(entity)
		} else {
			viewModel.updateDataDevice(entity.uid, data)
		}
	}

	private fun callDataPicker(entity: Entity) {
		val dateNow = Calendar.getInstance()
		val year = dateNow.get(Calendar.YEAR).plus(entity.verification)
		val month = dateNow.get(Calendar.MONTH)
		val day = dateNow.get(Calendar.DAY_OF_MONTH)
		DatePickerDialog(requireContext(),
			{ _, p1, p2, p3 ->
				dateNow.set(Calendar.YEAR, p1)
				dateNow.set(Calendar.MONTH, p2)
				dateNow.set(Calendar.DAY_OF_MONTH, p3)
				val nextDate: Timestamp = Timestamp(dateNow.time)
				data[KEY_NEXT_DATE] = nextDate
				viewModel.updateDataDevice(entity.uid, data)
				viewModel.deleteKipEntity(entity)
			}, year, month, day)
			.show()
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		_binding = FragmentWaitingForReturnBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		(requireActivity() as VerificationActivity).setOnBackPressedListener(this@WaitingForReturnFragment)
		viewModel.getAllEntity()
		viewModel.getLiveData().observe(viewLifecycleOwner, Observer { state ->
			ranger(state)
		})
		val recyclerView = binding.rvListWaiting
		recyclerView.layoutManager =
			LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
		recyclerView.adapter = adapter
	}

	private fun ranger(state: AppState) {
		when (state) {
			is AppState.OnError -> {
				Toast.makeText(
					requireContext(),
					state.exception.message.toString(),
					Toast.LENGTH_LONG
				).show()
			}
			is AppState.OnLoading -> {
				binding.pbProgress.visibility = if (state.load) {
					View.VISIBLE
				} else {
					View.GONE
				}
			}
			is AppState.OnSuccess<*> -> {
				val result = state.success as List<Entity>
				adapter.setData(result)
			}
			is AppState.OnShowMessage -> {
				Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
			}
		}
	}

	private fun showDialog(entity: Entity, statusList: Array<String>) {
		MaterialAlertDialogBuilder(requireContext())
			.setTitle(getString(R.string.dialog_status_title))
			.setNeutralButton(getString(R.string.dialog_status_cancel)) { dialog, _ ->
				dialog.cancel()
			}.setSingleChoiceItems(statusList, CHECKED_ITEM) { dialog, which ->
				this.status = statusList[which]
				status?.let {
					if (this.status == STATUS_REJECTED || this.status == STATUS_INSTALLED || this.status == STATUS_SAVED) {
						updateDataDevice(entity, this.status!!)
					} else if (status == STATUS_FIT) {
						status = STATUS_TRUSTED
						this.status?.let { viewModel.updateKipEntity(entity, it) }
					}
				}

//				if (statusList[which] != STATUS_REJECTED) {
//					this.status?.let { viewModel.updateKipEntity(entity, it) }
//				} else {
//					viewModel.deleteKipEntity(entity)
//				}
				dialog.dismiss()
			}.show()
	}

	override fun onDestroy() {
		super.onDestroy()
		_binding = null
	}
}
