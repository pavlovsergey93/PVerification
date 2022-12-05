package com.gmail.pavlovsv93.verification.ui.auth

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.gmail.pavlovsv93.verification.R
import com.gmail.pavlovsv93.verification.databinding.FragmentAuthBinding
import com.gmail.pavlovsv93.verification.ui.VerificationActivity
import com.gmail.pavlovsv93.verification.ui.verification.VerificationFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthFragment : Fragment() {
	companion object {
		fun newInstance() = AuthFragment()
		private const val ARG_SIGN_OUT_OR_DELETE = "ARG_SIGN_OUT_OR_DELETE"
		fun signOutOrDelete(flag: Boolean) = AuthFragment().apply {
			arguments = Bundle().apply {
				putBoolean(ARG_SIGN_OUT_OR_DELETE, flag)
			}
		}
	}

	private var _binding: FragmentAuthBinding? = null
	private val binding get() = _binding!!
	private lateinit var auth: FirebaseAuth

	@SuppressLint("RestrictedApi")
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		auth = AuthUI.getInstance().auth
		var flag = false
		arguments?.getBoolean(ARG_SIGN_OUT_OR_DELETE)?.let {
			flag = it
		}
		val currentUser = auth.currentUser
		if (currentUser == null) {
			Toast.makeText(requireContext(), "Пользователь не найден!", Toast.LENGTH_SHORT).show()
		} else if (flag){
			Toast.makeText(requireContext(), "Выполните вход!", Toast.LENGTH_SHORT).show()
		} else {
			updateUI(currentUser)
		}
		_binding = FragmentAuthBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		(requireActivity() as VerificationActivity).setOnBackPressedListener(this@AuthFragment)
		binding.btnSignIn.setOnClickListener {
			signInLauncher.launch(signInIntent)
		}
	}

	private fun updateUI(user: FirebaseUser?) {
		Toast.makeText(requireContext(), "Приветствую, ${user?.displayName}!", Toast.LENGTH_SHORT).show()
		parentFragmentManager.beginTransaction()
			.replace(R.id.fcContainerMain, VerificationFragment.newInstance())
			.commit()
	}

	override fun onDestroy() {
		_binding = null
		super.onDestroy()
	}

	private val signInLauncher : ActivityResultLauncher<Intent> =
		registerForActivityResult(FirebaseAuthUIActivityResultContract()) { task ->
			val response = task.idpResponse
			if (task.resultCode == Activity.RESULT_OK) {
				val currentUser = auth.currentUser
				updateUI(currentUser)
			} else {
				Toast.makeText(
					requireContext(),
					response?.error?.message.toString(),
					Toast.LENGTH_SHORT
				).show()
			}
		}


	private val provider: List<AuthUI.IdpConfig> by lazy {
		listOf(
			AuthUI.IdpConfig.EmailBuilder().build()
		)
	}

	private val signInIntent: Intent by lazy {
		AuthUI.getInstance()
			.createSignInIntentBuilder()
			.setAvailableProviders(provider)
			.build()
	}
}