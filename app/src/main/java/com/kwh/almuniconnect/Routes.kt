package com.kwh.almuniconnect

object Routes {
    const val SPLASH = "splash"
    const val SPLASH_HOME ="splash_home"
    const val INTRO = "intro"
    const val STARTED = "started"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val NETWORK = "network"
    const val COUTNRYLIST = "countrylist"

    const val PROFILE = "profile/{id}"
    fun profileRoute(id: String) = "profile/$id"

    const val MESSAGES = "messages"
    const val CREATE_POST = "create_post"
    const val EVENTS = "events"
    const val OTP = "otp"
    const val PASSWORD_LOGIN = "password_login"


    const val EVENT_DETAILS = "event/{eventId}"
    fun eventRoute(eventId: String) = "event/$eventId"

    const val JOB_DETAILS = "job/{jobId}"
    fun jobRoute(jobId: String) = "job/$jobId"
}
