package com.gmail.pavlovsv93.verification.ui.detailsdevice

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.pavlovsv93.verification.domain.DataSourceInterface
import com.gmail.pavlovsv93.verification.utils.AppState
import kotlinx.coroutines.launch

class DetailsViewModel(
	private val liveData: MutableLiveData<AppState>,
	private val dataSource: DataSourceInterface
) : ViewModel() {
	fun getLiveData(): LiveData<AppState> = liveData
	fun getItemKipEntities(idKip: String) {
		try {
			liveData.postValue(AppState.OnLoading(true))
			viewModelScope.launch {
				dataSource.getItemKipEntities(idKip)
					.collect { result ->
							Log.d("WWW viewModel", "${result}")
							liveData.postValue(AppState.OnSuccess(result))
					}
			}
		} catch (exception: Exception) {
			liveData.postValue(AppState.OnError(exception))
		} finally {
			liveData.postValue(AppState.OnLoading(false))
		}
	}
}