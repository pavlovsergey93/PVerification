package com.gmail.pavlovsv93.verification.data.repository

import android.util.Log
import com.gmail.pavlovsv93.verification.*
import com.google.firebase.firestore.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.*

class RemoteRepository(
	private val db: FirebaseFirestore
) : RepositoryInterface {

	override suspend fun getAllData(): Flow<List<DocumentSnapshot>> = callbackFlow {
		var eventCollection: CollectionReference? = null
		try {
			eventCollection = db.collection(COLLECTION)
		} catch (exception: Exception) {
			close(exception)
		}
		val subscription = eventCollection
			?.orderBy(KEY_STATION_KEY, Query.Direction.ASCENDING)
			?.orderBy(KEY_POSITION, Query.Direction.ASCENDING)
			?.addSnapshotListener { value, _ ->
				if (value == null) {
					return@addSnapshotListener
				}
				trySend(value.documents)
			}
		awaitClose {
			subscription?.remove()
		}
	}

	override suspend fun getDetailIntoToItemData(idKipEntity: String): Flow<DocumentSnapshot> =
		callbackFlow {
			var eventCollection: DocumentReference? = null
			try {
				eventCollection = db.collection(COLLECTION)
					.document(idKipEntity)
			} catch (exception: Exception) {
				close(exception)
			}
			val subscription =
				eventCollection?.addSnapshotListener { value, _ ->
					if (value == null) {
						return@addSnapshotListener
					}
					trySend(value)
				}
			awaitClose {
				subscription?.remove()
			}
		}


	override suspend fun getVerifiableDevices(): Flow<List<DocumentSnapshot>> =
		callbackFlow {
			var eventCollection: CollectionReference? = null
			try {
				eventCollection = db.collection(COLLECTION)
			} catch (exception: Exception) {
				close(exception)
			}
			val c = Calendar.getInstance()
			val mount = c.get(Calendar.MONTH).plus(1)
			val year = c.get(Calendar.YEAR)
			c.set(Calendar.DAY_OF_MONTH, 1)
			c.set(Calendar.MONTH, mount)
			c.set(Calendar.YEAR, year)
			val subscription = eventCollection
				?.orderBy(KEY_NEXT_DATE, Query.Direction.ASCENDING)
				?.whereLessThan(KEY_NEXT_DATE, c.time)
				?.addSnapshotListener { value, _ ->
					if (value == null) {
						return@addSnapshotListener
					}
					trySend(value.documents)
				}
			awaitClose {
				subscription?.remove()
			}
		}

	override suspend fun addData(data: Map<String, Any>) = callbackFlow {
		try {
			db.collection(COLLECTION)
				.add(data)
				.addOnSuccessListener { success ->
					Log.d("WWW.add.SUCCESS ", "${success.path[1]}")
					trySend(success)
					return@addOnSuccessListener
				}
				.addOnFailureListener { exception ->
					Log.d("WWW.add.EXCEPTION ", "${exception.message}")
					return@addOnFailureListener
				}
		} catch (exception: Exception) {
			close(exception)
		}
		awaitClose {
			return@awaitClose
		}
	}

	override suspend fun updateData(data: Map<String, Any>, idKip: String): Flow<String> =
		callbackFlow {
			val doc: DocumentReference = db.collection(COLLECTION).document(idKip)
			try {
				doc.update(data)
					.addOnSuccessListener {
						val message = "Successfully updated!"
						trySend(message)
						return@addOnSuccessListener
					}
					.addOnFailureListener { exception ->
						Log.d("WWW.update.EXCEPTION ", "${exception.message}")
						trySend(exception.message.toString())
						return@addOnFailureListener
					}
			} catch (exception: Exception) {
				close(exception)
			}
			awaitClose {
				return@awaitClose
			}
		}

	override suspend fun getDataAsFilter(directionFlag: Boolean): Flow<List<DocumentSnapshot>> =
		callbackFlow {
			val direction = if (directionFlag) {
				Query.Direction.ASCENDING
			} else {
				Query.Direction.DESCENDING
			}
			var eventCollection: CollectionReference? = null
			try {
				eventCollection = db.collection(COLLECTION)
			} catch (exception: Exception) {
				close(exception)
			}
			val subscription = eventCollection
				?.orderBy(KEY_NEXT_DATE, direction)
				?.addSnapshotListener { value, _ ->
					if (value == null) {
						return@addSnapshotListener
					}
					trySend(value.documents)
				}
			awaitClose {
				subscription?.remove()
			}
		}
}
