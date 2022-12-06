package com.gmail.pavlovsv93.verification.data.remote

import com.gmail.pavlovsv93.verification.domain.KipEntity
import com.gmail.pavlovsv93.verification.domain.datasource.RemoteInterface
import com.gmail.pavlovsv93.verification.utils.convertToKipEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RemoteDataSource(private val repository: RemoteRepositoryInterface) : RemoteInterface {
    override suspend fun getVerifiableDevices(): Flow<List<KipEntity>> {
        return repository.getVerifiableDevices().map { listVerification ->
            listVerification.map { item ->
                convertToKipEntity(item)
            }
        }
    }

    override suspend fun updateDate(id: String, data: Map<String, Any?>): Flow<String> {
        return repository.updateDate(id, data)
    }
}