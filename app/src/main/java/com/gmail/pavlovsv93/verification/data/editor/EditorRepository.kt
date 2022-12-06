package com.gmail.pavlovsv93.verification.data.editor

import android.util.Log
import com.gmail.pavlovsv93.verification.COLLECTION
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class EditorRepository(private val db: FirebaseFirestore) : EditorRepositoryInterface{
    override suspend fun addKip(data: Map<String, Any>): Flow<String> = callbackFlow {
        try {
            db.collection(COLLECTION)
                .add(data)
                .addOnSuccessListener { success ->
                    val message = "Прибор добавлен!\nID: ${success.id}"
                    trySend(message)
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

    override suspend fun updateKip(id: String, data: Map<String, Any>): Flow<String> =
        callbackFlow {
            val doc: DocumentReference = db.collection(COLLECTION).document(id)
            try {
                doc.update(data)
                    .addOnSuccessListener {
                        val message = "Прибор $id обновлен успешно!"
                        trySend(message)
                        return@addOnSuccessListener
                    }
                    .addOnFailureListener { exception ->
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