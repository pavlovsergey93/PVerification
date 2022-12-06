package com.gmail.pavlovsv93.verification.ui.verification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import com.gmail.pavlovsv93.verification.R
import com.gmail.pavlovsv93.verification.databinding.FragmentVerificationBinding
import com.gmail.pavlovsv93.verification.ui.detailsdevice.DetailsDeviceFragment
import com.gmail.pavlovsv93.verification.ui.listdevices.ListDevicesFragment
import com.gmail.pavlovsv93.verification.ui.listdevices.ListDevicesFragment.Companion.ARG_ID_KIP
import com.gmail.pavlovsv93.verification.ui.listdevices.ListDevicesFragment.Companion.KEY_ID_KIP
import com.gmail.pavlovsv93.verification.ui.remove.RemoveInVerificationFragment
import com.gmail.pavlovsv93.verification.ui.waitreturn.WaitingForReturnFragment

class MainVerificationFragment : Fragment() {
	companion object {
		fun newInstance() = MainVerificationFragment()
	}

	private var _binding: FragmentVerificationBinding? = null
	private val binding get() = _binding!!

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentVerificationBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		if (savedInstanceState == null) {
			showFragment(ListDevicesFragment.newInstance(), false)
		}
		parentFragmentManager.setFragmentResultListener(
			KEY_ID_KIP,
			this,
			FragmentResultListener { _, result ->
				val idKip = result.getString(ARG_ID_KIP)
				if (!idKip.isNullOrEmpty()) {
					showFragment(DetailsDeviceFragment.newInstance(idKip), true)
				}
			})
		binding.bnvNavigation.setOnItemSelectedListener { item ->
			when (item.itemId) {
				R.id.listDevice -> {
					showFragment(ListDevicesFragment.newInstance(), false)
					true
				}
				R.id.verification -> {
					showFragment(RemoveInVerificationFragment.newInstance(), false)
					true
				}
				R.id.returnDevices -> {
					showFragment(WaitingForReturnFragment.newInstance(), false)
					true
				}
				else -> false
			}
		}
		binding.bnvNavigation.selectedItemId = R.id.listDevice
	}

	private fun showFragment(fragment: Fragment, backstack: Boolean) {
		val fm = parentFragmentManager.beginTransaction()
		fm.replace(R.id.fcContainer, fragment)
		if (backstack) {
			fm.addToBackStack(null)
		}
		fm.commit()
	}

	override fun onDestroy() {
		_binding = null
		super.onDestroy()
	}
}