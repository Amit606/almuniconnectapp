package com.kwh.almuniconnect

import android.app.Application
import com.google.firebase.FirebaseApp

class HbtuApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}