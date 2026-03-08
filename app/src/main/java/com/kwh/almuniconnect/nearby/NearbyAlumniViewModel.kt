package com.kwh.almuniconnect.nearby

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class NearbyAlumniViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    var alumniList by mutableStateOf<List<NearAlumni>>(emptyList())
        private set

    fun loadNearby(
        lat: Double,
        lng: Double
    ) {

        db.collection("users")
            .get()
            .addOnSuccessListener { result ->

                val list = mutableListOf<NearAlumni>()

                for (doc in result) {

                    val alumni = doc.toObject(NearAlumni::class.java)

                    val distance = LocationUtils.distanceKm(
                        lat,
                        lng,
                        alumni.latitude,
                        alumni.longitude
                    )

                    if (distance <= 50) {

                        list.add(
                            alumni.copy(distanceKm = distance)
                        )
                    }
                }

                alumniList = list.sortedBy { it.distanceKm }
            }
    }
}