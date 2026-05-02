package com.kwh.almuniconnect.nearby

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.suspendCancellableCoroutine

//class LocationProvider(
//    private val context: Context
//) {
//
//    private val fusedClient =
//        LocationServices.getFusedLocationProviderClient(context)
//
//    @RequiresPermission(allOf = [
//        Manifest.permission.ACCESS_FINE_LOCATION,
//        Manifest.permission.ACCESS_COARSE_LOCATION
//    ])
//    fun getLocation(onLocation: (Location) -> Unit) {
//
//        // First try last location
//        fusedClient.lastLocation.addOnSuccessListener { location ->
//            if (location != null) {
//                onLocation(location)
//            } else {
//                requestNewLocation(onLocation)
//            }
//        }
//    }
//
//    @SuppressLint("MissingPermission")
//    private fun requestNewLocation(onLocation: (Location) -> Unit) {
//
//        val locationRequest = LocationRequest.Builder(
//            Priority.PRIORITY_HIGH_ACCURACY,
//            1000
//        ).apply {
//            setMinUpdateIntervalMillis(500)
//            setMaxUpdates(1)
//        }.build()
//
//        fusedClient.requestLocationUpdates(
//            locationRequest,
//            object : LocationCallback() {
//                override fun onLocationResult(result: LocationResult) {
//                    val location = result.lastLocation
//                    if (location != null) {
//                        onLocation(location)
//                        fusedClient.removeLocationUpdates(this)
//                    }
//                }
//            },
//            Looper.getMainLooper()
//        )
//    }
//}

class LocationProvider(
    private val context: Context
) {

    private val fusedClient =
        LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    suspend fun getLocationSuspend(): Location? =
        suspendCancellableCoroutine { cont ->

            fusedClient.lastLocation
                .addOnSuccessListener { lastLocation ->

                    if (lastLocation != null) {
                        cont.resume(lastLocation, null)
                        return@addOnSuccessListener
                    }

                    fusedClient.getCurrentLocation(
                        Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                        null
                    ).addOnSuccessListener { location ->
                        cont.resume(location, null)
                    }.addOnFailureListener {
                        cont.resume(null, null)
                    }
                }
                .addOnFailureListener {
                    cont.resume(null, null)
                }
        }
}