package com.gmail.pavlovsv93.verification.data.editor

import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.flow.Flow

interface EditorRepositoryInterface {
    suspend fun addKip(data: Map<String, Any>) : Flow<String>
    suspend fun updateKip(id: String, data: Map<String, Any>): Flow<String>
    suspend fun getItemKip(id: String) : Flow<DocumentSnapshot>
}