package addfragment

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.gmail.pavlovsv93.verification.*
import com.gmail.pavlovsv93.verification.databinding.FragmentAddKipEntityBinding
import com.gmail.pavlovsv93.verification.domain.KipEntity
import com.gmail.pavlovsv93.verification.ui.VerificationActivity
import com.gmail.pavlovsv93.verification.utils.AppState
import com.gmail.pavlovsv93.verification.utils.dataFormat
import com.gmail.pavlovsv93.verification.utils.dateParsed
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AddKipEntityFragment : Fragment() {
	companion object {
		const val ARG_ID_UPDATE = "ARG_ID_UPDATE.AddKipEntityFragment"
		fun newInstance() = AddKipEntityFragment()
		fun updateInstance(id: String): AddKipEntityFragment {
			return AddKipEntityFragment().apply {
				arguments = Bundle().apply {
					putString(ARG_ID_UPDATE, id)
				}
			}
		}
	}

	private var _binding: FragmentAddKipEntityBinding? = null
	private val binding get() = _binding!!
	private val viewModel: AddOrUpdateKipEntityViewModel by viewModel(named("AddOrUpdateKipEntityViewModel"))
	private var len = 0
	private var idKipEntity: String? = null

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentAddKipEntityBinding.inflate(inflater, container, false)
		val adapterModel = ArrayAdapter(requireContext(), R.layout.edit_text_list_item, modelList)
		binding.etModel.setAdapter(adapterModel)
		val adapterParameter =
			ArrayAdapter(requireContext(), R.layout.edit_text_list_item, parameterList)
		binding.etParameter.setAdapter(adapterParameter)
		val adapterKey = ArrayAdapter(requireContext(), R.layout.edit_text_list_item, keyStation)
		binding.etKeyStation.setAdapter(adapterKey)
		val adapterStation = ArrayAdapter(requireContext(), R.layout.edit_text_list_item, station)
		binding.etStation.setAdapter(adapterStation)
		val adapterVerification =
			ArrayAdapter(requireContext(), R.layout.edit_text_list_item, verificationList)
		binding.etVerification.setAdapter(adapterVerification)
		val adapterStatus = ArrayAdapter(requireContext(), R.layout.edit_text_list_item, statusList)
		binding.etStatus.setAdapter(adapterStatus)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		(requireActivity() as VerificationActivity).setOnBackPressedListener(null)
		viewModel.getLiveData().observe(viewLifecycleOwner, Observer { state ->
			ranger(state)
		})
		idKipEntity = arguments?.getString(ARG_ID_UPDATE)
		if (!idKipEntity.isNullOrEmpty()) {
			binding.btnSave.text = getString(R.string.update)
			viewModel.getItemKipEntities(idKipEntity!!)
		}
		binding.btnExit.setOnClickListener {
			parentFragmentManager.beginTransaction()
				.remove(this)
				.commit()
		}
		binding.etDate.addTextChangedListener(object : TextWatcher {
			override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
				len = p0?.length!!
			}

			override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
				len = p0?.length!!
			}

			override fun afterTextChanged(p0: Editable?) {
				if (p0?.length == 2) {
					p0.append(".")
				}
				if (p0?.length == 5) {
					p0.append(".")
				}
			}

		})
		binding.btnSave.setOnClickListener {
			val dataMap: MutableMap<String, Any> = mutableMapOf()
			if (checkOnNotEmpty()) {
				with(binding) {
					dataMap[KEY_NUMBER] = etNumber.text.toString()
					dataMap[KEY_DATE] = dateParsed(etDate.text.toString())
					dataMap[KEY_MODEL] = etModel.text.toString()
					dataMap[KEY_PARAMETER] = etParameter.text.toString()
					dataMap[KEY_STATION_KEY] = etKeyStation.text.toString().toInt()
					dataMap[KEY_STATION] = etStation.text.toString()
					dataMap[KEY_POSITION] = etPosition.text.toString()
					dataMap[KEY_DESCRIPTION] = if (etDescription.text.isNullOrEmpty()) {
						"Неизвестно"
					} else {
						etDescription.text.toString()
					}
					dataMap[KEY_STATUS] = etStatus.text.toString()
					dataMap[KEY_VERIFICATION] = etVerification.text.toString().toLong()
					dataMap[KEY_INFO] = if (etInfo.text.isNullOrEmpty()) {
						"Нет информации"
					} else {
						etInfo.text.toString()
					}
					dataMap[KEY_NEXT_DATE] =
						dateParsed(
							getNextDate(
								etDate.text.toString(),
								etVerification.text.toString().toLong()
							)
						)
				}
				if (idKipEntity.isNullOrEmpty()) {
					viewModel.addKipEntityToBase(dataMap)
				} else {
					idKipEntity?.let { viewModel.updateDataDevice(it, dataMap) }
				}
				parentFragmentManager.beginTransaction()
					.remove(this)
					.commit()
			} else {
				Snackbar.make(binding.root, getString(R.string.is_empty), Snackbar.LENGTH_LONG)
					.show()
			}
		}
	}

	private fun checkOnNotEmpty(): Boolean {
		var error = 0
		if (binding.etDate.text.isNullOrEmpty()) {
			binding.etDate.error = getString(R.string.error_data)
			error++
		}
		if (binding.etNumber.text.isNullOrEmpty()) {
			binding.etNumber.error = getString(R.string.error_number)
			error++
		}
		if (binding.etModel.text.isNullOrEmpty()) {
			binding.etModel.error = getString(R.string.error_model)
			error++
		}
		if (binding.etParameter.text.isNullOrEmpty()) {
			binding.etParameter.error = getString(R.string.error_parameter)
			error++
		}
		if (binding.etStatus.text.isNullOrEmpty()) {
			binding.etStatus.error = getString(R.string.error_status)
			error++
		}
		if (binding.etStation.text.isNullOrEmpty()) {
			binding.etStation.error = getString(R.string.error_station)
			error++
		}
		if (binding.etKeyStation.text.isNullOrEmpty()) {
			binding.etKeyStation.error = getString(R.string.error_station_key)
			error++
		}
		if (binding.etPosition.text.isNullOrEmpty()) {
			binding.etPosition.error = getString(R.string.error_position)
			error++
		}
		if (binding.etVerification.text.isNullOrEmpty()) {
			binding.etVerification.error = getString(R.string.error_verification)
			error++
		}
		return error == 0
	}

	private fun getNextDate(date: String, verification: Long): String {
		val formater: DateTimeFormatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			DateTimeFormatter.ofPattern("dd.MM.yyyy")
		} else {
			TODO("VERSION.SDK_INT < O")
		}
		return LocalDate.parse(date, formater).plusYears(verification).format(formater)
	}

	private fun ranger(state: AppState) {
		when (state) {
			is AppState.OnError -> {
				Snackbar.make(
					binding.root,
					state.exception.message.toString(),
					Snackbar.LENGTH_INDEFINITE
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
				(state.success as KipEntity).let {
					displayData(it)
				}
			}
			is AppState.OnShowMessage -> {
				Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
			}
		}

	}

	private fun displayData(entity: KipEntity) {
		with(binding) {
			etDate.setText(dataFormat(entity.date))
			etModel.setText(entity.model, false)
			etNumber.setText(entity.number)
			etParameter.setText(entity.parameter, false)
			etKeyStation.setText(entity.keyStation.toString(), false)
			etStation.setText(entity.station, false)
			etStatus.setText(entity.status, false)
			etPosition.setText(entity.position)
			etDescription.setText(entity.description)
			etVerification.setText(entity.verification.toString(), false)
			etInfo.setText(entity.info)
		}
	}

	override fun onDestroy() {
		super.onDestroy()
		_binding = null
	}
}

