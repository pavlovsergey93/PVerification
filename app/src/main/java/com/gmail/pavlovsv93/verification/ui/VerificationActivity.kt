package com.gmail.pavlovsv93.verification.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.gmail.pavlovsv93.verification.R
import com.gmail.pavlovsv93.verification.databinding.ActivityVerificationBinding
import com.gmail.pavlovsv93.verification.domain.KipEntity
import com.gmail.pavlovsv93.verification.ui.auth.AuthFragment

class VerificationActivity : AppCompatActivity() {

	private var fragment: Fragment? = null

	fun setOnBackPressedListener(fragment: Fragment?) {
		this.fragment = fragment
	}

	interface OnClickTheDevice {
		fun onClick(kipEntity: KipEntity)
	}

	companion object {
		const val DEFAULT_PAUSE = 2500L
	}

	private val toast: Toast by lazy {
		Toast.makeText(this, getString(R.string.message_exit), Toast.LENGTH_SHORT)
	}
	private var backPressTime: Long = 0L
	private lateinit var binding: ActivityVerificationBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		binding = ActivityVerificationBinding.inflate(layoutInflater)
		setContentView(binding.root)
		super.onCreate(savedInstanceState)
		showFragment(AuthFragment.newInstance(), false)
	}

	private fun showFragment(fragment: Fragment, backstack: Boolean) {
		val fm = supportFragmentManager.beginTransaction()
		fm.replace(R.id.fcContainerMain, fragment)
		if (backstack) {
			fm.addToBackStack(null)
		}
		fm.commit()
	}

	override fun onBackPressed() {
		if (fragment != null) {
			if (backPressTime + DEFAULT_PAUSE > System.currentTimeMillis()) {
				toast.cancel()
				super.onBackPressed()
			} else {
				toast.show()
			}
			backPressTime = System.currentTimeMillis()
		} else {
			super.onBackPressed()
		}
	}
}