package com.kwh.almuniconnect.nearby

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.kwh.almuniconnect.login.LoginRoute

class NearbyAlumniViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    var isLoading by mutableStateOf(false)
        private set
    var alumniList by mutableStateOf<List<NearAlumni>>(emptyList())
        private set

    fun loadNearby(lat: Double, lng: Double) {

        isLoading = true

        db.collection("users")
            .get()
            .addOnSuccessListener { result ->

                val list = mutableListOf<NearAlumni>()

                for (doc in result) {

                    val alumni = doc.toObject(NearAlumni::class.java)

                    val userLat = alumni.latitude
                    val userLng = alumni.longitude
                    Log.e("Location", "User: ${alumni.name}, Lat: $userLat, Lng: $userLng")

                    if (userLat == 0.0 || userLng == 0.0) continue

                    val distance = LocationUtils.distanceKm(
                        lat,
                        lng,
                        userLat,
                        userLng
                    )

                    if (distance <= 50) {
                        list.add(alumni.copy(distanceKm = distance))
                    }
                }

                alumniList = list.sortedBy { it.distanceKm }
                isLoading = false
            }
            .addOnFailureListener {
                isLoading = false
            }
    }
}