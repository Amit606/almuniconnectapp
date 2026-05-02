package com.kwh.almuniconnect.morefeature

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.kwh.almuniconnect.Routes

class MoreViewModel : ViewModel() {

    val features = getMoreFeatures()

    fun onFeatureClick(
        feature: MoreFeature,
        context: Context,
        navController: NavController
    ) {

        when (feature) {

            is MoreFeature.JobProfile ->
                navController.navigate(Routes.JOB_PROFILE_COMMING_SOON)

            is MoreFeature.Media ->
                navController.navigate(Routes.MEDIA_FEATURE)

            is MoreFeature.Nearby -> {
                val permission = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )

                if (permission == PackageManager.PERMISSION_GRANTED) {
                    navController.navigate(Routes.NEARBY_HARCOURTIANS)
                } else {
                    navController.navigate(Routes.NEARBY_HARCOURTIANS_PERMISSION)
                }
            }

            is MoreFeature.Verification ->
                navController.navigate(Routes.VERIFICATION)

            else ->
                navController.navigate(Routes.COMING_SOON)
        }
    }
}