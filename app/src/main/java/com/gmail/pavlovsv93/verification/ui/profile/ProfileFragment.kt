package com.gmail.pavlovsv93.verification.ui.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.firebase.ui.auth.AuthUI
import com.gmail.pavlovsv93.verification.R
import com.gmail.pavlovsv93.verification.databinding.FragmentProfileBinding
import com.gmail.pavlovsv93.verification.ui.VerificationActivity
import com.gmail.pavlovsv93.verification.ui.auth.AuthFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ProfileFragment : Fragment() {

	companion object {
		private const val ARG_NAME = "ARG_NAME"
		private const val ARG_IMAGE = "ARG_IMAGE"
		fun newInstance() = ProfileFragment()
	}

	private var _binding : FragmentProfileBinding? = null
	private val binding get() = _binding!!

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentProfileBinding.inflate(inflater, container, false)
		return binding.root
	}

	@SuppressLint("RestrictedApi", "CheckResult")
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		(requireActivity() as VerificationActivity).setOnBackPressedListener(null)
		val user = AuthUI.getInstance().auth.currentUser
		binding.tvName.text = user?.displayName+ "\n" + user?.email
		Glide.with(requireContext())
			.load(user?.photoUrl)
			.placeholder(R.drawable.ic_baseline_perm_identity_24)
			.transform(CircleCrop())
			.into(binding.ivAvatar)
		binding.btnSignOut.setOnClickListener {
			AuthUI.getInstance().signOut(requireContext())
			updateUI()
		}
		binding.btnDeleteAccount.setOnClickListener {
			MaterialAlertDialogBuilder(requireContext())
				.setTitle(R.string.title_delete)
				.setIcon(R.drawable.ic_baseline_close_24)
				.setNegativeButton(R.string.dialog_status_cancel){ dialog,_ ->
					dialog.cancel()
				}.setPositiveButton(R.string.delete_button){ dialog, _ ->
					deleteAccount()
					dialog.cancel()
				}.show()
		}
	}

	private fun deleteAccount() {
		AuthUI.getInstance().delete(requireContext())
		updateUI()
	}

	private fun updateUI() {
		parentFragmentManager.beginTransaction()
			.replace(R.id.fcContainerMain, AuthFragment.signOutOrDelete(true))
			.commitNow()
	}

	override fun onDestroy() {
		_binding = null
		super.onDestroy()
	}
}