package com.kwh.almuniconnect

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability

class InAppUpdateHelper(private val context: Context) {

    private val appUpdateManager: AppUpdateManager =
        AppUpdateManagerFactory.create(context)

    private val updateListener = InstallStateUpdatedListener { state ->
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            Toast.makeText(context, "Update downloaded. Restart app to apply.", Toast.LENGTH_LONG).show()
        }
    }

    fun checkUpdate(activity: Activity) {
        val task = appUpdateManager.appUpdateInfo

        task.addOnSuccessListener { info ->
            if (info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                when {
                    info.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE) -> {
                        appUpdateManager.registerListener(updateListener)
                        startUpdate(activity, info, AppUpdateType.FLEXIBLE)
                    }
                }
            }
        }
    }

    private fun startUpdate(activity: Activity, info: AppUpdateInfo, type: Int) {
        appUpdateManager.startUpdateFlowForResult(
            info,
            type,
            activity,
            UPDATE_CODE
        )
    }

    fun onResume(activity: Activity) {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
            if (info.installStatus() == InstallStatus.DOWNLOADED) {
                Toast.makeText(context, "Restart app to finish update", Toast.LENGTH_LONG).show()
            }
            if (info.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                startUpdate(activity, info, AppUpdateType.IMMEDIATE)
            }
        }
    }

    fun completeUpdate() {
        appUpdateManager.completeUpdate()
    }

    companion object {
        const val UPDATE_CODE = 1001
    }
}
