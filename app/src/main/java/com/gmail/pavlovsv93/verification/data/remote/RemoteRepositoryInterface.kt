package com.gmail.pavlovsv93.verification.data.remote

import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.flow.Flow

interface RemoteRepositoryInterface {
    suspend fun getVerifiableDevices() : Flow<List<DocumentSnapshot>>
    suspend fun updateDate(id: String, data: Map<String, Any?>) : Flow<String>
}