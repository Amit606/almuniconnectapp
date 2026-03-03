package com.kwh.almuniconnect.tallent

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
        val doc = collection.document()
        doc.set(talent.copy(id = doc.id)).await()
    }

    suspend fun likeTalent(talentId: String) {
        val doc = collection.document(talentId)

        db.runTransaction { transaction ->
            val snapshot = transaction.get(doc)
            val currentLikes = snapshot.getLong("likes") ?: 0
            transaction.update(doc, "likes", currentLikes + 1)
        }.await()
    }
}