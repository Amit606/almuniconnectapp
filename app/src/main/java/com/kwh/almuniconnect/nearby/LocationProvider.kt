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
    fun getLocation(onLocation: (Location?) -> Unit) {

        fusedClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            null
        ).addOnSuccessListener { location ->
            onLocation(location)
        }.addOnFailureListener {
            onLocation(null)
        }
    }
}