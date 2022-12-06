package com.gmail.pavlovsv93.verification.data.waiting

import com.gmail.pavlovsv93.verification.*
import com.gmail.pavlovsv93.verification.domain.KipEntity
import com.gmail.pavlovsv93.verification.domain.datasource.WaitingInterface
import com.gmail.pavlovsv93.verification.utils.convertToKipEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WaitingDataSource(private val repository: WaitingRepositoryInterface) : WaitingInterface {
    override suspend fun getWaitingKip(): Flow<List<KipEntity>> {
        return repository.getWaitingKip().map { list ->
            list.map { item ->
                convertToKipEntity(item)
            }.filter { item ->
                item.status == STATUS_REMOVED
                        || item.status == STATUS_VERIFICATION
                        || item.status == STATUS_TRUSTED
                        || item.status == STATUS_FIX
                        || item.status == STATUS_WAIT_FIX
            }
        }
    }

    override suspend fun updateKip(id: String, data: Map<String, Any?>): Flow<String> {
        return repository.updateKip(id, data)
    }
}