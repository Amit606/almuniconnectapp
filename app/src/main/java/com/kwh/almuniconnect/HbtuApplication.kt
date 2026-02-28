package com.kwh.almuniconnect

import android.app.Application
import com.drivemetadata.DriveMetaData
import com.kwh.almuniconnect.analytics.AnalyticsManager

class HbtuApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AnalyticsManager.init(this)
        // DriveMetaData.setMetaAppId(this,"336547575673335");//meta id =336547575673335
        val driveMetadata = DriveMetaData.Builder(
            this,
            1635,
            "4d17d90c78154c9a5569c073b67d8a5a22b2fabfc5c9415b6e7f709d68762054",
            3020,
            "336547575673335",
            1637
        ).build()
        DriveMetaData.setSingletonInstance(driveMetadata)

    }
}