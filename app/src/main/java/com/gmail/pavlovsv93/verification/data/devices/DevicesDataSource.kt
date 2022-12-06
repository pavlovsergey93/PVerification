package com.gmail.pavlovsv93.verification.data.devices

import com.gmail.pavlovsv93.verification.domain.KipEntity
import com.gmail.pavlovsv93.verification.domain.datasource.DevicesInterface
import com.gmail.pavlovsv93.verification.ui.listdevices.FilterEntity
import com.gmail.pavlovsv93.verification.utils.convertToKipEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DevicesDataSource(
    private val repository: DevicesRepositoryInterface
) : DevicesInterface {
    override suspend fun getAllKip(): Flow<List<KipEntity>> {
        return repository.getAllKip().map { list ->
            list.map { item ->
                convertToKipEntity(item)
            }
        }
    }

    override suspend fun getSearchListKip(filter: FilterEntity): Flow<List<KipEntity>> {
        return repository.getSearchListKip(filter).map { search ->
            search.map { item ->
                convertToKipEntity(item)
            }
        }
    }
}