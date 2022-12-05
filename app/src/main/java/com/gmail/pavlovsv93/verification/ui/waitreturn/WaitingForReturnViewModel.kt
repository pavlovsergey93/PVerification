package com.gmail.pavlovsv93.verification.ui.waitreturn

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.pavlovsv93.verification.data.realtime.RealtimeRepositoryInterface
import com.gmail.pavlovsv93.verification.data.realtime.entity.Entity
import com.gmail.pavlovsv93.verification.domain.DataSourceInterface
import com.gmail.pavlovsv93.verification.utils.AppState
import com.gmail.pavlovsv93.verification.utils.AppState.*
import kotlinx.coroutines.launch

class WaitingForReturnViewModel(
	private val repository: RealtimeRepositoryInterface,
	private val dataSource: DataSourceInterface,
	private val liveData: MutableLiveData<AppState>
) : ViewModel() {
	fun getLiveData(): LiveData<AppState> = liveData
	fun getAllEntity() {
		try {
			viewModelScope.launch {
				liveData.postValue(OnLoading(true))
				repository.getAllDevice()
					.collect { result ->
					liveData.postValue(OnLoading(false))
					if (!result.isNullOrEmpty()) {
						liveData.postValue(OnSuccess(result))
					} else {
						liveData.postValue(OnShowMessage("Список пуст!"))
					}
				}
			}
		} catch (exception: Exception) {
			liveData.postValue(OnError(exception))
		} finally {
			liveData.postValue(OnLoading(false))
		}
	}

	fun updateKipEntity(entity: Entity, status: String) {
		try {
			viewModelScope.launch {
				repository.updateStatus(entity, status)
			}
		} catch (exception: Exception){
			liveData.postValue(OnError(exception))
		}
	}

	fun updateDataDevice(idKip: String, data: Map<String, Any>) {
		viewModelScope.launch {
			dataSource.updateDataDevice(idKip, data).collect { result ->
				liveData.postValue(AppState.OnShowMessage(result))
			}
		}
	}

	fun deleteKipEntity(entity: Entity) {
		try {
			viewModelScope.launch {
				repository.deleteDevice(entity)
			}
		} catch (exception: Exception){
			liveData.postValue(OnError(exception))
		}
	}
}