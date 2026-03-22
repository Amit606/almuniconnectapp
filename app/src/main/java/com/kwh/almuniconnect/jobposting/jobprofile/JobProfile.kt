package com.kwh.almuniconnect.jobposting.jobprofile

import android.net.Uri

data class JobProfile(
    val name: String = "",
    val title: String = "",
    val company: String = "",
    val experience: String = "",
    val skills: List<String> = emptyList(),
    val openToWork: Boolean = true,
    val imageUri: Uri? = null
)