package com.gmail.pavlovsv93.verification.data.waiting

import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.flow.Flow

interface WaitingRepositoryInterface {
    suspend fun getWaitingKip() : Flow<List<DocumentSnapshot>>
    suspend fun updateKip(id: String, data: Map<String, Any?>) : Flow<String>
}