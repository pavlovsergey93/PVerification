package com.gmail.pavlovsv93.verification.data.details

import com.gmail.pavlovsv93.verification.COLLECTION
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class DetailsRepository(
    private val db: FirebaseFirestore
) : DetailsRepositoryInterface {
    override suspend fun getItemKip(id: String): Flow<DocumentSnapshot> =
        callbackFlow {
            var eventCollection: DocumentReference? = null
            try {
                eventCollection = db.collection(COLLECTION)
                    .document(id)
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
}
