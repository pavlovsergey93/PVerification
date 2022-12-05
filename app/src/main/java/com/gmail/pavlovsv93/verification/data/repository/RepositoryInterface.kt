package com.gmail.pavlovsv93.verification.data.repository

import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.flow.Flow

interface RepositoryInterface {
	suspend fun getAllData(): Flow<List<DocumentSnapshot>>
	suspend fun getDetailIntoToItemData(idKipEntity: String): Flow<DocumentSnapshot>
	suspend fun getVerifiableDevices(): Flow<List<DocumentSnapshot>>
	suspend fun addData(data: Map<String, Any>): Flow<Any>
	suspend fun updateData(data: Map<String, Any>, idKip: String): Flow<String>
	suspend fun getDataAsFilter(directionFlag: Boolean):  Flow<List<DocumentSnapshot>>
}