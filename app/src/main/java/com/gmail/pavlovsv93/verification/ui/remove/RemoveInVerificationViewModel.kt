package com.gmail.pavlovsv93.verification.ui.remove

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.pavlovsv93.verification.data.realtime.RealtimeRepositoryInterface
import com.gmail.pavlovsv93.verification.data.realtime.entity.Entity
import com.gmail.pavlovsv93.verification.domain.DataSourceInterface
import com.gmail.pavlovsv93.verification.utils.AppState
import kotlinx.coroutines.launch

class RemoveInVerificationViewModel(
	private val liveData: MutableLiveData<AppState>,
	private val dataSource: DataSourceInterface,
	private val repository: RealtimeRepositoryInterface
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

	fun updateDataDevice(idKip: String, data: Map<String, Any>) {
		viewModelScope.launch {
			dataSource.updateDataDevice(idKip, data).collect { result ->
				liveData.postValue(AppState.OnShowMessage(result))
			}
		}
	}

	fun addEntityRealtimeList(entity: Entity) {
		viewModelScope.launch {
			repository.addDevice(entity)
		}
	}
}