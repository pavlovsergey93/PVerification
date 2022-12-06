package com.gmail.pavlovsv93.verification.domain.datasource

import com.gmail.pavlovsv93.verification.domain.KipEntity
import kotlinx.coroutines.flow.Flow

interface EditorInterface {
    suspend fun addKip(data: Map<String, Any>) : Flow<String>
    suspend fun updateKip(id: String, data: Map<String, Any>): Flow<String>
    suspend fun getItemKip(id: String) : Flow<KipEntity>
}