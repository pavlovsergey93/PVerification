package com.gmail.pavlovsv93.verification.ui.listdevices.bottomsheet.update

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import com.gmail.pavlovsv93.verification.*
import com.gmail.pavlovsv93.verification.databinding.FragmentBottomSheetCorectLocationBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named

class BottomSheetLocationCorrectFragment : BottomSheetDialogFragment() {
    companion object {
        const val TAG = "BottomSheetLocationCorrectFragment"
        const val ARG_LOCATION_ID = "ARG_LOCATION_ID"
        const val ARG_LOCATION_KEY_STATION = "ARG_LOCATION_KEY_STATION"
        const val ARG_LOCATION_STATION = "ARG_LOCATION_STATION"
        const val ARG_LOCATION_POSITION = "ARG_LOCATION_POSITION"
        fun newInstance(id: String, keyStation: Long, station: String, position: String) =
            BottomSheetLocationCorrectFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_LOCATION_ID, id)
                    putLong(ARG_LOCATION_KEY_STATION, keyStation)
                    putString(ARG_LOCATION_STATION, station)
                    putString(ARG_LOCATION_POSITION, position)
                }
            }
    }

    private var argId: String? = null
    private var argKeyStation: Long = 0L
    private var argStation: String? = null
    private var argPosition: String? = null
    private var _binding: FragmentBottomSheetCorectLocationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: UpdateViewModel by viewModel(named("UpdateViewModel"))
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetCorectLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initEditText()
        super.onViewCreated(view, savedInstanceState)
        viewModel.getData().observe(viewLifecycleOwner, Observer { state ->
            ranger(state)
        })
        arguments?.let {
            argId = it.getString(ARG_LOCATION_ID)
            argKeyStation = it.getLong(ARG_LOCATION_KEY_STATION)
            argStation = it.getString(ARG_LOCATION_STATION)
            argPosition = it.getString(ARG_LOCATION_POSITION)
            binding.etKeyStation.setText(argKeyStation.toString(), false)
            binding.etStation.setText(argStation, false)
            binding.etPosition.setText(argPosition)
        }
        binding.btnUpdateLocation.setOnClickListener {
            if (!binding.etKeyStation.text.isNullOrEmpty()
                && !binding.etStation.text.isNullOrEmpty()
                && !binding.etPosition.text.isNullOrEmpty()
                && !argId.isNullOrEmpty()
            ) {
                argKeyStation = binding.etKeyStation.text.toString().toLong()
                argStation = binding.etStation.text.toString()
                argPosition = binding.etPosition.text.toString()
                val data = mutableMapOf<String, Any>()
                data[KEY_STATION_KEY] = argKeyStation
                data[KEY_STATION] = argStation!!
                data[KEY_POSITION] = argPosition!!
                viewModel.updateKip(argId!!, data)
            } else {
                if (binding.etStation.text.isNullOrEmpty()) {
                    binding.etStation.error = getString(R.string.error_station)
                }
                if (binding.etKeyStation.text.isNullOrEmpty()) {
                    binding.etKeyStation.error = getString(R.string.error_station_key)
                }
                if (binding.etPosition.text.isNullOrEmpty()) {
                    binding.etPosition.error = getString(R.string.error_position)
                }
            }
        }
    }

    private fun initEditText() {
        val adapterKey = ArrayAdapter(requireContext(), R.layout.edit_text_list_item, keyStation)
        binding.etKeyStation.setAdapter(adapterKey)
        val adapterStation = ArrayAdapter(requireContext(), R.layout.edit_text_list_item, station)
        binding.etStation.setAdapter(adapterStation)
    }

    private fun ranger(state: AppStateUpdate) {
        when(state){
            is AppStateUpdate.OnShowMessage -> {
                Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                this.dismiss()
            }
        }
    }


    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}