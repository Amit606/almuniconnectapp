package com.kwh.almuniconnect.help

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

fun submitChannelToFirestore(
    type: SocialType,
    title: String,
    description: String,
    link: String,
    userName: String,
    userEmail: String,
    userId: String,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {

    val user = FirebaseAuth.getInstance().currentUser

    if (user == null) {
        onError("User not logged in")
        return
    }

    val db = FirebaseFirestore.getInstance()

    val request = SocialChannelRequest(

        title = title,
        description = description,
        link = link,
        type = type.name,

        status = "pending",

        userId = user.uid,
        userName = userName ?: "",
        userEmail = userEmail ?: "",

        createdAt = System.currentTimeMillis()
    )

    db.collection("social_channel_requests")
        .add(request)
        .addOnSuccessListener {
            onSuccess()
        }
        .addOnFailureListener {
            onError(it.message ?: "Error submitting channel")
        }
}