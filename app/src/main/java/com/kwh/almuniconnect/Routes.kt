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

    const val PROFILE = "profile"
    const val PROFILE_ROUTE = "profile/{id}"
    fun profileRoute(id: String): String {
        return "profile/$id"
    }


    const val MESSAGES = "messages"
    const val CREATE_POST = "create_post"
    const val EVENTS = "events"
    const val OTP = "otp"
    const val PASSWORD_LOGIN = "password_login"


   // const val EVENT_DETAILS = "event/{eventId}"
   // fun eventRoute(eventId: String) = "event/$eventId"

    const val JOB_DETAILS = "job/{jobId}"
    fun jobRoute(jobId: String) = "job/$jobId"

    const val JOB_DETAILS_Full = "job_details"
    const val JOB_POST = "job_post"

    const val ALMUNI_POST="almuni_post"
    const val SETTINGS = "SETTINGS"
    const val USER_PROFILE = "user_profile"
    const val EVENT_DETAILS = "event_details"
    const val SERVICE_DETAILS = "service_details"

    const val HELP_SUPPORTS = "help_supports"
    const val ABOUT_US = "about_us"
    const val APP_VERSION = "1.0.3"
    const val DEVELOPER_NAME = "Apps Chance"
    const val DEVELOPER_EMAIL = "support@alumniconnect.com"

    const val PRIVACY_POLICY_URL = "https://alumniconnect.com/privacy"
    const val TERMS_URL = "https://alumniconnect.com/terms"
    const val WHATSUP_CHANNEL ="whatsup_channel"
    const val Internet_Splash = "internet_splash"
    const val NEWS = "news"
    fun ALUMNI_STORY_DETAIL(storyName: String) = "alumni_story_details/$storyName"
   const val SUBSCRIPTION = "premium_subscription"

    const val PRODUCT_SCREEN = "product_screen"
    const val ENTRY = "emergency_entry"
    const val REQUEST = "emergency_request"
    const val DONATION_SUCCESS = "donation_success/{amount}"
    const val FEED = "emergency_feed"
    const val DETAIL = "emergency_detail/{id}"
    const val DONATE = "emergency_donate/{id}"
    const val SUCCESS = "donation_success/{amount}"
}
