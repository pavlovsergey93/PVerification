package com.gmail.pavlovsv93.verification.ui.listdevices.bottomsheet.update

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.pavlovsv93.verification.domain.datasource.EditorInterface
import kotlinx.coroutines.launch

class UpdateViewModel(
    private val liveData: MutableLiveData<AppStateUpdate>,
    private val dataSource: EditorInterface
): ViewModel() {
    fun getData(): LiveData<AppStateUpdate> = liveData
    fun updateKip(id: String, data: Map<String, Any>) = viewModelScope.launch {
        try {
            dataSource.updateKip(id, data).collect { message ->
                liveData.postValue(AppStateUpdate.OnShowMessage(message))
            }
        } catch (exp: Exception){
            liveData.postValue(AppStateUpdate.OnShowMessage(exp.message.toString()))
        }
    }
}