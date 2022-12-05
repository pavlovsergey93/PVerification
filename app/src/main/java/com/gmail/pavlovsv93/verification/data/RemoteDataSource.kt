package com.gmail.pavlovsv93.verification.data

import com.gmail.pavlovsv93.verification.*
import com.gmail.pavlovsv93.verification.data.repository.RepositoryInterface
import com.gmail.pavlovsv93.verification.domain.DataSourceInterface
import com.gmail.pavlovsv93.verification.domain.KipEntity
import com.gmail.pavlovsv93.verification.ui.listdevices.FilterEntity
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RemoteDataSource(private val repository: RepositoryInterface) : DataSourceInterface {

	override suspend fun getAllKipEntities(): Flow<List<KipEntity>> {
		return repository.getAllData().map { resultList ->
			convert(resultList)
		}
	}

	override suspend fun getItemKipEntities(idKip: String): Flow<KipEntity> {
		return repository.getDetailIntoToItemData(idKip)
			.map { convertItem(it) }
	}

	override suspend fun addKipEntity(data: Map<String, Any>): Flow<Any> {
		return repository.addData(data)
	}

	override suspend fun getVerifiableDevices(): Flow<List<KipEntity>> {
		return repository.getVerifiableDevices().map { resultList ->
			convert(resultList).filter { item ->
				item.status != STATUS_REJECTED
						&& item.status != STATUS_FIX
						&& item.status != STATUS_REMOTE
						&& item.status != STATUS_REMOVED
			}
		}
		.map {
			it.sortedBy { it.keyStation }
		}
	}

	override suspend fun updateDataDevice(idKip: String, data: Map<String, Any>): Flow<String> {
		return repository.updateData(idKip = idKip, data = data)
	}

	override suspend fun getDataAsFilter(filter: FilterEntity): Flow<List<KipEntity>> {
		return if (filter.isCheckedData != null) {
			repository.getDataAsFilter(filter.isCheckedData!!).map {
				convert(it)
			}
		}else{
			repository.getAllData().map {
				convert(it)
			}
		}
	}

	private fun convert(list: List<DocumentSnapshot>): List<KipEntity> {
		val listDevice: MutableList<KipEntity> = mutableListOf()
		list.forEach { item ->
			convertItem(item).let {
				if (item.id != "") {
					listDevice.add(it)
				}
			}
		}
		return listDevice
	}

	private fun convertItem(item: DocumentSnapshot): KipEntity {
		val id = item.id
		item.data
		val date = item.data?.get(KEY_DATE)
		val number = item.data?.get(KEY_NUMBER)
		val parameter = item.data?.get(KEY_PARAMETER)
		val description = item.data?.get(KEY_DESCRIPTION)
		val model = item.data?.get(KEY_MODEL)
		val keyStation = item.data?.get(KEY_STATION_KEY)
		val station = item.data?.get(KEY_STATION)
		val position = item.data?.get(KEY_POSITION)
		val nextDate = item.data?.get(KEY_NEXT_DATE)
		val verification = item.data?.get(KEY_VERIFICATION)
		val status = item.data?.get(KEY_STATUS)
		val info = item.data?.get(KEY_INFO)
		return KipEntity(
			idKip = id,
			number = number as String,
			date = date as Timestamp,
			model = model as String,
			parameter = parameter as String,
			keyStation = keyStation as Long,
			station = station as String,
			position = position as String,
			description = description as String,
			nextDate = nextDate as Timestamp,
			status = status as String,
			verification = verification as Long,
			info = info as String
		)
	}
}
