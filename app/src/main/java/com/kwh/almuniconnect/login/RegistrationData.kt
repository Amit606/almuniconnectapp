package com.kwh.almuniconnect.login

import android.net.Uri

data class RegistrationData(
    val firstName: String = "",
    val mobileNo: String = "",
    val email: String = "",
    val branch: String = "",
    val PassoutYear: String = "",
    val jobDetails: String = "",
    val title:String = "",
    val linkedIn: String = "",
    val dateOfBirth: String = "",
   val  countryId:Int = 81,
    val  courseId:Int = 1,
    val dateOfMarriage: String = "",
    val profileUri: Uri? = null
)