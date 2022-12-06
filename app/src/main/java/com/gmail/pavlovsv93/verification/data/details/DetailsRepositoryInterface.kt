package com.gmail.pavlovsv93.verification.data.details

import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.flow.Flow

interface DetailsRepositoryInterface {
    suspend fun getItemKip(id: String): Flow<DocumentSnapshot>
}