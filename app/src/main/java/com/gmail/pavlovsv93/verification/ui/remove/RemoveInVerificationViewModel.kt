package com.gmail.pavlovsv93.verification.ui.remove

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.pavlovsv93.verification.domain.datasource.RemoteInterface
import com.gmail.pavlovsv93.verification.utils.AppState
import kotlinx.coroutines.launch

class RemoveInVerificationViewModel(
    private val liveData: MutableLiveData<AppState>,
    private val dataSource: RemoteInterface
) : ViewModel() {
	fun getLiveData(): LiveData<AppState> = liveData
	fun getVerifiableDevices() {
		liveData.postValue(AppState.OnLoading(true))
		try {
			viewModelScope.launch {
				dataSource.getVerifiableDevices().collect { result ->
					liveData.postValue(AppState.OnLoading(false))
					if (result.isEmpty()) {
						liveData.postValue(AppState.OnShowMessage("Список пуст!"))
					} else {
						liveData.postValue(AppState.OnSuccess(result))
					}
				}
			}
		} catch (exception: Exception) {
			liveData.postValue(AppState.OnLoading(false))
			liveData.postValue(AppState.OnError(exception))
		}
	}

	fun updateDataDevice(idKip: String, data: Map<String, Any?>) {
		viewModelScope.launch {
			dataSource.updateDate(idKip, data).collect { result ->
				liveData.postValue(AppState.OnShowMessage(result))
			}
		}
	}
}