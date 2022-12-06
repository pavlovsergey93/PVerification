package com.gmail.pavlovsv93.verification.data.editor

import com.gmail.pavlovsv93.verification.domain.KipEntity
import com.gmail.pavlovsv93.verification.domain.datasource.EditorInterface
import com.gmail.pavlovsv93.verification.utils.convertToKipEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class EditorDataSource(private val repository: EditorRepositoryInterface): EditorInterface {
    override suspend fun addKip(data: Map<String, Any>): Flow<String> {
        return repository.addKip(data)
    }

    override suspend fun updateKip(id: String, data: Map<String, Any>): Flow<String> {
        return repository.updateKip(id, data)
    }

    override suspend fun getItemKip(id: String): Flow<KipEntity> {
        return repository.getItemKip(id).map { item ->
            convertToKipEntity(item)
        }
    }
}