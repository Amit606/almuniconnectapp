package com.kwh.almuniconnect.analytics

sealed class AnalyticsEvent(
    val name: String,
    val params: Map<String, Any> = emptyMap()
) {

    // App Lifecycle
    object AppOpen : AnalyticsEvent("app_open")

    // Screens
    data class ScreenView(val screen: String) :
        AnalyticsEvent("screen_view", mapOf("screen" to screen))

    // Alumni Actions
    object AlumniListViewed : AnalyticsEvent("alumni_list_viewed")

    data class AlumniProfileOpened(val alumniId: String) :
        AnalyticsEvent("alumni_profile_opened", mapOf("id" to alumniId))

    // Events
    data class EventClicked(val eventId: String) :
        AnalyticsEvent("event_clicked", mapOf("event_id" to eventId))

    data class EventRegistered(val eventId: String) :
        AnalyticsEvent("event_registered", mapOf("event_id" to eventId))

    // Engagement
    // Notifications
    data class NotificationReceived(
        val type: String?,
        val destination: String?
    ) : AnalyticsEvent(
        "notification_received",
        mapOf(
            "type" to (type ?: ""),
            "destination" to (destination ?: "")
        )
    )

    data class NotificationOpened(
        val type: String?,
        val destination: String?
    ) : AnalyticsEvent(
        "notification_opened",
        mapOf(
            "type" to (type ?: ""),
            "destination" to (destination ?: "")
        )
    )

    data class NotificationCTAClicked(
        val type: String?,
        val destination: String?
    ) : AnalyticsEvent(
        "notification_cta_clicked",
        mapOf(
            "type" to (type ?: ""),
            "destination" to (destination ?: "")
        )
    )

}
