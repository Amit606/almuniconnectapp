package com.kwh.almuniconnect

object Routes {
    const val SPLASH = "splash"
    const val INTRO = "intro"
    const val STARTED = "started"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val NETWORK = "network"

    const val PROFILE = "profile/{userId}"
    fun profileRoute(userId: String) = "profile/$userId"

    const val MESSAGES = "messages"
    const val CREATE_POST = "create_post"

    const val EVENT_DETAILS = "event/{eventId}"
    fun eventRoute(eventId: String) = "event/$eventId"

    const val JOB_DETAILS = "job/{jobId}"
    fun jobRoute(jobId: String) = "job/$jobId"
}
