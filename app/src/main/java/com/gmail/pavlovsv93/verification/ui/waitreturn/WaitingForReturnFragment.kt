package com.gmail.pavlovsv93.verification.ui.waitreturn

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.gmail.pavlovsv93.verification.*
import com.gmail.pavlovsv93.verification.databinding.FragmentWaitingForReturnBinding
import com.gmail.pavlovsv93.verification.domain.KipEntity
import com.gmail.pavlovsv93.verification.ui.VerificationActivity
import com.gmail.pavlovsv93.verification.utils.AppState
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.Timestamp
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named
import java.util.*

class WaitingForReturnFragment : Fragment() {
    interface OnClickOrSweep {
        fun onClick(entity: KipEntity)
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
        override fun onClick(entity: KipEntity) {
            when (entity.status) {
                STATUS_REMOVED -> {
                    status = STATUS_VERIFICATION
                }
                STATUS_VERIFICATION -> {
                    showDialog(
                        entity,
                        statusListWaitingReturn,
                        getString(R.string.dialog_waiting_status_title)
                    )
                }
                else -> {
                    showDialog(
                        entity,
                        statusListWaiting,
                        getString(R.string.dialog_waiting_status_title2)
                    )
                }
            }
            viewModel.getAllEntity()
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
                val result = state.success as List<KipEntity>
                adapter.setData(result)
            }
            is AppState.OnShowMessage -> {
                Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDialog(entity: KipEntity, statusList: Array<String>, title: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setNeutralButton(getString(R.string.dialog_status_cancel)) { dialog, _ ->
                dialog.cancel()
            }.setSingleChoiceItems(statusList, CHECKED_ITEM) { dialog, which ->
                val s = statusList[which]
                if (s == STATUS_FIT) {
                    callDataPicker(entity, STATUS_TRUSTED)
                } else if (s == STATUS_REJECTED) {
                    callDataPicker(entity, s)
                } else {
                    data.clear()
                    data[KEY_STATUS] = s
                    data[KEY_DATE] = Timestamp.now()
                    viewModel.updateKipEntity(entity.idKip, data)
                }
                dialog.dismiss()
            }.show()
    }

    private fun callDataPicker(entity: KipEntity, status: String) {
        val dateNow = Calendar.getInstance()
        val year = if (status == STATUS_TRUSTED) {
            dateNow.get(Calendar.YEAR).plus(entity.verification.toInt())
        } else {
            dateNow.get(Calendar.YEAR)
        }
        val month = dateNow.get(Calendar.MONTH)
        val day = dateNow.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(
            requireContext(),
            { _, p1, p2, p3 ->
                dateNow.set(Calendar.YEAR, p1)
                dateNow.set(Calendar.MONTH, p2)
                dateNow.set(Calendar.DAY_OF_MONTH, p3)
                val nextDate: Timestamp = Timestamp(dateNow.time)
                data.clear()
                data[KEY_STATUS] = status
                if (status == STATUS_REJECTED) {
                    data[KEY_DATE] = nextDate
                } else {
                    data[KEY_NEXT_DATE] = nextDate
                }
                viewModel.updateKipEntity(entity.idKip, data)
            }, year, month, day
        ).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
