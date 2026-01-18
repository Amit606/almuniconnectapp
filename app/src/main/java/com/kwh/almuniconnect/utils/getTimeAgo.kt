package com.kwh.almuniconnect.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Duration
import java.time.OffsetDateTime
import java.time.ZoneOffset

@RequiresApi(Build.VERSION_CODES.O)
fun getTimeAgo(createdAtUtc: String): String {
    val createdTime = OffsetDateTime.parse(createdAtUtc)
    val now = OffsetDateTime.now(ZoneOffset.UTC)

    val duration = Duration.between(createdTime, now)

    val minutes = duration.toMinutes()
    val hours = duration.toHours()
    val days = duration.toDays()

    return when {
        minutes < 1 -> "Just now"
        minutes < 60 -> "$minutes min ago"
        hours < 24 -> "$hours hrs ago"
        days < 7 -> "$days days ago"
        else -> "${days / 7} weeks ago"
    }
}
