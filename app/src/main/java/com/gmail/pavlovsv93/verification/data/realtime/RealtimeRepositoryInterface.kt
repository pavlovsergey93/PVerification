package com.gmail.pavlovsv93.verification.data.realtime

import com.gmail.pavlovsv93.verification.data.realtime.entity.Entity
import kotlinx.coroutines.flow.Flow

interface RealtimeRepositoryInterface {
	suspend fun getAllDevice(): Flow<List<Entity>>
	suspend fun addDevice(entity: Entity)
	suspend fun updateStatus(entity: Entity, newStatus: String)
	suspend fun deleteDevice(entity: Entity)
}