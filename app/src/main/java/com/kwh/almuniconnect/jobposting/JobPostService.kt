package com.kwh.almuniconnect.jobposting

import com.kwh.almuniconnect.api.ApiService
import com.kwh.almuniconnect.api.NetworkClient

object JobPostService {
    val api: ApiService =
        NetworkClient.createService(ApiService::class.java)
}