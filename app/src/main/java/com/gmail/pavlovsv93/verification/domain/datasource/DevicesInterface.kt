package com.gmail.pavlovsv93.verification.domain.datasource

import com.gmail.pavlovsv93.verification.domain.KipEntity
import com.gmail.pavlovsv93.verification.ui.listdevices.bottomsheet.filter.FilterEntity
import kotlinx.coroutines.flow.Flow

interface DevicesInterface {
    suspend fun getAllKip() : Flow<List<KipEntity>>
    suspend fun getSearchListKip(filter: FilterEntity) : Flow<List<KipEntity>>
}