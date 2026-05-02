package com.kwh.almuniconnect.tallent

import android.util.Log
import androidx.compose.animation.core.updateTransition
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class TalentRepository {

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("talents")

    fun observeApprovedTalents(
        onResult: (List<Talent>) -> Unit
    ) {
        collection
            .whereEqualTo("status", "APPROVED")
            .addSnapshotListener { snapshot, _ ->

                val talents = snapshot?.documents?.mapNotNull {
                    it.toObject(Talent::class.java)
                } ?: emptyList()

                onResult(talents)
            }
    }
    suspend fun addTalent(talent: Talent) {
        try {
            val uid = FirebaseAuth.getInstance().currentUser?.uid
                ?: throw Exception("User not logged in")

            val doc = collection.document()

            doc.set(
                talent.copy(
                    id = doc.id,
                    userId = uid,
                    status = "PENDING"
                )
            ).await()

        } catch (e: Exception) {
            Log.e("FirestoreError", e.message.toString())
        }
    }
//
//    suspend fun addTalent(talent: Talent) {
//        val uid = FirebaseAuth.getInstance().currentUser?.uid
//            ?: throw Exception("User not logged in")
//        val doc = collection.document()
//        doc.set(talent.copy(id = doc.id, userId = uid, status = "PENDING")).await()
//    }

    suspend fun likeTalent(talentId: String) {
        val doc = collection.document(talentId)

        db.runTransaction { transaction ->
            val snapshot = transaction.get(doc)
            val currentLikes = snapshot.getLong("likes") ?: 0
            transaction.update(doc, "likes", currentLikes + 1)
        }.await()
    }
}