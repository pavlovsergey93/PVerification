package com.gmail.pavlovsv93.verification.data.remote

import android.util.Log
import com.gmail.pavlovsv93.verification.COLLECTION
import com.gmail.pavlovsv93.verification.KEY_NEXT_DATE
import com.google.firebase.firestore.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.*

class RemoteRepository(private val db: FirebaseFirestore) : RemoteRepositoryInterface {
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

    override suspend fun updateDate(id: String, data: Map<String, Any?>): Flow<String> =
        callbackFlow {
            val doc: DocumentReference = db.collection(COLLECTION).document(id)
            try {
                doc.update(data)
                    .addOnSuccessListener {
                        val message = "Дата обновлена!"
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