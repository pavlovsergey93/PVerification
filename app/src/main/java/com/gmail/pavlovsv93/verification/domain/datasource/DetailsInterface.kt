package com.gmail.pavlovsv93.verification.domain.datasource

import com.gmail.pavlovsv93.verification.domain.KipEntity
import kotlinx.coroutines.flow.Flow

interface DetailsInterface {
    suspend fun getItemKip(id: String): Flow<KipEntity>
}