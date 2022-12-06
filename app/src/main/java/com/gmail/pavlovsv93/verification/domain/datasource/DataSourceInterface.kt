package com.gmail.pavlovsv93.verification.domain.datasource

import com.gmail.pavlovsv93.verification.domain.KipEntity
import com.gmail.pavlovsv93.verification.ui.listdevices.FilterEntity
import kotlinx.coroutines.flow.Flow

interface DataSourceInterface {
	suspend fun getAllKipEntities(): Flow<List<KipEntity>>
	suspend fun getItemKipEntities(idKip: String): Flow<KipEntity>
	suspend fun getVerifiableDevices(): Flow<List<KipEntity>>
	suspend fun addKipEntity(data: Map<String, Any>): Flow<Any>
	suspend fun updateDataDevice(idKip: String, data: Map<String, Any>): Flow<String>
	suspend fun getDataAsFilter(filter: FilterEntity):  Flow<List<KipEntity>>
}