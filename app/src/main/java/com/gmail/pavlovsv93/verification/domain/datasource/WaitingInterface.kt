package com.gmail.pavlovsv93.verification.domain.datasource

import com.gmail.pavlovsv93.verification.domain.KipEntity
import kotlinx.coroutines.flow.Flow


interface WaitingInterface {
    suspend fun getWaitingKip() : Flow<List<KipEntity>>
    suspend fun updateKip(id: String, data: Map<String, Any?>) : Flow<String>
}