package com.gmail.pavlovsv93.verification.data.devices

import com.gmail.pavlovsv93.verification.COLLECTION
import com.gmail.pavlovsv93.verification.KEY_NEXT_DATE
import com.gmail.pavlovsv93.verification.KEY_POSITION
import com.gmail.pavlovsv93.verification.KEY_STATION_KEY
import com.gmail.pavlovsv93.verification.ui.listdevices.FilterEntity
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class DevicesRepository(private val db: FirebaseFirestore) : DevicesRepositoryInterface {
    override suspend fun getAllKip(): Flow<List<DocumentSnapshot>> = callbackFlow {
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

    override suspend fun getSearchListKip(filter: FilterEntity): Flow<List<DocumentSnapshot>> =
        callbackFlow {
            val direction = if (filter.isCheckedData == true) {
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
