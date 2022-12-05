package com.gmail.pavlovsv93.verification.data.realtime

import android.util.Log
import com.gmail.pavlovsv93.verification.KEY_STATUS
import com.gmail.pavlovsv93.verification.data.realtime.entity.Entity
import com.gmail.pavlovsv93.verification.utils.dateParsed
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class RealtimeRepository(
	private val reference: DatabaseReference
) : RealtimeRepositoryInterface {
	override suspend fun getAllDevice(): Flow<List<Entity>> = callbackFlow {
		val listener = object : ValueEventListener{
			override fun onDataChange(snapshot: DataSnapshot) {
				val resultList = mutableListOf<Entity>()
				snapshot.children.forEach { item ->
					val itemData = item.getValue(Entity::class.java)
					itemData?.let { resultList.add(it) }
				}
				resultList.sortBy { it.number}
				resultList.sortBy { dateParsed(it.date) }
				trySend(resultList)
			}

			override fun onCancelled(error: DatabaseError) {
				try {
					throw error.toException()
				}catch (e : Exception){
					Log.d("EEE", e.message.toString())
				}
			}
		}
		reference.addValueEventListener(listener)
		awaitClose {
			this.close()
		}
	}

	override suspend fun addDevice(entity: Entity) {
		Log.d("VVV.RealtimeRepos", "$entity")
		reference.child(entity.uid).setValue(entity)
	}

	override suspend fun updateStatus(entity: Entity, newStatus: String) {
		val postValue = hashMapOf<String, Any>("/${entity.uid}/$KEY_STATUS" to newStatus)
		reference.updateChildren(postValue)
	}

	override suspend fun deleteDevice(entity: Entity) {
		reference.child(entity.uid).removeValue()
	}

}