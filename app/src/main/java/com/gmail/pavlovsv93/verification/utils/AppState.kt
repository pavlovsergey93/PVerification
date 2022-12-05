package com.gmail.pavlovsv93.verification.utils

sealed class AppState{
	data class OnLoading(var load: Boolean) : AppState()
	data class OnSuccess<T>(val success: T) : AppState()
	data class OnError(val exception: Exception): AppState()
	data class OnShowMessage(val message: String): AppState()
}
