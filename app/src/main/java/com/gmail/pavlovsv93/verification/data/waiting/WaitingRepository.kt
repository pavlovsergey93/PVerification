package com.gmail.pavlovsv93.verification.data.waiting

import android.util.Log
import com.gmail.pavlovsv93.verification.COLLECTION
import com.gmail.pavlovsv93.verification.KEY_POSITION
import com.gmail.pavlovsv93.verification.KEY_STATION_KEY
import com.google.firebase.firestore.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class WaitingRepository(private val db: FirebaseFirestore) : WaitingRepositoryInterface {
    override suspend fun getWaitingKip(): Flow<List<DocumentSnapshot>> = callbackFlow {
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

    override suspend fun updateKip(id: String, data: Map<String, Any?>): Flow<String> =
        callbackFlow {
            val doc: DocumentReference = db.collection(COLLECTION).document(id)
            try {
                doc.update(data)
                    .addOnSuccessListener {
                        val message = "Состояние обновлено!"
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
}