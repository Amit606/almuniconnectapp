package com.kwh.almuniconnect.login

import android.net.Uri

data class RegistrationData(
    val name: String = "",
    val mobile: String = "",
    val email: String = "",
    val branch: String = "",
    val passingYear: String = "",
    val jobDetails: String = "",
    val linkedIn: String = "",
    val dob: String = "",
    val anniversary: String = "",
    val profileUri: Uri? = null
)