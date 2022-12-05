package com.gmail.pavlovsv93.verification.ui.listdevices

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.pavlovsv93.verification.domain.DataSourceInterface
import com.gmail.pavlovsv93.verification.domain.KipEntity
import com.gmail.pavlovsv93.verification.utils.AppState
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ListDeviceViewModel(
	private val liveData: MutableLiveData<AppState>,
	private val dataSource: DataSourceInterface
) : ViewModel() {

	fun getLiveData(): LiveData<AppState> = liveData

	fun getAllData() {
		try {
			liveData.postValue(AppState.OnLoading(true))
			viewModelScope.launch {
				dataSource.getAllKipEntities()
					.collect { result ->
						if (!result.isEmpty()) {
							Log.d("WWW viewModel", "${result}")
							liveData.postValue(AppState.OnSuccess(result))
						}
					}
			}
		} catch (exception: Exception) {
			liveData.postValue(AppState.OnError(exception))
		} finally {
			liveData.postValue(AppState.OnLoading(false))
		}
	}

	fun getResultSearch(searchStr: String) {
		try {
			liveData.postValue(AppState.OnLoading(true))
			viewModelScope.launch {
				dataSource.getAllKipEntities()
					.map { list ->
						list.filter { item ->
							item.model.contains(searchStr, true)
									|| item.parameter.contains(searchStr, true)
									|| item.status.contains(searchStr, true)
									|| item.station.contains(searchStr, true)
									|| item.description.contains(searchStr, true)
									|| item.info.contains(searchStr, true)
									|| item.number.contains(searchStr, true)
									|| item.position.contains(searchStr, true)
						}
					}
					.collect { result ->
						Log.d("WWW viewModel.Search", "${result}")
						if (result.isNullOrEmpty()) {
							liveData.postValue(AppState.OnShowMessage("Не найдено!"))
						}
						liveData.postValue(AppState.OnSuccess(result))
					}
			}
		} catch (exception: Exception) {
			liveData.postValue(AppState.OnError(exception))
		} finally {
			liveData.postValue(AppState.OnLoading(false))
		}
	}

	fun getDataAsFilter(filter: FilterEntity) {
		try {
			liveData.postValue(AppState.OnLoading(true))
			viewModelScope.launch {
				dataSource.getDataAsFilter(filter)
					.map { list ->
						var listResultFilter: List<KipEntity> = list
						filter.model?.let { parameterFilter ->
							listResultFilter = listResultFilter.filter { item ->
								item.model == parameterFilter
							}
						}
						filter.station?.let { parameterFilter ->
							listResultFilter = listResultFilter.filter { item ->
								item.station == parameterFilter
							}
						}
						filter.status?.let { parameterFilter ->
							listResultFilter = listResultFilter.filter { item ->
								item.status == parameterFilter
							}
						}
						filter.parameter?.let { parameterFilter ->
							listResultFilter = listResultFilter.filter { item ->
								item.parameter == parameterFilter
							}
						}
						return@map listResultFilter
					}.collect { result ->
						Log.d("WWW viewModel.Search", "$result")
						if (result.isNullOrEmpty()) {
							liveData.postValue(AppState.OnShowMessage("Не найдено!"))
						}
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