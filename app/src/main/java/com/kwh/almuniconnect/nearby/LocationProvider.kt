package com.kwh.almuniconnect.nearby

import android.Manifest
import android.content.Context
import android.location.Location
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.LocationServices

class LocationProvider(
    private val context: Context
) {

    private val fusedClient =
        LocationServices.getFusedLocationProviderClient(context)

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    fun getLocation(
        onLocation: (Location) -> Unit
    ) {

        fusedClient.lastLocation
            .addOnSuccessListener { location ->

                location?.let {
                    onLocation(it)
                }
            }
    }
}