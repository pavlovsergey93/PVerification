package addfragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.pavlovsv93.verification.IS_SUCCESS
import com.gmail.pavlovsv93.verification.domain.datasource.EditorInterface
import com.gmail.pavlovsv93.verification.utils.AppState
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.launch

class AddOrUpdateKipEntityViewModel(
	private val liveData: MutableLiveData<AppState>,
	private val dataSource: EditorInterface
) : ViewModel() {

	fun getLiveData() : LiveData<AppState> = liveData
	fun addKipEntityToBase(data: Map<String, Any>){
		liveData.postValue(AppState.OnLoading(true))
		viewModelScope.launch {
			try {
				dataSource.addKip(data).collect { result ->
					liveData.postValue(AppState.OnLoading(false))
					val id: String = (result as DocumentReference).path[1].toString()
					if (!id.isNullOrEmpty()) {
						liveData.postValue(AppState.OnShowMessage(IS_SUCCESS))
					} else {
						liveData.postValue((result).get().exception?.let {
							AppState.OnError(it)
						})
					}
				}
			} catch (exception : Exception){
				liveData.postValue(AppState.OnError(exception))
			} finally {
				liveData.postValue(AppState.OnLoading(false))
			}
		}
	}
	fun updateDataDevice(id: String, data: Map<String, Any>) {
		viewModelScope.launch {
			dataSource.updateKip(id, data).collect { result ->
				liveData.postValue(AppState.OnShowMessage(result))
			}
		}
	}
	fun getItemKipEntities(id: String) {
		try {
			liveData.postValue(AppState.OnLoading(true))
			viewModelScope.launch {
				dataSource.getItemKip(id)
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