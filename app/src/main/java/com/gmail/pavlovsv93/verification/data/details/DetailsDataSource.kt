package com.gmail.pavlovsv93.verification.data.details

import com.gmail.pavlovsv93.verification.domain.KipEntity
import com.gmail.pavlovsv93.verification.domain.datasource.DetailsInterface
import com.gmail.pavlovsv93.verification.utils.convertToKipEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DetailsDataSource(private val repository: DetailsRepositoryInterface) : DetailsInterface {
    override suspend fun getItemKip(id: String): Flow<KipEntity> {
        return repository.getItemKip(id).map { item ->
            convertToKipEntity(item)
        }
    }
}