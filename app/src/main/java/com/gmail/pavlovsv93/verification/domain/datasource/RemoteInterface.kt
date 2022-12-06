package com.gmail.pavlovsv93.verification.domain.datasource

import com.gmail.pavlovsv93.verification.domain.KipEntity
import kotlinx.coroutines.flow.Flow

interface RemoteInterface {
    suspend fun getVerifiableDevices() : Flow<List<KipEntity>>
    suspend fun updateDate(id: String, data: Map<String, Any?>) : Flow<String>
}