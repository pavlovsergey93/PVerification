package com.gmail.pavlovsv93.verification.ui.waitreturn

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.pavlovsv93.verification.domain.datasource.WaitingInterface
import com.gmail.pavlovsv93.verification.utils.AppState
import com.gmail.pavlovsv93.verification.utils.AppState.*
import kotlinx.coroutines.launch

class WaitingForReturnViewModel(
    private val dataSource: WaitingInterface,
    private val liveData: MutableLiveData<AppState>
) : ViewModel() {
    fun getLiveData(): LiveData<AppState> = liveData
    fun getAllEntity() {
        try {
            viewModelScope.launch {
                liveData.postValue(OnLoading(true))
                dataSource.getWaitingKip()
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

    fun updateKipEntity(id: String, data: Map<String, Any?>) {
        try {
            viewModelScope.launch {
                dataSource.updateKip(id, data)
            }
        } catch (exception: Exception) {
            liveData.postValue(OnError(exception))
        }
    }

}