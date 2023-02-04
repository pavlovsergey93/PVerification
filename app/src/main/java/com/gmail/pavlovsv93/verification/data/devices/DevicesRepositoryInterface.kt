package com.gmail.pavlovsv93.verification.data.devices

import com.gmail.pavlovsv93.verification.ui.listdevices.bottomsheet.filter.FilterEntity
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.flow.Flow

interface DevicesRepositoryInterface {
    suspend fun getAllKip() : Flow<List<DocumentSnapshot>>
    suspend fun getSearchListKip(filter: FilterEntity) : Flow<List<DocumentSnapshot>>
}