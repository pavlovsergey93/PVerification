package com.gmail.pavlovsv93.verification.utils

import com.gmail.pavlovsv93.verification.domain.KipEntity

interface FirebaseCallback{
	fun onSuccess(list: List<KipEntity>)
	fun onError(exception: Exception)
}